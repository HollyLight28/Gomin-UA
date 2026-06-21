package ua.gomin.messenger.configs

import android.content.Context
import android.content.SharedPreferences

/**
 * Експериментальні налаштування.
 */
object GominExperimentalConfig {

    private const val PREFS_NAME = "gomin_experimental_config"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun getTranslatorEnabled(context: Context): Boolean =
        getPrefs(context).getBoolean("translatorEnabled", false)

    fun setTranslatorEnabled(context: Context, enabled: Boolean) {
        getPrefs(context).edit().putBoolean("translatorEnabled", enabled).apply()
    }

    fun getJsonViewerEnabled(context: Context): Boolean =
        getPrefs(context).getBoolean("jsonViewerEnabled", false)

    fun setJsonViewerEnabled(context: Context, enabled: Boolean) {
        getPrefs(context).edit().putBoolean("jsonViewerEnabled", enabled).apply()
    }
}
