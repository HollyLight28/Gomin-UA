package ua.gomin.messenger.helpers;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;

import org.telegram.ui.LaunchActivity;

import java.util.ArrayList;
import java.util.Arrays;

public final class GominRestartHelper extends Activity {

    private static final String KEY_RESTART_INTENTS = "gomin_restart_intents";
    private static final String KEY_MAIN_PROCESS_PID = "gomin_main_process_pid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Intent intent = getIntent();
        if (intent != null) {
            int mainPid = intent.getIntExtra(KEY_MAIN_PROCESS_PID, -1);
            if (mainPid != -1 && mainPid != Process.myPid()) {
                Process.killProcess(mainPid);
            }
            
            ArrayList<Intent> intents = intent.getParcelableArrayListExtra(KEY_RESTART_INTENTS);
            if (intents != null && !intents.isEmpty()) {
                try {
                    startActivities(intents.toArray(new Intent[0]));
                } catch (Throwable e) {
                    // Safe fallback to launch a single activity
                    if (intents.get(0) != null) {
                        try {
                            startActivity(intents.get(0));
                        } catch (Throwable ignored) {}
                    }
                }
            }
        }
        
        finish();
        Runtime.getRuntime().exit(0);
    }

    private static void triggerRebirth(Context context, Intent... nextIntents) {
        nextIntents[0].addFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
        Intent intent = new Intent(context, GominRestartHelper.class);
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        intent.putParcelableArrayListExtra(KEY_RESTART_INTENTS, new ArrayList<>(Arrays.asList(nextIntents)));
        intent.putExtra(KEY_MAIN_PROCESS_PID, Process.myPid());
        context.startActivity(intent);
    }

    public static void restartApp(Context context) {
        triggerRebirth(context, new Intent(context, LaunchActivity.class));
    }

    public static void killApp() {
        System.exit(0);
    }
}
