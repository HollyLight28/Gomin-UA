package ua.gomin.messenger.hooks;

import org.telegram.messenger.ApplicationLoader;
import ua.gomin.messenger.configs.GominPrivacyConfig;

/**
 * Хуки для інтеграції фіч Гоміна в код Telegram.
 * Викликаються з мінімальними змінами в org.telegram.* файлах.
 */
public class GominFeatureHooks {

    public static final GominFeatureHooks INSTANCE = new GominFeatureHooks();

    /** Gomin start */
    public boolean shouldGhostRead() {
        return GominPrivacyConfig.INSTANCE.getGhostModeReadMessages(ApplicationLoader.applicationContext);
    }

    public boolean shouldHideTyping() {
        return GominPrivacyConfig.INSTANCE.getGhostModeHideTyping(ApplicationLoader.applicationContext);
    }

    public boolean shouldHideOnline() {
        return GominPrivacyConfig.INSTANCE.getGhostModeHideOnline(ApplicationLoader.applicationContext);
    }

    public boolean shouldKeepDeleted() {
        return GominPrivacyConfig.INSTANCE.getKeepDeletedMessages(ApplicationLoader.applicationContext);
    }

    public boolean shouldHideStoryViews() {
        return GominPrivacyConfig.INSTANCE.getGhostModeHideStoryViews(ApplicationLoader.applicationContext);
    }
    /** Gomin end */
}
