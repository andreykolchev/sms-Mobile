package com.sms.service;


/*
 * sms
 * Created by A.Kolchev  29.2.2016
 */

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.text.TextUtils;

import com.sms.Settings;
import com.sms.util.Log;
import com.sms.util.NetworkUtil;
import com.sms.util.SyncConstants;
import com.sms.util.TimeUtil;


public class SyncAlarmReceiver extends BroadcastReceiver {

    private static final String TAG = SyncAlarmReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        log("onReceive");

        if (NetworkUtil.isAnyNetworkConnected(context)) {
            if (noUserCredentials()) {
                log("onReceive: network is present, but user credentials are NOT present, so rescheduling a sync alarm..");
                rescheduleSyncAlarm(context);

            } else {
                log("onReceive: network, user credentials are present, going to start SyncService..");
                startSyncService(context);

            }

        } else {
            log("onReceive: network is NOT present, going to enable ConnectivityChangesReceiver..");
            ConnectivityChangesReceiver.enable(context);
        }
    }

    private void cancelWrongNetworkNotification(Context context) {
        NotificationManager notifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notifyManager.cancel(SyncConstants.WRONG_NETWORK_NOTIFICATION_ID);
    }


    private boolean noSyncId() {
        return Settings.INSTANCE.getSyncId()==0;
    }

    private boolean noUserCredentials() {
        Settings s = Settings.INSTANCE;
        return TextUtils.isEmpty(s.getUserId()) || TextUtils.isEmpty(s.getPassword());
    }

    @SuppressLint("Wakelock")
    private void startSyncService(Context context) {
        // The Alarm Manager holds a CPU wake lock as long as the alarm receiver's
        // onReceive() method is executing. This guarantees that the phone will not
        // sleep until you have finished handling the broadcast. Once onReceive()
        // returns, the Alarm Manager releases this wake lock. This means that the
        // phone will in some cases sleep as soon as your onReceive() method
        // completes. If your alarm receiver called Context.startService(), it is
        // possible that the phone will sleep before the requested service is
        // launched. To prevent this, your BroadcastReceiver and Service will need
        // to implement a separate wake lock policy to ensure that the phone
        // continues running until the service becomes available.

        PowerManager.WakeLock wakeLock = SyncService.getWakeLock(context);
        wakeLock.acquire();

        Intent serviceIntent = new Intent(context, SyncService.class);
        context.startService(serviceIntent);
    }

    private void rescheduleSyncAlarm(Context context) {
        long when = getNextSyncAlarmTime();
        SyncAlarm.requestSyncAlarm(context, when, false);
    }

    private long getNextSyncAlarmTime() {
        return TimeUtil.getTimeWithDelay(SyncConstants.DEFAULT_SYNC_DELAY);
    }

    private static void log(String msg) {
        Log.d(TAG, msg);
    }
}
