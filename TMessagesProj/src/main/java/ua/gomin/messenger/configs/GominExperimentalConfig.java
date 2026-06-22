package ua.gomin.messenger.configs;

import android.content.Context;
import android.content.SharedPreferences;

public class GominExperimentalConfig {

    public static final GominExperimentalConfig INSTANCE = new GominExperimentalConfig();
    private static final String PREFS_NAME = "gomin_experimental_config";

    

    private SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public boolean getTranslatorEnabled(Context context) {
        return getPrefs(context).getBoolean("translatorEnabled", false);
    }
    public boolean getJsonViewerEnabled(Context context) {
        return getPrefs(context).getBoolean("jsonViewerEnabled", false);
    }
    public void setTranslatorEnabled(Context context, boolean value) {
        getPrefs(context).edit().putBoolean("translatorEnabled", value).apply();
    }
    public void setJsonViewerEnabled(Context context, boolean value) {
        getPrefs(context).edit().putBoolean("jsonViewerEnabled", value).apply();
    }
}
