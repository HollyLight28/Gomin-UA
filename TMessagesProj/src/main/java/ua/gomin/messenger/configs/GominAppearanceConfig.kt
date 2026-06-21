package ua.gomin.messenger.configs

import android.content.Context
import android.content.SharedPreferences

/**
 * Налаштування зовнішнього вигляду.
 */
object GominAppearanceConfig {

    private const val PREFS_NAME = "gomin_appearance_config"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun getHideSearchFiled(context: Context): Boolean =
        getPrefs(context).getBoolean("hideSearchFiled", false)

    fun setHideSearchFiled(context: Context, hidden: Boolean) {
        getPrefs(context).edit().putBoolean("hideSearchFiled", hidden).apply()
    }

    fun getShowMainTabs(context: Context): Boolean =
        getPrefs(context).getBoolean("showMainTabs", false)

    fun setShowMainTabs(context: Context, enabled: Boolean) {
        getPrefs(context).edit().putBoolean("showMainTabs", enabled).apply()
    }

    fun getProfileBackgroundColor(context: Context): Boolean =
        getPrefs(context).getBoolean("profileBackgroundColor", false)

    fun setProfileBackgroundColor(context: Context, enabled: Boolean) {
        getPrefs(context).edit().putBoolean("profileBackgroundColor", enabled).apply()
    }

    fun getCenterTitle(context: Context): Boolean =
        getPrefs(context).getBoolean("centerTitle", false)

    fun setCenterTitle(context: Context, enabled: Boolean) {
        getPrefs(context).edit().putBoolean("centerTitle", enabled).apply()
    }

    fun getDrawSnowInActionBar(context: Context): Boolean =
        getPrefs(context).getBoolean("drawSnowInActionBar", false)

    fun setDrawSnowInActionBar(context: Context, enabled: Boolean) {
        getPrefs(context).edit().putBoolean("drawSnowInActionBar", enabled).apply()
    }
}
