package com.sms.net;

/*
 * sms
 * Created by A.Kolchev  29.2.2016
 */

import retrofit.client.Response;
import retrofit.http.GET;

import com.sms.data.model.Presenter;
import com.sms.data.model.Route;
import java.util.List;

public interface RetrofitService {


    @GET("/login")
    Response doLogin() throws Exception;

    @GET("/routs/getRouts")
    List<Route> getUpdateRouts() throws Exception;

    @GET("/presenter/getPresenter")
    List<Presenter> getUpdatePresenter() throws Exception;


}
