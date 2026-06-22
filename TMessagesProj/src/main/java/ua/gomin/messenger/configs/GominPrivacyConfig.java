package ua.gomin.messenger.configs;

import android.content.Context;
import android.content.SharedPreferences;

public class GominPrivacyConfig {

    public static final GominPrivacyConfig INSTANCE = new GominPrivacyConfig();
    private static final String PREFS_NAME = "gomin_privacy_config";

    

    private SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public boolean getGhostModeReadMessages(Context context) {
        return getPrefs(context).getBoolean("ghostModeReadMessages", false);
    }
    public boolean getGhostModeHideTyping(Context context) {
        return getPrefs(context).getBoolean("ghostModeHideTyping", false);
    }
    public boolean getGhostModeHideStoryViews(Context context) {
        return getPrefs(context).getBoolean("ghostModeHideStoryViews", false);
    }
    public boolean getGhostModeHideOnline(Context context) {
        return getPrefs(context).getBoolean("ghostModeHideOnline", false);
    }
    public boolean getKeepDeletedMessages(Context context) {
        return getPrefs(context).getBoolean("keepDeletedMessages", false);
    }
    public boolean getAskBiometricsToOpenChat(Context context) {
        return getPrefs(context).getBoolean("askBiometricsToOpenChat", false);
    }
    public boolean getAskBiometricsToOpenEncrypted(Context context) {
        return getPrefs(context).getBoolean("askBiometricsToOpenEncrypted", false);
    }
    public boolean getAskBiometricsToOpenArchive(Context context) {
        return getPrefs(context).getBoolean("askBiometricsToOpenArchive", false);
    }
    public boolean getAskPasscodeBeforeDelete(Context context) {
        return getPrefs(context).getBoolean("askPasscodeBeforeDelete", false);
    }
    public boolean getAllowSystemPasscode(Context context) {
        return getPrefs(context).getBoolean("allowSystemPasscode", false);
    }
    public void setGhostModeReadMessages(Context context, boolean value) {
        getPrefs(context).edit().putBoolean("ghostModeReadMessages", value).apply();
    }
    public void setGhostModeHideTyping(Context context, boolean value) {
        getPrefs(context).edit().putBoolean("ghostModeHideTyping", value).apply();
    }
    public void setGhostModeHideStoryViews(Context context, boolean value) {
        getPrefs(context).edit().putBoolean("ghostModeHideStoryViews", value).apply();
    }
    public void setGhostModeHideOnline(Context context, boolean value) {
        getPrefs(context).edit().putBoolean("ghostModeHideOnline", value).apply();
    }
    public void setKeepDeletedMessages(Context context, boolean value) {
        getPrefs(context).edit().putBoolean("keepDeletedMessages", value).apply();
    }
    public void setAskBiometricsToOpenChat(Context context, boolean value) {
        getPrefs(context).edit().putBoolean("askBiometricsToOpenChat", value).apply();
    }
    public void setAskBiometricsToOpenEncrypted(Context context, boolean value) {
        getPrefs(context).edit().putBoolean("askBiometricsToOpenEncrypted", value).apply();
    }
    public void setAskBiometricsToOpenArchive(Context context, boolean value) {
        getPrefs(context).edit().putBoolean("askBiometricsToOpenArchive", value).apply();
    }
    public void setAskPasscodeBeforeDelete(Context context, boolean value) {
        getPrefs(context).edit().putBoolean("askPasscodeBeforeDelete", value).apply();
    }
    public void setAllowSystemPasscode(Context context, boolean value) {
        getPrefs(context).edit().putBoolean("allowSystemPasscode", value).apply();
    }
}
