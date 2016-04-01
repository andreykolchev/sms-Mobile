package com.sms.data.model;


/*
 * sms
 * Created by A.Kolchev  26.2.2016
 */

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.google.gson.annotations.SerializedName;
import com.sms.App;
import com.sms.data.DatabaseHelper;
import com.sms.data.exception.ModelException;
import com.sms.util.Log;

public abstract class BaseEntity<T> {

    private static final String TAG = BaseEntity.class.getSimpleName();
    protected final static String AUTHORITY = "com.sms.data";

    protected transient String tableName;

    public static final String COL_ID = BaseColumns._ID;
    public static final String COL_SERVER_ID = "server_id";


    @SerializedName(value = "local_id")
    protected transient long id;
    @SerializedName(value = "id")
    protected long serverId;

    public BaseEntity(String tableName) {
        this.tableName = tableName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getServerId() {
        return serverId;
    }

    public void setServerId(long serverId) {
        this.serverId = serverId;
    }

    protected static DatabaseHelper getDBHelper() {
        return App.getInstance().getDBHelper();
    }

    public T load(Cursor cursor){
        this.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COL_ID)));
        this.setServerId(cursor.getLong(cursor.getColumnIndexOrThrow(COL_SERVER_ID)));
        return (T) this;
    }

    public void save() throws ModelException {
        if (serverId>0){
            //look local entity with given server id
            long idLocal = getDBHelper().getLocalID(tableName, serverId);
            if (idLocal>0){
                id = idLocal;
            } else if (id>0){ // got id from server. Check if local serverId = received serverId
                long idServer = getDBHelper().getServerID(tableName, id);
                if (idServer>0 && idServer!=this.serverId){
                    id = 0;
                }
            }
        }
    }

    public void notifyObserverById() {
        Uri notifyUri = Uri.withAppendedPath(getContentUri(tableName), String.valueOf(id));
        Log.d(TAG, "Notify by uri: " + notifyUri.toString());
        App.getInstance().getContentResolver().notifyChange(notifyUri, null);
    }

    public static void notifyObserver(String tableName) {
        Uri notifyUri = getContentUri(tableName);
        Log.d(TAG, "Notify by uri: " + notifyUri.toString());
        App.getInstance().getContentResolver().notifyChange(notifyUri, null);
    }

    protected static Uri getContentUri(String tableName) {
        return Uri.parse("content://" + AUTHORITY
                + "/" + tableName);
    }

    public ContentValues getContentValues(){
        ContentValues values = new ContentValues();
        values.put(COL_SERVER_ID, serverId);
        return values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseEntity<?> that = (BaseEntity<?>) o;

        if (id != that.id) return false;
        return serverId == that.serverId;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (serverId ^ (serverId >>> 32));
        return result;
    }
}
