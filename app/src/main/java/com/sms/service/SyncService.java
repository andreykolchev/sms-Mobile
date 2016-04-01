package com.sms.service;


/*
 * sms
 * Created by A.Kolchev  29.2.2016
 */

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import com.sms.App;
import com.sms.R;
import com.sms.api.ApiException;
import com.sms.api.ApiManager;
import com.sms.data.model.Route;
import com.sms.data.model.dto.UpdateRouteDto;
import com.sms.net.NetworkResult;
import com.sms.util.Log;
import com.sms.util.NetworkUtil;
import com.sms.util.SyncConstants;
import com.sms.util.TimeUtil;

import java.util.List;


public class SyncService extends IntentService {

    private static final String TAG = SyncService.class.getSimpleName();


    private static PowerManager.WakeLock wakeLock = null;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder notificationBuilder;
    private ProgressInfo mProgressInfo;
    public SyncService() {
        super("SyncService");
    }

    public synchronized static PowerManager.WakeLock getWakeLock(Context context) {
        if (wakeLock == null) {
            PowerManager mgr = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            wakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
            wakeLock.setReferenceCounted(true);
        }
        return wakeLock;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(getText(R.string.app_name))
                .setSmallIcon(R.drawable.ic_notify_sync)
                .setOngoing(true);
    }

    @Override
    public void onDestroy() {
        notificationManager.cancel(SyncConstants.ONGOING_SYNC_NOTIFICATION_ID);
        super.onDestroy();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try {
            if (!NetworkUtil.isAnyNetworkConnected(this)) {
                Log.d(TAG, "onHandleIntent: no network state detected");
                // if there is no network connection - just reschedule (in finally section)
                return;
            }
            // make sure we use WiFi (if it's available) since we might come here
            // from a state where device was asleep so WiFi has become suspended
            WifiManager wifim = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            WifiManager.WifiLock wifiLock = wifim.createWifiLock(WifiManager.WIFI_MODE_FULL, TAG);
            wifiLock.acquire();
            try {
                doPayload();
            } catch (Exception e){
                Log.e(TAG, "Sync throws an exception.", e);
            }
            finally {
                wifiLock.release();
            }
        }finally {
            if (NetworkUtil.isAnyNetworkConnected(this)) {
                Log.d(TAG, "onHandleIntent: rescheduling sync alarm..");
                rescheduleSyncAlarm();
            } else {
                Log.d(TAG, "onHandleIntent: network is NOT present, going to enable ConnectivityChangesReceiver..");
                ConnectivityChangesReceiver.enable(this);
            }
            // once the job is done we should release the lock
            // that we acquired in SyncAlarmReceiver.onReceive()
            getWakeLock(this).release();
        }
    }

    private void doPayload() throws Exception {
        Log.d(TAG, "Start Payload");
        mProgressInfo = new ProgressInfo(3, 0, false);
        notifyProgress(0);
        // post update
        //postUpdate();
        // get update
        notifyProgress(1);
        //getUpdate();
        notifyProgress(1);
        Log.d(TAG, "End Payload");
    }

//    private void getUpdate(){
//        Log.d(TAG, "Starting getUpdateRouts");
//        NetworkResult<List<Route>> resultGetUpdate = App.getInstance().getNetworker().getUpdateRouts(this);
//        if (resultGetUpdate.isSuccess()){
//            List<Route> result = resultGetUpdate.result;
//            Log.d(TAG, "getUpdateRouts executed succesfully.");
//            try{
//                ApiManager.processGetUpdateRouts(result);
//            } catch (ApiException e){
//                Log.e(TAG, "Can't process getUpdateRouts response", e);
//            }
//        } else {
//            Log.d(TAG, "getUpdateRouts failed. Result: " + resultGetUpdate.toString());
//        }
//    }

//    private void postUpdate(){
//        Log.d(TAG, "Starting postUpdate");
//        List<Assignment> assignments = App.getInstance().getDBHelper().loadChanges();
//        Log.d(TAG, "Got changed assignments " + assignments.size());
//        if (!assignments.isEmpty()){
//            UpdateDataDto postData = new UpdateDataDto();
//            postData.setAssignments(assignments);
//            NetworkResult<UpdateDataDto> resultPostUpdate  = App.getInstance().getNetworker().doPostUpdate(this, postData);
//            try {
//                ApiManager.processPostUpdate(resultPostUpdate.result, assignments);
//            } catch (ApiException e){
//                Log.e(TAG, "Can't process postUpdate response", e);
//            }
//
//        }
//    }

    private void notifyProgress(int progress) {
        Notification notification = getNotification(getString(R.string.ongoing_sync_notification), mProgressInfo.incProgress(progress));
        notificationManager.notify(SyncConstants.ONGOING_SYNC_NOTIFICATION_ID, notification);
    }

    private Notification getNotification(String progressText, ProgressInfo progressInfo) {
        notificationBuilder.setContentText(progressText);

        if (progressInfo == null) {
            notificationBuilder.setProgress(0, 0, false); // removes the progress bar
        } else {
            ProgressInfo info = progressInfo;
            notificationBuilder.setProgress(info.max, info.progress, info.indeterminate);
        }
        return notificationBuilder.build();
    }

    private void rescheduleSyncAlarm() {
        long when = getNextSyncAlarmTime();
        SyncAlarm.requestSyncAlarm(App.getInstance(), when, false);
    }

    private long getNextSyncAlarmTime() {
        return TimeUtil.getTimeWithDelay(SyncConstants.DEFAULT_SYNC_DELAY);
    }
}
