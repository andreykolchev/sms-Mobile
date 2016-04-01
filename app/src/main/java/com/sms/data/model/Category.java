package com.sms.data.model;

/*
 * sms
 * Created by A.Kolchev  24.2.2016
 */

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.google.gson.annotations.SerializedName;
import com.sms.App;
import com.sms.data.exception.ModelException;

public class Category extends ChangeableEntity<Category> {

    public static final String TABLE_NAME = "category";
    public static final Uri CONTENT_URI = getContentUri(TABLE_NAME);

    public static final String COL_NAME = "name";
    public static final String COL_PARENT_ID = "parentId";

    @SerializedName(value = "name")
    private String name;
    @SerializedName(value = "parent")
    private Category parent;

    private transient Long parentId;

    public Category() {
        super(TABLE_NAME);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @Override
    public void save() throws ModelException {
        super.save();

        if (parent!=null) {
            parent.save();
            this.setParentId(parent.getId());
        }

        ContentValues values = getContentValues();
        if (id>0){
            long result =  getDBHelper().update(TABLE_NAME, values, id);
            if (result<=0){
                throw new ModelException(String.format("Can't update category %d", id));
            }
        } else {
            long result = getDBHelper().insert(TABLE_NAME, values);
            if (result>0){
                id = result;
            } else {
                throw new ModelException("Can't insert category");
            }
        }
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = super.getContentValues();
        values.put(COL_NAME, name);
        values.put(COL_PARENT_ID, parentId);
        return values;
    }

    @Override
    public Category load(Cursor cursor) {
        super.load(cursor);
        this.setName(cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)));
        this.setParentId(cursor.getLong(cursor.getColumnIndexOrThrow(COL_PARENT_ID)));
        this.setParent(App.getInstance().getDBHelper().getCategoryByID(this.parentId));
        return this;
    }
}
