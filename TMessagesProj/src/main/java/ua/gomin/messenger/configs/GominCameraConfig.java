package ua.gomin.messenger.configs;

import android.content.Context;
import android.content.SharedPreferences;

public class GominCameraConfig {

    public static final GominCameraConfig INSTANCE = new GominCameraConfig();
    private static final String PREFS_NAME = "gomin_camera_config";

    

    private SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public boolean getUseDualCamera(Context context) {
        return getPrefs(context).getBoolean("useDualCamera", false);
    }
    public void setUseDualCamera(Context context, boolean value) {
        getPrefs(context).edit().putBoolean("useDualCamera", value).apply();
    }
}
