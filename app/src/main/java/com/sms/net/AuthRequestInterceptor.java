package com.sms.net;

/*
 * sms
 * Created by A.Kolchev  2.3.2016
 */

public class AuthRequestInterceptor implements retrofit.RequestInterceptor {

    String basic;

    public AuthRequestInterceptor(String basic) {
        this.basic = basic;
    }

    @Override
    public void intercept(RequestFacade request) {
        request.addHeader("Authorization", basic);
        request.addHeader("Accept", "application/json");
    }

}

