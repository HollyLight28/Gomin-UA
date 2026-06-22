# gomin-ua-init - Work Plan

## TL;DR (For humans)

**What you'll get:** CLAUDE.md (конституція для AI), структура пакета Гоміна (`ua.gomin.messenger.*`) і головна сторінка налаштувань Гоміна, яка відкривається з профілю. Всі фічі — як надбудови над чистим Telegram, без Cherrygram.

**Why this approach:** Старий Гомін був форком Cherrygram — 1.5 місяці роботи втрачено через конфлікти при мержі. Нова архітектура "надбудова" мінімізує зміни в коді Telegram (5-10 рядків на файл), всі фічі живуть у своєму пакеті — мерж стає простим.

**What it will NOT do:** Не буде коду з Cherrygram (`uz.unnarsx.cherrygram.*`), не буде важких змін у Telegram, не буде копіпасти зі старого Гоміна, не буде Firebase без потреби.

**Effort:** Large
**Risk:** Medium — нова архітектура, але чітко прописана
**Decisions to sanity-check:** CLAUDE.md правила, структура пакетів, порядок перенесення фіч

Your next move: Approve the plan, then run `$start-work` to execute.

---

> TL;DR (machine): Large effort, Medium risk. Create CLAUDE.md constitution, scaffold `ua.gomin.messenger.*`, build settings menu as UniversalFragment, add minimal Telegram hooks. 10 waves, ~30+ todos total.

## Scope
### Must have
- CLAUDE.md — проектна конституція (правила, заборони, архітектура)
- Пакет `ua.gomin.messenger.*` з усією структурою
- GominSettingsEntry.java — головна сторінка налаштувань Гоміна
- Хуки в ProfileActivity.java для входу в налаштування
- Порт всіх фіч з Частини 1 GOMIN_FEATURES_CATALOG.md
- Вибірковий порт фіч з Частини 2
- Unit-тести для кожної фічі

### Must NOT have (guardrails, anti-slop, scope boundaries)
- Жодного `uz.unnarsx.cherrygram.*`
- Жодного `res-cherrygram/` — тільки `res-gomin/`
- Жодного копіпасту без переписування
- Не більше 10 рядків змін у Telegram-файлах
- Firebase/Crashlytics тільки якщо user явно скаже

## Verification strategy
> Zero human intervention - all verification is agent-executed.
- Test decision: TDD (write test first, then implementation)
- Framework: JUnit 4 + JSONObject + Mockito (де треба)
- Evidence: .omo/evidence/task-<N>-gomin-ua-init.txt

## Execution strategy
### Parallel execution waves
- Wave 1 (Foundation): CLAUDE.md + package scaffold + Gradle config + resource dirs
- Wave 2 (Configs): Всі Gomin*Config.kt (Core, Privacy, Chats, Appearance, Camera, Messages, Experimental)
- Wave 3 (Settings Menu): GominSettingsEntry.java + PreferencesNavigator + всі під-екрани
- Wave 4 (Telegram Hooks): ProfileActivity.java edits (minimal)
- Wave 5 (Ghost Mode + Security): Ghost + Biometric + DeleteAccount
- Wave 6 (Air Alert): Alerts module
- Wave 7 (Speed Engine): Network optimization
- Wave 8 (Black Edition): Theme
- Wave 9 (Gomin AI + Shield): Gemini integration (most complex)
- Wave 10 (Cherrygram select): Solar Icons, CameraX, Translator, etc.

### Dependency matrix
| Todo | Depends on | Blocks | Can parallelize with |
| --- | --- | --- | --- |
| 1. CLAUDE.md | — | 2 | — |
| 2. Package scaffold | 1 | 3-9 | — |
| 3. GominCoreConfig | 2 | 7-12 | 4,5,6 |
| 4. GominPrivacyConfig | 2 | 7 | 3,5,6 |
| 5. GominChatsConfig | 2 | — | 3,4,6 |
| 6. GominAppearanceConfig | 2 | — | 3,4,5 |
| 7. GominCameraConfig | 2 | — | 3,4,5,6 |
| 8. GominMessagesConfig | 2 | — | 3,4,5,6 |
| 9. GominExperimentalConfig | 2 | — | 3,4,5,6 |
| 10. GominPreferencesNavigator | 2 | 11 | — |
| 11. GominSettingsEntry | 10 | 12 | — |
| 12. Settings helpers | 11 | — | — |
| 13. Telegram hooks | 11 | — | — |
| 14-17. Ghost Mode | 4 | — | 18-24 |
| 18-20. Air Alert | 3 | — | 14-17,21-24 |
| 21-22. Speed Engine | 3 | — | 14-20,23-24 |
| 23-24. Black Edition | 3 | — | 14-22 |
| 25-31. Gomin AI | 3 | 32-38 | — |
| 32-38. Cherrygram port | 25-31 | — | — |

## Todos
> Implementation + Test = ONE todo. Never separate.
<!-- APPEND TASK BATCHES BELOW THIS LINE WITH edit/apply_patch - never rewrite the headers above. -->

### WAVE 1: Foundation
- [ ] 1. Створити CLAUDE.md — конституція проєкту
  What to do / Must NOT do: Написати CLAUDE.md в корінь Gomin-UA. Вміст: ідентичність (чистий Telegram, не Cherrygram), архітектура "надбудови", правила (ua.gomin.messenger.*, мінімум змін у Telegram, коментарі Gomin start/end), заборони (немає uz.unnarsx.*), команди збірки, стандарти коду, структура пакетів, тестування.
  Must NOT: Не містити згадок про Cherrygram як про базу — тільки як про те, що ми залишили.
  Parallelization: Wave 1 | Blocked by: — | Blocks: 2
  References: GOMIN_FEATURES_CATALOG.md (повний каталог), old Gomin/CLAUDE.md (зразок), draft gomin-ua-init.md (знахідки та рішення), CGPreferencesEntry.java (зразок меню)
  Acceptance criteria (agent-executable): Файл існує в G:\Code\Java\Gomin-UA\CLAUDE.md, містить розділи: Ідентичність, Архітектура, Структура пакетів, Заборони, Стандарти.
  QA scenarios: Прочитати файл, перевірити наявність ключових секцій.
  Commit: Y | feat(docs): create CLAUDE.md project constitution

- [ ] 2. Заскaffолдити структуру пакета `ua.gomin.messenger`
  What to do / Must NOT do: Створити директорії під `TMessagesProj/src/main/java/ua/gomin/messenger/` з підпапками: configs, preferences, ai, shield, alerts, ghost, speed, black, hooks, helpers. У кожній створити пустий `.gitkeep` (або базові файли). Створити `res-gomin/` і `res-gomin/values-uk/`.
  Must NOT: Не створювати `uz.unnarsx.*`, не копіювати нічого з Cherrygram.
  Parallelization: Wave 1 | Blocked by: 1 | Blocks: 3,4
  References: Стара структура `Gomin/TMessagesProj/src/main/java/uz/unnarsx/cherrygram/`
  Acceptance criteria: Існують всі зазначені директорії.
  QA scenarios: Перевірити `ls -R ua/gomin/messenger/` на наявність усіх підпапок.
  Commit: Y | feat(infra): scaffold ua.gomin.messenger package structure

### WAVE 2: Configs (Feature Toggles)
- [ ] 3. GominCoreConfig.kt
  What to do: Базові налаштування (AirAlert, SpringAnimation, HideStories, DownloadSpeedBoost, UploadSpeedBoost, SlowNetworkMode). Kotlin object over SharedPreferences.
  Must NOT: Не копіювати з CherrygramCoreConfig напряму — переписати.
  Parallelization: Wave 2 | Blocked by: 2 | Blocks: 7-12 | Can parallelize with: 4,5,6
  References: Old `CherrygramCoreConfig.kt` at `Gomin/.../cherrygram/core/configs/CherrygramCoreConfig.kt`
  Acceptance: Config об'єкт з усіма полями.
  Commit: Y | feat(config): GominCoreConfig with base features

- [ ] 4. GominPrivacyConfig.kt (Ghost Mode + Security)
  What to do: ghostModeReadMessages, ghostModeHideTyping, ghostModeHideStoryViews, ghostModeHideOnline, keepDeletedMessages, askBiometricsToOpenChat, etc.
  References: Old `CherrygramPrivacyConfig.kt`
  Parallelization: Wave 2 | Blocked by: 2 | Blocks: 7
  Commit: Y | feat(config): GominPrivacyConfig with Ghost Mode + Security

- [ ] 5. GominChatsConfig.kt
- [ ] 6. GominAppearanceConfig.kt
- [ ] 7. GominCameraConfig.kt
- [ ] 8. GominMessagesConfig.kt
- [ ] 9. GominExperimentalConfig.kt

### WAVE 3: Settings Menu
- [ ] 10. GominPreferencesNavigator.kt
  What to do: Навігаційний об'єкт з методами createGominSettings(), createGemini(), createAbout(), etc. Кожен викликає presentFragment() з відповідним фрагментом.
  References: Old `CherrygramPreferencesNavigator.kt`
  Parallelization: Wave 3 | Blocked by: 2 | Blocks: 11
  Commit: Y | feat(menu): GominPreferencesNavigator

- [ ] 11. GominSettingsEntry.java — головна сторінка налаштувань
  What to do: UniversalFragment з усіма секціями: Support card, About, 🤖 Gomin AI, 👻 Ghost Mode, 🚨 Air Alert, 🔊 Notification Sound, ⚡ Speed Engine, 📷 Camera, 💬 Chat Behavior, 🎨 Interface, 🛠️ Other, 🔒 Security. Кожен пункт — це UItem.asSwitchCG або UItem.asButton. Обробка onClick для кожного.
  Must NOT: Не використовувати Cherrygram-специфічні R.string, тільки нові Gomin-ресурси або стандартні Telegram.
  References: Old `CGPreferencesEntry.java` (861 рядків) — треба переписати повністю
  Parallelization: Wave 3 | Blocked by: 10 | Blocks: 12
  Acceptance: Settings screen показується і має всі секції.
  Commit: Y | feat(menu): GominSettingsEntry with all feature sections

- [ ] 12. Створити допоміжні класи: SettingsHelper.java (аналог старого), GominBulletinCreator.kt, PopupHelper.java
  References: Old `SettingsHelper.java`, `CGBulletinCreator.kt`, `PopupHelper.java`
  Commit: Y | feat(menu): settings helper utilities

### WAVE 4: Telegram Entry Hooks
- [ ] 13. Додати "Налаштування Гоміна" в ProfileActivity.java
  What to do: В ProfileActivity.java додати:
  1. `private int gominSettingsRow;` — поле класу
  2. `gominSettingsRow = rowCount++;` в методі fillItems (після settingsSectionRow)
  3. Рендер: `textCell.setTextAndIcon("Налаштування Гоміна", R.drawable.ic_launcher, true)` в onBindViewHolder
  4. onClick: `GominPreferencesNavigator.INSTANCE.createGominSettings(this);`
  ВСІ зміни обгорнути в `/** Gomin start */` / `/** Gomin end */`.
  Must NOT: Не змінювати більше ніж 10 рядків. Не чіпати інші частини ProfileActivity.
  References: Old code lines: 10552-10554 (row creation), 13698-13701 (render), 4329-4330 (onClick)
  Parallelization: Wave 4 | Blocked by: 11 | Blocks: —
  Acceptance: В профілі з'являється пункт "Налаштування Гоміна", який відкриває GominSettingsEntry.
  Commit: Y | feat(hooks): add Gomin settings entry in ProfileActivity

### WAVE 5: Ghost Mode + Security
- [ ] 14. GominGhostReadHelper (логіка невидимого читання)
- [ ] 15. Інтеграція Ghost Mode перемикачів в GominSettingsEntry
- [ ] 16. Біометричний захист (GominBiometricPrompt)
- [ ] 17. GominDeleteAccountDialog

### WAVE 6: Air Alert
- [ ] 18. GominAirAlertController (опитування сервера, 26 регіонів)
- [ ] 19. GominAirAlertNotificationHelper (3 канали, сирена)
- [ ] 20. Інтеграція Air Alert в меню

### WAVE 7: Speed Engine
- [ ] 21. GominSpeedEngineHelper (Download/Upload boost)
- [ ] 22. Інтеграція Speed Engine в меню (Speed Engine card)

### WAVE 8: Black Edition
- [ ] 23. GominBlackEditionActivity (OLED тема)
- [ ] 24. Інтеграція Black Edition в меню

### WAVE 9: Gomin AI + Shield
- [ ] 25. GominAiChatHelper (Multi-turn Gemini chat)
- [ ] 26. GominLiveManager (WebSocket Live Voice)
- [ ] 27. GominMicrophoneService
- [ ] 28. GominLiveEdgeGlowView
- [ ] 29. GominShieldBottomSheet
- [ ] 30. GeminiPreferencesEntry (налаштування Gemini)
- [ ] 31. API клієнт для моделей (GominAiApiClient)

### WAVE 10: Cherrygram Selective Port
- [ ] 32. Solar Icons (заміна іконок)
- [ ] 33. CameraX (кастомна камера)
- [ ] 34. Translator (перекладач)
- [ ] 35. JSON Viewer
- [ ] 36. Main Tabs (порядок вкладок)
- [ ] 37. Message Menu Preferences
- [ ] 38. OTA Updater

## Final verification wave
> Runs in parallel after ALL todos. ALL must APPROVE. Surface results and wait for the user's explicit okay before declaring complete.
- [ ] F1. Plan compliance audit — всі todo виконані, жодного Cherrygram-коду
- [ ] F2. Code quality review — структура пакетів, розмір файлів, коментарі
- [ ] F3. Real manual QA — збірка APK, перевірка меню, тест кожної фічі
- [ ] F4. Scope fidelity — немає зайвого коду, всі заборони дотримані

## Commit strategy
- Кожна фіча — окремий коміт
- Формат: `feat(scope): description` (напр. `feat(menu): add GominSettingsEntry`)
- Configs — однією серією комітів
- Settings menu — одним комітом
- Telegram hooks — одним комітом
- Кожна фіча (Ghost, Air Alert, Speed Engine, etc.) — окремим комітом

## Success criteria
1. CLAUDE.md існує і читається AI при старті
2. APK збирається без помилок
3. В профілі Telegram є пункт "Налаштування Гоміна"
4. Відкривається сторінка з усіма секціями фіч
5. Кожна фіча працює (Ghost Mode, Air Alert, Speed Engine, AI, etc.)
6. Жодного файлу з `uz.unnarsx.cherrygram` в проєкті
7. Максимум 10 рядків змін у кожному Telegram-файлі
