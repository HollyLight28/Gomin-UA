# nunito-font - Work Plan

## TL;DR (For humans)

**What you'll get:** Шрифт Nunito по ВСЬОМУ додатку. Замість Roboto скрізь Нуніто.

**Why this approach:** Змінюємо ОДИН метод `getTypeface()` в AndroidUtilities — і вся дільна зміна відбувається там. Мінімум коду, максимум результату.

**What it will NOT do:** Не додаємо перемикачі. Не додаємо конфіги. Не чіпаємо емодзі.

**Effort:** Short (1-2 дні)
**Risk:** Low - змінюємо тільки один метод, fallback на Roboto працює

Your next move: Апробуй план.

---

> TL;DR (machine): Short effort, Low risk - one method change replaces Roboto with Nunito everywhere.

## Scope
### Must have
1. Додати Nunito шрифти в `assets/fonts/`
2. Змінити один метод `getTypeface()` в `AndroidUtilities.java`

### Must NOT have
- Ніяких перемикачів
- Ніяких конфігів
- Ніякого UI
- Ніякого FontHelper

## Todos

- [ ] 1. Додати Nunito шрифти
  What to do: Завантажити Nunito-Regular.ttf, Nunito-Medium.ttf, Nunito-Bold.ttf в `TMessagesProj/src/main/assets/fonts/`. Назвати їх `nunito_regular.ttf`, `nunito_medium.ttf`, `nunito_bold.ttf`.
  References: G:\Code\Java\Gomin-UA\TMessagesProj\src\main\assets\fonts\
  Acceptance: Файли існують в assets/fonts/
  Commit: Y | feat(fonts): add Nunito font files

- [ ] 2. Змінити getTypeface() в AndroidUtilities
  What to do: Знайти метод `getTypeface()` (рядок 2403) і замінити його на:
  ```java
  /** Gomin start */
  public static Typeface getTypeface(String assetPath) {
      synchronized (typefaceCache) {
          if (!typefaceCache.containsKey(assetPath)) {
              try {
                  // Gomin: always use Nunito
                  String fontPath;
                  if (assetPath.contains("medium") || assetPath.contains("rbold") || assetPath.contains("rextrabold")) {
                      fontPath = "fonts/nunito_bold.ttf";
                  } else if (assetPath.contains("italic")) {
                      fontPath = "fonts/nunito_regular.ttf"; // Android симулює курсив
                  } else if (assetPath.contains("mono") || assetPath.contains("condensed")) {
                      fontPath = "fonts/nunito_regular.ttf";
                  } else {
                      fontPath = "fonts/nunito_regular.ttf";
                  }
                  Typeface t = Typeface.createFromAsset(ApplicationLoader.applicationContext.getAssets(), fontPath);
                  typefaceCache.put(assetPath, t);
              } catch (Exception e) {
                  if (BuildVars.LOGS_ENABLED) {
                      FileLog.e("Could not get typeface '" + assetPath + "' because " + e.getMessage());
                  }
                  return null;
              }
          }
          return typefaceCache.get(assetPath);
      }
  }
  /** Gomin end */
  ```
  References: G:\Code\Java\Gomin-UA\TMessagesProj\src\main\java\org\telegram\messenger\AndroidUtilities.java:2403
  Acceptance: Додаток компілюється, шрифт Nunito скрізь
  Commit: Y | feat(font): replace Roboto with Nunito everywhere

## Success criteria
1. Nunito скрізь в додатку
2. Метод `getTypeface()` повертає Nunito для будь-якого запиту
3. Fallback на null при помилці (як і раніше)
4. Додаток компілюється
