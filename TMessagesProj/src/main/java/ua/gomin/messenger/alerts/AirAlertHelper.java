package ua.gomin.messenger.alerts;

public class AirAlertHelper {
    public static boolean shouldProcessAlert(String pushRegionId, String userRegionId) {
        if (pushRegionId == null) {
            return true; // Якщо регіон у пуші пустий, це глобальний пуш, дозволяємо
        }
        if (userRegionId == null || userRegionId.isEmpty()) {
            return false; // Якщо користувач не вибрав регіон, ігноруємо пуш із регіоном
        }
        return pushRegionId.equals(userRegionId);
    }
}
