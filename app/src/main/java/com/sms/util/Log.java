package com.sms.util;

/*
 * sms
 * Created by A.Kolchev  24.2.2016
 */

import com.sms.BuildConfig;
import com.sms.Constants;


public class Log {
    public static final boolean FORCE_LOGGING = Constants.FORCE_LOGGING;
    public static void i(String tag, String msg) {
            if (msg == null) {
                android.util.Log.i(tag, "NULL");
            } else {
                android.util.Log.i(tag, msg);
            }
    }

    @SuppressWarnings("unused")
    public static void d(String tag, String msg) {
        if (BuildConfig.DEBUG || FORCE_LOGGING) {
            if (msg == null) {
                android.util.Log.d(tag, "NULL");
            } else {
                android.util.Log.d(tag, msg);
            }
        }
    }

    public static void w(String tag, String msg) {
        if (msg == null) {
            android.util.Log.w(tag, "NULL");
        } else {
            android.util.Log.w(tag, msg);
        }

    }

    /**
     * Log message and Save ARCA report
     * @param tag
     * @param msg
     */
    public static void wa(String tag, String msg) {
        if (msg == null) {
            android.util.Log.w(tag, "NULL");
        } else {
            android.util.Log.w(tag, msg);
        }

    }

    public static void e(String tag, String msg, Throwable t) {
        if (msg == null) {
            android.util.Log.e(tag, "NULL", t);
        } else {
            android.util.Log.e(tag, msg, t);
        }

    }
}
