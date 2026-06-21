package ua.gomin.messenger.configs

import android.content.Context
import android.content.SharedPreferences

/**
 * Налаштування камери.
 */
object GominCameraConfig {

    private const val PREFS_NAME = "gomin_camera_config"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun getUseDualCamera(context: Context): Boolean =
        getPrefs(context).getBoolean("useDualCamera", false)

    fun setUseDualCamera(context: Context, enabled: Boolean) {
        getPrefs(context).edit().putBoolean("useDualCamera", enabled).apply()
    }
}
