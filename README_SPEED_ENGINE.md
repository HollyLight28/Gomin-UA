# ⚡ Gomin Speed Engine 2.0: Специфікація та налаштування

Братан, тут розписано, як працює наша надбудова прискорення мережі **Speed Engine 2.0** над офіційними класами `FileLoadOperation` та `FileUploadOperation` у клієнті **Gomin-UA**. Ми витискаємо максимум із сучасного мобільного та Wi-Fi інтернету за рахунок багатопотокового завантаження та оптимізованого розміру буферів.

---

## 🚀 1. Концепція Speed Engine 2.0

Оригінальний Telegram качає файли консервативно: маленькими чанками (128 KB) та в 4 потоки. Для сучасного гігабітного інтернету це занадто повільно.
**Speed Engine 2.0** дозволяє паралельно завантажувати й вивантажувати медіафайли у кілька потоків, значно збільшуючи швидкість.

### 📊 Порівняльна таблиця режимів швидкості

| Режим роботи | Розмір чанка (Download) | Кількість потоків (Download) | Черга вивантаження (Upload) | Призначення |
| :--- | :--- | :--- | :--- | :--- |
| **BOOST_NONE** (Дефолт) | 128 KB | 4 потоки | 2 MB (до 4 потоків) | Базова ванільна логіка Telegram |
| **BOOST_AVERAGE** (Баланс) | 512 KB | 8 потоків | 8 MB (до 16 потоків при бусті) | Оптимально для мобільного 4G |
| **BOOST_EXTREME** (Гомін) | 1 MB | 12 потоків | 8 MB (до 16 потоків при бусті) | Гігабітний Wi-Fi / Повний безліміт |

---

## 📥 2. Download Speed Boost (Завантаження)

Залежно від обраного користувачем рівня швидкості в налаштуваннях додатка, у класі [FileLoadOperation.java](file:///G:/Code/Java/Gomin-UA/TMessagesProj/src/main/java/org/telegram/messenger/FileLoadOperation.java) динамічно змінюються параметри:

* **Вимкнено (BOOST_NONE)**:
  * `downloadChunkSizeBig = 128 KB`
  * `maxDownloadRequests = 4`
* **Баланс (BOOST_AVERAGE)**:
  * `downloadChunkSizeBig = 512 KB`
  * `maxDownloadRequests = 8`
* **Максимально (BOOST_EXTREME)**:
  * `downloadChunkSizeBig = 1 MB`
  * `maxDownloadRequests = 12`

Це дозволяє завантажувати великі файли (відео, аудіо, великі документи) значно швидше за рахунок ширшого вікна прийому даних (до 12 MB сумарно в дорозі).

---

## 📤 3. Upload Speed Boost (Вивантаження)

Для прискорення відправки файлів у хмару Telegram у класі [FileUploadOperation.java](file:///G:/Code/Java/Gomin-UA/TMessagesProj/src/main/java/org/telegram/messenger/FileUploadOperation.java) ми реалізували:

1. **Збільшений розмір чанка**:
   Якщо активовано тумблер прискорення вивантаження, для великих файлів (>10 MB) мінімальний розмір чанка становить **512 KB** (замість стандартних 128 KB).
2. **Розширення черги вивантаження**:
   Замість стандартного ліміту черги у 2 MB (`maxUploadingKBytes`), при увімкненому бусті ліміт розширюється до **8 MB** (`1024 * 8`).
3. **Більше паралельних потоків**:
   Завдяки розширенню черги кількість паралельних запитів на вивантаження вираховується динамічно й може досягати **до 16 потоків** одночасно, повністю завантажуючи гігабітний аплоад.

---

## 🛠️ 4. Технічна інтеграція (Surgical Hooks)

Всі зміни впроваджені хірургічним шляхом з дотриманням правила мінімального втручання в оригінальний код Telegram (не більше 10 рядків змін на файл):

### Інтеграція в завантажувач (`FileLoadOperation.java`)
```java
    private void updateParams() {
        /** Gomin start */
        int boostMode = ua.gomin.messenger.configs.GominCoreConfig.INSTANCE.getDownloadSpeedBoost(ApplicationLoader.applicationContext);
        if (boostMode == 2 && !forceSmallChunk) {
            downloadChunkSizeBig = 1024 * 1024;
            maxDownloadRequests = 12;
            maxDownloadRequestsBig = 12;
        } else if ((boostMode == 1 || preloadPrefixSize > 0 || MessagesController.getInstance(currentAccount).getfileExperimentalParams) && !forceSmallChunk) {
            downloadChunkSizeBig = 1024 * 512;
            maxDownloadRequests = 8;
            maxDownloadRequestsBig = 8;
        } else {
            downloadChunkSizeBig = 1024 * 128;
            maxDownloadRequests = 4;
            maxDownloadRequestsBig = 4;
        }
        /** Gomin end */
        maxCdnParts = (int) (FileLoader.DEFAULT_MAX_FILE_SIZE / downloadChunkSizeBig);
    }
```

### Інтеграція у вивантажувач (`FileUploadOperation.java`)
```java
                /** Gomin start */
                boolean uploadBoost = ua.gomin.messenger.configs.GominCoreConfig.INSTANCE.getUploadSpeedBoost(ApplicationLoader.applicationContext);
                int currentMinUploadChunkSize = (uploadBoost && isBigFile) ? 512 : minUploadChunkSize;
                int currentMaxUploadingKBytes = uploadBoost ? (1024 * 8) : maxUploadingKBytes;
                uploadChunkSize = (int) Math.max(slowNetwork ? minUploadChunkSlowNetworkSize : currentMinUploadChunkSize, (totalFileSize + 1024L * maxUploadParts - 1) / (1024L * maxUploadParts));
                /** Gomin end */
                if (1024 % uploadChunkSize != 0) {
                    int chunkSize = 64;
                    while (uploadChunkSize > chunkSize) {
                        chunkSize *= 2;
                    }
                    uploadChunkSize = chunkSize;
                }
                /** Gomin start */
                maxRequestsCount = Math.max(1, (slowNetwork ? maxUploadingSlowNetworkKBytes : currentMaxUploadingKBytes) / uploadChunkSize);
                /** Gomin end */
```
