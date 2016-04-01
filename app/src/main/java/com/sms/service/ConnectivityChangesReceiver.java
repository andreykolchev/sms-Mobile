package com.sms.service;


/*
 * sms
 * Created by A.Kolchev  29.2.2016
 */

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.sms.util.Log;
import com.sms.util.NetworkUtil;
import com.sms.util.SyncConstants;
import com.sms.util.TimeUtil;


public class ConnectivityChangesReceiver extends BroadcastReceiver {

    private static final String TAG = ConnectivityChangesReceiver.class.getSimpleName();

    public static void enable(Context context) {
        enable(context, true);
    }

    public static void disable(Context context) {
        enable(context, false);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        log("onReceive");

        if (NetworkUtil.isAnyNetworkConnected(context)) {
            log("onReceive: network appeared, so disabling ConnectivityChangesReceiver..");
            disable(context);

            log("onReceive: network appeared, so scheduling a sync alarm..");
            long when = getNextSyncAlarmTime();
            SyncAlarm.requestSyncAlarm(context, when, false);

        } else {
            log("onReceive: network is NOT present, just skipping the event");
        }
    }

    private long getNextSyncAlarmTime() {
        return TimeUtil.getTimeWithDelay(SyncConstants.NETWORK_RESTORED_SYNC_DELAY);
    }

    private static void enable(Context context, boolean enable) {
        ComponentName receiver = new ComponentName(context, ConnectivityChangesReceiver.class);
        PackageManager pm = context.getPackageManager();
        int newState;
        if (enable) {
            newState = PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
        } else {
            newState = PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
        }
        pm.setComponentEnabledSetting(receiver, newState, PackageManager.DONT_KILL_APP);
    }

    private static void log(String msg) {
        Log.d(TAG, msg);
    }
}