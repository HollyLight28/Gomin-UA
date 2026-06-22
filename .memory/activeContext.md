# Active Context (Gomin-UA)

## Current Mission
The current goal is to ensure the core Gomin settings layout is integrated into the native Telegram UI, to manage hook registrations, verify local build compilation, and establish a framework for porting feature modules sequentially.

## Completed Steps
- **Gradle & Kotlin Config Refactoring**: 
  - Analyzed and resolved critical Kotlin JVM compile compatibility errors (`Class kotlin.Unit compiled with an incompatible version`) by disabling the `kotlin-android` plugin in the `TMessagesProj` build.gradle.
  - Converted the entire `ua.gomin.messenger.configs` package from Kotlin `object` declarations to standard Java singleton classes with Android `SharedPreferences` interaction.
- **Java Config Translation**:
  - Translated 7 configuration files: `GominAppearanceConfig`, `GominCameraConfig`, `GominChatsConfig`, `GominCoreConfig`, `GominExperimentalConfig`, `GominMessagesConfig`, and `GominPrivacyConfig`.
# CURRENT MISSION
1. Скласти системний план перенесення функціоналу (Air Alerts, Gomin AI, Ghost Mode, Speed Engine) зі старого Cherrygram-based проекту (Telegram 12.5.0) до нового DrKLO-based проекту (Telegram 12.8.1). [IN_PROGRESS]
2. Вирішити проблему падіння CI/CD збірок на GitHub Actions. [COMPLETED]

# COMPLETED ATOMIC STEPS
- Проаналізовано структуру папок `Gomin` (стара база) та `Gomin-UA` (нова база).
- Прочитано конфігураційні файли пам'яті `projectBrief.md`, `activeContext.md`, `systemPatterns.md` та `techContext.md`.
- Досліджено `LocaleController.java` та виявлено причину англійського інтерфейсу: `BuildVars.USE_CLOUD_STRINGS = false`.
- Виправлено локалізацію: активовано `BuildVars.USE_CLOUD_STRINGS = true` для завантаження хмарних мовних файлів.
- Створено та успішно інтегровано приватний статичний метод `applyBrandingReplacement` у `LocaleController.java` з регулярними виразами для безпечної заміни "Telegram" на "Гомін" / "Gomin" у методах `getStringInternal`, `getServerString`, `formatPluralStringComma`, `formatString` та `formatSpannable`.
- Успішно перевірено компіляцію проекту локально за допомогою Gradle (Java-частина збирається без помилок за 3хв 39с).
- Діагностовано причину падіння GitHub Actions (конфлікт двох workflow, брак пам'яті на раннері через `-Xmx7g` і збій застарілих екшенів).
- Оптимізовано `.github/workflows/build.yml`: увімкнено кешування Gradle, додано автоматичне ліцензування Android SDK та знижено ліміт JVM heap до `-Xmx4g` для запобігання OOM крашу.
- Видалено дублюючий та багнутий workflow-файл `.github/workflows/release.yml`.
- Проведено безпековий рефакторинг підпису додатку:
  - Вилучено `release.keystore` з індексу Git та додано до `.gitignore`, щоб запобігти витоку приватного ключа.
  - Оновлено [TMessagesProj_AppStandalone/build.gradle](file:///G:/Code/Java/Gomin-UA/TMessagesProj_AppStandalone/build.gradle) для безпечного зчитування паролів підпису з змінних оточення (з безпечним фолбеком на dummy-значення для локального білду).
  - Оновлено [.github/workflows/build.yml](file:///G:/Code/Java/Gomin-UA/.github/workflows/build.yml) для динамічного декодування Base64 ключа підпису (`SIGNING_KEY`) з використанням прапорця `--ignore-garbage`, щоб ігнорувати пробіли та переноси рядків, та впорскування паролів через секрети GitHub.

# OPEN PROBLEMS
- Відсутній український переклад для нових фіч у новій базі.

# MODIFIED FILES
- `.memory/activeContext.md` -> Оновлено поточну місію та кроки аналізу.
- [TMessagesProj/src/main/java/org/telegram/ui/SettingsActivity.java](file:///g:/Code/Java/Gomin-UA/TMessagesProj/src/main/java/org/telegram/ui/SettingsActivity.java): Injected custom item layout row, import bindings, and theme mapping.
- [TMessagesProj/src/main/java/ua/gomin/messenger/hooks/GominFeatureHooks.java](file:///g:/Code/Java/Gomin-UA/TMessagesProj/src/main/java/ua/gomin/messenger/hooks/GominFeatureHooks.java): Converted to Java structure with method signatures for feature hooks.
- [TMessagesProj/src/main/java/ua/gomin/messenger/preferences/GominPreferencesNavigator.java](file:///g:/Code/Java/Gomin-UA/TMessagesProj/src/main/java/ua/gomin/messenger/preferences/GominPreferencesNavigator.java): Converted to Java navigation dispatcher.
- [TMessagesProj/src/main/java/ua/gomin/messenger/configs/*](file:///g:/Code/Java/Gomin-UA/TMessagesProj/src/main/java/ua/gomin/messenger/configs/): Translated configuration files from Kotlin to Java to work around JVM linkage issue.
