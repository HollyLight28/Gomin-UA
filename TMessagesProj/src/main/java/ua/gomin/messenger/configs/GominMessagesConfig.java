package ua.gomin.messenger.configs;

import android.content.Context;
import android.content.SharedPreferences;

public class GominMessagesConfig {

    public static final GominMessagesConfig INSTANCE = new GominMessagesConfig();
    private static final String PREFS_NAME = "gomin_messages_config";

    
    public static final int DOUBLE_TAP_ACTION_NONE = 0;
    public static final int DOUBLE_TAP_ACTION_REACTION = 1;
    public static final int DOUBLE_TAP_ACTION_REPLY = 2;
    public static final int DOUBLE_TAP_ACTION_SAVE = 3;
    public static final int DOUBLE_TAP_ACTION_EDIT = 4;
    public static final int DOUBLE_TAP_ACTION_TRANSLATE = 5;
    public static final int DOUBLE_TAP_ACTION_TRANSLATE_GEMINI = 6;
    public static final int MESSAGE_SLIDE_ACTION_REPLY = 0;
    public static final int MESSAGE_SLIDE_ACTION_SAVE = 1;
    public static final int MESSAGE_SLIDE_ACTION_TRANSLATE = 2;
    public static final int MESSAGE_SLIDE_ACTION_TRANSLATE_GEMINI = 3;
    public static final int MESSAGE_SLIDE_ACTION_DIRECT_SHARE = 4;
    public static final int TRANSCRIPTION_PROVIDER_GEMINI = 0;
    public static final int TRANSCRIPTION_PROVIDER_TELEGRAM = 1;

    private SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public boolean getDeleteForAll(Context context) {
        return getPrefs(context).getBoolean("deleteForAll", false);
    }
    public int getDoubleTapAction(Context context) {
        return getPrefs(context).getInt("doubleTapAction", DOUBLE_TAP_ACTION_NONE);
    }
    public int getMessageSlideAction(Context context) {
        return getPrefs(context).getInt("messageSlideAction", MESSAGE_SLIDE_ACTION_REPLY);
    }
    public int getVoiceTranscriptionProvider(Context context) {
        return getPrefs(context).getInt("voiceTranscriptionProvider", TRANSCRIPTION_PROVIDER_TELEGRAM);
    }
    public void setDeleteForAll(Context context, boolean value) {
        getPrefs(context).edit().putBoolean("deleteForAll", value).apply();
    }
    public void setDoubleTapAction(Context context, int value) {
        getPrefs(context).edit().putInt("doubleTapAction", value).apply();
    }
    public void setMessageSlideAction(Context context, int value) {
        getPrefs(context).edit().putInt("messageSlideAction", value).apply();
    }
    public void setVoiceTranscriptionProvider(Context context, int value) {
        getPrefs(context).edit().putInt("voiceTranscriptionProvider", value).apply();
    }
}
