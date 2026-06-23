package ua.gomin.messenger.preferences;

import android.content.Context;
import android.view.View;

import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.Components.UniversalAdapter;

import ua.gomin.messenger.configs.GominAppearanceConfig;
import ua.gomin.messenger.configs.GominChatsConfig;
import ua.gomin.messenger.configs.GominCoreConfig;
import ua.gomin.messenger.configs.GominMessagesConfig;
import ua.gomin.messenger.configs.GominPrivacyConfig;
import ua.gomin.messenger.preferences.GominPreferencesNavigator;
import org.telegram.ui.Components.UniversalFragment;

import java.util.ArrayList;

import ua.gomin.messenger.configs.GominCameraConfig;

/**
 * Головна сторінка налаштувань Гоміна.
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

    // Camera
    private final int cameraDualRow = 60;

    // Chat Behavior
    private final int autoQuoteRow = 70;
    private final int deleteForAllRow = 71;
    private final int keepDeletedMessagesRow = 72;
    private final int doubleTapRow = 74;
    private final int slideActionRow = 75;

    // Security
    private final int securityAskBioRow = 80;
    private final int securityBioEncryptedRow = 81;
    private final int securityBioArchiveRow = 82;
    private final int securityBioDeleteRow = 83;
    private final int securitySystemPinRow = 84;

    // Interface
    private final int hideSearchBarRow = 90;
    private final int hideStoriesRow = 91;

    // Other
    private final int customWallpapersRow = 100;
    private final int springAnimationRow = 101;

    @Override
    protected CharSequence getTitle() {
        return "Налаштування Гоміна";
    }

    @Override
    protected void fillItems(ArrayList<UItem> items, UniversalAdapter adapter) {
        Context context = getContext();
        if (context == null) return;

        // Support Card
        items.add(UItem.asCustom(supportCardRow, null));
        // 🤖 GOMIN AI
        items.add(SettingsHelper.asHeaderWithIcon(context, R.drawable.msg_bot, "🤖 Gomin AI"));
        items.add(UItem.asButton(geminiSettingsRow, "Налаштування нейромережі", "Управління токенами та пресетами"));
        items.add(UItem.asShadow(null));

        // 👻 Ghost Mode
        items.add(SettingsHelper.asHeaderWithIcon(context, R.drawable.msg_secret, "👻 Ghost Mode"));
        items.add(SettingsHelper.asSwitchCG(ghostModeReadMessagesRow, "Невидиме читання", "Читай повідомлення без позначки прочитано")
                .setChecked(GominPrivacyConfig.INSTANCE.getGhostModeReadMessages(context)));
        items.add(SettingsHelper.asSwitchCG(ghostModeHideTypingRow, "Приховати друкування", "Ніхто не бачить коли ти печатаєш")
                .setChecked(GominPrivacyConfig.INSTANCE.getGhostModeHideTyping(context)));
        items.add(SettingsHelper.asSwitchCG(ghostModeHideStoryViewsRow, "Анонімні історії", "Перегляди сторіс не відображаються")
                .setChecked(GominPrivacyConfig.INSTANCE.getGhostModeHideStoryViews(context)));
        items.add(SettingsHelper.asSwitchCG(ghostModeHideOnlineRow, "Прихований онлайн", "Ніхто не бачить що ти в мережі")
                .setChecked(GominPrivacyConfig.INSTANCE.getGhostModeHideOnline(context)));
        items.add(UItem.asShadow(null));

        // 🚨 Повітряна тривога
        items.add(SettingsHelper.asHeaderWithIcon(context, R.drawable.msg_notifications, "🚨 Повітряна тривога"));
        items.add(SettingsHelper.asSwitchCG(airAlertEnabledRow, "Увімкнено", null)
                .setChecked(GominCoreConfig.INSTANCE.getAirAlertEnabled(context)));
        if (GominCoreConfig.INSTANCE.getAirAlertEnabled(context)) {
            String regionName = GominCoreConfig.INSTANCE.getAirAlertRegionName(context);
            items.add(UItem.asButton(airAlertRegionRow, "Регіон", regionName.isEmpty() ? "Не встановлено" : regionName));
            items.add(UItem.asButton(airAlertTestRow, "Тест", null));
        }
        items.add(UItem.asShadow(null));

        // 🔊 Звук сповіщень
        items.add(SettingsHelper.asHeaderWithIcon(context, R.drawable.msg_speed, "🔊 Звук сповіщень"));
        items.add(UItem.asButton(notificationSoundRow, "Вибір звуку", getNotificationSoundValue(context)));
        items.add(UItem.asShadow(null));

        // ⚡ Speed Engine
        items.add(SettingsHelper.asHeaderWithIcon(context, R.drawable.msg_speed, "⚡ Speed Engine"));
        items.add(UItem.asButton(downloadSpeedBoostRow, "Завантаження", getDownloadSpeedBoostValue(context)));
        items.add(SettingsHelper.asSwitchCG(uploadSpeedBoostRow, "Прискорення вивантаження", "Буфер 512KB")
                .setChecked(GominCoreConfig.INSTANCE.getUploadSpeedBoost(context)));
        items.add(UItem.asShadow(null));

        // 📷 Камера
        items.add(SettingsHelper.asHeaderWithIcon(context, R.drawable.msg_camera, "📷 Камера"));
        items.add(SettingsHelper.asSwitchCG(cameraDualRow, "Подвійна камера", "Використовувати дві камери одночасно")
                .setChecked(GominCameraConfig.INSTANCE.getUseDualCamera(context)));
        items.add(UItem.asShadow(null));

        // 💬 Поведінка
        items.add(SettingsHelper.asHeaderWithIcon(context, R.drawable.msg_settings, "💬 Поведінка"));
        items.add(SettingsHelper.asSwitchCG(autoQuoteRow, "Авто-цитата", "Автоматично цитувати при відповіді")
                .setChecked(GominChatsConfig.INSTANCE.getAutoQuoteReplies(context)));
        items.add(SettingsHelper.asSwitchCG(deleteForAllRow, "Видалити у всіх", "Підтвердження для видалення у всіх")
                .setChecked(GominMessagesConfig.INSTANCE.getDeleteForAll(context)));
        items.add(SettingsHelper.asSwitchCG(keepDeletedMessagesRow, "Зберігати видалені", "Не видаляти з локальної БД")
                .setChecked(GominPrivacyConfig.INSTANCE.getKeepDeletedMessages(context)));
        items.add(SettingsHelper.asSwitchCG(customWallpapersRow, "Кастомні шпалери", "Використовувати преміум шпалери")
                .setChecked(GominChatsConfig.INSTANCE.getCustomWallpapers(context)));
        items.add(UItem.asShadow(null));

        // 🎨 Інтерфейс
        items.add(SettingsHelper.asHeaderWithIcon(context, R.drawable.msg_palette, "🎨 Інтерфейс"));
        items.add(SettingsHelper.asSwitchCG(hideSearchBarRow, "Приховати пошук", "Приховати рядок пошуку зверху")
                .setChecked(GominAppearanceConfig.INSTANCE.getHideSearchFiled(context)));
        items.add(SettingsHelper.asSwitchCG(hideStoriesRow, "Приховати історії", "Не показувати Stories")
                .setChecked(GominCoreConfig.INSTANCE.getHideStories(context)));
        items.add(UItem.asShadow(null));

        // 🛠️ Інше
        items.add(SettingsHelper.asHeaderWithIcon(context, R.drawable.msg_settings, "🛠️ Інше"));
        items.add(SettingsHelper.asSwitchCG(springAnimationRow, "Spring анімація", "Пружинна анімація переходів")
                .setChecked(GominCoreConfig.INSTANCE.getSpringAnimation(context) == GominCoreConfig.ANIMATION_SPRING));
        items.add(UItem.asButton(doubleTapRow, "Подвійний тап", getDoubleTapActionValue(context)));
        items.add(UItem.asButton(slideActionRow, "Дія свайпа", getSlideActionValue(context)));
        items.add(UItem.asShadow(null));

        // 🔒 Безпека
        items.add(SettingsHelper.asHeaderWithIcon(context, R.drawable.msg_stealth_locked, "🔒 Безпека"));
        items.add(SettingsHelper.asSwitchCG(securityAskBioRow, "Біометрія для чатів", "Захист відбитком пальця")
                .setChecked(GominPrivacyConfig.INSTANCE.getAskBiometricsToOpenChat(context)));
        items.add(SettingsHelper.asSwitchCG(securityBioEncryptedRow, "Біометрія для encrypted", "Захист секретних чатів")
                .setChecked(GominPrivacyConfig.INSTANCE.getAskBiometricsToOpenEncrypted(context)));
        items.add(SettingsHelper.asSwitchCG(securityBioArchiveRow, "Біометрія для архіву", "Захист архіву")
                .setChecked(GominPrivacyConfig.INSTANCE.getAskBiometricsToOpenArchive(context)));
        items.add(SettingsHelper.asSwitchCG(securityBioDeleteRow, "Пін перед видаленням", "Підтвердження видалення акаунту")
                .setChecked(GominPrivacyConfig.INSTANCE.getAskPasscodeBeforeDelete(context)));
        items.add(SettingsHelper.asSwitchCG(securitySystemPinRow, "Системний пін", "Використовувати системний пін-код")
                .setChecked(GominPrivacyConfig.INSTANCE.getAllowSystemPasscode(context)));
        items.add(UItem.asShadow(null));
    }

    @Override
    protected void onClick(UItem item, View view, int position, float x, float y) {
        Context context = getContext();
        if (context == null) return;

        if (item.id == geminiSettingsRow) {
            GominPreferencesNavigator.INSTANCE.createGemini(this);
        } else if (item.id == airAlertRegionRow) {
            ArrayList<String> names = new ArrayList<>();
            ArrayList<String> ids = new ArrayList<>();
            String[] regionsData = {
                "1", "Вінницька", "2", "Волинська", "3", "Дніпропетровська", "4", "Донецька",
                "5", "Житомирська", "6", "Закарпатська", "7", "Запорізька", "8", "Івано-Франківська",
                "9", "Київська", "10", "Кіровоградська", "11", "Луганська", "12", "Львівська",
                "13", "Миколаївська", "14", "Одеська", "15", "Полтавська", "16", "Рівненська",
                "17", "Сумська", "18", "Тернопільська", "19", "Харківська", "20", "Херсонська",
                "21", "Хмельницька", "22", "Черкаська", "23", "Чернівецька", "24", "Чернігівська",
                "25", "м. Київ", "26", "АР Крим"
            };
            for (int i = 0; i < regionsData.length; i += 2) {
                ids.add(regionsData[i]);
                names.add(regionsData[i + 1]);
            }
            ua.gomin.messenger.helpers.ui.PopupHelper.show(names, "Оберіть регіон", ids.indexOf(GominCoreConfig.INSTANCE.getAirAlertRegionId(context)), context, i -> {
                String oldRegionId = GominCoreConfig.INSTANCE.getAirAlertRegionId(context);
                String newRegionId = ids.get(i);
                if (!oldRegionId.equals(newRegionId)) {
                    if (!oldRegionId.isEmpty()) {
                        try {
                            com.google.firebase.messaging.FirebaseMessaging.getInstance().unsubscribeFromTopic("region_" + oldRegionId);
                        } catch (Exception e) {
                            org.telegram.messenger.FileLog.e(e);
                        }
                    }
                    try {
                        com.google.firebase.messaging.FirebaseMessaging.getInstance().subscribeToTopic("region_" + newRegionId);
                    } catch (Exception e) {
                        org.telegram.messenger.FileLog.e(e);
                    }
                }
                GominCoreConfig.INSTANCE.setAirAlertRegionId(context, newRegionId);
                GominCoreConfig.INSTANCE.setAirAlertRegionName(context, names.get(i));
                listView.adapter.update(true);
            });
        } else if (item.id == airAlertTestRow) {
            ua.gomin.messenger.alerts.AirAlertController.testAlert();
        } else if (item.id == notificationSoundRow) {
            // TODO: Show sound selector
        } else if (item.id == downloadSpeedBoostRow) {
            org.telegram.ui.ActionBar.AlertDialog.Builder builder = new org.telegram.ui.ActionBar.AlertDialog.Builder(context);
            builder.setTitle("Завантаження");
            builder.setItems(new CharSequence[]{
                "Вимкнено (Telegram)",
                "Баланс",
                "Максимально (Гомін)"
            }, (dialog, which) -> {
                GominCoreConfig.INSTANCE.setDownloadSpeedBoost(context, which);
                ua.gomin.messenger.speed.GominSpeedController.INSTANCE.resetCache();
                listView.adapter.update(true);
            });
            showDialog(builder.create());
        } else if (item.id == doubleTapRow) {
            // TODO: Show double tap selector
        } else if (item.id == slideActionRow) {
            // TODO: Show slide action selector
        }

        // Ghost Mode switches
        else if (item.id == ghostModeReadMessagesRow) {
            GominPrivacyConfig.INSTANCE.setGhostModeReadMessages(context, !GominPrivacyConfig.INSTANCE.getGhostModeReadMessages(context));
            SettingsHelper.updateCheckState(view, GominPrivacyConfig.INSTANCE.getGhostModeReadMessages(context));
        } else if (item.id == ghostModeHideTypingRow) {
            GominPrivacyConfig.INSTANCE.setGhostModeHideTyping(context, !GominPrivacyConfig.INSTANCE.getGhostModeHideTyping(context));
            SettingsHelper.updateCheckState(view, GominPrivacyConfig.INSTANCE.getGhostModeHideTyping(context));
        } else if (item.id == ghostModeHideStoryViewsRow) {
            GominPrivacyConfig.INSTANCE.setGhostModeHideStoryViews(context, !GominPrivacyConfig.INSTANCE.getGhostModeHideStoryViews(context));
            SettingsHelper.updateCheckState(view, GominPrivacyConfig.INSTANCE.getGhostModeHideStoryViews(context));
        } else if (item.id == ghostModeHideOnlineRow) {
            GominPrivacyConfig.INSTANCE.setGhostModeHideOnline(context, !GominPrivacyConfig.INSTANCE.getGhostModeHideOnline(context));
            SettingsHelper.updateCheckState(view, GominPrivacyConfig.INSTANCE.getGhostModeHideOnline(context));
        }

        // Air Alert
        else if (item.id == airAlertEnabledRow) {
            boolean newValue = !GominCoreConfig.INSTANCE.getAirAlertEnabled(context);
            GominCoreConfig.INSTANCE.setAirAlertEnabled(context, newValue);
            SettingsHelper.updateCheckState(view, newValue);
            listView.adapter.update(true);
            if (newValue) {
                ua.gomin.messenger.alerts.AirAlertController.startMonitoring();
                String currentRegion = GominCoreConfig.INSTANCE.getAirAlertRegionId(context);
                if (!currentRegion.isEmpty()) {
                    try {
                        com.google.firebase.messaging.FirebaseMessaging.getInstance().subscribeToTopic("region_" + currentRegion);
                    } catch (Exception e) {
                        org.telegram.messenger.FileLog.e(e);
                    }
                }
            } else {
                ua.gomin.messenger.alerts.AirAlertController.stopMonitoring();
                String currentRegion = GominCoreConfig.INSTANCE.getAirAlertRegionId(context);
                if (!currentRegion.isEmpty()) {
                    try {
                        com.google.firebase.messaging.FirebaseMessaging.getInstance().unsubscribeFromTopic("region_" + currentRegion);
                    } catch (Exception e) {
                        org.telegram.messenger.FileLog.e(e);
                    }
                }
            }
        }

        // Speed Engine
        else if (item.id == uploadSpeedBoostRow) {
            GominCoreConfig.INSTANCE.setUploadSpeedBoost(context, !GominCoreConfig.INSTANCE.getUploadSpeedBoost(context));
            ua.gomin.messenger.speed.GominSpeedController.INSTANCE.resetCache();
            SettingsHelper.updateCheckState(view, GominCoreConfig.INSTANCE.getUploadSpeedBoost(context));
        }

        // Camera
        else if (item.id == cameraDualRow) {
            GominCameraConfig.INSTANCE.setUseDualCamera(context, !GominCameraConfig.INSTANCE.getUseDualCamera(context));
            SettingsHelper.updateCheckState(view, GominCameraConfig.INSTANCE.getUseDualCamera(context));
        }

        // Chat Behavior
        else if (item.id == autoQuoteRow) {
            GominChatsConfig.INSTANCE.setAutoQuoteReplies(context, !GominChatsConfig.INSTANCE.getAutoQuoteReplies(context));
            SettingsHelper.updateCheckState(view, GominChatsConfig.INSTANCE.getAutoQuoteReplies(context));
        } else if (item.id == deleteForAllRow) {
            GominMessagesConfig.INSTANCE.setDeleteForAll(context, !GominMessagesConfig.INSTANCE.getDeleteForAll(context));
            SettingsHelper.updateCheckState(view, GominMessagesConfig.INSTANCE.getDeleteForAll(context));
        } else if (item.id == keepDeletedMessagesRow) {
            GominPrivacyConfig.INSTANCE.setKeepDeletedMessages(context, !GominPrivacyConfig.INSTANCE.getKeepDeletedMessages(context));
            SettingsHelper.updateCheckState(view, GominPrivacyConfig.INSTANCE.getKeepDeletedMessages(context));
        } else if (item.id == customWallpapersRow) {
            GominChatsConfig.INSTANCE.setCustomWallpapers(context, !GominChatsConfig.INSTANCE.getCustomWallpapers(context));
            SettingsHelper.updateCheckState(view, GominChatsConfig.INSTANCE.getCustomWallpapers(context));
        }

        // Interface
        else if (item.id == hideSearchBarRow) {
            boolean newVal = !GominAppearanceConfig.INSTANCE.getHideSearchFiled(context);
            GominAppearanceConfig.INSTANCE.setHideSearchFiled(context, newVal);
            SettingsHelper.updateCheckState(view, newVal);
            NotificationCenter.getInstance(currentAccount).postNotificationName(NotificationCenter.updateInterfaces, 0);
        } else if (item.id == hideStoriesRow) {
            boolean newVal = !GominCoreConfig.INSTANCE.getHideStories(context);
            GominCoreConfig.INSTANCE.setHideStories(context, newVal);
            SettingsHelper.updateCheckState(view, newVal);
            NotificationCenter.getInstance(currentAccount).postNotificationName(NotificationCenter.updateInterfaces, 0);
        }

        // Other
        else if (item.id == springAnimationRow) {
            int current = GominCoreConfig.INSTANCE.getSpringAnimation(context);
            int next = current == GominCoreConfig.ANIMATION_SPRING ? GominCoreConfig.ANIMATION_CLASSIC : GominCoreConfig.ANIMATION_SPRING;
            GominCoreConfig.INSTANCE.setSpringAnimation(context, next);
            SettingsHelper.updateCheckState(view, next == GominCoreConfig.ANIMATION_SPRING);
        }

        // Security
        else if (item.id == securityAskBioRow) {
            GominPrivacyConfig.INSTANCE.setAskBiometricsToOpenChat(context, !GominPrivacyConfig.INSTANCE.getAskBiometricsToOpenChat(context));
            SettingsHelper.updateCheckState(view, GominPrivacyConfig.INSTANCE.getAskBiometricsToOpenChat(context));
        } else if (item.id == securityBioEncryptedRow) {
            GominPrivacyConfig.INSTANCE.setAskBiometricsToOpenEncrypted(context, !GominPrivacyConfig.INSTANCE.getAskBiometricsToOpenEncrypted(context));
            SettingsHelper.updateCheckState(view, GominPrivacyConfig.INSTANCE.getAskBiometricsToOpenEncrypted(context));
        } else if (item.id == securityBioArchiveRow) {
            GominPrivacyConfig.INSTANCE.setAskBiometricsToOpenArchive(context, !GominPrivacyConfig.INSTANCE.getAskBiometricsToOpenArchive(context));
            SettingsHelper.updateCheckState(view, GominPrivacyConfig.INSTANCE.getAskBiometricsToOpenArchive(context));
        } else if (item.id == securityBioDeleteRow) {
            GominPrivacyConfig.INSTANCE.setAskPasscodeBeforeDelete(context, !GominPrivacyConfig.INSTANCE.getAskPasscodeBeforeDelete(context));
            SettingsHelper.updateCheckState(view, GominPrivacyConfig.INSTANCE.getAskPasscodeBeforeDelete(context));
        } else if (item.id == securitySystemPinRow) {
            GominPrivacyConfig.INSTANCE.setAllowSystemPasscode(context, !GominPrivacyConfig.INSTANCE.getAllowSystemPasscode(context));
            SettingsHelper.updateCheckState(view, GominPrivacyConfig.INSTANCE.getAllowSystemPasscode(context));
        }
    }

    // Helper methods
    private String getNotificationSoundValue(Context context) {
        int sound = GominChatsConfig.INSTANCE.getNotificationSound(context);
        if (sound == GominChatsConfig.NOTIF_SOUND_GOMIN_1) return "Гомін: Маріо";
        if (sound == GominChatsConfig.NOTIF_SOUND_GOMIN_2) return "Гомін: Сурма";
        if (sound == GominChatsConfig.NOTIF_SOUND_GOMIN_3) return "Гомін: Дзвіночок";
        return "Стандартний";
    }

    private String getDownloadSpeedBoostValue(Context context) {
        int level = GominCoreConfig.INSTANCE.getDownloadSpeedBoost(context);
        if (level == GominCoreConfig.BOOST_NONE) return "Вимкнено";
        if (level == GominCoreConfig.BOOST_AVERAGE) return "Баланс";
        return "Максимально";
    }

    private String getDoubleTapActionValue(Context context) {
        int action = GominMessagesConfig.INSTANCE.getDoubleTapAction(context);
        if (action == GominMessagesConfig.DOUBLE_TAP_ACTION_REACTION) return "Реакція";
        if (action == GominMessagesConfig.DOUBLE_TAP_ACTION_REPLY) return "Відповісти";
        if (action == GominMessagesConfig.DOUBLE_TAP_ACTION_SAVE) return "Зберегти";
        return "Вимкнено";
    }

    private String getSlideActionValue(Context context) {
        int action = GominMessagesConfig.INSTANCE.getMessageSlideAction(context);
        if (action == GominMessagesConfig.MESSAGE_SLIDE_ACTION_SAVE) return "Зберегти";
        if (action == GominMessagesConfig.MESSAGE_SLIDE_ACTION_TRANSLATE) return "Перекласти";
        return "Відповісти";
    }

    @Override
    protected boolean onLongClick(UItem item, View view, int position, float x, float y) {
        return false;
    }
}
