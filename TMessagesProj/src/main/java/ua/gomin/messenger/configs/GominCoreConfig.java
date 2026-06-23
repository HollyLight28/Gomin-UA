package ua.gomin.messenger.configs;

import android.content.Context;
import android.content.SharedPreferences;

public class GominCoreConfig {

    public static final GominCoreConfig INSTANCE = new GominCoreConfig();
    private static final String PREFS_NAME = "gomin_core_config";

    
    public static final int AIR_ALERT_DISABLED = 0;
    public static final int AIR_ALERT_ENABLED = 1;
    public static final int BOOST_NONE = 0;
    public static final int BOOST_AVERAGE = 1;
    public static final int BOOST_EXTREME = 2;
    public static final int ANIMATION_CLASSIC = 0;
    public static final int ANIMATION_SPRING = 1;

    private SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public boolean getAirAlertEnabled(Context context) {
        return getPrefs(context).getBoolean("airAlertEnabled", false);
    }
    public boolean getAirAlertLastActive(Context context) {
        return getPrefs(context).getBoolean("airAlertLastActive", false);
    }
    public String getAirAlertRegionId(Context context) {
        return getPrefs(context).getString("airAlertRegionId", "");
    }
    public String getAirAlertRegionName(Context context) {
        return getPrefs(context).getString("airAlertRegionName", "");
    }
    public int getDownloadSpeedBoost(Context context) {
        return getPrefs(context).getInt("downloadSpeedBoost", BOOST_EXTREME);
    }
    public boolean getUploadSpeedBoost(Context context) {
        return getPrefs(context).getBoolean("uploadSpeedBoost", false);
    }
    public int getSpringAnimation(Context context) {
        return getPrefs(context).getInt("springAnimation", ANIMATION_SPRING);
    }
    public boolean getHideStories(Context context) {
        return getPrefs(context).getBoolean("hideStories", false);
    }
    public void setAirAlertEnabled(Context context, boolean value) {
        getPrefs(context).edit().putBoolean("airAlertEnabled", value).apply();
    }
    public void setAirAlertLastActive(Context context, boolean value) {
        getPrefs(context).edit().putBoolean("airAlertLastActive", value).apply();
    }
    public void setAirAlertRegionId(Context context, String value) {
        getPrefs(context).edit().putString("airAlertRegionId", value).apply();
    }
    public void setAirAlertRegionName(Context context, String value) {
        getPrefs(context).edit().putString("airAlertRegionName", value).apply();
    }
    public void setDownloadSpeedBoost(Context context, int value) {
        getPrefs(context).edit().putInt("downloadSpeedBoost", value).apply();
    }
    public void setUploadSpeedBoost(Context context, boolean value) {
        getPrefs(context).edit().putBoolean("uploadSpeedBoost", value).apply();
    }
    public void setSpringAnimation(Context context, int value) {
        getPrefs(context).edit().putInt("springAnimation", value).apply();
    }
    public void setHideStories(Context context, boolean value) {
        getPrefs(context).edit().putBoolean("hideStories", value).apply();
    }
}
