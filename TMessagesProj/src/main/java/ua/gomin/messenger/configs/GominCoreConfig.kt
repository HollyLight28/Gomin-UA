package ua.gomin.messenger.configs

import android.content.Context
import android.content.SharedPreferences

/**
 * Основні налаштування Гоміна.
 * Зберігає feature toggles в SharedPreferences.
 */
object GominCoreConfig {

    private const val PREFS_NAME = "gomin_core_config"

    // Air Alert
    const val AIR_ALERT_DISABLED = 0
    const val AIR_ALERT_ENABLED = 1

    // Download Speed Boost
    const val BOOST_NONE = 0
    const val BOOST_AVERAGE = 1
    const val BOOST_EXTREME = 2

    // Animation
    const val ANIMATION_CLASSIC = 0
    const val ANIMATION_SPRING = 1

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    // Air Alert
    fun getAirAlertEnabled(context: Context): Boolean =
        getPrefs(context).getBoolean("airAlertEnabled", false)

    fun setAirAlertEnabled(context: Context, enabled: Boolean) {
        getPrefs(context).edit().putBoolean("airAlertEnabled", enabled).apply()
    }

    fun getAirAlertRegionId(context: Context): String =
        getPrefs(context).getString("airAlertRegionId", "") ?: ""

    fun setAirAlertRegionId(context: Context, regionId: String) {
        getPrefs(context).edit().putString("airAlertRegionId", regionId).apply()
    }

    fun getAirAlertRegionName(context: Context): String =
        getPrefs(context).getString("airAlertRegionName", "") ?: ""

    fun setAirAlertRegionName(context: Context, regionName: String) {
        getPrefs(context).edit().putString("airAlertRegionName", regionName).apply()
    }

    // Speed Engine
    fun getDownloadSpeedBoost(context: Context): Int =
        getPrefs(context).getInt("downloadSpeedBoost", BOOST_EXTREME)

    fun setDownloadSpeedBoost(context: Context, level: Int) {
        getPrefs(context).edit().putInt("downloadSpeedBoost", level).apply()
    }

    fun getUploadSpeedBoost(context: Context): Boolean =
        getPrefs(context).getBoolean("uploadSpeedBoost", false)

    fun setUploadSpeedBoost(context: Context, enabled: Boolean) {
        getPrefs(context).edit().putBoolean("uploadSpeedBoost", enabled).apply()
    }

    fun getSlowNetworkMode(context: Context): Boolean =
        getPrefs(context).getBoolean("slowNetworkMode", false)

    fun setSlowNetworkMode(context: Context, enabled: Boolean) {
        getPrefs(context).edit().putBoolean("slowNetworkMode", enabled).apply()
    }

    // Animation
    fun getSpringAnimation(context: Context): Int =
        getPrefs(context).getInt("springAnimation", ANIMATION_SPRING)

    fun setSpringAnimation(context: Context, type: Int) {
        getPrefs(context).edit().putInt("springAnimation", type).apply()
    }

    // Appearance
    fun getHideStories(context: Context): Boolean =
        getPrefs(context).getBoolean("hideStories", false)

    fun setHideStories(context: Context, hidden: Boolean) {
        getPrefs(context).edit().putBoolean("hideStories", hidden).apply()
    }

    // Build info
    fun isDevBuild(): Boolean = false
    fun isStandalonePremiumBuild(): Boolean = false
}
