# Active Context (Gomin-UA)

## CURRENT MISSION
1. Вирішити проблему з обрізанням векторної іконки пташки в Android-лаунчері. [COMPLETED]
2. Прибрати білі квадрати з-під круглих іконок в налаштуваннях `AppIconsSelectorCell`. [COMPLETED]
3. Повернути вивід іконок у 2 ряди в `AppIconsSelectorCell` замість 1 ряду. [COMPLETED]

## COMPLETED ATOMIC STEPS
- Зменшено `scaleX` та `scaleY` з `0.85` до `0.6` у всіх файлах `icon_foreground_gomin_*.xml` за допомогою Python-скрипту, щоб вектор розміром ~942px поміщався в safe zone (~682px) адаптивної іконки.
- Змінено `fillPaint.setColor(Color.WHITE)` на `Color.TRANSPARENT` в `AppIconsSelectorCell.java`, щоб прибрати білі фонові квадрати під круглими іконками.
- Змінено `LinearLayoutManager.HORIZONTAL` на `GridLayoutManager` (spanCount = 2, HORIZONTAL) в `AppIconsSelectorCell.java`.
- Оновлено `ItemDecoration` для розрахунку відступів між двома рядами сітки.

## OPEN PROBLEMS
- Жодних критичних багів не виявлено. Зміни перевірені та підготовлені до коміту.

## MODIFIED FILES
- `TMessagesProj/src/main/java/org/telegram/ui/Cells/AppIconsSelectorCell.java` -> Переведено на GridLayoutManager у 2 ряди, прибрано білий фон.
- `TMessagesProj/src/main/res-gomin/drawable/icon_foreground_gomin_*.xml` -> Оновлено scale на 0.6.
- `TMessagesProj/src/main/res/drawable/icon_foreground_gomin.xml` -> Оновлено scale на 0.6.
