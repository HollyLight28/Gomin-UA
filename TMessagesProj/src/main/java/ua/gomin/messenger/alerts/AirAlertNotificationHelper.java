package ua.gomin.messenger.alerts;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.R;

public class AirAlertNotificationHelper {

    private static final String CHANNEL_CRITICAL_ID = "air_alert_critical_v2";
    private static final String CHANNEL_INFO_ID = "air_alert_info_v2";
    private static final String CHANNEL_SILENT_ID = "air_alert_silent_v2";
    private static final int NOTIFICATION_ID = 1001;

    public static void createNotificationChannels(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager == null) return;

            NotificationChannel existingCriticalChannel = notificationManager.getNotificationChannel(CHANNEL_CRITICAL_ID);
            if (existingCriticalChannel != null) {
                try {
                    notificationManager.deleteNotificationChannel("air_alert_critical");
                    notificationManager.deleteNotificationChannel("air_alert_info");
                    notificationManager.deleteNotificationChannel("air_alert_silent");
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }

            // Канал для ТРИВОГИ (Гучний, з сиреною)
            Uri sirenUri = Uri.parse("android.resource://" + context.getPackageName() + "/raw/gomin_siren");
            NotificationChannel criticalChannel = new NotificationChannel(
                CHANNEL_CRITICAL_ID,
                "Повітряна тривога (Критичні)",
                NotificationManager.IMPORTANCE_HIGH
            );
            criticalChannel.setDescription("Сповіщення про початок повітряної тривоги");
            criticalChannel.setSound(sirenUri, new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build());
            criticalChannel.enableVibration(true);
            criticalChannel.setVibrationPattern(new long[]{0, 500, 200, 500, 200, 500});
            criticalChannel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);

            // Канал для ВІДБОЮ (Звичайний зі звуком відбою)
            Uri cancelUri = Uri.parse("android.resource://" + context.getPackageName() + "/raw/gomin_cancel");
            NotificationChannel infoChannel = new NotificationChannel(
                CHANNEL_INFO_ID,
                "Повітряна тривога (Інфо)",
                NotificationManager.IMPORTANCE_HIGH
            );
            infoChannel.setDescription("Сповіщення про відбій тривоги");
            infoChannel.setSound(cancelUri, new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build());
            infoChannel.enableVibration(true);
            infoChannel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);

            // Канал для ТИХОЇ ТРИВОГИ (Коли сирену зупинили, але статус висить)
            NotificationChannel silentChannel = new NotificationChannel(
                CHANNEL_SILENT_ID,
                "Повітряна тривога (Без звуку)",
                NotificationManager.IMPORTANCE_LOW
            );
            silentChannel.setDescription("Активна тривога з вимкненим звуком сирени");
            silentChannel.setSound(null, null);
            silentChannel.enableVibration(false);
            silentChannel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);

            notificationManager.createNotificationChannel(criticalChannel);
            notificationManager.createNotificationChannel(infoChannel);
            notificationManager.createNotificationChannel(silentChannel);
        }
    }

    public static void showStartNotification(Context context, String title, String body) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager == null) return;

        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        PendingIntent contentIntent = null;
        if (launchIntent != null) {
            contentIntent = PendingIntent.getActivity(context, 0, launchIntent, PendingIntent.FLAG_IMMUTABLE);
        }

        Intent stopIntent = new Intent(context, AirAlertStopReceiver.class);
        stopIntent.setAction("STOP_SIREN");
        PendingIntent stopPendingIntent = PendingIntent.getBroadcast(
            context, 1, stopIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        Uri sirenUri = Uri.parse("android.resource://" + context.getPackageName() + "/raw/gomin_siren");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_CRITICAL_ID)
            .setSmallIcon(R.drawable.notification)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setSound(sirenUri)
            .setAutoCancel(false)
            .setOngoing(true)
            .setContentIntent(contentIntent)
            .addAction(0, "ЗУПИНИТИ СИРЕНУ", stopPendingIntent)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setFullScreenIntent(contentIntent, true);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    public static void showSilentNotification(Context context, String title, String body) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager == null) return;

        notificationManager.cancel(NOTIFICATION_ID);

        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        PendingIntent contentIntent = null;
        if (launchIntent != null) {
            contentIntent = PendingIntent.getActivity(context, 0, launchIntent, PendingIntent.FLAG_IMMUTABLE);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_SILENT_ID)
            .setSmallIcon(R.drawable.notification)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setSound(null)
            .setAutoCancel(false)
            .setOngoing(true)
            .setContentIntent(contentIntent)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    public static void showEndNotification(Context context, String title, String body) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager == null) return;

        notificationManager.cancel(NOTIFICATION_ID);

        Uri cancelUri = Uri.parse("android.resource://" + context.getPackageName() + "/raw/gomin_cancel");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_INFO_ID)
            .setSmallIcon(R.drawable.notification)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(cancelUri)
            .setAutoCancel(true);

        notificationManager.notify(NOTIFICATION_ID + 1, builder.build());
    }

    public static void cancelAll(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.cancel(NOTIFICATION_ID);
        }
    }
}
