# Active Context (Gomin-UA)

## CURRENT MISSION
1. Вирішити проблему падіння CI/CD збірок на GitHub Actions через пошкоджений keystore. [COMPLETED]
2. Провести жорсткий аудит останніх комітів (шрифти Nunito та Ghost Mode) на наявність багів та витоків ресурсів. [COMPLETED]
3. Запровадити системні фікси для виявлених архітектурних проблем (витоки SQLite, вбивство моноширини шрифтів, очищення каналів). [COMPLETED]
4. Провести глибоке дослідження та порівняльний аналіз Speed Engine (завантаження/вивантаження) у старій та новій базі. [COMPLETED]
5. Перенести інші фічі зі старої бази Cherrygram до нової структури. [IN_PROGRESS]

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

## OPEN PROBLEMS
- Дослідити та перенести інші фічі зі старої бази (повітряні тривоги, Gemini AI тощо).

## MODIFIED FILES
- `TMessagesProj/src/main/java/ua/gomin/messenger/helpers/ui/GominFontHelper.java` -> [MODIFY] Додано lazy init, імпорт ApplicationLoader, фільтрацію моно/курсивних шрифтів та використання `nunitoMedium`.
- `TMessagesProj/src/main/java/org/telegram/messenger/AndroidUtilities.java` -> [MODIFY] Додано fallback-логіку завантаження оригінальних шрифтів при поверненні null з хелпера.
- `TMessagesProj/src/main/java/org/telegram/messenger/MessagesStorage.java` -> [MODIFY] Запобігли витоку SQLitePreparedStatement та дозволили чистити канали.
- `TMessagesProj/src/main/java/org/telegram/messenger/FileLoadOperation.java` -> [MODIFY] Інтегровано Download Speed Boost (Баланс та Максимум) через GominSpeedController.
- `TMessagesProj/src/main/java/org/telegram/messenger/FileUploadOperation.java` -> [MODIFY] Інтегровано Upload Speed Boost (512KB чанки та 8MB черга) через GominSpeedController.
- `TMessagesProj/src/main/java/ua/gomin/messenger/preferences/GominSettingsEntry.java` -> [MODIFY] Додано селектор вибору швидкості завантаження.
- `TMessagesProj/src/main/java/ua/gomin/messenger/configs/GominCoreConfig.java` -> [MODIFY] Видалено методи slowNetworkMode для повного очищення від режиму слабкої мережі.
- `CLAUDE.md` -> [MODIFY] Оновлено команди збірки та встановлення на Standalone.
- `.memory/techContext.md` -> [MODIFY] Оновлено опис локальних збірок на Standalone.
- `.agents/skills/gomin-feature-porter/SKILL.md` -> [MODIFY] Замінено крок компіляції на Standalone.
- `README_SPEED_ENGINE.md` -> [MODIFY] Оновлено опис хуків під GominSpeedController та прибрано згадки про Shelter Mode.
- `README.md` -> [MODIFY] Переписано під брендінг та опис фіч Гомін.UA.
- `.memory/activeContext.md` -> [MODIFY] Оновлено поточний стан контексту.

