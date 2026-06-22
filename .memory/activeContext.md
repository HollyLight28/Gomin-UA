[CURRENT MISSION]: Провірка багів, збірка локальна, налаштування Github Actions з постійним перезаписом останнього релізу, підключення Гомін Налаштувань до UI (чорний фон, іконка bird.svg).

[COMPLETED ATOMIC STEPS]:
- Видалено дублювання ресурсів (`AppName`, `AppAbout`) у `values-uk/gomin_strings.xml`.
- Виявлено критичну проблему з компіляцією Kotlin (`Class kotlin.Unit was compiled with an incompatible version of Kotlin`) через конфлікт версій `kotlin-android` плагіна та `kotlin-stdlib` у великому C++ проєкті.
- Написано та виконано Python-скрипт (`converter.py`) для автоматичної трансляції 7 файлів `Gomin*Config.kt` на чисту Java (`Gomin*Config.java`).
- Переписано `GominFeatureHooks.kt` та `GominPreferencesNavigator.kt` на Java.
- Видалено плагін `kotlin-android` з `build.gradle` (модуль `TMessagesProj`) для забезпечення повної сумісності та стабільності збірки.
- Зібрано локальну збірку `afatDebug` без жодних помилок (BUILD SUCCESSFUL).
- Додано векторний файл `bird.xml` (з `bird.svg`) у `res-gomin/drawable/`.
- Змінено іконку та фон (чорний колір `0xff000000`) пункту меню "Налаштування Гоміна" у `SettingsActivity.java`.
- Створено `release.yml` для Github Actions, що збирає APK та автоматично перезаписує реліз з тегом `latest`.

[OPEN PROBLEMS]:
Немає. Локальна збірка успішно пройшла.

[MODIFIED FILES]:
- `TMessagesProj/src/main/res-gomin/values-uk/gomin_strings.xml` -> Видалено дублікати -> Архітектурний конфлікт ресурсів у Gradle.
- `TMessagesProj/src/main/java/ua/gomin/messenger/configs/*` -> Переведено на Java -> Обхід багів Kotlin JVM-target у старих Android проектах.
- `TMessagesProj/src/main/java/org/telegram/ui/SettingsActivity.java` -> Оновлено `items.add` -> Підключення власного UI з кастомним фоном і іконкою пташки.
- `.github/workflows/release.yml` -> Створено -> CI/CD пайплайн з одним постійним релізом.
