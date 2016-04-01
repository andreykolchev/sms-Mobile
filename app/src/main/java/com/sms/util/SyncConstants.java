package com.sms.util;


/*
 * sms
 * Created by A.Kolchev  29.2.2016
 */

import java.util.concurrent.TimeUnit;

public class SyncConstants {

    /**
     * Time period in seconds used for scheduling an alarm for
     * SyncAlarmReceiver broadcast on device bootup.
     */
    public final static int ON_BOOT_SYNC_DELAY = 30;

    /**
     * A default time period in seconds used for scheduling an alarm
     * for SyncAlarmReceiver broadcast.
     */
    public final static int DEFAULT_SYNC_DELAY = (int) TimeUnit.MINUTES.toSeconds(5);

    /**
     * A default time period in seconds used for scheduling an alarm
     * for SyncAlarmReceiver broadcast on network restore.
     */
    public final static int NETWORK_RESTORED_SYNC_DELAY = 5;

    /**
     * A unique ID (for the application) for the notification that is showing
     * while SyncService is running.
     * 
     * <p>
     * The ID <b>must be > 0</b>, otherwise the notification is not shown (despite the API says nothing on this).
     * <p>
     */
    public static final int ONGOING_SYNC_NOTIFICATION_ID = 1;

    public static final int WRONG_NETWORK_NOTIFICATION_ID = 2;
    
    public static final int GOT_NEW_ASSIGNMENTS_NOTIFICATION_ID = 3;

    public static final int HAVE_NEW_APP_VERSION_NOTIFICATION_ID = 4;

    public static final int UPLOAD_SYNC_NOTIFICATION_ID = 5;

}
