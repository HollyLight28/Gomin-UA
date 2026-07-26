# ТЗ для OpenCode — Задача 2: Встановити Nunito як основний шрифт додатку

## 🎯 Мета

Замінити всі шляхи до шрифтів в коді з Roboto/Montserrat на Nunito (SemiBold, Bold, ExtraBold). Файли вже лежать в `assets/fonts/`.

## 📁 Файли для змін

### 1. `TMessagesProj/src/main/java/org/telegram/messenger/AndroidUtilities.java`

Знайти блок констант (рядки ~254-259):

```java
public final static String TYPEFACE_ROBOTO_REGULAR = "fonts/rregular.ttf";
public final static String TYPEFACE_ROBOTO_MEDIUM = "fonts/rmedium.ttf";
public final static String TYPEFACE_ROBOTO_EXTRA_BOLD = "fonts/rextrabold.ttf";
public final static String TYPEFACE_ROBOTO_MEDIUM_ITALIC = "fonts/rmediumitalic.ttf";
public final static String TYPEFACE_ROBOTO_MONO = "fonts/rmono.ttf";
public final static String TYPEFACE_MERRIWEATHER_BOLD = "fonts/mw_bold.ttf";
```

Замінити на:

```java
public final static String TYPEFACE_ROBOTO_REGULAR = "fonts/nunito_semibold.ttf";
public final static String TYPEFACE_ROBOTO_MEDIUM = "fonts/nunito_bold.ttf";
public final static String TYPEFACE_ROBOTO_EXTRA_BOLD = "fonts/nunito_extrabold.ttf";
public final static String TYPEFACE_ROBOTO_MEDIUM_ITALIC = "fonts/nunito_semibold.ttf";
public final static String TYPEFACE_ROBOTO_MONO = "fonts/rmono.ttf";
public final static String TYPEFACE_MERRIWEATHER_BOLD = "fonts/nunito_extrabold.ttf";
```

**Пояснення замін:**
- `rregular.ttf` → `nunito_semibold.ttf` — основний шрифт тексту (SemiBold 600, не Regular бо буде занадто тонкий)
- `rmedium.ttf` → `nunito_bold.ttf` — шрифт для назв (Bold 700)
- `rextrabold.ttf` → `nunito_extrabold.ttf` — жирні заголовки (ExtraBold 800)
- `rmediumitalic.ttf` → `nunito_semibold.ttf` — курсивів Nunito немає, ставимо той самий SemiBold
- `rmono.ttf` — **НЕ ЧІПАТИ**, це Roboto Mono для моноширинного тексту
- `mw_bold.ttf` → `nunito_extrabold.ttf` — Merriweather Bold більше не потрібен

**ВАЖЛИВО:** Також в цьому файлі є методи `regular()` і `bold()` (рядки ~267-282). Вони використовують `getTypeface(TYPEFACE_ROBOTO_REGULAR)` та `getTypeface(TYPEFACE_ROBOTO_MEDIUM)`. Після зміни констант вони будуть автоматично використовувати Nunito — нічого додатково міняти не треба.

### 2. `TMessagesProj/src/main/java/org/telegram/ui/ActionBar/Theme.java`

Знайти блок (рядок ~8589):

```java
/** Gomin start — Set Plus Jakarta Sans for chat texts, quotes, timestamps */
Typeface regularTypeface = AndroidUtilities.getTypeface("fonts/rregular.ttf");
if (regularTypeface != null) {
    chat_msgTextPaint.setTypeface(regularTypeface);
    chat_replyTextPaint.setTypeface(regularTypeface);
    chat_quoteTextPaint.setTypeface(regularTypeface);
    chat_explanationTextPaint.setTypeface(regularTypeface);
    chat_titleLabelTextPaint.setTypeface(regularTypeface);
    chat_adminPaint.setTypeface(regularTypeface);
    chat_timePaint.setTypeface(regularTypeface);
}
/** Gomin end */
```

Замінити `"fonts/rregular.ttf"` на `"fonts/nunito_semibold.ttf"`:

```java
Typeface regularTypeface = AndroidUtilities.getTypeface("fonts/nunito_semibold.ttf");
```

## ✅ Очікуваний результат

- Весь текст в додатку (повідомлення, цитати, таймстемпи, меню, налаштування) — **Nunito SemiBold (600)**
- Назви чатів, заголовки — **Nunito Bold (700)**
- Жирні акценти — **Nunito ExtraBold (800)**
- Моноширинний текст (код) — **Roboto Mono** (без змін)
- Українська кирилиця — відображається коректно

## ❌ Заборони

- Не міняти `rmono.ttf` — він окремо для моноширинного тексту
- Не міняти інші файли крім вказаних
- Не додавати нові шрифти
- Не видаляти `rmono.ttf` та `num.otf`
