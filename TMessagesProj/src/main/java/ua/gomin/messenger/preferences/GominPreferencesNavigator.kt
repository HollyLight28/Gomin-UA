package ua.gomin.messenger.preferences

import org.telegram.ui.ActionBar.BaseFragment

/**
 * Навігація між екранами налаштувань Гоміна.
 */
object GominPreferencesNavigator {

    fun createGominSettings(fragment: BaseFragment) {
        fragment.presentFragment(GominSettingsEntry())
    }

    fun createAbout(fragment: BaseFragment) {
        // TODO: AboutPreferencesEntry
    }

    fun createGemini(fragment: BaseFragment) {
        // TODO: GeminiPreferencesEntry
    }

    fun createDebug(fragment: BaseFragment) {
        // TODO: DebugPreferencesEntry
    }
}
