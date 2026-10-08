package ua.gomin.messenger.alerts;

import org.telegram.messenger.FileLog;

public class AirAlertHelper {
    public static boolean shouldProcessAlert(String pushRegionId, String userRegionId) {
        if (pushRegionId == null || pushRegionId.isEmpty()) {
            FileLog.d("AirAlertHelper: pushRegionId is null or empty, treating as global alert");
            return true; // Якщо регіон у пуші пустий, це глобальний пуш, дозволяємо
        }
        if (userRegionId == null || userRegionId.isEmpty()) {
            FileLog.e("AirAlertHelper: userRegionId is null or empty, cannot match push region: " + pushRegionId);
            return true; // Змінено: замість блокування - пропускаємо пуш, щоб не втрачати тривоги
        }
        boolean matches = pushRegionId.equals(userRegionId);
        if (!matches) {
            FileLog.d("AirAlertHelper: push region_id (" + pushRegionId + ") does not match user region_id (" + userRegionId + ")");
        }
        return matches;
    }
}
