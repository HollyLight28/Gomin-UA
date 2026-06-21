package ua.gomin.messenger.configs

import android.content.Context
import android.content.SharedPreferences

/**
 * Налаштування приватності та Ghost Mode.
 */
object GominPrivacyConfig {

    private const val PREFS_NAME = "gomin_privacy_config"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    // Ghost Mode
    fun getGhostModeReadMessages(context: Context): Boolean =
        getPrefs(context).getBoolean("ghostModeReadMessages", false)

    fun setGhostModeReadMessages(context: Context, enabled: Boolean) {
        getPrefs(context).edit().putBoolean("ghostModeReadMessages", enabled).apply()
    }

    fun getGhostModeHideTyping(context: Context): Boolean =
        getPrefs(context).getBoolean("ghostModeHideTyping", false)

    fun setGhostModeHideTyping(context: Context, enabled: Boolean) {
        getPrefs(context).edit().putBoolean("ghostModeHideTyping", enabled).apply()
    }

    fun getGhostModeHideStoryViews(context: Context): Boolean =
        getPrefs(context).getBoolean("ghostModeHideStoryViews", false)

    fun setGhostModeHideStoryViews(context: Context, enabled: Boolean) {
        getPrefs(context).edit().putBoolean("ghostModeHideStoryViews", enabled).apply()
    }

    fun getGhostModeHideOnline(context: Context): Boolean =
        getPrefs(context).getBoolean("ghostModeHideOnline", false)

    fun setGhostModeHideOnline(context: Context, enabled: Boolean) {
        getPrefs(context).edit().putBoolean("ghostModeHideOnline", enabled).apply()
    }

    // Anti-delete
    fun getKeepDeletedMessages(context: Context): Boolean =
        getPrefs(context).getBoolean("keepDeletedMessages", false)

    fun setKeepDeletedMessages(context: Context, enabled: Boolean) {
        getPrefs(context).edit().putBoolean("keepDeletedMessages", enabled).apply()
    }

    // Biometric security
    fun getAskBiometricsToOpenChat(context: Context): Boolean =
        getPrefs(context).getBoolean("askBiometricsToOpenChat", false)

    fun setAskBiometricsToOpenChat(context: Context, enabled: Boolean) {
        getPrefs(context).edit().putBoolean("askBiometricsToOpenChat", enabled).apply()
    }

    fun getAskBiometricsToOpenEncrypted(context: Context): Boolean =
        getPrefs(context).getBoolean("askBiometricsToOpenEncrypted", false)

    fun setAskBiometricsToOpenEncrypted(context: Context, enabled: Boolean) {
        getPrefs(context).edit().putBoolean("askBiometricsToOpenEncrypted", enabled).apply()
    }

    fun getAskBiometricsToOpenArchive(context: Context): Boolean =
        getPrefs(context).getBoolean("askBiometricsToOpenArchive", false)

    fun setAskBiometricsToOpenArchive(context: Context, enabled: Boolean) {
        getPrefs(context).edit().putBoolean("askBiometricsToOpenArchive", enabled).apply()
    }

    fun getAskPasscodeBeforeDelete(context: Context): Boolean =
        getPrefs(context).getBoolean("askPasscodeBeforeDelete", false)

    fun setAskPasscodeBeforeDelete(context: Context, enabled: Boolean) {
        getPrefs(context).edit().putBoolean("askPasscodeBeforeDelete", enabled).apply()
    }

    fun getAllowSystemPasscode(context: Context): Boolean =
        getPrefs(context).getBoolean("allowSystemPasscode", false)

    fun setAllowSystemPasscode(context: Context, enabled: Boolean) {
        getPrefs(context).edit().putBoolean("allowSystemPasscode", enabled).apply()
    }
}
