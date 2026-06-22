package ua.gomin.messenger.preferences;

import org.telegram.ui.ActionBar.BaseFragment;

/**
 * Навігація між екранами налаштувань Гоміна.
 */
public class GominPreferencesNavigator {

    public static final GominPreferencesNavigator INSTANCE = new GominPreferencesNavigator();

    public void createGominSettings(BaseFragment fragment) {
        fragment.presentFragment(new GominSettingsEntry());
    }

    public void createAbout(BaseFragment fragment) {
        // TODO: AboutPreferencesEntry
    }

    public void createGemini(BaseFragment fragment) {
        // TODO: GeminiPreferencesEntry
    }

    public void createDebug(BaseFragment fragment) {
        // TODO: DebugPreferencesEntry
    }
}
