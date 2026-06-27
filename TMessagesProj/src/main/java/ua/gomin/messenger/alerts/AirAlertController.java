package ua.gomin.messenger.alerts;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.util.Pair;

import org.json.JSONObject;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.Utilities;
import ua.gomin.messenger.configs.GominCoreConfig;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AirAlertController {

    private static final String TAG = "AirAlertController";
    
    private static volatile boolean isAlertActive = false;
    private static volatile String lastAlertTitle = null;
    private static volatile String lastAlertBody = null;
    
    private static Timer timer = null;
    
    private static Runnable testStopRunnable = null;
    private static Runnable sirenStopRunnable = null;
    private static final Runnable safetyStopRunnable = () -> setAlertStatus(false, null, null);
    
    private static volatile boolean isTesting = false;
    private static boolean savedAlertState = false;
    private static volatile Boolean pendingAlertStatus = null;
    
    private static final long SAFETY_TIMEOUT_MS = 43200000L; // 12 годин
    private static final long SIREN_DURATION_MS = 15000L;

    public interface StatusCallback {
        void onStatusChecked(boolean isActive);
    }

    public interface RegionsCallback {
        void onRegionsLoaded(List<Pair<String, String>> regions);
    }

    public static void init() {
        Context context = ApplicationLoader.applicationContext;
        if (context == null) return;
        
        isAlertActive = GominCoreConfig.INSTANCE.getAirAlertLastActive(context);

        if (GominCoreConfig.INSTANCE.getAirAlertEnabled(context)) {
            startMonitoring();
            String regionId = GominCoreConfig.INSTANCE.getAirAlertRegionId(context);
            if (regionId != null && !regionId.isEmpty()) {
                try {
                    com.google.firebase.messaging.FirebaseMessaging.getInstance().subscribeToTopic("region_" + regionId);
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
        }
    }

    public static void startMonitoring() {
        stopMonitoring();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkAlertStatus(null);
            }
        }, 0, 60000);
    }

    public static void stopMonitoring() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public static void checkAlertStatus(final StatusCallback callback) {
        if (isTesting) {
            if (callback != null) {
                AndroidUtilities.runOnUIThread(() -> callback.onStatusChecked(isAlertActive));
            }
            return;
        }

        final Context context = ApplicationLoader.applicationContext;
        if (context == null) return;
        
        final String regionId = GominCoreConfig.INSTANCE.getAirAlertRegionId(context);
        if (regionId == null || regionId.isEmpty()) {
            if (callback != null) {
                AndroidUtilities.runOnUIThread(() -> callback.onStatusChecked(false));
            }
            return;
        }

        Utilities.globalQueue.postRunnable(() -> {
            try {
                URL url = new URL("http://204.168.201.148:5000/status?region_id=" + regionId);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                if (connection.getResponseCode() == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    reader.close();
                    
                    JSONObject jsonObject = new JSONObject(sb.toString());
                    final boolean hasAirAlert = jsonObject.optBoolean("alert", false);

                    AndroidUtilities.runOnUIThread(() -> {
                        setAlertStatus(hasAirAlert, null, null);
                        if (callback != null) {
                            callback.onStatusChecked(hasAirAlert);
                        }
                    });
                } else {
                    AndroidUtilities.runOnUIThread(() -> {
                        if (callback != null) {
                            callback.onStatusChecked(isAlertActive);
                        }
                    });
                }
            } catch (Exception e) {
                FileLog.e(e);
                AndroidUtilities.runOnUIThread(() -> {
                    if (callback != null) {
                        callback.onStatusChecked(isAlertActive);
                    }
                });
            }
        });
    }

    private static void setAlertStatus(boolean active, String title, String body) {
        Context context = ApplicationLoader.applicationContext;
        if (context == null) return;
        
        if (!GominCoreConfig.INSTANCE.getAirAlertEnabled(context)) {
            isAlertActive = false;
            GominCoreConfig.INSTANCE.setAirAlertLastActive(context, false);
            AirAlertNotificationHelper.cancelAll(context);
            return;
        }

        if (isTesting) {
            pendingAlertStatus = active;
            return;
        }
        
        boolean changed = isAlertActive != active;
        boolean textChanged = active && ((title != null && !title.equals(lastAlertTitle)) || (body != null && !body.equals(lastAlertBody)));
        
        if (changed || textChanged) {
            isAlertActive = active;
            GominCoreConfig.INSTANCE.setAirAlertLastActive(context, active);
            
            String configRegionName = GominCoreConfig.INSTANCE.getAirAlertRegionName(context);
            String regionName = (configRegionName == null || configRegionName.isEmpty()) ? "Ваша область" : configRegionName;
            
            if (isAlertActive) {
                String finalTitle = title != null ? title : "🚨 ПОВІТРЯНА ТРИВОГА";
                String finalBody = body != null ? body : regionName;
                
                lastAlertTitle = finalTitle;
                lastAlertBody = finalBody;
                
                AirAlertNotificationHelper.showStartNotification(context, finalTitle, finalBody);
                
                if (safetyStopRunnable != null) {
                    AndroidUtilities.cancelRunOnUIThread(safetyStopRunnable);
                }
                
                AndroidUtilities.runOnUIThread(safetyStopRunnable, SAFETY_TIMEOUT_MS);
            } else {
                if (safetyStopRunnable != null) {
                    AndroidUtilities.cancelRunOnUIThread(safetyStopRunnable);
                }
                
                String endTitle = title != null ? title : "✅ ВІДБІЙ ТРИВОГИ";
                String endBody = body != null ? body : regionName;
                AirAlertNotificationHelper.showEndNotification(context, endTitle, endBody);
            }
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.cgAirAlertStatusChanged);
        }
    }

    public static boolean isAlertActive() {
        return isAlertActive;
    }

    public static void stopSiren() {
        AndroidUtilities.runOnUIThread(() -> {
            if (isTesting) {
                stopTest();
                return;
            }
            
            Context context = ApplicationLoader.applicationContext;
            if (context == null) return;
            
            if (isAlertActive) {
                String title = lastAlertTitle != null ? lastAlertTitle : "🚨 ПОВІТРЯНА ТРИВОГА";
                String body = lastAlertBody != null ? lastAlertBody : "Звук сирени вимкнено";
                AirAlertNotificationHelper.showSilentNotification(context, title, body);
            } else {
                AirAlertNotificationHelper.cancelAll(context);
            }
        });
    }

    public static void testAlert() {
        if (isTesting) {
            stopTest();
            return;
        }

        isTesting = true;
        savedAlertState = isAlertActive;
        if (safetyStopRunnable != null) {
            AndroidUtilities.cancelRunOnUIThread(safetyStopRunnable);
        }
        isAlertActive = true;
        AirAlertNotificationHelper.showStartNotification(ApplicationLoader.applicationContext, "🚨 ТЕСТОВА ТРИВОГА", "Перевірка звуку сирени");
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.cgAirAlertStatusChanged);

        testStopRunnable = AirAlertController::stopTest;
        AndroidUtilities.runOnUIThread(testStopRunnable, SIREN_DURATION_MS);
    }

    private static void stopTest() {
        isTesting = false;
        if (testStopRunnable != null) {
            AndroidUtilities.cancelRunOnUIThread(testStopRunnable);
            testStopRunnable = null;
        }
        if (sirenStopRunnable != null) {
            AndroidUtilities.cancelRunOnUIThread(sirenStopRunnable);
            sirenStopRunnable = null;
        }

        boolean wasAlertActiveBeforeTest = savedAlertState;
        if (pendingAlertStatus != null) {
            savedAlertState = pendingAlertStatus;
            pendingAlertStatus = null;
        }

        boolean targetAlertState = savedAlertState;
        Context context = ApplicationLoader.applicationContext;

        if (targetAlertState) {
            isAlertActive = false;
            setAlertStatus(true, lastAlertTitle, lastAlertBody);
        } else {
            if (wasAlertActiveBeforeTest) {
                isAlertActive = true;
                setAlertStatus(false, null, null);
            } else {
                isAlertActive = false;
                if (context != null) {
                    GominCoreConfig.INSTANCE.setAirAlertLastActive(context, false);
                    AirAlertNotificationHelper.cancelAll(context);
                }
                NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.cgAirAlertStatusChanged);
            }
        }
    }

    public static void fetchRegions(String apiKey, final RegionsCallback callback) {
        final List<Pair<String, String>> regions = Arrays.asList(
            new Pair<>("1", "Вінницька"), new Pair<>("2", "Волинська"), new Pair<>("3", "Дніпропетровська"), new Pair<>("4", "Донецька"),
            new Pair<>("5", "Житомирська"), new Pair<>("6", "Закарпатська"), new Pair<>("7", "Запорізька"), new Pair<>("8", "Івано-Франківська"),
            new Pair<>("9", "Київська"), new Pair<>("10", "Кіровоградська"), new Pair<>("11", "Луганська"), new Pair<>("12", "Львівська"),
            new Pair<>("13", "Миколаївська"), new Pair<>("14", "Одеська"), new Pair<>("15", "Полтавська"), new Pair<>("16", "Рівненська"),
            new Pair<>("17", "Сумська"), new Pair<>("18", "Тернопільська"), new Pair<>("19", "Харківська"), new Pair<>("20", "Херсонська"),
            new Pair<>("21", "Хмельницька"), new Pair<>("22", "Черкаська"), new Pair<>("23", "Чернівецька"), new Pair<>("24", "Чернігівська"),
            new Pair<>("25", "м. Київ"), new Pair<>("26", "АР Крим")
        );
        AndroidUtilities.runOnUIThread(() -> callback.onRegionsLoaded(regions));
    }

    public static void handlePushStatus(final boolean alert, final String title, final String body, final String regionId) {
        final Context context = ApplicationLoader.applicationContext;
        if (context == null) return;
        
        if (!GominCoreConfig.INSTANCE.getAirAlertEnabled(context)) {
            return;
        }
        
        String userRegionId = GominCoreConfig.INSTANCE.getAirAlertRegionId(context);
        if (!AirAlertHelper.shouldProcessAlert(regionId, userRegionId)) {
            FileLog.d("AirAlertController: push region_id (" + regionId + ") does not match user region_id (" + userRegionId + "). Ignore.");
            return;
        }
        
        AndroidUtilities.runOnUIThread(() -> {
            if (isTesting) {
                pendingAlertStatus = alert;
            } else {
                setAlertStatus(alert, title, body);
            }
        });
    }
}
