package ua.gomin.messenger.helpers.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import org.telegram.messenger.ApplicationLoader;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Gomin Font Helper — замінює Roboto на Plus Jakarta Sans по всьому додатку.
 *
 * Архітектура:
 * 1. Завантажує Plus Jakarta Sans з assets/fonts/ (regular, medium, bold, italic).
 * 2. При init() безпечно замінює sSystemFontMap.
 * 3. Мапить ВСІ запити getTypeface() на Plus Jakarta Sans, включаючи mw_bold та italic.
 *
 * Безпечний для оновлень Telegram — мінімум файлів для зміни.
 */
public class GominFontHelper {

    private static final String TAG = "GominFontHelper";

    private static final String FONT_REGULAR = "fonts/gomin_regular.ttf";
    private static final String FONT_MEDIUM = "fonts/gomin_medium.ttf";
    private static final String FONT_BOLD = "fonts/gomin_bold.ttf";

    private static Typeface fontRegular;
    private static Typeface fontMedium;
    private static Typeface fontBold;
    private static Typeface fontItalic;
    
    // [Architectural Fix] Synthetic Typefaces для збереження стилізації (Bold Italic)
    private static Typeface fontBoldItalic;
    private static Typeface fontMediumItalic;

    private static volatile boolean initialized = false;

    /**
     * Ініціалізація шрифтів та заміна стандартних шрифтів Android.
     * Викликається один раз з ApplicationLoader.onCreate().
     */
    public static synchronized void init(Context context) {
        if (initialized) return;

        try {
            // Завантажуємо шрифти з assets
            fontRegular = Typeface.createFromAsset(context.getAssets(), FONT_REGULAR);
            fontMedium = Typeface.createFromAsset(context.getAssets(), FONT_MEDIUM);
            fontBold = Typeface.createFromAsset(context.getAssets(), FONT_BOLD);

            // Генеруємо Faux-Italic для курсивних накреслень
            fontItalic = Typeface.create(fontRegular, Typeface.ITALIC);
            fontBoldItalic = Typeface.create(fontBold, Typeface.ITALIC);
            fontMediumItalic = Typeface.create(fontMedium, Typeface.ITALIC);

            // Замінюємо стандартні шрифти Android через reflection
            replaceDefaultTypeface();
            replaceDefaultBoldTypeface();
            replaceSystemFontMap();

            initialized = true;
            Log.d(TAG, "GominFontHelper initialized — Fixel Text font active");
        } catch (Throwable e) {
            Log.e(TAG, "Failed to initialize GominFontHelper: " + e.getMessage());
            // Навіть якщо впали (немає файлу), ставимо true, щоб не спамити init()
            initialized = true; 
        }
    }

    private static void replaceDefaultTypeface() {
        try {
            Field field = Typeface.class.getDeclaredField("DEFAULT");
            field.setAccessible(true);
            try {
                // [BugFix 4] Ізолюємо зміну modifiers, бо на API 28+ це викине виняток і скасує всю заміну.
                Field modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                modifiersField.setInt(field, field.getModifiers() & ~java.lang.reflect.Modifier.FINAL);
            } catch (Exception ignored) {
                // На Android 9+ field.set() може спрацювати навіть без зняття FINAL (залежить від вендора)
            }
            field.set(null, fontRegular);
        } catch (Throwable e) {
            Log.w(TAG, "Could not replace Typeface.DEFAULT: " + e.getMessage());
        }
    }

    private static void replaceDefaultBoldTypeface() {
        try {
            Field field = Typeface.class.getDeclaredField("DEFAULT_BOLD");
            field.setAccessible(true);
            try {
                Field modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                modifiersField.setInt(field, field.getModifiers() & ~java.lang.reflect.Modifier.FINAL);
            } catch (Exception ignored) {
            }
            field.set(null, fontBold);
        } catch (Throwable e) {
            Log.w(TAG, "Could not replace Typeface.DEFAULT_BOLD: " + e.getMessage());
        }
    }

    /**
     * Замінює Typeface.sSystemFontMap.
     */
    private static void replaceSystemFontMap() {
        try {
            // [BugFix 6] Прибираємо synchronized (Typeface.class), щоб уникнути дедлоків з системними потоками Android.
            Field field;
            try {
                field = Typeface.class.getDeclaredField("sSystemFontMap");
            } catch (NoSuchFieldException e) {
                // [BugFix 5] На Android 10+ поле називається "systemFontMap" замість "sSystemFontMap"
                field = Typeface.class.getDeclaredField("systemFontMap");
            }
            field.setAccessible(true);
            Map<String, Typeface> systemFontMap = (Map<String, Typeface>) field.get(null);
            if (systemFontMap != null) {
                Map<String, Typeface> newFontMap = new HashMap<>(systemFontMap);
                newFontMap.put("sans-serif", fontRegular);
                newFontMap.put("sans-serif-medium", fontMedium);
                newFontMap.put("sans-serif-bold", fontBold);
                newFontMap.put("sans-serif-black", fontBold);
                newFontMap.put("sans-serif-light", fontRegular);
                newFontMap.put("sans-serif-thin", fontRegular);
                newFontMap.put("default", fontRegular);
                newFontMap.put("default-bold", fontBold);
                
                field.set(null, newFontMap);
                Log.d(TAG, "SystemFontMap replaced successfully with custom font");
            }
        } catch (Throwable e) {
            Log.w(TAG, "Could not replace systemFontMap: " + e.getMessage());
        }
    }

    public static Typeface getDefault() {
        return fontRegular;
    }

    public static Typeface getDefaultBold() {
        return fontBold;
    }

    /**
     * Мапить будь-який шлях до шрифту на Plus Jakarta Sans.
     */
    public static Typeface getTypeface(String assetPath) {
        // [BugFix 1] Захист від NullPointerException
        if (assetPath == null) {
            return null;
        }

        // [BugFix 2] Використання volatile initialized для потокобезпечності
        if (!initialized && ApplicationLoader.applicationContext != null) {
            init(ApplicationLoader.applicationContext);
        }

        if (fontRegular == null) {
            return null; // Fallback
        }

        // 1. Не перехоплюємо моноширинні шрифти (вони необхідні для коду в ботах).
        if (assetPath.contains("mono") || assetPath.contains("courier")) {
            return null; 
        }

        // [BugFix 3] Правильний італік-мапінг з урахуванням ваги (rmediumitalic.ttf)
        if (assetPath.contains("italic")) {
            // Використовуємо згенеровані Faux-Italic для збереження і стилю, і ваги
            if (assetPath.contains("bold")) {
                return fontBoldItalic; 
            }
            if (assetPath.contains("medium")) {
                return fontMediumItalic;
            }
            return fontItalic;
        }

        // 3. Мапінг ваг (Weights)
        if (assetPath.contains("rextrabold") || assetPath.contains("rbold") || assetPath.contains("mw_bold")) {
            return fontBold;
        }
        if (assetPath.contains("rmedium") || assetPath.contains("medium")) {
            return fontMedium;
        }
        
        // Все інше (rregular, rcondensed, etc.)
        return fontRegular;
    }

    public static boolean isInitialized() {
        return initialized;
    }
}
