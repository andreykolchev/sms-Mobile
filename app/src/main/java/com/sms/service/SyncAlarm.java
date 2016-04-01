package com.sms.service;


/*
 * sms
 * Created by A.Kolchev  29.2.2016
 */

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class SyncAlarm {

    /**
     * Sets alarm to start SyncService in <code>when</code> milliseconds.
     * @param context
     * @param when delay from now to launch alarm for sync
     * @param fullSync sets request to perform all stages of sync in this way <code>doPerformFullSync = currentFullSyncFlag || fullSync</code>
     */
    public static void requestSyncAlarm(Context context, long when, boolean fullSync) {

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // RTC_WAKEUP means:
        // Alarm time in System.currentTimeMillis() (wall clock time in UTC), 
        // which will wake up the device when it goes off.
        int type = AlarmManager.RTC_WAKEUP;

        PendingIntent operation = getPendingOperation(context);

        am.set(type, when, operation);

    }

    public static boolean isAlarmActive(Context context){
        int requestCode = 0; // the doc says it is not used
        Intent intent = new Intent(context, SyncAlarmReceiver.class);
        int flags = PendingIntent.FLAG_NO_CREATE;
        return PendingIntent.getBroadcast(context, requestCode, intent, flags)!=null;
    }

    private static PendingIntent getPendingOperation(Context context) {
        int requestCode = 0; // the doc says it is not used
        Intent intent = new Intent(context, SyncAlarmReceiver.class);
        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        return PendingIntent.getBroadcast(context, requestCode, intent, flags);
    }
}
