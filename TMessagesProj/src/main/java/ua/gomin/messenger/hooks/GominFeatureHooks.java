package ua.gomin.messenger.hooks;

/**
 * Хуки для інтеграції фіч Гоміна в код Telegram.
 * Викликаються з мінімальними змінами в org.telegram.* файлах.
 */
public class GominFeatureHooks {

    public static final GominFeatureHooks INSTANCE = new GominFeatureHooks();

    /**
     * Перевіряє чи Ghost Mode увімкнений для читання.
     */
    public boolean shouldGhostRead() {
        // TODO: Implement Ghost Read logic
        return false;
    }

    /**
     * Перевіряє чи треба приховувати друкування.
     */
    public boolean shouldHideTyping() {
        // TODO: Implement Hide Typing logic
        return false;
    }

    /**
     * Перевіряє чи треба приховувати онлайн.
     */
    public boolean shouldHideOnline() {
        // TODO: Implement Hide Online logic
        return false;
    }

    /**
     * Перевіряє чи зберігати видалені повідомлення.
     */
    public boolean shouldKeepDeleted() {
        // TODO: Implement Keep Deleted logic
        return false;
    }
}
