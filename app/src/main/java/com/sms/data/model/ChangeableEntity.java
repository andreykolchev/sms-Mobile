package com.sms.data.model;


/*
 * sms
 * Created by A.Kolchev  26.2.2016
 */

import android.content.ContentValues;
import android.database.Cursor;

import com.sms.data.exception.ModelException;


public abstract class ChangeableEntity<T> extends BaseEntity<T> {

    public static final String COL_LAST_CHANGE= "last_change";
    public static final String COL_IS_CHANGED= "is_changed";

    protected Long lastChange;
    protected boolean changed=false;


    public ChangeableEntity(String tableName) {
        super(tableName);
    }

    public Long getLastChange() {
        return lastChange;
    }


    public void setLastChange(Long lastChange) {
        this.lastChange = lastChange;
    }

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
        if (changed){
            setLastChange(System.currentTimeMillis());
        }
    }

    @Override
    public void save() throws ModelException {
        super.save();
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = super.getContentValues();
        values.put(COL_LAST_CHANGE, lastChange);
        values.put(COL_IS_CHANGED, isChanged());
        return values;
    }

    @Override
    public T load(Cursor cursor) {
        super.load(cursor);
        this.lastChange = cursor.getLong(cursor.getColumnIndexOrThrow(COL_LAST_CHANGE));
        this.changed = cursor.getInt(cursor.getColumnIndexOrThrow(COL_IS_CHANGED))==1;
        return (T) this;
    }
}
