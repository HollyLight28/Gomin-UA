# ТЗ для OpenCode — Задача 4: Анти-видалення діалогу (KeepDeleted)

## 🎯 Мета

При включеному `keepDeletedMessages` — повністю блокувати видалення повідомлень/діалогів з локальної БД, коли співрозмовник видаляє "для всіх". Без змін UI. Просто не видаляти.

## 📁 Файли та зміни

### Файл 1: `TMessagesProj/src/main/java/org/telegram/messenger/MessagesController.java`

**Хук 1: `deleteMessages()` — особисті чати (рядки ~9299-9314)**

Знайти блок:
```java
} else {
    if (channelId == 0) {
        for (int a = 0; a < messages.size(); a++) {
            Integer id = messages.get(a);
            MessageObject obj = dialogMessagesByIds.get(id);
            if (obj != null) {
                obj.deleted = true;
            }
        }
    } else {
        markDialogMessageAsDeleted(dialogId, messages);
    }
    getMessagesStorage().markMessagesAsDeleted(dialogId, messages, true, forAll, 0, topicId);
    getMessagesStorage().updateDialogsWithDeletedMessages(dialogId, channelId, messages, null);
}
getNotificationCenter().postNotificationName(NotificationCenter.messagesDeleted, messages, channelId, scheduled, false, movedToScheduled, movedToScheduledMessageId);
```

Замінити на:
```java
} else {
    /** Gomin start — Anti-delete: перевіряємо keepDeletedMessages */
    if (channelId == 0) {
        for (int a = 0; a < messages.size(); a++) {
            Integer id = messages.get(a);
            MessageObject obj = dialogMessagesByIds.get(id);
            if (obj != null) {
                obj.deleted = true;
            }
        }
    } else {
        markDialogMessageAsDeleted(dialogId, messages);
    }
    if (!(forAll && ua.gomin.messenger.hooks.GominFeatureHooks.INSTANCE.shouldKeepDeleted())) {
        getMessagesStorage().markMessagesAsDeleted(dialogId, messages, true, forAll, 0, topicId);
        getMessagesStorage().updateDialogsWithDeletedMessages(dialogId, channelId, messages, null);
    }
    /** Gomin end */
}
if (!(forAll && ua.gomin.messenger.hooks.GominFeatureHooks.INSTANCE.shouldKeepDeleted())) {
    getNotificationCenter().postNotificationName(NotificationCenter.messagesDeleted, messages, channelId, scheduled, false, movedToScheduled, movedToScheduledMessageId);
}
```

**Пояснення логіки:**
- Якщо `forAll == true` (співрозмовник видалив для всіх) І `shouldKeepDeleted() == true` (ввімкнено анти-видалення) — пропускаємо `markMessagesAsDeleted()` і `postNotificationName()`
- Повідомлення залишаються в БД
- `obj.deleted = true` все одно ставиться (повідомлення позначиться як видалене в пам'яті, але не видаляється з БД)
- Потрібен саме такий підхід: перевірка forAll всередині, щоб не блокувати власне видалення користувачем

**ВАЖЛИВО:** Використовувати повну назву пакету `ua.gomin.messenger.hooks.GominFeatureHooks.INSTANCE.shouldKeepDeleted()`, бо в цьому файлі немає import для GominFeatureHooks.

## ✅ Очікуваний результат

- При включеному `keepDeletedMessages`: співрозмовник видаляє діалог/повідомлення "для всіх" — у Вови нічого не зникає
- При вимкненому: все працює як стандартний Telegram
- Ніяких змін UI, іконок, сповіщень
- Gomin Shield може аналізувати всі повідомлення навіть після "видалення"

## ❌ Заборони

- Не міняти ніякі інші файли
- Не додавати нові іконки, тексти, UI елементи
- Не міняти логіку `deleteDialog()` — тільки `deleteMessages()`
- Не міняти імпорти (пакет вказувати повністю)
