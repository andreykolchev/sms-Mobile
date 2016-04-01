package com.sms.api;


/*
 * sms
 * Created by A.Kolchev  29.2.2016
 */

public class ApiException extends Exception {
    public ApiException() {
    }

    public ApiException(String detailMessage) {
        super(detailMessage);
    }

    public ApiException(String detailMessage, Throwable throwable) {
        super(String.format("%s. Cause: %s", detailMessage, throwable.getMessage()), throwable);
    }

    public ApiException(Throwable throwable) {
        super(throwable);
    }
}
