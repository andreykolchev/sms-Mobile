package com.sms.data.exception;


/*
 * sms
 * Created by A.Kolchev  26.2.2016
 */

public class NetworkException extends Exception {
    private int code;
    public NetworkException(String detailMessage) {
        super(detailMessage);
    }

    public NetworkException(String detailMessage, int code) {
        super(detailMessage);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
