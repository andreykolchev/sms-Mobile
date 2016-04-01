package com.sms;

/*
 * sms
 * Created by A.Kolchev  24.2.2016
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import java.util.concurrent.TimeUnit;

public enum Settings {
    INSTANCE;
    public static final long SESSION_LIFE_TIME = TimeUnit.MINUTES.toMillis(10); // 10 min
    public static final String PREFS_NAME = "sms_prefs";

    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_PASSWORD_SHA = "password_md5";
    private static final String KEY_GCM_REG_ID = "gcm_reg_id";
    private static final String KEY_DEVICE_ID = "device_id";
    private static final String KEY_SAVED_APP_VERSION = "saved_app_version";
    private static final String KEY_SYNC_ID = "sync_id";

    private String userId;
    private String password;
    private long syncId;
    private String gcmRegId;
    private int savedAppVersion;
    private String deviceId;
    private final SharedPreferences prefs;

    Settings() {
        prefs = App.getInstance().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userId = prefs.getString(KEY_USER_ID, null);
        password = prefs.getString(KEY_PASSWORD_SHA, null);
        gcmRegId = prefs.getString(KEY_GCM_REG_ID, null);
        deviceId = prefs.getString(KEY_DEVICE_ID, null);
        savedAppVersion = prefs.getInt(KEY_SAVED_APP_VERSION, 0);
        syncId = prefs.getLong(KEY_SYNC_ID, 0);
    }

    public long getSyncId() {
        return syncId;
    }

    public void setSyncId(long syncId) {
        this.syncId = syncId;
        if (syncId > 0) {
            prefs.edit().putLong(KEY_SYNC_ID, syncId).apply();
        } else {
            prefs.edit().remove(KEY_SYNC_ID).apply();
        }
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
        if (!TextUtils.isEmpty(userId)) {
            prefs.edit().putString(KEY_USER_ID, userId).apply();
        } else {
            prefs.edit().remove(KEY_USER_ID).apply();
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String passwordSHA) {
        this.password = passwordSHA;
        if (!TextUtils.isEmpty(passwordSHA)) {
            prefs.edit().putString(KEY_PASSWORD_SHA, passwordSHA).apply();
        } else {
            prefs.edit().remove(KEY_PASSWORD_SHA).apply();
        }
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
        if (!TextUtils.isEmpty(deviceId)) {
            prefs.edit().putString(KEY_DEVICE_ID, deviceId).apply();
        } else {
            prefs.edit().remove(KEY_GCM_REG_ID).apply();
        }
    }

    public String getGcmRegId() {
        return gcmRegId;
    }

    public void setGcmRegId(String gcmRegId) {
        this.gcmRegId = gcmRegId;
        if (!TextUtils.isEmpty(gcmRegId)) {
            prefs.edit().putString(KEY_GCM_REG_ID, gcmRegId).apply();
        } else {
            prefs.edit().remove(KEY_GCM_REG_ID).apply();
        }

    }

    public int getSavedAppVersion() {
        return savedAppVersion;
    }

    public void setSavedAppVersion(int savedAppVersion) {
        this.savedAppVersion = savedAppVersion;
        if (savedAppVersion > 0) {
            prefs.edit().putInt(KEY_SAVED_APP_VERSION, savedAppVersion).apply();
        } else {
            prefs.edit().remove(KEY_SAVED_APP_VERSION);
        }
    }

     /**
     * @return Application's version code from the {@code PackageManager}.
     */
    public static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

}
