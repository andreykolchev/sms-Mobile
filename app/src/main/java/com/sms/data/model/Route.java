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
import com.sms.util.TimeUtil;

import java.util.Date;

public class Route extends ChangeableEntity<Route> {

    public static final String TABLE_NAME="route";
    public static final Uri CONTENT_URI = getContentUri(TABLE_NAME);

    public static final String COL_DATE = "route_date";
    public static final String COL_PRIORITY = "priority";
    public static final String COL_DATE_TIME = "route_date_time";
    public static final String COL_TRADEPOINT_ID = "tradePointId";
    public static final String COL_DONE = "done";

    @SerializedName(value = "date")
    private Date date;
    @SerializedName(value = "priority")
    private Integer priority;
    @SerializedName(value = "dateTime")
    private Date dateTime;
    @SerializedName(value = "tradePoint")
    private TradePoint tradePoint;

    private transient boolean isDone;
    private transient Long tradePointId;

    public Route() {
        super(TABLE_NAME);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public Long getTradePointId() {
        return tradePointId;
    }

    public void setTradePointId(Long tradePointId) {
        this.tradePointId = tradePointId;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }

    public String getTime(){
        return TimeUtil.format(TimeUtil.createTimeFormatter(), dateTime);
    }

    public TradePoint getTradePoint() {
        return tradePoint;
    }

    public void setTradePoint(TradePoint tradePoint) {
        this.tradePoint = tradePoint;
    }

    @Override
    public void save() throws ModelException {
        super.save();

        if (tradePoint!=null) {
            tradePoint.save();
            this.setTradePointId(tradePoint.getId());
        }

        ContentValues values = getContentValues();
        if (id>0){
            long result =  getDBHelper().update(TABLE_NAME, values, id);
            if (result<=0){
                throw new ModelException(String.format("Can't update route %d", id));
            }
        } else {
            long result = getDBHelper().insert(TABLE_NAME, values);
            if (result>0){
                id = result;
            } else {
                throw new ModelException("Can't insert route");
            }
        }
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = super.getContentValues();
        values.put(COL_DATE, date!=null?date.getTime():null);
        values.put(COL_PRIORITY, priority);
        values.put(COL_DATE_TIME, dateTime!=null?dateTime.getTime():null);
        values.put(COL_TRADEPOINT_ID, tradePointId);
        values.put(COL_DONE, (isDone ? 1 : 0));
        return values;
    }

    @Override
    public Route load(Cursor cursor) {
        super.load(cursor);
        long date = cursor.getLong(cursor.getColumnIndexOrThrow(COL_DATE));
        this.setDate(date > 0 ? new Date(date) : null);
        this.setPriority(cursor.getInt(cursor.getColumnIndexOrThrow(COL_PRIORITY)));
        long dateTime = cursor.getLong(cursor.getColumnIndexOrThrow(COL_DATE_TIME));
        this.setDateTime(dateTime > 0 ? new Date(dateTime) : null);
        this.setIsDone(cursor.getInt(cursor.getColumnIndexOrThrow(COL_DONE)) == 1);
        this.setTradePointId(cursor.getLong(cursor.getColumnIndexOrThrow(COL_TRADEPOINT_ID)));
        this.setTradePoint(App.getInstance().getDBHelper().getTradePointByID(this.tradePointId));
        return this;
    }

}

