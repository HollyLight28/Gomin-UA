package ua.gomin.messenger.configs;

import android.content.Context;
import android.content.SharedPreferences;

public class GominAppearanceConfig {

    public static final GominAppearanceConfig INSTANCE = new GominAppearanceConfig();
    private static final String PREFS_NAME = "gomin_appearance_config";

    

    private SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public boolean getHideSearchFiled(Context context) {
        return getPrefs(context).getBoolean("hideSearchFiled", false);
    }
    public boolean getShowMainTabs(Context context) {
        return getPrefs(context).getBoolean("showMainTabs", false);
    }
    public boolean getProfileBackgroundColor(Context context) {
        return getPrefs(context).getBoolean("profileBackgroundColor", false);
    }
    public boolean getCenterTitle(Context context) {
        return getPrefs(context).getBoolean("centerTitle", false);
    }
    public boolean getDrawSnowInActionBar(Context context) {
        return getPrefs(context).getBoolean("drawSnowInActionBar", false);
    }
    public void setHideSearchFiled(Context context, boolean value) {
        getPrefs(context).edit().putBoolean("hideSearchFiled", value).apply();
    }
    public void setShowMainTabs(Context context, boolean value) {
        getPrefs(context).edit().putBoolean("showMainTabs", value).apply();
    }
    public void setProfileBackgroundColor(Context context, boolean value) {
        getPrefs(context).edit().putBoolean("profileBackgroundColor", value).apply();
    }
    public void setCenterTitle(Context context, boolean value) {
        getPrefs(context).edit().putBoolean("centerTitle", value).apply();
    }
    public void setDrawSnowInActionBar(Context context, boolean value) {
        getPrefs(context).edit().putBoolean("drawSnowInActionBar", value).apply();
    }
}
