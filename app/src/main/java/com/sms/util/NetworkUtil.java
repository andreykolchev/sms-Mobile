package com.sms.util;

/*
 * sms
 * Created by A.Kolchev  29.2.2016
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {

    public static boolean isAnyNetworkConnected(Context context) {
        NetworkInfo active = getActiveNetworkInfo(context);
        return (active != null) && active.isConnected();
    }

    public static NetworkInfo getActiveNetworkInfo(Context context) {
        return getConnectivityManager(context).getActiveNetworkInfo();
    }

    private static ConnectivityManager getConnectivityManager(Context context) {
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }



}
