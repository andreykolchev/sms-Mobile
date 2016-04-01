package com.sms.service;

/*
 * sms
 * Created by A.Kolchev  3.3.2016
 */

import android.app.Activity;
import android.os.AsyncTask;

import com.afollestad.materialdialogs.MaterialDialog;
import com.sms.App;
import com.sms.R;
import com.sms.api.ApiException;
import com.sms.api.ApiManager;
import com.sms.data.model.Route;
import com.sms.net.NetworkResult;
import com.sms.util.Log;

import java.util.List;


public class GetRoutsTask extends AsyncTask<String, Void, Boolean> {

    private static final String TAG = GetRoutsTask.class.getSimpleName();
    private Activity mContext;
    private String errorMessage;
    private MaterialDialog mPleaseWaitDialog;
    private AsyncTaskCompleteListener callback = null;

    public GetRoutsTask(Activity mContext, AsyncTaskCompleteListener callback)  {
        this.mContext = mContext;
        this.callback = callback;
        mPleaseWaitDialog = new MaterialDialog.Builder(mContext).title(R.string.loading)
                .content(R.string.progress_message)
                .progress(true, 0).build();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mPleaseWaitDialog.show();
    }

    @Override
    protected Boolean doInBackground(String... params) {

        Log.d(TAG, "Starting getUpdateRouts");
        NetworkResult<List<Route>> resultGetUpdate = App.getInstance().getNetworker().getUpdateRouts(mContext);
        if (resultGetUpdate.isSuccess()){
            List<Route> result = resultGetUpdate.result;
            Log.d(TAG, "getUpdateRouts executed succesfully.");
            try{
                ApiManager.processGetUpdateRouts(result);
            } catch (ApiException e){
                Log.e(TAG, "Can't process getUpdateRouts response", e);
            }
            return true;
        } else {
            if (resultGetUpdate.code == -101) {
                Log.d(TAG, "Can't connect to server. Error: " + resultGetUpdate.error);
                errorMessage = mContext.getResources().getString(R.string.error_server_connection);
            } else {
                Log.d(TAG, "getUpdateRouts failed. Status code: " + resultGetUpdate.code + ". Status message: " + resultGetUpdate.error);
                errorMessage = mContext.getResources().getString(R.string.no_data);
            }
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        if (result){
            mPleaseWaitDialog.dismiss();
            callback.onTaskComplete();
        }else {
            new MaterialDialog.Builder(mContext).title(R.string.error_title)
                    .content(errorMessage)
                    .positiveText("Ок").show();
        }
    }
}
