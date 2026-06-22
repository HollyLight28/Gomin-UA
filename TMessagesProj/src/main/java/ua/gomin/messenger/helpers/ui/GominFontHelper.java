package ua.gomin.messenger.helpers.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Gomin Font Helper — замінює Roboto на Nunito по всьому додатку.
 *
 * Архітектура:
 * 1. Завантажує Nunito з assets/fonts/ (regular, medium, bold)
 * 2. При init() замінює Typeface.DEFAULT та Typeface.DEFAULT_BOLD через reflection
 * 3. Мапить ВСІ запити getTypeface() на Nunito
 *
 * Безпечний для оновлень Telegram — мінімум файлів для зміни.
 */
public class GominFontHelper {

    private static final String TAG = "GominFontHelper";

    private static final String NUNITO_REGULAR = "fonts/nunito_regular.ttf";
    private static final String NUNITO_MEDIUM = "fonts/nunito_medium.ttf";
    private static final String NUNITO_BOLD = "fonts/nunito_bold.ttf";

    private static Typeface nunitoRegular;
    private static Typeface nunitoMedium;
    private static Typeface nunitoBold;

    private static boolean initialized = false;

    /**
     * Ініціалізація шрифтів та заміна стандартних шрифтів Android.
     * Викликається один раз з ApplicationLoader.onCreate().
     */
    public static synchronized void init(Context context) {
        if (initialized) return;

        try {
            // Завантажуємо Nunito шрифти з assets
            nunitoRegular = Typeface.createFromAsset(context.getAssets(), NUNITO_REGULAR);
            nunitoMedium = Typeface.createFromAsset(context.getAssets(), NUNITO_MEDIUM);
            nunitoBold = Typeface.createFromAsset(context.getAssets(), NUNITO_BOLD);

            // Замінюємо стандартні шрифти Android через reflection
            replaceDefaultTypeface();
            replaceDefaultBoldTypeface();

            initialized = true;
            Log.d(TAG, "GominFontHelper initialized — Nunito font active");
        } catch (Exception e) {
            Log.e(TAG, "Failed to initialize GominFontHelper: " + e.getMessage());
        }
    }

    /**
     * Замінює Typeface.DEFAULT на Nunito Regular через reflection.
     * Робить будь-який TextView з Typeface.DEFAULT використовувати Nunito.
     */
    private static void replaceDefaultTypeface() {
        try {
            Field field = Typeface.class.getDeclaredField("DEFAULT");
            field.setAccessible(true);

            // Знімаємо final модифікатор (Java reflection allows this)
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

            field.set(null, nunitoRegular);
        } catch (Exception e) {
            // Fallback: якщо reflection не працює — Typeface.DEFAULT залишиться Roboto
            // Основний шлях (getTypeface()) все одно буде повертати Nunito
            Log.w(TAG, "Could not replace Typeface.DEFAULT: " + e.getMessage());
        }
    }

    /**
     * Замінює Typeface.DEFAULT_BOLD на Nunito Bold через reflection.
     */
    private static void replaceDefaultBoldTypeface() {
        try {
            Field field = Typeface.class.getDeclaredField("DEFAULT_BOLD");
            field.setAccessible(true);

            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

            field.set(null, nunitoBold);
        } catch (Exception e) {
            Log.w(TAG, "Could not replace Typeface.DEFAULT_BOLD: " + e.getMessage());
        }
    }

    /**
     * Повертає Nunito Regular.
     */
    public static Typeface getDefault() {
        return nunitoRegular;
    }

    /**
     * Повертає Nunito Bold.
     */
    public static Typeface getDefaultBold() {
        return nunitoBold;
    }

    /**
     * Мапить будь-який шлях до шрифту на Nunito.
     * Використовується AndroidUtilities.getTypeface().
     *
     * Логіка маппінгу:
     * - medium/extrabold/rbold → Nunito Bold (найжирніший варіант)
     * - italic → Nunito Regular (Android симулює курсив)
     * - mono/condensed → Nunito Regular (Nunito не має моноширинного/конденсованого)
     * - все інше → Nunito Regular
     */
    public static Typeface getTypeface(String assetPath) {
        if (nunitoRegular == null) {
            // Fallback якщо шрифт не завантажився
            return Typeface.DEFAULT;
        }

        if (assetPath.contains("medium") || assetPath.contains("rbold") || assetPath.contains("rextrabold")) {
            return nunitoBold;
        }
        return nunitoRegular;
    }

    /**
     * Перевіряє, чи ініціалізовано шрифти.
     */
    public static boolean isInitialized() {
        return initialized;
    }
}
