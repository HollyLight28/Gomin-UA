package ua.gomin.messenger.preferences;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.text.InputType;

import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.R;
import org.telegram.messenger.Utilities;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.Components.UniversalAdapter;
import org.telegram.ui.Components.UniversalFragment;
import org.telegram.ui.Components.LayoutHelper;

import ua.gomin.messenger.configs.GominGeminiConfig;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class GeminiPreferencesEntry extends UniversalFragment {

    private final int apiKeyRow = 1;
    private final int modelNameRow = 2;

    private static class GeminiModel {
        final String id;
        final String displayName;

        GeminiModel(String id, String displayName) {
            this.id = id;
            this.displayName = displayName;
        }
    }

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
            onModelClicked();
        }
    }

    /** Gomin start — вибір моделі зі списку Google API (замість ручного вводу) */
    private void onModelClicked() {
        Context context = getContext();
        if (context == null) return;

        String apiKey = GominGeminiConfig.getApiKey();
        if (apiKey.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Gomin AI");
            builder.setMessage("Спочатку введіть API Key");
            builder.setPositiveButton("OK", null);
            showDialog(builder.create());
            return;
        }

        AlertDialog progressDialog = new AlertDialog(context, AlertDialog.ALERT_TYPE_LOADING);
        progressDialog.setTitle("Завантаження моделей");
        progressDialog.setCanCancel(true);
        showDialog(progressDialog);

        Utilities.globalQueue.postRunnable(() -> {
            try {
                List<GeminiModel> models = fetchAvailableModels(apiKey);
                AndroidUtilities.runOnUIThread(() -> {
                    try {
                        progressDialog.dismiss();
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                    if (models.isEmpty()) {
                        showModelsErrorDialog("Список моделей порожній (можливо, ключ недійсний).");
                    } else {
                        showModelPicker(models);
                    }
                });
            } catch (Exception e) {
                FileLog.e(e);
                AndroidUtilities.runOnUIThread(() -> {
                    try {
                        progressDialog.dismiss();
                    } catch (Exception e2) {
                        FileLog.e(e2);
                    }
                    showModelsErrorDialog("Не вдалося завантажити список моделей: " + e.getMessage());
                });
            }
        });
    }

    private List<GeminiModel> fetchAvailableModels(String apiKey) throws Exception {
        URL url = new URL("https://generativelanguage.googleapis.com/v1beta/models?key=" + apiKey + "&pageSize=200");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);

        int responseCode = connection.getResponseCode();
        boolean error = responseCode >= 400;
        java.io.InputStream stream = error ? connection.getErrorStream() : connection.getInputStream();
        if (stream == null) {
            throw new IllegalStateException("HTTP " + responseCode);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        connection.disconnect();

        if (error) {
            throw new IllegalStateException("HTTP " + responseCode);
        }

        JSONObject root = new JSONObject(sb.toString());
        JSONArray modelsArray = root.getJSONArray("models");

        List<GeminiModel> filtered = new ArrayList<>();
        for (int a = 0; a < modelsArray.length(); a++) {
            JSONObject modelJson = modelsArray.getJSONObject(a);
            JSONArray methods = modelJson.optJSONArray("supportedGenerationMethods");
            if (methods == null) {
                continue;
            }
            boolean supportsGenerateContent = false;
            for (int b = 0; b < methods.length(); b++) {
                if ("generateContent".equals(methods.optString(b))) {
                    supportsGenerateContent = true;
                    break;
                }
            }
            if (!supportsGenerateContent) {
                continue;
            }
            String name = modelJson.optString("name", "");
            if (name.startsWith("models/")) {
                name = name.substring("models/".length());
            }
            if (name.isEmpty()) {
                continue;
            }
            filtered.add(new GeminiModel(name, modelJson.optString("displayName", name)));
        }

        Collections.sort(filtered, (o1, o2) -> Integer.compare(modelRank(o1.id), modelRank(o2.id)));
        return filtered;
    }

    private static final int RANK_GEMINI_4 = 0;
    private static final int RANK_GEMINI_3_PRO = 1;
    private static final int RANK_GEMINI_3 = 2;
    private static final int RANK_GEMMA_4 = 3;
    private static final int RANK_REST = 4;

    private static int modelRank(String modelId) {
        String n = modelId.toLowerCase(Locale.US);
        if (n.startsWith("gemini-4")) {
            return RANK_GEMINI_4;
        }
        if (n.startsWith("gemini-3") && n.contains("pro")) {
            return RANK_GEMINI_3_PRO;
        }
        if (n.startsWith("gemini-3")) {
            return RANK_GEMINI_3;
        }
        if (n.startsWith("gemma-4")) {
            return RANK_GEMMA_4;
        }
        return RANK_REST;
    }

    private void showModelPicker(List<GeminiModel> models) {
        Context context = getContext();
        if (context == null) return;

        String[] displayNames = new String[models.size()];
        for (int a = 0; a < models.size(); a++) {
            displayNames[a] = models.get(a).displayName;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Оберіть модель Gemini");
        builder.setItems(displayNames, (dialog, which) -> {
            GominGeminiConfig.setModelName(models.get(which).id);
            if (listView != null) {
                listView.adapter.update(true);
            }
        });
        builder.setNegativeButton("Ввести вручну", (d, w) ->
                showTextInputDialog("Модель", GominGeminiConfig.getModelName(), "Введіть назву моделі (напр. gemini-2.5-flash)", newValue -> {
                    GominGeminiConfig.setModelName(newValue);
                    listView.adapter.update(true);
                }));
        showDialog(builder.create());
    }

    private void showModelsErrorDialog(String message) {
        Context context = getContext();
        if (context == null) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Gomin AI");
        builder.setMessage(message);
        builder.setPositiveButton("Ввести вручну", (d, w) ->
                showTextInputDialog("Модель", GominGeminiConfig.getModelName(), "Введіть назву моделі (напр. gemini-2.5-flash)", newValue -> {
                    GominGeminiConfig.setModelName(newValue);
                    listView.adapter.update(true);
                }));
        builder.setNegativeButton("Скасувати", null);
        showDialog(builder.create());
    }
    /** Gomin end */

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
        editText.setPadding(0, AndroidUtilities.dp(16), 0, AndroidUtilities.dp(16));
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
