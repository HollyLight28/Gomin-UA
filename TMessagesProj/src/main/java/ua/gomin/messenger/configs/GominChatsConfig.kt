package ua.gomin.messenger.configs

import android.content.Context
import android.content.SharedPreferences

/**
 * Налаштування чатів.
 */
object GominChatsConfig {

    private const val PREFS_NAME = "gomin_chats_config"

    // Notification sounds
    const val NOTIF_SOUND_DEFAULT = 0
    const val NOTIF_SOUND_GOMIN_1 = 1
    const val NOTIF_SOUND_GOMIN_2 = 2
    const val NOTIF_SOUND_GOMIN_3 = 3

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun getAutoQuoteReplies(context: Context): Boolean =
        getPrefs(context).getBoolean("autoQuoteReplies", false)

    fun setAutoQuoteReplies(context: Context, enabled: Boolean) {
        getPrefs(context).edit().putBoolean("autoQuoteReplies", enabled).apply()
    }

    fun getCustomWallpapers(context: Context): Boolean =
        getPrefs(context).getBoolean("customWallpapers", false)

    fun setCustomWallpapers(context: Context, enabled: Boolean) {
        getPrefs(context).edit().putBoolean("customWallpapers", enabled).apply()
    }

    fun getNotificationSound(context: Context): Int =
        getPrefs(context).getInt("notificationSound", NOTIF_SOUND_DEFAULT)

    fun setNotificationSound(context: Context, sound: Int) {
        getPrefs(context).edit().putInt("notificationSound", sound).apply()
    }

    fun getDisableVibration(context: Context): Boolean =
        getPrefs(context).getBoolean("disableVibration", false)

    fun setDisableVibration(context: Context, disabled: Boolean) {
        getPrefs(context).edit().putBoolean("disableVibration", disabled).apply()
    }

    fun getCenterChatTitle(context: Context): Boolean =
        getPrefs(context).getBoolean("centerChatTitle", false)

    fun setCenterChatTitle(context: Context, enabled: Boolean) {
        getPrefs(context).edit().putBoolean("centerChatTitle", enabled).apply()
    }
}
