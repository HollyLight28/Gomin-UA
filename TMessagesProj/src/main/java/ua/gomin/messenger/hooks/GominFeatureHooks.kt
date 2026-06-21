package ua.gomin.messenger.hooks

/**
 * Хуки для інтеграції фіч Гоміна в код Telegram.
 * Викликаються з мінімальними змінами в org.telegram.* файлах.
 */
object GominFeatureHooks {

    /**
     * Перевіряє чи Ghost Mode увімкнений для читання.
     */
    fun shouldGhostRead(): Boolean {
        // TODO: Implement Ghost Read logic
        return false
    }

    /**
     * Перевіряє чи треба приховувати друкування.
     */
    fun shouldHideTyping(): Boolean {
        // TODO: Implement Hide Typing logic
        return false
    }

    /**
     * Перевіряє чи треба приховувати онлайн.
     */
    fun shouldHideOnline(): Boolean {
        // TODO: Implement Hide Online logic
        return false
    }

    /**
     * Перевіряє чи зберігати видалені повідомлення.
     */
    fun shouldKeepDeleted(): Boolean {
        // TODO: Implement Keep Deleted logic
        return false
    }
}
