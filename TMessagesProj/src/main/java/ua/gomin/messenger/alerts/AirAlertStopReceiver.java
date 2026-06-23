package ua.gomin.messenger.alerts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AirAlertStopReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && "STOP_SIREN".equals(intent.getAction())) {
            AirAlertController.stopSiren();
        }
    }
}
