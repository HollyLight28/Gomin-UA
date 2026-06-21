package ua.gomin.messenger.preferences;

import android.content.Context;
import android.view.View;

import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.Components.UniversalAdapter;
import org.telegram.ui.Components.UniversalFragment;

import java.util.ArrayList;

/**
 * Головна сторінка налаштувань Гоміна.
 * Відкривається з профілю Telegram.
 */
public class GominSettingsEntry extends UniversalFragment {

    // Row IDs
    private final int supportCardRow = 0;
    private final int aboutRow = 1;

    // Gomin AI
    private final int geminiSettingsRow = 10;

    // Ghost Mode
    private final int ghostModeReadMessagesRow = 20;
    private final int ghostModeHideTypingRow = 21;
    private final int ghostModeHideStoryViewsRow = 22;
    private final int ghostModeHideOnlineRow = 23;

    // Air Alert
    private final int airAlertEnabledRow = 30;
    private final int airAlertRegionRow = 31;
    private final int airAlertTestRow = 32;

    // Notification Sound
    private final int notificationSoundRow = 40;

    // Speed Engine
    private final int downloadSpeedBoostRow = 50;
    private final int uploadSpeedBoostRow = 51;
    private final int slowNetworkModeRow = 52;

    // Camera
    private final int cameraDualRow = 60;

    // Chat Behavior
    private final int autoQuoteRow = 70;
    private final int deleteForAllRow = 71;
    private final int keepDeletedMessagesRow = 72;
    private final int customWallpapersRow = 73;

    // Interface
    private final int hideSearchBarRow = 80;
    private final int hideStoriesRow = 81;

    // Other
    private final int springAnimationRow = 90;
    private final int doubleTapRow = 91;
    private final int slideActionRow = 92;

    // Security
    private final int securityAskBioRow = 100;
    private final int securityLockedChatsRow = 101;
    private final int securityBioEncryptedRow = 102;
    private final int securityBioArchiveRow = 103;
    private final int securityBioDeleteRow = 104;
    private final int securitySystemPinRow = 105;

    @Override
    protected CharSequence getTitle() {
        return "Налаштування Гоміна";
    }

    @Override
    protected void fillItems(ArrayList<UItem> items, UniversalAdapter adapter) {
        Context context = getContext();
        if (context == null) return;

        // ❤️ Підтримка проекту
        // items.add(UItem.asShadow(null));

        // 🤖 GOMIN AI
        items.add(UItem.asHeaderWithIcon(geminiSettingsRow, "🤖 Gomin AI"));
        items.add(UItem.asButton(geminiSettingsRow, "Налаштування Gemini"));
        items.add(UItem.asShadow(null));

        // 👻 Ghost Mode
        items.add(UItem.asHeaderWithIcon(ghostModeReadMessagesRow, "👻 Ghost Mode"));
        items.add(UItem.asSwitch(ghostModeReadMessagesRow, "Невидиме читання", "Читай повідомлення без позначки "прочитано""));
        items.add(UItem.asSwitch(ghostModeHideTypingRow, "Приховати друкування", "Ніхто не бачить коли ти печатаєш"));
        items.add(UItem.asSwitch(ghostModeHideStoryViewsRow, "Анонімні історії", "Перегляди сторіс не відображаються"));
        items.add(UItem.asSwitch(ghostModeHideOnlineRow, "Прихований онлайн", "Ніхто не бачить що ти в мережі"));
        items.add(UItem.asShadow(null));

        // 🚨 Повітряна тривога
        items.add(UItem.asHeaderWithIcon(airAlertEnabledRow, "🚨 Повітряна тривога"));
        items.add(UItem.asSwitch(airAlertEnabledRow, "Увімкнено", null));
        items.add(UItem.asButton(airAlertRegionRow, "Регіон", "Не встановлено"));
        items.add(UItem.asButton(airAlertTestRow, "Тест", null));
        items.add(UItem.asShadow(null));

        // 🔊 Звук сповіщень
        items.add(UItem.asHeaderWithIcon(notificationSoundRow, "🔊 Звук сповіщень"));
        items.add(UItem.asButton(notificationSoundRow, "Вибір звуку", "Стандартний"));
        items.add(UItem.asShadow(null));

        // ⚡ Speed Engine
        items.add(UItem.asHeaderWithIcon(downloadSpeedBoostRow, "⚡ Speed Engine"));
        items.add(UItem.asButton(downloadSpeedBoostRow, "Завантаження", "Максимально"));
        items.add(UItem.asSwitch(uploadSpeedBoostRow, "Прискорення завантаження", "Буфер 512KB"));
        items.add(UItem.asSwitch(slowNetworkModeRow, "Режим слабкої мережі", "1 потік × 32KB"));
        items.add(UItem.asShadow(null));

        // 📷 Камера
        items.add(UItem.asHeaderWithIcon(cameraDualRow, "📷 Камера"));
        items.add(UItem.asSwitch(cameraDualRow, "Подвійна камера", "Використовувати дві камери одночасно"));
        items.add(UItem.asShadow(null));

        // 💬 Поведінка
        items.add(UItem.asHeaderWithIcon(autoQuoteRow, "💬 Поведінка"));
        items.add(UItem.asSwitch(autoQuoteRow, "Авто-цитата", "Автоматично цитувати при відповіді"));
        items.add(UItem.asSwitch(deleteForAllRow, "Видалити у всіх", "Підтвердження для видалення у всіх"));
        items.add(UItem.asSwitch(keepDeletedMessagesRow, "Зберігати видалені", "Не видаляти з локальної БД"));
        items.add(UItem.asSwitch(customWallpapersRow, "Кастомні шпалери", "Використовувати преміум шпалери"));
        items.add(UItem.asShadow(null));

        // 🎨 Інтерфейс
        items.add(UItem.asHeaderWithIcon(hideSearchBarRow, "🎨 Інтерфейс"));
        items.add(UItem.asSwitch(hideSearchBarRow, "Приховати пошук", "Приховати рядок пошуку"));
        items.add(UItem.asSwitch(hideStoriesRow, "Приховати історії", "Не показувати Stories"));
        items.add(UItem.asShadow(null));

        // 🛠️ Інше
        items.add(UItem.asHeaderWithIcon(springAnimationRow, "🛠️ Інше"));
        items.add(UItem.asSwitch(springAnimationRow, "Spring анімація", "Пружинна анімація переходів"));
        items.add(UItem.asButton(doubleTapRow, "Подвійний тап", "Реакція"));
        items.add(UItem.asButton(slideActionRow, "Дія свайпа", "Відповісти"));
        items.add(UItem.asShadow(null));

        // 🔒 Безпека
        items.add(UItem.asHeaderWithIcon(securityAskBioRow, "🔒 Безпека"));
        items.add(UItem.asSwitch(securityAskBioRow, "Біометрія для чатів", "Захист відбитком пальця"));
        items.add(UItem.asSwitch(securityBioEncryptedRow, "Біометрія для encrypted", "Захист секретних чатів"));
        items.add(UItem.asSwitch(securityBioArchiveRow, "Біометрія для архіву", "Захист архіву"));
        items.add(UItem.asSwitch(securityBioDeleteRow, "Пін перед видаленням", "Підтвердження видалення акаунту"));
        items.add(UItem.asSwitch(securitySystemPinRow, "Системний пін", "Використовувати системний пін-код"));
        items.add(UItem.asShadow(null));
    }

    @Override
    protected void onClick(UItem item, View view, int position, float x, float y) {
        if (item.id == geminiSettingsRow) {
            GominPreferencesNavigator.INSTANCE.createGemini(this);
        } else if (item.id == airAlertRegionRow) {
            // TODO: Show region selector
        } else if (item.id == airAlertTestRow) {
            // TODO: Test alert
        } else if (item.id == notificationSoundRow) {
            // TODO: Show sound selector
        } else if (item.id == downloadSpeedBoostRow) {
            // TODO: Show speed selector
        } else if (item.id == doubleTapRow) {
            // TODO: Show double tap selector
        } else if (item.id == slideActionRow) {
            // TODO: Show slide action selector
        }

        // Switches
        if (item.id >= ghostModeReadMessagesRow && item.id <= ghostModeHideOnlineRow) {
            // TODO: Toggle ghost mode setting
        }
        if (item.id >= securityAskBioRow && item.id <= securitySystemPinRow) {
            // TODO: Toggle security setting
        }
    }
}
