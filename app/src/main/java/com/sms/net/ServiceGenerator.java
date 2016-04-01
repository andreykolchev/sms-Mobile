package com.sms.net;

/*
 * sms
 * Created by A.Kolchev  29.2.2016
 */

import android.util.Base64;

import com.google.gson.Gson;
import com.sms.Constants;
import com.sms.Settings;
import com.sms.data.exception.ContentException;
import com.sms.data.exception.NetworkException;
import com.sms.util.GsonTransformer;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

public class ServiceGenerator {

    public static <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, Settings.INSTANCE.getUserId(), Settings.INSTANCE.getPassword());
    }

    public static <S> S createService(Class<S> serviceClass, String username, String password) {

        String credentials = username + ":" + password;
        String basic = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        Gson gson = GsonTransformer.getGson();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(30, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(30, TimeUnit.SECONDS);
        okHttpClient.setWriteTimeout(30, TimeUnit.SECONDS);

        OkClient client = new OkClient(okHttpClient);

        retrofit.ErrorHandler errorHandler = new retrofit.ErrorHandler() {
            @Override
            public Throwable handleError(RetrofitError cause) {
                switch (cause.getKind()) {
                    case NETWORK:
                        return new NetworkException(cause.getLocalizedMessage());
                    case CONVERSION:
                        return new ContentException(cause.getLocalizedMessage());
                    case HTTP:
                        return new NetworkException(cause.getLocalizedMessage(), cause.getResponse().getStatus());
                    case UNEXPECTED:
                }
                return new Exception(cause.getLocalizedMessage());
            }
        };

        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(Constants.SERVER)
                .setClient(new OkClient(new OkHttpClient()))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(gson))
                .setClient(client)
                .setErrorHandler(errorHandler)
                .setRequestInterceptor(new AuthRequestInterceptor(basic));

        RestAdapter adapter = builder.build();

        return adapter.create(serviceClass);

    }

}
