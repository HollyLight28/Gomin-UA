# ТЗ для OpenCode — Задача 1: Зовнішній вигляд (аватарка + шрифт)

## 🎯 Мета

Збільшити аватарку в списку чатів з 56dp до 60dp та шрифт назви чату на 2dp.

## 📁 Файл для змін

**Один файл:** `TMessagesProj/src/main/java/org/telegram/ui/Cells/DialogCell.java`

---

## 🅰️ Зміна 1: Аватарка 56→60dp

### 1.1 Відступ зліва

Знайти:
```java
public int avatarStart = 11;
```
Замінити на:
```java
public int avatarStart = 10;
```

### 1.2 Радіус аватарки (3 місця)

**Місце 1** — ініціалізація:
```java
avatarImage.setRoundRadius(dp(28));
```
Замінити на:
```java
avatarImage.setRoundRadius(dp(30));
```

**Місце 2** — після скидання:
```java
avatarImage.setRoundRadius(dp(28));
```
Замінити на:
```java
avatarImage.setRoundRadius(dp(30));
```

**Місце 3** — умовний радіус (форуми, свої чати):
```java
avatarImage.setRoundRadius(drawMonoforumAvatar ? 1 : chat != null && chat.forum && currentDialogFolderId == 0 && !useFromUserAsAvatar || !isSavedDialog && user != null && user.self && MessagesController.getInstance(currentAccount).savedViewAsChats ? dp(16) : dp(28));
```
Замінити **тільки** `dp(28)` на `dp(30)`:
```java
avatarImage.setRoundRadius(drawMonoforumAvatar ? 1 : chat != null && chat.forum && currentDialogFolderId == 0 && !useFromUserAsAvatar || !isSavedDialog && user != null && user.self && MessagesController.getInstance(currentAccount).savedViewAsChats ? dp(16) : dp(30));
```

### 1.3 Координати аватарки (2 місця)

**Місце 1** — thumbLeft (відступ для прев'ю):
```java
thumbLeft = avatarLeft + dp(56 + 13);
```
Замінити на:
```java
thumbLeft = avatarLeft + dp(60 + 13);
```

**Місце 2** — rectangle аватарки:
```java
storyParams.originalAvatarRect.set(avatarLeft, avatarTop, avatarLeft + dp(56), avatarTop + dp(56));
```
Замінити на:
```java
storyParams.originalAvatarRect.set(avatarLeft, avatarTop, avatarLeft + dp(60), avatarTop + dp(60));
```

---

## 🅱️ Зміна 2: Шрифт назви чату +2dp

Знайти блок установки розмірів шрифтів (рядки ~1143-1165, метод `buildLayout()`):

```java
if (useForceThreeLines || SharedConfig.useThreeLinesLayout || true) {
    Theme.dialogs_namePaint[0].setTextSize(dp(17));
    Theme.dialogs_nameEncryptedPaint[0].setTextSize(dp(17));
    Theme.dialogs_messagePaint[0].setTextSize(dp(16));
    Theme.dialogs_messagePrintingPaint[0].setTextSize(dp(16));

    Theme.dialogs_namePaint[1].setTextSize(dp(16));
    Theme.dialogs_nameEncryptedPaint[1].setTextSize(dp(16));
    Theme.dialogs_messagePaint[1].setTextSize(dp(15));
    Theme.dialogs_messagePrintingPaint[1].setTextSize(dp(15));
    ...
    paintIndex = 1;
    ...
    } else {
    ...
    paintIndex = 0;
    }
```

Замінити **тільки значення для назви** (`namePaint` та `nameEncryptedPaint`):

### Що міняємо (жирним — нові значення):

| Змінна | Було | Стало |
|---|---|---|
| `dialogs_namePaint[0].setTextSize` | `dp(17)` | **dp(19)** |
| `dialogs_nameEncryptedPaint[0].setTextSize` | `dp(17)` | **dp(19)** |
| `dialogs_namePaint[1].setTextSize` | `dp(16)` | **dp(18)** |
| `dialogs_nameEncryptedPaint[1].setTextSize` | `dp(16)` | **dp(18)** |

**`dialogs_messagePaint` та `dialogs_messagePrintingPaint` — НЕ ЧІПАТИ**, залишаються 16dp і 15dp.

---

## ✅ Очікуваний результат

- Аватарка в списку чатів стала **60×60dp** (була 56×56)
- Назва чату стала **19dp** (була 17dp) у звичайному режимі / **18dp** (була 16dp) у трилінійному
- Текст повідомлення під назвою — без змін (16dp/15dp)
- Відступ зліва трохи зменшився (11→10dp) для компенсації більшої аватарки
- Ніяких обрізань, накладань, вилазів за межі
- Працює на всіх розмірах екранів і в усіх станах (форум, три лінії, архів)

## ❌ Заборони

- Не міняти нічого крім вказаних рядків
- Не переписувати сусідні функції
- Не робити рефакторинг
- Не чіпати `messagePaint`
