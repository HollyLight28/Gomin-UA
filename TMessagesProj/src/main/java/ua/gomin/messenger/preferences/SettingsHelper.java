package ua.gomin.messenger.preferences;

import android.content.Context;
import android.view.View;

import org.telegram.messenger.R;
import org.telegram.ui.Cells.NotificationsCheckCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Components.UItem;

/**
 * Допоміжний клас для створення елементів меню налаштувань.
 */
public class SettingsHelper {

    /**
     * Заголовок з іконкою.
     */
    public static UItem asHeaderWithIcon(Context context, int iconRes, String text) {
        UItem item = UItem.asHeader(text);
        item.iconResId = iconRes;
        return item;
    }

    /**
     * Вимикач з описом.
     */
    public static UItem asSwitchCG(int id, String text, String desc) {
        if (desc != null) {
            return UItem.asButtonCheck(id, text, desc);
        }
        return UItem.asCheck(id, text);
    }

    /**
     * Оновити стан перемикача.
     * Підтримує TextCheckCell (asCheck) та NotificationsCheckCell (asButtonCheck).
     */
    public static void updateCheckState(View view, boolean checked) {
        if (view instanceof TextCheckCell) {
            ((TextCheckCell) view).setChecked(checked);
        } else if (view instanceof NotificationsCheckCell) {
            ((NotificationsCheckCell) view).setChecked(checked);
        }
    }

    /**
     * Оновити значення кнопки.
     */
    public static void updateButtonValue(View view, String value) {
        // TODO: Implement button value update
    }
}
