package com.sms.data.exception;


/*
 * sms
 * Created by A.Kolchev  26.2.2016
 */

public class ModelException extends Exception {
    public ModelException() {
    }

    public ModelException(String detailMessage) {
        super(detailMessage);
    }

    public ModelException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ModelException(Throwable throwable) {
        super(throwable);
    }
}
