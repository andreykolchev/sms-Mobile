package com.sms.net;

/*
 * sms
 * Created by A.Kolchev  26.2.2016
 */

import android.content.Context;

import com.sms.data.exception.ContentException;
import com.sms.data.exception.NetworkException;
import com.sms.data.model.Presenter;
import com.sms.data.model.Route;
import com.sms.util.Log;

import java.util.List;

import retrofit.client.Response;

public class DefaultNetworker implements Networker {

    private static final String TAG = DefaultNetworker.class.getSimpleName();

    @Override
    public NetworkResult<String> doLogin(Context context, String username, String password) {
        if (context == null) {
            throw new IllegalArgumentException("Context can not be null");
        }
        Log.d(TAG, "doLogin started...");
        NetworkResult<String> result = null;
        try {
            RetrofitService mService = ServiceGenerator.createService(RetrofitService.class, username, password);
            Response response = mService.doLogin();
            if (response != null) {
                Log.d(TAG, "doLogin result: " + response.getReason());
                result = new NetworkResult<>(response.getStatus(), null, null, response.getReason());
            }
        } catch (NetworkException e) {
            Log.e(TAG, "Network error", e);
            result = new NetworkResult<>(-101, e.getMessage(), null, null);
        } catch (ContentException e) {
            Log.e(TAG, "Content error", e);
            result = new NetworkResult<>(0, e.getMessage(), e.getMessage(), null);
        } catch (Exception e) {
            Log.e(TAG, "Unknown error", e);
            result = new NetworkResult<>(0, e.getMessage(), e.getMessage(), null);
        }
        Log.d(TAG, "doLogin result: " + result);
        return result;
    }

    @Override
    public NetworkResult<List<Route>> getUpdateRouts(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context can not be null");
        }
        Log.d(TAG, "getUpdateRouts start...");
        NetworkResult<List<Route>> result = null;
        List<Route> response;
        try {
            RetrofitService mService = ServiceGenerator.createService(RetrofitService.class);
            response = mService.getUpdateRouts();
            if (response != null) {
                Log.d(TAG, "getUpdateRouts success. syncId: " + response);
            }
            result = new NetworkResult<>(200, null, null, response);
        } catch (NetworkException e) {
            Log.e(TAG, "Network error", e);
            result = new NetworkResult<>(-101, e.getMessage(), null, null);
        } catch (ContentException e) {
            Log.e(TAG, "Content error", e);
            result = new NetworkResult<>(0, e.getMessage(), e.getMessage(), null);
        } catch (Exception e) {
            Log.e(TAG, "Unknown error", e);
            result = new NetworkResult<>(0, e.getMessage(), e.getMessage(), null);
        }
        Log.d(TAG, "getUpdateRouts result: " + result);
        return result;
    }

    @Override
    public NetworkResult <List<Presenter>> getUpdatePresenter(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context can not be null");
        }
        Log.d(TAG, "getUpdatePresenter start...");
        NetworkResult<List<Presenter>> result = null;
        List<Presenter> response;
        try {
            RetrofitService mService = ServiceGenerator.createService(RetrofitService.class);
            response = mService.getUpdatePresenter();
            if (response != null) {
                Log.d(TAG, "getUpdatePresenter success. syncId: " + response);
            }
            result = new NetworkResult<>(200, null, null, response);
        } catch (NetworkException e) {
            Log.e(TAG, "Network error", e);
            result = new NetworkResult<>(-101, e.getMessage(), null, null);
        } catch (ContentException e) {
            Log.e(TAG, "Content error", e);
            result = new NetworkResult<>(0, e.getMessage(), e.getMessage(), null);
        } catch (Exception e) {
            Log.e(TAG, "Unknown error", e);
            result = new NetworkResult<>(0, e.getMessage(), e.getMessage(), null);
        }
        Log.d(TAG, "getUpdatePresenter result: " + result);
        return result;
    }

//
//    @Override
//    public NetworkResult<String> doAddUpdateDevice(Context context, String gcmId) {
//        if (context == null) {
//            throw new IllegalArgumentException("Context can not be null");
//        }
//        Log.d(TAG, "doAddUpdateDevice started...");
//        NetworkResult<String> result = null;
//        Map<String, Object> deviceParams = new HashMap<>();
//        deviceParams.put("deviceId", android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID));
//        deviceParams.put("googleId", gcmId);
//        try {
//            DistanceSalesService mService = ServiceGenerator.createService(DistanceSalesService.class);
//            Response response = mService.doAddUpdateDevice(deviceParams);
//            String deviceId = new String(((TypedByteArray) response.getBody()).getBytes());
//            result = new NetworkResult<>(200, null, null, deviceId);
//        } catch (NetworkException e) {
//            Log.e(TAG, "Network error", e);
//            result = new NetworkResult<>(e.getCode(), e.getMessage(), null, null);
//        } catch (ContentException e) {
//            Log.e(TAG, "Content error", e);
//            result = new NetworkResult<>(0, e.getMessage(), e.getMessage(), null);
//        } catch (Exception e) {
//            Log.e(TAG, "Unknown error", e);
//            result = new NetworkResult<>(0, e.getMessage(), e.getMessage(), null);
//        }
//        Log.d(TAG, "doAddUpdateDevice result: " + result);
//        return result;
//    }
//
//    @Override
//    public NetworkResult<UpdateDataDto> doGetUpdate(Context context) {
//        if (context == null) {
//            throw new IllegalArgumentException("Context can not be null");
//        }
//        Log.d(TAG, "getUpdate start...");
//        NetworkResult<UpdateDataDto> result = null;
//        UpdateDataDto response;
//        try {
//            DistanceSalesService mService = ServiceGenerator.createService(DistanceSalesService.class);
//            if (Settings.INSTANCE.getSyncId() > 0) {
//                response = mService.doGetUpdate(String.valueOf(Settings.INSTANCE.getSyncId()));
//            } else {
//                response = mService.doGetUpdate();
//            }
//            if (response != null) {
//                Log.d(TAG, "doGetUpdate success. syncId: " + response.getSyncId());
//            }

//            result = new NetworkResult<>(200, null, null, response);
//        } catch (NetworkException e) {
//            Log.e(TAG, "Network error", e);
//            result = new NetworkResult<>(e.getCode(), e.getMessage(), null, null);
//        } catch (ContentException e) {
//            Log.e(TAG, "Content error", e);
//            result = new NetworkResult<>(0, e.getMessage(), e.getMessage(), null);
//        } catch (Exception e) {
//            Log.e(TAG, "Unknown error", e);
//            result = new NetworkResult<>(0, e.getMessage(), e.getMessage(), null);
//        }
//        Log.d(TAG, "doGetUpdate result: " + result);
//        return result;
//    }
//
//    @Override
//    public NetworkResult<UpdateDataDto> doPostUpdate(Context context, UpdateDataDto updates) {
//        if (context == null) {
//            throw new IllegalArgumentException("Context can not be null");
//        }
//        if (updates == null) {
//            return null;
//        }
//        Log.d(TAG, "postUpdate start...");
//        NetworkResult<UpdateDataDto> result = null;
//        try {
//            DistanceSalesService mService = ServiceGenerator.createService(DistanceSalesService.class);
//            UpdateDataDto response = mService.doPostUpdate(updates);
//            if (response != null) {
//                Log.d(TAG, "postUpdate success. syncId: " + response.getSyncId());
//            }
//            result = new NetworkResult<>(200, null, null, response);
//        } catch (NetworkException e) {
//            Log.e(TAG, "Network error", e);
//            result = new NetworkResult<>(e.getCode(), e.getMessage(), null, null);
//        } catch (ContentException e) {
//            Log.e(TAG, "Content error", e);
//            result = new NetworkResult<>(0, e.getMessage(), e.getMessage(), null);
//        } catch (Exception e) {
//            Log.e(TAG, "Unknown error", e);
//            result = new NetworkResult<>(0, e.getMessage(), e.getMessage(), null);
//        }
//        Log.d(TAG, "postUpdate result: " + result);
//        return result;
//    }
//
//    @Override
//    public NetworkResult<String> doUploadPhoto(Context context, Control control) {
//        if (context == null) {
//            throw new IllegalArgumentException("Context can not be null");
//        }
//        if (control == null) {
//            return null;
//        }
//        Log.i(TAG, "Uploading " + control.getFileName() + " to server " + NetworkUtil.METHOD_UPLOAD);
//        if (PhotoUtil.isPhotoFileExists(control.getFileName())) {
//            NetworkResult<String> result = null;
//            try {
//                String UploadFileUrl = ServiceGenerator.doUploadPhoto(control.getFileName(),
//                        String.valueOf(control.getServerId()),
//                        PhotoUtil.getPublicPhotoFile(control.getFileName()));
//                Log.d(TAG, "uploadPhoto success. file url: " + UploadFileUrl);
//                result = new NetworkResult<>(200, null, null, UploadFileUrl);
//                Log.i(TAG, "uploadPhoto result: " + result);
//                return result;
//            } catch (NetworkException e) {
//                Log.e(TAG, "Network error", e);
//                result = new NetworkResult<>(e.getCode(), e.getMessage(), null, null);
//            } catch (Exception e) {
//                Log.e(TAG, "Unknown error", e);
//                result = new NetworkResult<>(0, e.getMessage(), e.getMessage(), null);
//            }
//            Log.i(TAG, "uploadPhoto result: " + result);
//            return result;
//        } else {
//            Log.i(TAG, "Can't find file " + control.getFileName() + " for upload");
//            return new NetworkResult<>(NetworkResult.RESULT_FILE_NOT_FOUND, "Can't upload photo. file not found", null, null);
//        }
//    }
//
//    @Override
//    public NetworkResult<String> doUploadCurrentLocation(Context context, LocationEntity locationEntity) {
//        if (context == null) {
//            throw new IllegalArgumentException("Context can not be null");
//        }
//        if (locationEntity == null) {
//            return null;
//        }
//
//        Log.d(TAG, "uploadCurrentLocation start...");
//        NetworkResult<String> result = null;
//        try {
//            DistanceSalesService mService = ServiceGenerator.createService(DistanceSalesService.class);
//            Response response = mService.doUploadCurrentLocation(locationEntity);
//            if (response!=null){
//                result = new NetworkResult<>(response.getStatus(), null, null, response.getReason());
//            }
//        } catch (NetworkException e) {
//            Log.e(TAG, "Network error", e);
//            result = new NetworkResult<>(e.getCode(), e.getMessage(), null, null);
//        } catch (ContentException e) {
//            Log.e(TAG, "Content error", e);
//            result = new NetworkResult<>(0, e.getMessage(), e.getMessage(), null);
//        } catch (Exception e) {
//            Log.e(TAG, "Unknown error", e);
//            result = new NetworkResult<>(0, e.getMessage(), e.getMessage(), null);
//        }
//        Log.d(TAG, "uploadCurrentLocation result: " + result);
//        return result;
//    }
//
//    @Override
//    public NetworkResult<String> doUploadCalls(Context context, List<CallEntity> callEntities) {
//        if (context == null) {
//            throw new IllegalArgumentException("Context can not be null");
//        }
//        if (callEntities == null) {
//            return null;
//        }
//        Log.d(TAG, "uploadCalls start...");
//        NetworkResult<String> result = null;
//        try {
//            DistanceSalesService mService = ServiceGenerator.createService(DistanceSalesService.class);
//            Response response = mService.doUploadCalls(callEntities);
//            if (response!=null){
//               result = new NetworkResult<>(response.getStatus(), null, null, response.getReason());
//            }
//        } catch (NetworkException e) {
//            Log.e(TAG, "Network error", e);
//            result = new NetworkResult<>(e.getCode(), e.getMessage(), null, null);
//        } catch (ContentException e) {
//            Log.e(TAG, "Content error", e);
//            result = new NetworkResult<>(0, e.getMessage(), e.getMessage(), null);
//        } catch (Exception e) {
//            Log.e(TAG, "Unknown error", e);
//            result = new NetworkResult<>(0, e.getMessage(), e.getMessage(), null);
//        }
//        Log.d(TAG, "uploadCalls result: " + result);
//        return result;
//    }
//
//    @Override
//    public NetworkResult<String> doUploadApps(Context context, List<AppEntity> appEntities) {
//        if (context == null) {
//            throw new IllegalArgumentException("Context can not be null");
//        }
//        if (appEntities == null) {
//            return null;
//        }
//        Log.d(TAG, "uploadApps start...");
//        NetworkResult<String> result = null;
//        try {
//            DistanceSalesService mService = ServiceGenerator.createService(DistanceSalesService.class);
//            Response response = mService.doUploadApps(appEntities);
//            if (response!=null){
//                result = new NetworkResult<>(response.getStatus(), null, null, response.getReason());
//            }
//        } catch (NetworkException e) {
//            Log.e(TAG, "Network error", e);
//            result = new NetworkResult<>(e.getCode(), e.getMessage(), null, null);
//        } catch (ContentException e) {
//            Log.e(TAG, "Content error", e);
//            result = new NetworkResult<>(0, e.getMessage(), e.getMessage(), null);
//        } catch (Exception e) {
//            Log.e(TAG, "Unknown error", e);
//            result = new NetworkResult<>(0, e.getMessage(), e.getMessage(), null);
//        }
//        Log.d(TAG, "uploadApps result: " + result);
//        return result;
//    }


}
