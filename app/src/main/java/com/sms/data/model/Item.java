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

public class Item extends ChangeableEntity<Item>{

    public static final String TABLE_NAME="item";
    public static final Uri CONTENT_URI = getContentUri(TABLE_NAME);

    public static final String COL_DESC = "description";
    public static final String COL_CATEGORY_ID = "categoryId";

    @SerializedName(value = "description")
    private String description;
    @SerializedName(value = "category")
    private Category category;

    private transient Long categoryId;

    public Item() {
        super(TABLE_NAME);
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }


    @Override
    public void save() throws ModelException {
        super.save();

        if (category!=null) {
            category.save();
            this.setCategoryId(category.getId());
        }

        ContentValues values = getContentValues();
        if (id>0){
            long result =  getDBHelper().update(TABLE_NAME, values, id);
            if (result<=0){
                throw new ModelException(String.format("Can't update item %d", id));
            }
        } else {
            long result = getDBHelper().insert(TABLE_NAME, values);
            if (result>0){
                id = result;
            } else {
                throw new ModelException("Can't insert item");
            }
        }
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = super.getContentValues();
        values.put(COL_DESC, description);
        values.put(COL_CATEGORY_ID, categoryId);
        return values;
    }

    @Override
    public Item load(Cursor cursor) {
        super.load(cursor);
        this.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COL_DESC)));
        this.setCategoryId(cursor.getLong(cursor.getColumnIndexOrThrow(COL_CATEGORY_ID)));
        this.setCategory(App.getInstance().getDBHelper().getCategoryByID(this.categoryId));
        return this;
    }

}
