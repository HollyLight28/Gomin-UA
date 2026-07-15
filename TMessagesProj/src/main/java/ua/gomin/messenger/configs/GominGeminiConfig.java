package ua.gomin.messenger.configs;

import android.content.SharedPreferences;
import org.telegram.messenger.ApplicationLoader;

/**
 * Gomin Gemini Config — зберігає API-ключ та назву моделі Gemini.
 *
 * ⚠️ SECURITY NOTE: API-ключ зберігається в plain SharedPreferences.
 * На rooted пристроях він доступний у /data/data/.../shared_prefs/.
 * TODO: Мігрувати на EncryptedSharedPreferences (androidx.security.crypto)
 * після додавання залежності `androidx.security:security-crypto`.
 */
public class GominGeminiConfig {
    private static final String PREFS_NAME = "gomin_gemini_config";

    private static SharedPreferences getPrefs() {
        return ApplicationLoader.applicationContext.getSharedPreferences(PREFS_NAME, android.content.Context.MODE_PRIVATE);
    }

    public static String getApiKey() {
        return getPrefs().getString("api_key", "");
    }

    public static void setApiKey(String apiKey) {
        getPrefs().edit().putString("api_key", apiKey).apply();
    }

    public static String getModelName() {
        return getPrefs().getString("model_name", "gemini-2.5-flash");
    }

    public static void setModelName(String modelName) {
        getPrefs().edit().putString("model_name", modelName).apply();
    }

    /**
     * Очищає всі дані Gemini (викликається при виході з акаунту).
     */
    public static void clear() {
        getPrefs().edit().clear().apply();
    }
}
