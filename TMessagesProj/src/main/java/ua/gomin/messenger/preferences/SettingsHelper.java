package ua.gomin.messenger.preferences;

import android.content.Context;
import android.view.View;

import org.telegram.messenger.R;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.Components.Switch;

/**
 * Допоміжний клас для створення елементів меню налаштувань.
 */
public class SettingsHelper {

    /**
     * Заголовок з іконкою.
     */
    public static UItem asHeaderWithIcon(Context context, int iconRes, String text) {
        UItem item = UItem.asHeader(text);
        item.icon = iconRes;
        return item;
    }

    /**
     * Вимикач з описом.
     */
    public static UItem asSwitchCG(int id, String text, String desc) {
        return UItem.asCheck(id, text, desc);
    }

    /**
     * Оновити стан перемикача.
     */
    public static void updateCheckState(View view, boolean checked) {
        if (view instanceof Switch) {
            ((Switch) view).setChecked(checked, true);
        }
    }

    /**
     * Оновити значення кнопки.
     */
    public static void updateButtonValue(View view, String value) {
        // TODO: Implement button value update
    }
}
