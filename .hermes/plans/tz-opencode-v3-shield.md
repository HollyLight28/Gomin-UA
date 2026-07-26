# ТЗ для OpenCode — Задача 3: Gomin Shield — індикація кількості повідомлень

## 🎯 Мета

Покращити зворотній зв'язок в Gomin Shield: показувати реальну кількість повідомлень, знайдених в базі, і перевірити що ліміт 1500 працює.

## 📁 Файли для змін

### 1. `TMessagesProj/src/main/java/ua/gomin/messenger/ai/GominShieldBottomSheet.kt`

В `companion object`, метод `show()`. Знайти рядок:

```kotlin
GominMessagesStorageHelper.getMessagesForGominShield(currentAccount, dialogId, 1500) { messages ->
```

Ліміт 1500 — правильний. Залишити як є.

**Змінити** блок після отримання повідомлень (рядки ~298-318) — додати логування в `FileLog` кількості знайдених повідомлень:

Після рядка:
```kotlin
val sdf = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault())
```

Додати:
```kotlin
FileLog.d("GominShield: знайдено ${messages.size} повідомлень в базі для dialogId=$dialogId")
```

І в кінці після формування `historyList` (рядок ~316):
```kotlin
historyList.reverse()
val finalHistoryText = historyList.joinToString("\n").trim()
```

Додати:
```kotlin
FileLog.d("GominShield: з них текстових реплік: $actualCount")
```

### 2. Файл залишити без інших змін

Ліміт 1500 вже працює і повідомлення дістаються з SQLite напряму — це правильно.

## ✅ Очікуваний результат

- При виклику Shield в логах (`logcat | grep GominShield`) буде видно скільки повідомлень знайдено
- Можна перевірити чи дійсно всі 1500 використовуються
- Користувач бачить кількість реплік в UI (вже реалізовано через `counterTextView`)

## ❌ Заборони

- Не міняти ліміт з 1500 (він нормальний)
- Не переписувати логіку вибірки з SQLite
- Не додавати нові UI елементи
