package ua.gomin.messenger.configs;

import android.content.Context;
import android.content.SharedPreferences;

public class GominChatsConfig {

    public static final GominChatsConfig INSTANCE = new GominChatsConfig();
    private static final String PREFS_NAME = "gomin_chats_config";

    
    public static final int NOTIF_SOUND_DEFAULT = 0;
    public static final int NOTIF_SOUND_GOMIN_1 = 1;
    public static final int NOTIF_SOUND_GOMIN_2 = 2;
    public static final int NOTIF_SOUND_GOMIN_3 = 3;

    private SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public boolean getAutoQuoteReplies(Context context) {
        return getPrefs(context).getBoolean("autoQuoteReplies", false);
    }
    public boolean getCustomWallpapers(Context context) {
        return getPrefs(context).getBoolean("customWallpapers", false);
    }
    public int getNotificationSound(Context context) {
        return getPrefs(context).getInt("notificationSound", NOTIF_SOUND_DEFAULT);
    }
    public boolean getDisableVibration(Context context) {
        return getPrefs(context).getBoolean("disableVibration", false);
    }
    public boolean getCenterChatTitle(Context context) {
        return getPrefs(context).getBoolean("centerChatTitle", false);
    }
    public void setAutoQuoteReplies(Context context, boolean value) {
        getPrefs(context).edit().putBoolean("autoQuoteReplies", value).apply();
    }
    public void setCustomWallpapers(Context context, boolean value) {
        getPrefs(context).edit().putBoolean("customWallpapers", value).apply();
    }
    public void setNotificationSound(Context context, int value) {
        getPrefs(context).edit().putInt("notificationSound", value).apply();
    }
    public void setDisableVibration(Context context, boolean value) {
        getPrefs(context).edit().putBoolean("disableVibration", value).apply();
    }
    public void setCenterChatTitle(Context context, boolean value) {
        getPrefs(context).edit().putBoolean("centerChatTitle", value).apply();
    }
}
