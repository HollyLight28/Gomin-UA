package ua.gomin.messenger.configs

import android.content.Context
import android.content.SharedPreferences

/**
 * Налаштування повідомлень.
 */
object GominMessagesConfig {

    private const val PREFS_NAME = "gomin_messages_config"

    // Double tap actions
    const val DOUBLE_TAP_ACTION_NONE = 0
    const val DOUBLE_TAP_ACTION_REACTION = 1
    const val DOUBLE_TAP_ACTION_REPLY = 2
    const val DOUBLE_TAP_ACTION_SAVE = 3
    const val DOUBLE_TAP_ACTION_EDIT = 4
    const val DOUBLE_TAP_ACTION_TRANSLATE = 5
    const val DOUBLE_TAP_ACTION_TRANSLATE_GEMINI = 6

    // Slide actions
    const val MESSAGE_SLIDE_ACTION_REPLY = 0
    const val MESSAGE_SLIDE_ACTION_SAVE = 1
    const val MESSAGE_SLIDE_ACTION_TRANSLATE = 2
    const val MESSAGE_SLIDE_ACTION_TRANSLATE_GEMINI = 3
    const val MESSAGE_SLIDE_ACTION_DIRECT_SHARE = 4

    // Transcription providers
    const val TRANSCRIPTION_PROVIDER_GEMINI = 0
    const val TRANSCRIPTION_PROVIDER_TELEGRAM = 1

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun getDeleteForAll(context: Context): Boolean =
        getPrefs(context).getBoolean("deleteForAll", false)

    fun setDeleteForAll(context: Context, enabled: Boolean) {
        getPrefs(context).edit().putBoolean("deleteForAll", enabled).apply()
    }

    fun getDoubleTapAction(context: Context): Int =
        getPrefs(context).getInt("doubleTapAction", DOUBLE_TAP_ACTION_NONE)

    fun setDoubleTapAction(context: Context, action: Int) {
        getPrefs(context).edit().putInt("doubleTapAction", action).apply()
    }

    fun getMessageSlideAction(context: Context): Int =
        getPrefs(context).getInt("messageSlideAction", MESSAGE_SLIDE_ACTION_REPLY)

    fun setMessageSlideAction(context: Context, action: Int) {
        getPrefs(context).edit().putInt("messageSlideAction", action).apply()
    }

    fun getVoiceTranscriptionProvider(context: Context): Int =
        getPrefs(context).getInt("voiceTranscriptionProvider", TRANSCRIPTION_PROVIDER_TELEGRAM)

    fun setVoiceTranscriptionProvider(context: Context, provider: Int) {
        getPrefs(context).edit().putInt("voiceTranscriptionProvider", provider).apply()
    }
}
