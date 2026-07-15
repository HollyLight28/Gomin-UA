package ua.gomin.messenger.preferences;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.text.InputType;

import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.Components.UniversalAdapter;
import org.telegram.ui.Components.UniversalFragment;
import org.telegram.ui.Components.LayoutHelper;

import ua.gomin.messenger.configs.GominGeminiConfig;

import java.util.ArrayList;

public class GeminiPreferencesEntry extends UniversalFragment {

    private final int apiKeyRow = 1;
    private final int modelNameRow = 2;

    @Override
    protected CharSequence getTitle() {
        return "Налаштування нейромережі";
    }

    @Override
    protected void fillItems(ArrayList<UItem> items, UniversalAdapter adapter) {
        Context context = getContext();
        if (context == null) return;

        items.add(SettingsHelper.asHeaderWithIcon(context, R.drawable.msg_bot, "🤖 Gomin AI (Gemini)"));
        
        String currentKey = GominGeminiConfig.getApiKey();
        String displayKey = currentKey.isEmpty() ? "Не встановлено" : "Встановлено (натисніть щоб змінити)";
        items.add(UItem.asButton(apiKeyRow, "API Key", displayKey));
        
        String currentModel = GominGeminiConfig.getModelName();
        items.add(UItem.asButton(modelNameRow, "Модель", currentModel));
        
        items.add(UItem.asShadow("Отримайте API ключ на сайті aistudio.google.com/apikey"));
    }

    @Override
    protected void onClick(UItem item, View view, int position, float x, float y) {
        Context context = getContext();
        if (context == null) return;

        if (item.id == apiKeyRow) {
            showTextInputDialog("API Key", GominGeminiConfig.getApiKey(), "Введіть Gemini API Key", newValue -> {
                GominGeminiConfig.setApiKey(newValue);
                listView.adapter.update(true);
            });
        } else if (item.id == modelNameRow) {
            showTextInputDialog("Модель", GominGeminiConfig.getModelName(), "Введіть назву моделі (напр. gemini-1.5-flash)", newValue -> {
                GominGeminiConfig.setModelName(newValue);
                listView.adapter.update(true);
            });
        }
    }

    private void showTextInputDialog(String title, String currentValue, String hint, final OnTextEditedListener listener) {
        Context context = getContext();
        if (context == null) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        
        EditText editText = new EditText(context);
        editText.setText(currentValue);
        editText.setHint(hint);
        editText.setTextColor(Theme.getColor(Theme.key_dialogTextBlack));
        editText.setHintTextColor(Theme.getColor(Theme.key_dialogTextHint));
        editText.setBackgroundDrawable(Theme.createEditTextDrawable(context, false));
        editText.setPadding(0, org.telegram.messenger.AndroidUtilities.dp(16), 0, org.telegram.messenger.AndroidUtilities.dp(16));
        editText.setMaxLines(1);
        editText.setLines(1);
        editText.setSingleLine(true);
        if (title.contains("API")) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
        }

        linearLayout.addView(editText, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 24, 0, 24, 0));
        builder.setView(linearLayout);

        builder.setPositiveButton("Зберегти", (dialogInterface, i) -> {
            listener.onTextEdited(editText.getText().toString().trim());
        });
        builder.setNegativeButton("Скасувати", null);
        showDialog(builder.create());
    }

    @Override
    protected boolean onLongClick(UItem item, View view, int position, float x, float y) {
        return false;
    }

    private interface OnTextEditedListener {
        void onTextEdited(String newValue);
    }
}
