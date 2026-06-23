# CLAUDE.md — Конституція проєкту GOMIN.UA

> Цей файл є **обов'язковим** для прочитання кожним AI, який працює з цим кодом.
> Він визначає архітектуру, стиль коду, заборони та стандарти.
> **Не дотримуєшся — переписуєш.**

---

## Ідентичність проєкту

**GOMIN.UA (Gomin-UA)** — це Український форк Telegram для Android, побудований на **чистому Telegram** (github.com/DrKLO/Telegram).

**ВАЖЛИВО: Gomin-UA НЕ є форком Cherrygram.** Ніякого коду з `uz.unnarsx.cherrygram.*` в цьому проєкті НЕМАЄ і НІКОЛИ НЕ БУДЕ.

---

## Архітектура: Правило "Надбудови"

**Головне правило — МІНІМАЛЬНЕ ВТРУЧАННЯ В КОД ТЕЛЕГРАМА.**

```
┌─────────────────────────────────────┐
│  ua.gomin.messenger.*               │  ← ВЕСЬ наш код тут
├─────────────────────────────────────┤
│  org.telegram.*                      │  ← Код Telegram (мінімально змінюємо)
└─────────────────────────────────────┘
```

### Правила:

1. **ВСІ фічі Гоміна** пишуться в пакеті `ua.gomin.messenger.*`
2. **У код Telegram вносимо ТІЛЬКИ те, що неможливо зробити інакше**
3. **Кожна зміна в коді Telegram позначається:**
   ```java
   /** Gomin start */
   // твій код
   /** Gomin end */
   ```
4. **Максимум 10 рядків змін на файл** в коді Telegram
5. **НЕ копіювати код із Cherrygram** (старий Гомін/`uz.unnarsx.cherrygram.*`)
6. **НЕ використовувати пакет `uz.unnarsx.*` взагалі**

---

## Структура пакетів

```
ua.gomin.messenger/
├── configs/                 # Feature toggles (SharedPreferences)
│   ├── GominCoreConfig.kt
│   ├── GominPrivacyConfig.kt
│   ├── GominChatsConfig.kt
│   ├── GominAppearanceConfig.kt
│   ├── GominCameraConfig.kt
│   ├── GominMessagesConfig.kt
│   └── GominExperimentalConfig.kt
├── preferences/             # Settings UI
│   ├── GominSettingsEntry.java
│   ├── GominPreferencesNavigator.kt
│   └── SettingsHelper.java
├── ai/                      # Gomin AI (Google Gemini)
├── shield/                  # Gomin Shield
├── alerts/                  # Повітряні тривоги
├── ghost/                   # Ghost Mode
├── speed/                   # Speed Engine
├── black/                   # Black Edition
├── hooks/                   # Хуки для Telegram
└── helpers/                 # Допоміжні утиліти
```

---

## ЗАБОРОНЕНО

| Що | Чому |
|----|------|
| `uz.unnarsx.cherrygram.*` | Це Cherrygram, ми від нього відмовились |
| `res-cherrygram/` | Використовуємо `res-gomin/` |
| Firebase Analytics/Crashlytics | Не тягни зайві залежності |
| Копіпаст без змін | Треба переписувати під `ua.gomin.messenger.*` |
| >10 рядків змін у Telegram файлах | Порушує принцип надбудови |

---

## Команди збірки

- **Основна збірка (Standalone Release)**: `./gradlew assembleAfatStandalone` (Локально завжди збираємо релізну автономну версію)
- **Google Play AAB**: `./gradlew bundleAfatRelease`
- **Встановити Standalone**: `./gradlew installAfatStandalone`
- **Очистити**: `./gradlew clean`

Вимоги: JDK 17, Android SDK 36, NDK 21.4.7075529.

---

## Стандарти коду

- Максимум 250 рядків на файл
- Конфіги — Kotlin object over SharedPreferences
- Event bus — `NotificationCenter`
- Threading — `Utilities.globalQueue` / `AndroidUtilities.runOnUIThread`
- Ресурси — `res-gomin/` з `values-uk/gomin_strings.xml`
- Автотести не пишуться через несумісність тестового фреймворку Telegram. Тестування проводиться вручну.

---

## Каталог фіч

Дивись **[GOMIN_FEATURES_CATALOG.md](./GOMIN_FEATURES_CATALOG.md)**

---

## Головне правило

> **Весь код Гоміна — в `ua.gomin.messenger.*`.**
> **У коді Telegram — тільки "ворота" (entry points).**
> **Ніякого Cherrygram. Ніколи.**
