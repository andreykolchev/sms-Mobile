package com.sms.data.model;

/*
 * sms
 * Created by A.Kolchev  24.2.2016
 */

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.google.gson.annotations.SerializedName;
import com.sms.data.exception.ModelException;

import java.util.Date;

public class TradePoint extends ChangeableEntity<TradePoint>{

    public static final String TABLE_NAME="trade_point";
    public static final Uri CONTENT_URI = getContentUri(TABLE_NAME);

    public static final String COL_DESC = "description";
    public static final String COL_CONTACT = "contact";
    public static final String COL_PHONE = "phoneNumber";
    public static final String COL_REGION = "region";
    public static final String COL_ADDRESS = "address";
    public static final String COL_LATITUDE = "latitude";
    public static final String COL_LONGITUDE = "longitude";

    @SerializedName(value = "description")
    private String description;
    @SerializedName(value = "contact")
    private String contact;
    @SerializedName(value = "phoneNumber")
    private String phoneNumber;
    @SerializedName(value = "region")
    private String region;
    @SerializedName(value = "address")
    private String address;
    @SerializedName(value = "latitude")
    private Double latitude;
    @SerializedName(value = "longitude")
    private Double longitude;

    public TradePoint() {
        super(TABLE_NAME);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public void save() throws ModelException {
        super.save();
        ContentValues values = getContentValues();
        if (id>0){
            long result =  getDBHelper().update(TABLE_NAME, values, id);
            if (result<=0){
                throw new ModelException(String.format("Can't update trade point %d", id));
            }
        } else {
            long result = getDBHelper().insert(TABLE_NAME, values);
            if (result>0){
                id = result;
            } else {
                throw new ModelException("Can't insert trade point");
            }
        }
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = super.getContentValues();
        values.put(COL_DESC, description);
        values.put(COL_CONTACT, contact);
        values.put(COL_PHONE, phoneNumber);
        values.put(COL_REGION, region);
        values.put(COL_ADDRESS, address);
        values.put(COL_LATITUDE, latitude);
        values.put(COL_LONGITUDE, longitude);
        return values;
    }

    @Override
    public TradePoint load(Cursor cursor) {
        super.load(cursor);
        setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COL_DESC)));
        setContact(cursor.getString(cursor.getColumnIndexOrThrow(COL_CONTACT)));
        setPhoneNumber(cursor.getString(cursor.getColumnIndexOrThrow(COL_PHONE)));
        setRegion(cursor.getString(cursor.getColumnIndexOrThrow(COL_REGION)));
        setAddress(cursor.getString(cursor.getColumnIndexOrThrow(COL_ADDRESS)));
        setLatitude(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_LATITUDE)));
        setLongitude(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_LONGITUDE)));
        return this;
    }

}
