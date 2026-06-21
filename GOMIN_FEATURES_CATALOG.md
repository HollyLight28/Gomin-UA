# Гомін — Каталог функцій для перенесення в нову базу (Telegram 12.8.1)

> Створено: 21.06.2026
> Поточний Гомін: Telegram 12.5.0 + Cherrygram 1.0.0
> Нова база: Telegram 12.8.1 (6916) — github.com/DrKLO/Telegram

---

## Частина 1: УНІКАЛЬНІ ФІЧІ ГОМІНА (обов'язкові до перенесення)

### 🤖 Gomin AI (Gemini)

**Розташування:** `uz/unnarsx/cherrygram/chats/gemini/` (14 файлів)

- [ ] **Чат-режим з AI-ботом** — `GominAiChatHelper.kt` (1091 рядків)
  - Multi-turn діалог, історія в JSON, вибір моделі
  - Україномовний системний промпт (300+ рядків)
  - Самовідновлення чергування ролей

- [ ] **Gemini Live Voice (WebSocket)** — `GominLiveManager.kt` (912 рядків)
  - Voice Call режим: 16kHz → PCM → WebSocket → PCM 24kHz
  - Transcription режим: голос → текст з дедуплікацією
  - Аудіо-пайплайн: AudioRecord, AudioTrack, AEC, NoiseSuppressor
  - Модель: gemini-3.1-flash-live-preview
  - Підтверджено unit-тестами (`GominLiveManagerPayloadTest.kt`)

- [ ] **Gomin Microphone Service** — `GominMicrophoneService.kt` (133 рядки)
  - Foreground-сервіс для Android 14+ (зелена крапка)

- [ ] **Edge-Glow візуалізація** — `GominLiveEdgeGlowView.kt` (209 рядків)
  - RGB-рамка під час Live сесії
  - 3 колірних режими: idle / user speaking / AI speaking

- [ ] **Gemini SDK (контекстне меню)** — `GeminiSDKImplementation.java` (404 рядки)
  - Переклад, підсумовування, OCR, опис фото, транскрипція

- [ ] **Gemini Buttons Layout** — `GeminiButtonsLayout.java`
  - UI для вибору дії над медіа

- [ ] **Gemini Results Bottom Sheet** — `GeminiResultsBottomSheet.java` (910 рядків)

- [ ] **API-клієнт моделей** — `network/ApiClient.java`, `network/ModelInfo.java`
  - Динамічний список доступних моделей

---

### 🚨 Повітряні тривоги

**Розташування:** `uz/unnarsx/cherrygram/alerts/` (5 файлів)

- [ ] **AirAlertController** — `AirAlertController.kt` (308 рядків)
  - Опитування сервера кожні 60с
  - 26 регіонів України
  - Firebase push-підписка
  - 12-годинний safety timeout

- [ ] **AirAlertNotificationHelper** — `AirAlertNotificationHelper.kt` (171 рядок)
  - 3 канали сповіщень (критичний, інфо, тихий)
  - Сирена + вібрація

- [ ] **AirAlertStopReceiver** — `AirAlertStopReceiver.kt`
- [ ] **AirAlertHelper** — `AirAlertHelper.kt`
- [ ] **AirAlertStatsActivity** — `AirAlertStatsActivity.kt` (243 рядки)

**Звуки:** `res-cherrygram/raw/gomin_siren`, `res-cherrygram/raw/gomin_cancel`

---

### 🛡️ Gomin Shield

- [ ] **GominShieldBottomSheet** — `GominShieldBottomSheet.kt` (399 рядків)
  - Психологічний аналіз до 1500 повідомлень
  - Системний промпт на 370 рядків українською
  - 25+ маніпулятивних технік
  - Структурований звіт (8 секцій)
  - Кешування результатів

- [ ] **Методи в GominAiChatHelper** — `getMessagesForGominShield()`, Shield-промпт

---

### 👻 Ghost Mode

- [ ] **Невидиме читання** — `ghostModeReadMessages`
- [ ] **Прихований статус друку** — `ghostModeHideTyping`
- [ ] **Анонімні історії** — `ghostModeHideStoryViews`
- [ ] **Прихований онлайн** — `ghostModeHideOnline`
- [ ] **Анти-видалення** — `keepDeletedMessages`
- [ ] **Біометричний захист чатів** — через `CherrygramPrivacyConfig`
- [ ] **Kaboom Widget** — аварійне видалення даних

---

### ⚡ Gomin Speed Engine

- [ ] **Download Boost** — 12 TCP-потоків × 1MB (Max)
- [ ] **Upload Boost** — буфер 512KB
- [ ] **Shelter Mode** — 1 потік × 32KB для слабкої мережі
- [ ] **AudioEnhance** — вибір джерела мікрофона

---

### ⚫ Black Edition / Карбон

- [ ] **GominBlackEditionActivity** — `GominBlackEditionActivity.kt` (144 рядки)
  - OLED-тема (Monet Light / Monet AMOLED)
  - Філософський маніфест

---

### 📦 Кастомні ресурси

**Директорія:** `res-cherrygram/` (242 файли)

- [ ] 17 варіантів іконок додатку (mipmap-xxxhdpi)
- [ ] Українська локалізація: `values-uk/cg_strings.xml` (500+ рядків)
- [ ] Звуки: `gomin_siren`, `gomin_cancel`, `sound_in_ios.mp3`
- [ ] Логотип: `cg_logo_bird.png`, векторний `icon_foreground_gomin_white.xml`
- [ ] Анімації: `round_overlay_animation_cherry.json`, `cg_star_reaction.json`

---

### 🔧 Збірка

- [ ] `APP_PACKAGE=ua.gomin.messenger`
- [ ] `APP_VERSION_NAME_CHERRY=1.0.0`
- [ ] Автокопіювання APK на робочий стіл
- [ ] `Gomin-{CHERRY_VER}-TG-{TG_VER}-{abi}.apk`
- [ ] Залежності: `generativeai:0.9.0`, `okhttp:4.12.0`, `jackson-databind`

---

## Частина 2: ФІЧІ ЧЕРІГРАМА (вибірково)

> Вибирай тільки те що реально потрібно. Не тягни все підряд.

### 🎯 Що варто перенести (high priority)

- [ ] **Solar Icons** — `SolarIconReplace.kt` (280+ замін іконок)
- [ ] **CameraX** — `camera/CameraXController.java` + 10 файлів (кастомна камера)
- [ ] **Перекладач** — `chats/translator/Translator.java` (3 файли)
- [ ] **JSON Viewer** — `JsonBottomSheet.java`
- [ ] **Головні вкладки** — `MainTabsManager.kt` (порядок вкладок)
- [ ] **Налаштування меню** — `MessageMenuPreferencesEntry.java`
- [ ] **OTA-оновлення** — `updater/UpdaterUtils.java`

### 🤔 Що можна перенести (medium priority)

- [ ] **Таймер сну** — `SleepHelper.kt`
- [ ] **Резервне копіювання** — `BackupHelper.kt` (експорт/імпорт налаштувань)
- [ ] **Біометричний захист** — `CGBiometricPrompt.java`, `ChatsPasswordHelper.kt`
- [ ] **Фільтри повідомлень** — `MessagesFilterHelper.java`
- [ ] **Пружинні анімації** — `springAnimation`
- [ ] **Системні шрифти** — `FontHelper.java`
- [ ] **Material You** — `MonetHelper.java`

### 👎 Що можна пропустити (low priority)

- [ ] Система донатів (DonatesManager, BadgeHelper) — якщо не плануєш збирати
- [ ] Firebase Analytics / Crashlytics — якщо не треба
- [ ] Експериментальні фічі
- [ ] Тематичні ресурси (.ny, .hw, .cn)
- [ ] Huawei-модуль (вже вимкнено)

---

## Частина 3: ЯК ОНОВЛЮВАТИСЯ В МАЙБУТНЬОМУ

```bash
# Один раз налаштувати:
cd Gomin-NG
git remote add telegram https://github.com/DrKLO/Telegram.git

# Кожного разу коли Telegram викочує оновлення:
git fetch telegram
git merge telegram/master
# Вирішити конфлікти → git commit → git push
```

**Важливо:** ніколи не мерж все одразу якщо відстав на багато версій.
Мерж по одній версії:

```bash
git merge <коміт-12.8.2>
# вирішити → збілдити → протестувати
git merge <коміт-12.9.0>
# вирішити → збілдити → протестувати
```

90% конфліктів будуть тупими: «Гомін» vs «Telegram» у UI-рядках.
Залишай «Гомін» і йдеш далі.

---

## Порядок перенесення (рекомендований)

1. **Спочатку інфраструктура:** пакет `ua.gomin.messenger`, ресурси, українська локалізація
2. **Базові фічі Черіграма:** Solar Icons, CameraX, вкладки, перекладач
3. **Ghost Mode** — бо він простий у перенесенні
4. **Speed Engine** — теж простий
5. **Black Edition** — суто візуальне
6. **Air Raid Alerts** — окремий модуль
7. **Gemini AI** — найскладніше, потребує OkHttp + generativeai SDK
8. **Gomin Shield** — залежить від Gemini AI

**Кожну фічу — окремим комітом.** Тестуй після кожного.
