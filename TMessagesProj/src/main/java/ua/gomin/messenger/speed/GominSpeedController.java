package ua.gomin.messenger.speed;

import android.content.Context;
import org.telegram.messenger.ApplicationLoader;
import ua.gomin.messenger.configs.GominCoreConfig;

/**
 * Контролер швидкості Speed Engine 2.0.
 * Вся бізнес-логіка розрахунку чанків та потоків винесена сюди для ізоляції коду.
 */
public class GominSpeedController {

    public static final GominSpeedController INSTANCE = new GominSpeedController();

    // Кешовані значення для усунення зайвих викликів SharedPreferences
    private volatile int cachedDownloadSpeedBoost = -1;
    private volatile int cachedUploadSpeedBoost = -1; // -1: не ініціалізовано, 0: false, 1: true

    private GominSpeedController() {}

    /**
     * Скидає кеш налаштувань. Має викликатися при зміні конфігу в налаштуваннях.
     */
    public void resetCache() {
        cachedDownloadSpeedBoost = -1;
        cachedUploadSpeedBoost = -1;
    }

    private int getDownloadSpeedBoostCached(Context context) {
        if (cachedDownloadSpeedBoost == -1) {
            cachedDownloadSpeedBoost = GominCoreConfig.INSTANCE.getDownloadSpeedBoost(context);
        }
        return cachedDownloadSpeedBoost;
    }

    private boolean getUploadSpeedBoostCached(Context context) {
        if (cachedUploadSpeedBoost == -1) {
            cachedUploadSpeedBoost = GominCoreConfig.INSTANCE.getUploadSpeedBoost(context) ? 1 : 0;
        }
        return cachedUploadSpeedBoost == 1;
    }

    /**
     * Визначає розмір чанка для скачування.
     */
    public int getDownloadChunkSize(int defaultSize, int preloadPrefixSize, boolean experimentalParams, boolean forceSmallChunk) {
        Context context = ApplicationLoader.applicationContext;
        if (context == null) {
            return defaultSize;
        }

        int boostMode = getDownloadSpeedBoostCached(context);
        if (boostMode == 2 && !forceSmallChunk) {
            return 1024 * 1024; // 1 MB (Extreme)
        } else if ((boostMode == 1 || preloadPrefixSize > 0 || experimentalParams) && !forceSmallChunk) {
            return 1024 * 512; // 512 KB (Balanced)
        }

        return defaultSize; // 128 KB
    }

    /**
     * Визначає максимальну кількість паралельних потоків скачування.
     */
    public int getMaxDownloadRequests(int defaultRequests, int preloadPrefixSize, boolean experimentalParams, boolean forceSmallChunk) {
        Context context = ApplicationLoader.applicationContext;
        if (context == null) {
            return defaultRequests;
        }

        int boostMode = getDownloadSpeedBoostCached(context);
        if (boostMode == 2 && !forceSmallChunk) {
            return 12; // 12 потоків
        } else if ((boostMode == 1 || preloadPrefixSize > 0 || experimentalParams) && !forceSmallChunk) {
            return 8; // 8 потоків
        }

        return defaultRequests; // 4 потоки
    }

    /**
     * Визначає мінімальний розмір чанка для вивантаження.
     */
    public int getMinUploadChunkSize(int defaultMinSize, boolean isBigFile, boolean telegramSlowNetwork) {
        Context context = ApplicationLoader.applicationContext;
        if (context == null) {
            return defaultMinSize;
        }

        if (telegramSlowNetwork) {
            return 32; // 32 KB
        }

        boolean uploadBoost = getUploadSpeedBoostCached(context);
        if (uploadBoost && isBigFile) {
            return 512; // 512 KB
        }

        return defaultMinSize; // 128 KB
    }

    /**
     * Визначає ліміт черги вивантаження (в КБ).
     */
    public int getMaxUploadingKBytes(int defaultMaxKBytes, boolean telegramSlowNetwork) {
        Context context = ApplicationLoader.applicationContext;
        if (context == null) {
            return defaultMaxKBytes;
        }

        if (telegramSlowNetwork) {
            return 32; // 32 KB
        }

        boolean uploadBoost = getUploadSpeedBoostCached(context);
        if (uploadBoost) {
            return 1024 * 8; // 8 MB queue
        }

        return defaultMaxKBytes; // 2 MB
    }
}
