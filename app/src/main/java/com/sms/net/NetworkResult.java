package com.sms.net;

/*
 * sms
 * Created by A.Kolchev  29.2.2016
 */

public class NetworkResult<T> {

    public static final int RESULT_FILE_NOT_FOUND=-1000;

    public int code;
    public String error;
    public String message;
    public T result;


    public NetworkResult(int code, String error, String message, T result) {
        this.code = code;
        this.error = error;
        this.message = message;
        this.result = result;
    }


    public boolean isSuccess(){
        return code==200||code==201;
    }

    @Override
    public String toString() {
        return "NetworkResult{" +
                "code=" + code +
                ", error='" + error + '\'' +
                ", message='" + message + '\'' +
                ", result='" + (result==null?"is null":"not null") + '\'' +
                '}';
    }
}
