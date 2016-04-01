package com.sms;


/*
 * sms
 * Created by A.Kolchev  24.2.2016
 */

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.sms.data.DatabaseHelper;
import com.sms.net.DefaultNetworker;
import com.sms.net.Networker;
import com.sms.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class App extends Application {
    private static final String TAG = App.class.getSimpleName();
    private static App instance;
    private long sessionTime;
    private DatabaseHelper dbHelper;
    private final Lock dbHelperLock = new ReentrantLock();
    private Networker networker;
    /**
     * Thread to execute tasks in background..
     */
    private final ExecutorService backgroundExecutor;
    private static String appFullVersion;

    public App() {
        instance = this;
        backgroundExecutor = Executors
                .newSingleThreadExecutor(new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable runnable) {
                        Thread thread = new Thread(runnable,
                                "Background executor service");
                        thread.setPriority(Thread.MIN_PRIORITY);
                        thread.setDaemon(true);
                        return thread;
                    }
                });
    }
    @Override
    public void onCreate() {
        super.onCreate();
        networker = new DefaultNetworker();
        //if (!TextUtils.isEmpty(Settings.INSTANCE.getUserId()) && !TextUtils.isEmpty(Settings.INSTANCE.getPassword())){
        //    SyncAlarm.requestSyncAlarm(this, TimeUtil.getTimeWithDelay(5), false);
        //}
        loadVersionName();
    }

    public String getVersionName(){
        return appFullVersion;
    }

    private void loadVersionName(){
        try {
            PackageInfo packageInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
//            int flags = packageInfo.applicationInfo.flags;
//            String mVersionCode = String.valueOf(packageInfo.versionCode);
//            String mVersionName = packageInfo.versionName;
            appFullVersion = packageInfo.versionName;// + " ("+packageInfo.versionCode+")";
            //isDebugMode = (flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (PackageManager.NameNotFoundException e) {
            //isDebugMode = false;
        }
    }

    public static App getInstance() {
        return instance;
    }

    public DatabaseHelper getDBHelper() {
        dbHelperLock.lock();
        try {
            if (dbHelper == null) {
                dbHelper = new DatabaseHelper(this);
            }
        } finally {
            dbHelperLock.unlock();
        }
        return dbHelper;
    }

    public Networker getNetworker() {
        return networker;
    }

    /**
     * Submits request to be executed in background.
     *
     * @param runnable
     */
    public void runInBackground(final Runnable runnable) {
        backgroundExecutor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } catch (Exception e) {
                    Log.e(TAG, "Background work exception.", e);
                }
            }
        });
    }

    public boolean isSessionLive() {
        return System.currentTimeMillis() - sessionTime <= Settings.SESSION_LIFE_TIME;

    }

    public void openSession() {
        Log.d(TAG, "Session opened");
        sessionTime = System.currentTimeMillis();
    }

    public void closeSession() {
        Log.d(TAG, "Session closed");
        sessionTime = 0;
    }

    public boolean resetSessionTime() {
        if (isSessionLive() && !TextUtils.isEmpty(Settings.INSTANCE.getUserId())) {
            sessionTime = System.currentTimeMillis();
            return true;
        }
        return false;
    }




}
