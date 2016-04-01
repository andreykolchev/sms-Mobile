package com.sms.util;

/*
 * sms
 * Created by A.Kolchev  26.2.2016
 */

import android.database.Cursor;
import android.os.Environment;

import java.io.Closeable;


public class IOUtil {


    public static void safelyClose(Closeable c) {
        if (c != null) {
            if (c instanceof Cursor){
                try {
                    ((Cursor) c).close();
                }catch (Exception ignored){

                }
            } else {
                try {
                    c.close();
                } catch (Exception ignored) {
                }
            }
        }
    }

    /**
     * Checks if external storage available
     * @return
     */

    public static boolean isExternalStorageAvailable(){
        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWriteable = false;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            // Something else is wrong. It may be one of many other states, but all we need
            // to know is we can neither read nor write
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }
        return mExternalStorageAvailable && mExternalStorageWriteable;
    }
}
