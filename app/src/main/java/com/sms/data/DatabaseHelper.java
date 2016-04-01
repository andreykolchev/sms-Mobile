package com.sms.data;



/*
 * sms
 * Created by A.Kolchev  26.2.2016
 */

import android.content.ContentValues;
import android.content.Context;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteException;
import android.database.Cursor;

//import net.sqlcipher.database.SQLiteDatabase;
//import net.sqlcipher.database.SQLiteOpenHelper;
//import net.sqlcipher.database.SQLiteException;

import com.sms.data.model.BaseEntity;
import com.sms.data.model.Category;
import com.sms.data.model.ChangeableEntity;
import com.sms.data.model.Item;
import com.sms.data.model.Presenter;
import com.sms.data.model.Route;
import com.sms.data.model.TradePoint;
import com.sms.util.IOUtil;
import com.sms.util.Log;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private final String TAG = DatabaseHelper.class.getSimpleName();
    public final static String DB_NAME = "sms.db";

    public final static int VERSION = 19;
    private final static String DROP_TABLE = "DROP TABLE IF EXISTS ";

    private final String PASS = "1234";

    /**
     * Do not call this directly. Whenever you need an instance, please use <code>App.getDBHelper()</code>.
     *
     * @param context Context
     */
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
        //SQLiteDatabase.loadLibs(context);
    }

//    SQLiteDatabase getReadableDatabase() {
//        return(super.getReadableDatabase(PASS));
//    }
//
//    SQLiteDatabase getWritableDatabase() {
//        return(super.getWritableDatabase(PASS));
//    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DROP_TABLE + TradePoint.TABLE_NAME);
        db.execSQL("CREATE TABLE " + TradePoint.TABLE_NAME + " ("
                + TradePoint.COL_ID + " " + DBFieldType.PRIMARY_KEY + ", "
                + TradePoint.COL_SERVER_ID + " " + DBFieldType.INTEGER + ", "
                + TradePoint.COL_LAST_CHANGE + " " + DBFieldType.INTEGER + ", "
                + TradePoint.COL_DESC + " " + DBFieldType.TEXT + ", "
                + TradePoint.COL_CONTACT + " " + DBFieldType.TEXT + ", "
                + TradePoint.COL_PHONE + " " + DBFieldType.TEXT + ", "
                + TradePoint.COL_REGION + " " + DBFieldType.TEXT + ", "
                + TradePoint.COL_ADDRESS + " " + DBFieldType.TEXT + ", "
                + TradePoint.COL_LATITUDE + " " + DBFieldType.INTEGER + ", "
                + TradePoint.COL_LONGITUDE + " " + DBFieldType.INTEGER + ", "
                + ChangeableEntity.COL_IS_CHANGED + " " + DBFieldType.INTEGER + " DEFAULT 0"
                + ")");
        db.execSQL(DROP_TABLE + Route.TABLE_NAME);
        db.execSQL("CREATE TABLE " + Route.TABLE_NAME + " ("
                + Route.COL_ID + " " + DBFieldType.PRIMARY_KEY + ", "
                + Route.COL_SERVER_ID + " " + DBFieldType.INTEGER + ", "
                + Route.COL_LAST_CHANGE + " " + DBFieldType.INTEGER + ", "
                + Route.COL_DATE + " " + DBFieldType.INTEGER + ", "
                + Route.COL_PRIORITY + " " + DBFieldType.INTEGER + ", "
                + Route.COL_DATE_TIME + " " + DBFieldType.INTEGER + ", "
                + Route.COL_DONE + " " + DBFieldType.INTEGER + ", "
                + ChangeableEntity.COL_IS_CHANGED + " " + DBFieldType.INTEGER + " DEFAULT 0, "
                + Route.COL_TRADEPOINT_ID + " " + DBFieldType.INTEGER + ", "
                + " FOREIGN KEY (" + Route.COL_TRADEPOINT_ID + ") REFERENCES " + TradePoint.TABLE_NAME + " (" + TradePoint.COL_ID + ")"
                + ")");
        db.execSQL(DROP_TABLE + Category.TABLE_NAME);
        db.execSQL("CREATE TABLE " + Category.TABLE_NAME + " ("
                + Category.COL_ID + " " + DBFieldType.PRIMARY_KEY + ", "
                + Category.COL_SERVER_ID + " " + DBFieldType.INTEGER + ", "
                + Category.COL_LAST_CHANGE + " " + DBFieldType.INTEGER + ", "
                + Category.COL_NAME + " " + DBFieldType.TEXT + ", "
                + Category.COL_PARENT_ID + " " + DBFieldType.INTEGER + ", "
                + ChangeableEntity.COL_IS_CHANGED + " " + DBFieldType.INTEGER + " DEFAULT 0, "
                + " FOREIGN KEY (" + Category.COL_PARENT_ID + ") REFERENCES " + Category.TABLE_NAME + " (" + Category.COL_ID + ")"
                + ")");
        db.execSQL(DROP_TABLE + Item.TABLE_NAME);
        db.execSQL("CREATE TABLE " + Item.TABLE_NAME + " ("
                + Item.COL_ID + " " + DBFieldType.PRIMARY_KEY + ", "
                + Item.COL_SERVER_ID + " " + DBFieldType.INTEGER + ", "
                + Item.COL_LAST_CHANGE + " " + DBFieldType.INTEGER + ", "
                + Item.COL_DESC + " " + DBFieldType.TEXT + ", "
                + Item.COL_CATEGORY_ID + " " + DBFieldType.INTEGER + ", "
                + ChangeableEntity.COL_IS_CHANGED + " " + DBFieldType.INTEGER + " DEFAULT 0, "
                + " FOREIGN KEY (" + Item.COL_CATEGORY_ID + ") REFERENCES " + Category.TABLE_NAME + " (" + Category.COL_ID + ")"
                + ")");
        db.execSQL(DROP_TABLE + Presenter.TABLE_NAME);
        db.execSQL("CREATE TABLE " + Presenter.TABLE_NAME + " ("
                + Presenter.COL_ID + " " + DBFieldType.PRIMARY_KEY + ", "
                + Presenter.COL_SERVER_ID + " " + DBFieldType.INTEGER + ", "
                + Presenter.COL_LAST_CHANGE + " " + DBFieldType.INTEGER + ", "
                + Presenter.COL_PRIORITY + " " + DBFieldType.INTEGER + ", "
                + Presenter.COL_URI + " " + DBFieldType.TEXT + ", "
                + Presenter.COL_ITEM_ID + " " + DBFieldType.INTEGER + ", "
                + ChangeableEntity.COL_IS_CHANGED + " " + DBFieldType.INTEGER + " DEFAULT 0, "
                + " FOREIGN KEY (" + Presenter.COL_ITEM_ID + ") REFERENCES " + Item.TABLE_NAME + " (" + Item.COL_ID + ")"
                + ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG,
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        onCreate(db);

    }

    /**
     * Insert a record to DB
     *
     * @param tableName
     * @param values
     * @return
     */
    public synchronized long insert(String tableName, ContentValues values) {
        long id = 0;
        try {
            SQLiteDatabase db = getWritableDatabase();
            id = db.insert(tableName, null, values);
            Log.d(TAG, String.format("Record with ID=%d has been inserted to %s", id, tableName));
        } catch (SQLiteException e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
        }

        return id;
    }

    /**
     * Update a record
     *
     * @param tableName
     * @param values
     * @param id
     * @return
     */
    public synchronized int update(String tableName, ContentValues values, long id) {
        int count = 0;
        try {
            SQLiteDatabase db = getWritableDatabase();
            count = db.update(tableName, values, BaseEntity.COL_ID + "=?", new String[]{String.valueOf(id)});
            Log.d(TAG, String.format("%d records have been updated in %s", (long) count, tableName));
        } catch (SQLiteException e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
        }
        return count;
    }

    /**
     * Delete a record
     *
     * @param tableName
     * @param id
     * @return
     */
    public synchronized int delete(String tableName, long id) {
        int count = 0;
        try {
            SQLiteDatabase db = getWritableDatabase();
            count = db.delete(tableName, BaseEntity.COL_ID + "=?", new String[]{String.valueOf(id)});
            Log.d(TAG, String.format("%d records have been deleted from %s", (long) count, tableName));

        } catch (SQLiteException e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
        }

        return count;
    }

    /**
     * get local ID of Entity
     *
     * @param tableName
     * @param serverId
     * @return
     */
    public long getLocalID(String tableName, long serverId) {
        long id = 0;

        Cursor cursor = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            cursor = db.query(tableName,
                    new String[]{
                            BaseEntity.COL_ID},
                    BaseEntity.COL_SERVER_ID + "=?",
                    new String[]{String.valueOf(serverId)},
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                id = cursor.getLong(0);
            }

        } catch (SQLiteException e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
        } finally {
            IOUtil.safelyClose(cursor);
        }

        return id;
    }

    /**
     * Get ServerID from the table
     *
     * @param table
     * @param id
     * @return
     */
    public long getServerID(String table, long id) {
        long idServer = 0;

        Cursor cursor = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            cursor = db.query(table,
                    new String[]{
                            BaseEntity.COL_SERVER_ID},
                    BaseEntity.COL_ID + "=?",
                    new String[]{String.valueOf(id)},
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                idServer = cursor.getLong(0);
            }

        } catch (SQLiteException e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
        } finally {
            IOUtil.safelyClose(cursor);
        }

        return idServer;
    }

    public Cursor getEntity(String tableName, long id) {
        if (id > 0) {
            Cursor cursor = null;
            try {
                String selection = BaseEntity.COL_ID + "=?";
                String[] selectionArgs = new String[]{String.valueOf(id)};
                SQLiteDatabase db = getReadableDatabase();
                cursor = db.query(tableName,
                        new String[]{"*"},
                        selection,
                        selectionArgs,
                        null, null, null);
                return cursor;
            } catch (SQLiteException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
            }
        }
        return null;
    }

    public List<Route> getRouts() {

        Cursor cursor = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            cursor = db.query(Route.TABLE_NAME,
                    new String[]{"*"},
                    null,
                    null,
                    null, null, BaseEntity.COL_ID);
            if (cursor != null && cursor.moveToFirst()) {
                List<Route> result = new ArrayList<>(cursor.getCount());
                do {
                    result.add(new Route().load(cursor));
                } while (cursor.moveToNext());
                return result;
            }
        } catch (SQLiteException e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
        } finally {
            IOUtil.safelyClose(cursor);
        }
        return Collections.emptyList();
    }

    public Route getRoutByID(long id) {

        if (id > 0) {
            Cursor cursor = getEntity(Route.TABLE_NAME, id);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    return new Route().load(cursor);
                }
            } catch (SQLiteException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
            } finally {
                IOUtil.safelyClose(cursor);
            }
        }
        return null;
    }

    public TradePoint getTradePointByID(long id) {
        if (id > 0) {
            Cursor cursor = getEntity(TradePoint.TABLE_NAME, id);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    return new TradePoint().load(cursor);
                }
            } catch (SQLiteException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
            } finally {
                IOUtil.safelyClose(cursor);
            }
        }
        return null;
    }

    public List<Presenter> getPresenterItems() {

        Cursor cursor = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            cursor = db.query(Presenter.TABLE_NAME,
                    new String[]{"*"},
                    null,
                    null,
                    null, null, BaseEntity.COL_ID);
            if (cursor != null && cursor.moveToFirst()) {
                List<Presenter> result = new ArrayList<>(cursor.getCount());
                do {
                    result.add(new Presenter().load(cursor));
                } while (cursor.moveToNext());
                return result;
            }
        } catch (SQLiteException e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
        } finally {
            IOUtil.safelyClose(cursor);
        }
        return Collections.emptyList();
    }

    public Cursor getPresenterItemsCursor() {

        Cursor cursor = null;

        try {
            SQLiteDatabase db = getReadableDatabase();
            cursor = db.query(Presenter.TABLE_NAME,
                    new String[]{"*"},
                    null,
                    null,
                    null, null, BaseEntity.COL_ID);
            return cursor;
        } catch (SQLiteException e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
        }
        return null;
    }

    public List<Presenter> getPresenterItemsBuFilter(String param) {

        Cursor cursor = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            String sql = "select * from " + Presenter.TABLE_NAME +
            " where " + Presenter.COL_ITEM_ID +
                    " in ( select " + Item.COL_ID +
                         " from " + Item.TABLE_NAME + " where " + Item.COL_DESC + " LIKE '" + param + "' or "+ Item.COL_CATEGORY_ID +
                    " in (select " + Category.COL_ID + " from " + Category.TABLE_NAME + " where " + Category.COL_NAME + " LIKE '" + param + "'))";
            cursor = db.rawQuery(sql, null);

            if (cursor != null && cursor.moveToFirst()) {
                List<Presenter> result = new ArrayList<>(cursor.getCount());
                do {
                    result.add(new Presenter().load(cursor));
                } while (cursor.moveToNext());
                return result;
            }
        } catch (SQLiteException e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
        } finally {
            IOUtil.safelyClose(cursor);
        }
        return Collections.emptyList();
    }

    public Presenter getPresenterItemByID(long id) {

        if (id > 0) {
            Cursor cursor = getEntity(Presenter.TABLE_NAME, id);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    return new Presenter().load(cursor);
                }
            } catch (SQLiteException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
            } finally {
                IOUtil.safelyClose(cursor);
            }
        }
        return null;
    }

    public Item getItemByID(long id) {

        if (id > 0) {
            Cursor cursor = getEntity(Item.TABLE_NAME, id);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    return new Item().load(cursor);
                }
            } catch (SQLiteException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
            } finally {
                IOUtil.safelyClose(cursor);
            }
        }
        return null;
    }

    public Category getCategoryByID(long id) {

        if (id > 0) {
            Cursor cursor = getEntity(Category.TABLE_NAME, id);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    return new Category().load(cursor);
                }
            } catch (SQLiteException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
            } finally {
                IOUtil.safelyClose(cursor);
            }
        }
        return null;
    }

}
