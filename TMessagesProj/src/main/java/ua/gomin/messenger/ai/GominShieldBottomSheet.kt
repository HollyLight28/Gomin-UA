package ua.gomin.messenger.ai

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.widget.NestedScrollView
import android.widget.TextView
import androidx.core.content.ContextCompat
import android.text.TextUtils
import org.telegram.messenger.AndroidUtilities
import org.telegram.messenger.AndroidUtilities.dp
import org.telegram.messenger.LocaleController
import org.telegram.messenger.MessageObject
import org.telegram.messenger.MessagesController
import org.telegram.messenger.R
import org.telegram.messenger.UserConfig
import org.telegram.messenger.UserObject
import org.telegram.messenger.Utilities
import org.telegram.tgnet.TLRPC
import org.telegram.ui.ActionBar.AlertDialog
import org.telegram.ui.ActionBar.BottomSheet
import org.telegram.ui.ActionBar.Theme
import org.telegram.ui.ChatActivity
import org.telegram.ui.Components.LayoutHelper
import org.telegram.ui.Components.RadialProgressView
import org.telegram.ui.LaunchActivity
import org.telegram.ui.Stories.recorder.ButtonWithCounterView
import java.util.ArrayList

class GominShieldBottomSheet(
    private val chatActivity: ChatActivity,
    private val partnerName: String,
    var historyText: String,
    private val cachedResult: String? = null
) : BottomSheet(chatActivity.parentActivity, false, chatActivity.resourceProvider) {

    private val rootLayout: LinearLayout
    private val scrollView: NestedScrollView
    private val textView: TextView
    private val counterTextView: TextView
    private val loadingText: TextView
    private val loadingLayout: LinearLayout
    private val closeButton: ButtonWithCounterView

    private var analysisResult: String? = null
    private var updateProgressRunnable: Runnable? = null
    private var messagesCount: Int = 0

    init {
        setCanDismissWithSwipe(true)
        backgroundPaddingLeft = 0
        backgroundPaddingTop = 0
        setApplyTopPadding(true)

        val context = chatActivity.parentActivity

        rootLayout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(0, dp(24f), 0, dp(16f))
            setBackgroundColor(getThemedColor(Theme.key_dialogBackground))
        }

        val headerView = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(22f), 0, dp(22f), dp(12f))
        }

        val titleContainer = FrameLayout(context)

        val headerTitle = TextView(context).apply {
            text = "Gomin Shield"
            setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20f)
            setTextColor(getThemedColor(Theme.key_dialogTextBlack))
            typeface = AndroidUtilities.bold()
        }
        titleContainer.addView(headerTitle, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.LEFT or Gravity.CENTER_VERTICAL))

        counterTextView = TextView(context).apply {
            setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13f)
            setTextColor(getThemedColor(Theme.key_dialogTextGray2))
            visibility = View.GONE
        }
        titleContainer.addView(counterTextView, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.RIGHT or Gravity.CENTER_VERTICAL))

        headerView.addView(titleContainer, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT))

        val headerSubtitle = TextView(context).apply {
            text = "Аналіз діалогу з $partnerName"
            setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13f)
            setTextColor(getThemedColor(Theme.key_dialogTextGray2))
            setPadding(0, dp(4f), 0, 0)
        }
        headerView.addView(headerSubtitle, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT))

        rootLayout.addView(headerView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT))

        val divider = View(context).apply {
            setBackgroundColor(getThemedColor(Theme.key_dialogShadowLine))
        }
        rootLayout.addView(divider, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 1, 0f, 0f, 0f, 8f))

        scrollView = NestedScrollView(context).apply {
            overScrollMode = View.OVER_SCROLL_IF_CONTENT_SCROLLS
            isVerticalScrollBarEnabled = true
        }
        nestedScrollChild = scrollView

        textView = TextView(context).apply {
            setPadding(dp(22f), dp(8f), dp(22f), dp(40f))
            setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15f)
            setTextColor(getThemedColor(Theme.key_dialogTextBlack))
            setTextIsSelectable(true)
            lineHeight = dp(22f)
            text = ""
        }
        scrollView.addView(textView)

        loadingLayout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(0, dp(40f), 0, dp(40f))
        }

        loadingText = TextView(context).apply {
            text = "Зчитуємо контекст вашого діалогу... 🔍"
            setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f)
            setTextColor(getThemedColor(Theme.key_dialogTextGray2))
            gravity = Gravity.CENTER
            setPadding(dp(32f), dp(16f), dp(32f), 0)
            lineHeight = dp(20f)
        }
        loadingLayout.addView(loadingText, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT))

        val disclaimerText = TextView(context).apply {
            text = "⚠️ Дисклеймер: Аналіз виконано штучним інтелектом на основі відкритих психологічних патернів. Гомін AI може помилятися та не дає медичних чи юридичних діагнозів. Головний орієнтир — це твоє самопочуття, бро."
            setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11.5f)
            setTextColor(getThemedColor(Theme.key_dialogTextGray2))
            gravity = Gravity.CENTER
            setPadding(dp(32f), dp(16f), dp(32f), 0)
            lineHeight = dp(16f)
        }
        loadingLayout.addView(disclaimerText, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT))

        rootLayout.addView(loadingLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT))
        rootLayout.addView(scrollView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 0f, 0f, 0f, 0f))
        scrollView.visibility = View.GONE

        val buttonLayout = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(dp(16f), dp(12f), dp(16f), 0)
        }

        closeButton = ButtonWithCounterView(context, chatActivity.resourceProvider).apply {
            setRound()
            setFilled(true)
            setText("Зрозуміло", false)
            setOnClickListener {
                dismiss()
            }
        }
        buttonLayout.addView(closeButton, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 48))

        rootLayout.addView(buttonLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT))

        setCustomView(rootLayout)

        if (!TextUtils.isEmpty(historyText)) {
            startAnalysis()
        }
    }

    override fun dismiss() {
        super.dismiss()
        updateProgressRunnable?.let {
            AndroidUtilities.cancelRunOnUIThread(it)
            updateProgressRunnable = null
        }
    }

    fun initAnalysis(count: Int = 0) {
        this.messagesCount = count
        if (count > 0) {
            counterTextView.text = "$count реплік"
            counterTextView.visibility = View.VISIBLE
        }
        startAnalysis()
    }

    private fun startAnalysis() {
        if (messagesCount > 0) {
            counterTextView.text = "$messagesCount реплік"
            counterTextView.visibility = View.VISIBLE
        }
        
        if (cachedResult != null) {
            loadingLayout.visibility = View.GONE
            scrollView.visibility = View.VISIBLE
            analysisResult = cachedResult
            textView.text = cachedResult
            return
        }

        val steps = arrayOf(
            "Зчитуємо контекст вашого діалогу... 🔍",
            "Аналізуємо структуру реплік на предмет пасивної агресії та тиску... 🧩",
            "Перевіряємо наявність газлайтингу, знецінення та патернів DARVO... 🚩",
            "Оцінюємо психологічний вплив на твої особисті кордони... 🛡️",
            "Визначаємо приховані наміри та вигоди співрозмовника... 🧐",
            "Формулюємо готові фрази-відповіді для впевненого захисту... 🗣️",
            "Завершуємо формування твого ментального щита... 🚀"
        )
        var stepIndex = 0

        val run = object : Runnable {
            override fun run() {
                if (stepIndex < steps.size) {
                    val nextText = steps[stepIndex]
                    stepIndex++
                    
                    loadingText.animate()
                        .alpha(0f)
                        .setDuration(200)
                        .withEndAction {
                            loadingText.text = nextText
                            loadingText.animate()
                                .alpha(1f)
                                .setDuration(200)
                                .start()
                        }
                        .start()
                        
                    AndroidUtilities.runOnUIThread(this, 2200)
                }
            }
        }
        updateProgressRunnable = run
        AndroidUtilities.runOnUIThread(run)

        GominAiChatHelper.analyzeManipulation(partnerName, historyText) { success, resultText ->
            updateProgressRunnable?.let {
                AndroidUtilities.cancelRunOnUIThread(it)
                updateProgressRunnable = null
            }
            loadingLayout.visibility = View.GONE
            scrollView.visibility = View.VISIBLE
            
            if (success) {
                analysisResult = resultText
                textView.text = resultText
                GominAiChatHelper.saveToCache(chatActivity.dialogId, resultText, historyText)
            } else {
                textView.text = resultText
                textView.setTextColor(getThemedColor(Theme.key_text_RedRegular))
            }
        }
    }

    companion object {
        @JvmStatic
        fun show(chatActivity: ChatActivity) {
            val currentAccount = chatActivity.currentAccount
            val dialogId = chatActivity.dialogId

            val partnerUser = MessagesController.getInstance(currentAccount).getUser(dialogId)
            val partnerName = if (partnerUser != null) UserObject.getUserName(partnerUser) else "Співрозмовник"

            val cachedResult = GominAiChatHelper.getCachedResult(dialogId)
            val cachedHistory = GominAiChatHelper.getCachedHistory(dialogId)

            val bottomSheet = GominShieldBottomSheet(
                chatActivity,
                partnerName,
                "",
                null
            )
            bottomSheet.show()
            
            GominMessagesStorageHelper.getMessagesForGominShield(currentAccount, dialogId, 1500) { messages ->
                // Захист від race condition: перевіряємо стан activity ОДИН раз і зберігаємо в local
                val activity = chatActivity.parentActivity
                if (activity == null || activity.isFinishing || messages == null) {
                    return@getMessagesForGominShield
                }
                
                // Додатковий захист: якщо sheet вже закритий — нічого не робимо
                if (bottomSheet.isDismissed) {
                    return@getMessagesForGominShield
                }
                
                val historyList = ArrayList<String>()
                val sdf = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault())
                
                var actualCount = 0
                for (i in 0 until messages.size) {
                    val mo = messages[i]
                    val messageOwner = mo.messageOwner
                    if (messageOwner != null && !TextUtils.isEmpty(messageOwner.message)) {
                        val text = messageOwner.message.trim()
                        val fromId = if (messageOwner.from_id != null) messageOwner.from_id.user_id else if (messageOwner.peer_id != null) messageOwner.peer_id.user_id else 0L
                        val senderUser = if (fromId != 0L) MessagesController.getInstance(currentAccount).getUser(fromId) else null
                        val sender = if (senderUser != null) UserObject.getUserName(senderUser) else "Невідомий"
                        val formattedTime = sdf.format(java.util.Date(messageOwner.date * 1000L))
                        historyList.add("[$formattedTime] $sender: $text")
                        actualCount++
                    }
                }
                
                historyList.reverse()
                val finalHistoryText = historyList.joinToString("\n").trim()
                
                if (finalHistoryText.isEmpty()) {
                    // Повторна перевірка перед UI операцією
                    if (activity.isFinishing || bottomSheet.isDismissed) {
                        return@getMessagesForGominShield
                    }
                    bottomSheet.dismiss()
                    val builder = AlertDialog.Builder(activity, chatActivity.resourceProvider)
                    builder.setTitle("Gomin Shield")
                    builder.setMessage("Бро, у цьому чаті немає текстових повідомлень для аналізу!")
                    builder.setPositiveButton("Зрозуміло", null)
                    builder.show()
                } else {
                    // Повторна перевірка перед UI операцією
                    if (activity.isFinishing || bottomSheet.isDismissed) {
                        return@getMessagesForGominShield
                    }
                    if (cachedHistory == finalHistoryText && cachedResult != null) {
                        bottomSheet.historyText = finalHistoryText
                        bottomSheet.initAnalysis(actualCount)
                        bottomSheet.loadingLayout.visibility = View.GONE
                        bottomSheet.scrollView.visibility = View.VISIBLE
                        bottomSheet.analysisResult = cachedResult
                        bottomSheet.textView.text = cachedResult
                    } else {
                        bottomSheet.historyText = finalHistoryText
                        bottomSheet.initAnalysis(actualCount)
                    }
                }
            }
        }
    }
}
