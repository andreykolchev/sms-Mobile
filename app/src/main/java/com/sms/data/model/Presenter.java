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


public class Presenter extends ChangeableEntity<Presenter> {

    public static final String TABLE_NAME="presenter";
    public static final Uri CONTENT_URI = getContentUri(TABLE_NAME);

    public static final String COL_PRIORITY = "priority";
    public static final String COL_ITEM_ID = "itemId";
    public static final String COL_URI = "image_uri";


    @SerializedName(value = "item")
    private Item item;
    @SerializedName(value = "priority")
    private Integer priority;
    @SerializedName(value = "imageURI")
    private String imageURI = "";

    private transient Long itemId;

    public Presenter() {
        super(TABLE_NAME);
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getImageURI() {
        return imageURI;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    @Override
    public void save() throws ModelException {
        super.save();

        if (item!=null) {
            item.save();
            this.setItemId(item.getId());
        }

        ContentValues values = getContentValues();
        if (id>0){
            long result =  getDBHelper().update(TABLE_NAME, values, id);
            if (result<=0){
                throw new ModelException(String.format("Can't update pres %d", id));
            }
        } else {
            long result = getDBHelper().insert(TABLE_NAME, values);
            if (result>0){
                id = result;
            } else {
                throw new ModelException("Can't insert pres");
            }
        }
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = super.getContentValues();
        values.put(COL_PRIORITY, priority);
        values.put(COL_ITEM_ID, itemId);
        values.put(COL_URI, imageURI);
        return values;
    }

    @Override
    public Presenter load(Cursor cursor) {
        super.load(cursor);
        this.setPriority(cursor.getInt(cursor.getColumnIndexOrThrow(COL_PRIORITY)));
        this.setImageURI(cursor.getString(cursor.getColumnIndexOrThrow(COL_URI)));
        this.setItemId(cursor.getLong(cursor.getColumnIndexOrThrow(COL_ITEM_ID)));
        this.setItem(App.getInstance().getDBHelper().getItemByID(this.itemId));
        return this;
    }

}
