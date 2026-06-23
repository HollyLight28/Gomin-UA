---
slug: nunito-font
status: drafting
intent: clear
pending-action: write .omo/plans/nunito-font.md
approach: Використати архітектуру FontHelper зі старого проекту (мінімальні зміни в AndroidUtilities.java, максимальна ізоляція в ua.gomin.messenger.*)
---

# Draft: nunito-font

## Components (topology ledger)

| id | outcome | status | evidence path |
|----|---------|--------|---------------|
| 1. Завантажити шрифти | Nunito Regular/Medium/Bold в assets/fonts/ | active | G:\Code\Java\Gomin\TMessagesProj\src\main\assets\fonts\ |
| 2. GominFontHelper | Java клас для створення Typeface | active | G:\Code\Java\Gomin\TMessagesProj\src\main\java\uz\unnarsx\cherrygram\helpers\ui\FontHelper.java |
| 3. AndroidUtilities модифікація | Виклик GominFontHelper в createTypeface() | active | G:\Code\Java\Gomin-UA\TMessagesProj\src\main\java\org\telegram\messenger\AndroidUtilities.java |
| 4. GominAppearanceConfig | enableCustomFont toggle | active | G:\Code\Java\Gomin-UA\TMessagesProj\src\main\java\ua\gomin\messenger\configs\GominAppearanceConfig.java |
| 5. UI налаштування | Перемикач в GominSettingsEntry | active | G:\Code\Java\Gomin-UA\TMessagesProj\src\main\java\ua\gomin\messenger\preferences\GominSettingsEntry.java |
| 6. Переклади | Українські рядки | active | G:\Code\Java\Gomin-UA\TMessagesProj\src\main\res-gomin\values-uk\gomin_strings.xml |
| 7. Тести | Unit тести для GominFontHelper | active | G:\Code\Java\Gomin-UA\TMessagesProj\src\test\java\ |

## Open assumptions (announced defaults)

| assumption | adopted default | rationale | reversible? |
|------------|-----------------|-----------|-------------|
| Використовувати Nunito Regular, Medium, Bold | True | Це основні ваги, які покривають 95% випадків | Yes |
| За замовчуванням кастомний шрифт увімкнено | True | Користувачі хочуть бачити зміни одразу | Yes |
| Fallback на Roboto при помилці | True | Забезпечує стабільність | Yes |
| Не змінювати шрифт емодзі | True | Системний шрифт емодзі кращий | Yes |

## Findings (cited - path:lines)

### Як Telegram використовує шрифти
- `AndroidUtilities.java` має константи: `TYPEFACE_ROBOTO`, `TYPEFACE_ROBOTO_MEDIUM`, `TYPEFACE_ROBOTO_MEDIUM_ITALIC`, `TYPEFACE_ROBOTO_CONDENSED_BOLD`, `TYPEFACE_ROBOTO_ITALIC`, `TYPEFACE_ROBOTO_MONO`
- Метод `createTypeface(String assetPath)` створює Typeface на основі шляху до файлу
- Використовується по всьому коду через `AndroidUtilities.getTypeface()`

### Як старий Гомін реалізовував шрифти
- `FontHelper.java` (234 рядки) перехоплює `createTypeface()` та `createTypefaceFromAsset()`
- Модифікований `AndroidUtilities.java` викликає `FontHelper.createTypeface()` та `FontHelper.createTypefaceFromAsset()`
- 14 файлів шрифтів в `assets/fonts/` (Geist, Playfair, Roboto Flex, тощо)

### Поточний стан в Gomin-UA
- `AndroidUtilities.java` НЕ модифіковано (оригінальний Telegram)
- `assets/fonts/` НЕ існує
- `GominAppearanceConfig.java` існує, але немає enableCustomFont

## Decisions (with rationale)

1. **Архітектура FontHelper**: Використовуємо ту саму архітектуру, що й старий Гомін, бо вона довела свою ефективність та мінімальність змін.

2. **Тільки Nunito Regular/Medium/Bold**: Не додаємо Light, ExtraBold, Black, бо це збільшить APK та ускладнить підтримку.

3. **Fallback на Roboto**: Якщо Nunito не завантажується (помилка, пошкодження), використовуємо Roboto. Це забезпечує стабільність.

4. **Конфіг enableCustomFont**: Дає можливість вимкнути кастомний шрифт, якщо він не подобається або є проблеми.

5. **Максимум 10 рядків змін в AndroidUtilities**: Дотримуємось правила "надбудови" з CLAUDE.md.

## ПІДВОДНІ КАМЕНІ (POTENTIAL ISSUES)

### 1. Оновлення Telegram (HIGH RISK)
**Проблема:** Коли Telegram випускає оновлення, `AndroidUtilities.java` може змінитися. Наші 10 рядків змін можуть конфліктувати.
**Рішення:** Мінімізуємо зміни (до 10 рядків), позначаємо коментарями `/** Gomin start */` та `/** Gomin end */`. При мержі оновлень буде легше вирішити конфлікти.

### 2. Розмір шрифтів (MEDIUM RISK)
**Проблема:** Nunito шрифти можуть бути великими (100-200KB кожен). Три шрифти = 300-600KB до APK.
**Рішення:** Використовуємо тільки Regular, Medium, Bold (не всі варіанти). Це мінімально необхідний набір.

### 3. Продуктивність завантаження (MEDIUM RISK)
**Проблема:** Завантаження шрифтів з assets може уповільнити запуск додатку.
**Рішення:** Завантажуємо шрифти ліниво (lazy) при першому використанні, а не при старті додатку. Кешуємо в статичних змінних.

### 4. Використання пам'яті (LOW RISK)
**Проблема:** Кожен Typeface об'єкт займає пам'ять.
**Рішення:** Кешуємо Typefaces в статичних змінних (як це робить FontHelper). Не створюємо нові об'єкти при кожному виклику.

### 5. Підтримка мов (LOW RISK)
**Проблема:** Nunito може не підтримувати деякі мови (арабська, іврит, тайська, тощо).
**Рішення:** Android автоматично використовує fallback шрифт для непідтримуваних символів. Nunito підтримує латиницю, кирилицю, грецьку.

### 6. Жирний/Курсив (MEDIUM RISK)
**Проблема:** Nunito Regular не має вбудованого курсиву. Android може симулювати курсив, але він виглядає неідеально.
**Рішення:** Використовуємо тільки Regular, Medium, Bold. Для курсиву залишаємо системний Roboto Italic.

### 7. Моноширинний шрифт (LOW RISK)
**Проблема:** Nunito не моноширинний. Код та монотекст виглядатиме інакше.
**Рішення:** Для `TYPEFACE_ROBOTO_MONO` залишаємо оригінальний моноширинний шрифт. Не замінюємо його на Nunito.

### 8. Конденсований шрифт (LOW RISK)
**Проблема:** Nunito не має конденсованого варіанту.
**Рішення:** Для `TYPEFACE_ROBOTO_CONDENSED_BOLD` залишаємо оригінальний конденсований шрифт. Не замінюємо його на Nunito.

### 9. Thread Safety (LOW RISK)
**Проблема:** Завантаження шрифтів з assets може бути небезпечним в потоках.
**Рішення:** Використовуємо synchronized блок для ініціалізації. Typeface об'єкти потокобезпечні після створення.

### 10. Зміна конфігурації (LOW RISK)
**Проблема:** Зміна мови або розміру шрифту в налаштуваннях системи може вплинути на кастомний шрифт.
**Рішення:** Кастомний шрифт ігнорує системний розмір шрифту (але це стандартна поведінка для кастомних шрифтів).

## Scope IN
1. Завантажити шрифти Nunito (Regular, Medium, Bold) в assets/fonts/
2. Створити GominFontHelper.java в ua.gomin.messenger.helpers.ui
3. Модифікувати AndroidUtilities.java (до 10 рядків)
4. Додати конфіг enableCustomFont в GominAppearanceConfig
5. Додати UI налаштування в GominSettingsEntry
6. Додати український переклад
7. Написати unit тести

## Scope OUT (Must NOT have)
- Не видаляти Roboto з коду (залишити як fallback)
- Не додавати нові шрифти крім Nunito
- Не змінювати шрифт емодзі
- Не перевищувати 10 рядків змін в AndroidUtilities.java
- Не використовувати пакет `uz.unnarsx.*`
- Не замінювати моноширинний та конденсований шрифти

## Open questions
1. Чи треба додати Nunito Italic та Bold Italic? (Зараз плануємо тільки Regular, Medium, Bold)
2. Чи треба додати опцію для вибору між Nunito та іншими шрифтами? (Зараз плануємо тільки Nunito)

## Approval gate
status: awaiting-approval
<!-- When exploration is exhausted and unknowns are answered, set status: awaiting-approval. -->
<!-- That durable record is the loop guard: on a later turn read it and resume at the gate instead of re-running exploration. -->
