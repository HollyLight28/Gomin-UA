# Active Context (Gomin-UA)

## CURRENT MISSION
1. Вирішити проблему падіння CI/CD збірок на GitHub Actions через пошкоджений keystore. [COMPLETED]
2. Провести жорсткий аудит останніх комітів (шрифти Nunito та Ghost Mode) на наявність багів та витоків ресурсів. [COMPLETED]
3. Запровадити системні фікси для виявлених архітектурних проблем (витоки SQLite, вбивство моноширини шрифтів, очищення каналів). [COMPLETED]
4. Провести глибоке дослідження та порівняльний аналіз Speed Engine (завантаження/вивантаження) у старій та новій базі. [COMPLETED]
5. Провести глобальний архітектурний аудит змін за добу та виправити критичні баги в Ghost Mode (SQLite обхід) та Speed Engine 2.0 (витік мутабельного дефолту). [COMPLETED]
6. Перенести інші фічі зі старої бази Cherrygram до нової структури. [IN_PROGRESS]

## COMPLETED ATOMIC STEPS
- Проаналізовано структуру папок `Gomin` (стара база) та `Gomin-UA` (нова база).
- Оптимізовано `.github/workflows/build.yml` (Gradle кеш, ліміти пам'яті раннера, Python-декодер) та вирішено проблему підпису (коректний Base64 декодинг keystore).
- Проведено детальний аудит коміту `9902496d9` (Fonts) та виявлено баги: відсутність імпорту `ApplicationLoader`, затирання моноширини та курсиву, мертвий вантаж `nunito_medium.ttf` в RAM, та обмеження рефлексії на Android 9+.
- Проведено детальний аудит коміту `189984f26` (Ghost Mode) та виявлено критичні баги: витік `SQLitePreparedStatement` у `MessagesStorage.java` та блокування повного очищення історії в каналах.
- Створено детальний звіт-артефакт [gomin_architecture_review.md](file:///C:/Users/VovA/.gemini/antigravity-cli/brain/ee178584-df8d-4cdd-be7d-ca9681adaf24/gomin_architecture_review.md) з детальним аналізом та дифами фіксів.
- Накачено хірургічні виправлення (фікси в кодових файлах після автоматичного системного апруву):
  - Додано lazy init, імпорт `ApplicationLoader`, фільтрацію моно/курсивних шрифтів та використання `nunitoMedium` у `GominFontHelper.java`.
  - Додано fallback-логіку побудови оригінальних шрифтів у `AndroidUtilities.java` у разі повернення null з хелпера.
  - Усунуто витік SQLitePreparedStatement через `try-finally` блок та дозволено повне очищення історії каналів у `MessagesStorage.java`.
- Успішно скомпільовано Java-код модуля `:TMessagesProj` (`BUILD SUCCESSFUL` за 2м 59с).
- Створено повноцінну специфікацію та план інтеграції у [README_SPEED_ENGINE.md](file:///G:/Code/Java/Gomin-UA/README_SPEED_ENGINE.md).
- Реалізовано та протестовано Speed Engine 2.0 (Download Boost у FileLoadOperation.java, Upload Boost у FileUploadOperation.java та селектор у GominSettingsEntry.java).
- За рішенням користувача повністю відкинуто ідею інтеграції автоматичного режиму «Укриття» та ручного «Режиму слабкої мережі» (slowNetworkMode). Всі згадки та логіку slowNetworkMode повністю видалено з GominCoreConfig.
- Зроблено комміт та пуш змін у гілку `master` репозиторію.
- Проведено глибокий рев'ю-аудит змін за останні 24 години:
  - Знайдено та виправлено критичний баг у Ghost Mode (видалення kept повідомлень через використання оригінального `ids` замість відфільтрованого `idsStr` у SQLite `DELETE` та `SELECT` запитах в `MessagesStorage.java`).
  - Знайдено та виправлено баг витоку мутабельного стану у Speed Engine 2.0 (метод `updateParams()` у `FileLoadOperation.java` перезаписував дефолтний розмір чанка та ліміт потоків, ламаючи режим `forceSmallChunk`).
- Успішно скомпільовано та підтверджено працездатність фіксів.
- Досліджено архітектуру повітряних тривог (Air Alert): підтверджено роботу через нативні Android Notification Channels з кастомним `sirenUri` (без MediaPlayer у бекграунд сервісах), що робить рішення максимально легким.
- Перевірено наявність та логіку роботи "Статистики тривог" (AirAlertStatsActivity), яка інтегрована в меню трьох крапок (DialogsActivity) та працює через пул потоків.
- Виправлено AAPT помилку компіляції бібліотеки TMessagesProj: скопійовано іконки лаунчера в стандартну директорію `res/drawable` та додано пропущений `xmlns:aapt` namespace у `icon_background_telegram.xml`.
- Успішно скомпільовано Standalone збірку додатку (`BUILD SUCCESSFUL`).

## OPEN PROBLEMS
- Перевірити та перенести інші фічі зі старої бази за запитом користувача.

## MODIFIED FILES
- `TMessagesProj/src/main/res-gomin/drawable/icon_background_telegram.xml` -> [MODIFY] Додано декларацію `xmlns:aapt="http://schemas.android.com/apk/res-auto"` для успішної збірки AAPT.
- `TMessagesProj/src/main/res/drawable/` -> [CREATE] Скопійовано launcher background/foreground drawables для ліквідації AAPT error при збірці бібліотеки.
- `TMessagesProj/src/main/java/org/telegram/messenger/MessagesStorage.java` -> [MODIFY] Запобігли витоку SQLitePreparedStatement, дозволили чистити канали, та виправили обхід видалення kept повідомлень через перехід на `idsStr` / `nonKeptIdsStr`.
- `TMessagesProj/src/main/java/org/telegram/messenger/FileLoadOperation.java` -> [MODIFY] Інтегровано Download Speed Boost та виправлено витік мутабельного стану при перерахунку параметрів (скидання до константних дефолтів `128KB` / `4` потоки).
- `TMessagesProj/src/main/java/org/telegram/messenger/AndroidUtilities.java` -> [MODIFY] Додано fallback-логіку завантаження оригінальних шрифтів при поверненні null з хелпера.
- `TMessagesProj/src/main/java/org/telegram/messenger/FileUploadOperation.java` -> [MODIFY] Інтегровано Upload Speed Boost (512KB чанки та 8MB черга) через GominSpeedController.
- `TMessagesProj/src/main/java/ua/gomin/messenger/helpers/ui/GominFontHelper.java` -> [MODIFY] Додано lazy init, імпорт ApplicationLoader, фільтрацію моно/курсивних шрифтів та використання `nunitoMedium`.
- `TMessagesProj/src/main/java/ua/gomin/messenger/preferences/GominSettingsEntry.java` -> [MODIFY] Додано селектор вибору швидкості завантаження.
- `TMessagesProj/src/main/java/ua/gomin/messenger/configs/GominCoreConfig.java` -> [MODIFY] Видалено методи slowNetworkMode для повного очищення від режиму слабкої мережі.
- `CLAUDE.md` -> [MODIFY] Оновлено команди збірки та встановлення на Standalone.
- `.memory/techContext.md` -> [MODIFY] Оновлено опис локальних збірок на Standalone.
- `.agents/skills/gomin-feature-porter/SKILL.md` -> [MODIFY] Замінено крок компіляції на Standalone.
- `README_SPEED_ENGINE.md` -> [MODIFY] Оновлено опис хуків під GominSpeedController та прибрано згадки про Shelter Mode.
- `README.md` -> [MODIFY] Переписано під брендінг та опис фіч Гомін.UA.
- `.memory/activeContext.md` -> [MODIFY] Оновлено поточний стан контексту.

