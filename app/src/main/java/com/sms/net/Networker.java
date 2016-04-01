package com.sms.net;

/*
 * sms
 * Created by A.Kolchev  26.2.2016
 */

import android.content.Context;

import com.sms.data.model.Presenter;
import com.sms.data.model.Route;
import java.util.List;

public interface Networker {

    /**
     * Process login using given username, password and gcmId
     * @param context Context
     * @return token
     */
    NetworkResult<String> doLogin(Context context, String username, String password);

    /**
     * Process get update
     * @param context Context
     * @return Update data
     */
    NetworkResult <List<Route>> getUpdateRouts(Context context);

    /**
     * Process get update
     * @param context Context
     * @return Update data
     */
    NetworkResult <List<Presenter>> getUpdatePresenter(Context context);

//    /**
//     * Process post update
//     * @param context Context
//     * @return Update data
//     */
//    NetworkResult<UpdateDataDto> doPostUpdate(Context context, UpdateDataDto updates);
//
//    /**
//     * Upload photo to server
//     * @param context Context
//     * @return Update data
//     */
//    NetworkResult<String> doUploadPhoto(Context context, Control control);
//
//    /**
//     * Upload current location to server
//     * @param context Context
//     */
//    NetworkResult<String> doUploadCurrentLocation(Context context, LocationEntity locationEntity);
//
//    /**
//     * Upload calls to server
//     * @param context Context
//     */
//    NetworkResult<String> doUploadCalls(Context context, List<CallEntity> callEntities);
//
//    /**
//     * Upload apps to server
//     * @param context Context
//     */
//    NetworkResult<String> doUploadApps(Context context, List<AppEntity> appEntities);

}
