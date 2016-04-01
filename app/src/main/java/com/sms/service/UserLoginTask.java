package com.sms.service;

/*
 * sms
 * Created by A.Kolchev  1.3.2016
 */

import android.app.Activity;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.afollestad.materialdialogs.MaterialDialog;
import com.sms.App;
import com.sms.R;
import com.sms.Settings;
import com.sms.net.NetworkResult;
import com.sms.util.Log;
import com.sms.util.TimeUtil;

public class UserLoginTask extends AsyncTask<String, Void, Boolean> {

    private static final String TAG = UserLoginTask.class.getSimpleName();
    private Activity mContext;
    private MaterialDialog mPleaseWaitDialog;
    private String errorMessage;

    public UserLoginTask(Activity mContext) {
        this.mContext = mContext;
        mPleaseWaitDialog = new MaterialDialog.Builder(mContext).title(R.string.login_progress_title)
                .content(R.string.progress_message)
                .progress(true, 0).build();
    }

    @Override
    protected void onPreExecute() {
        mPleaseWaitDialog.show();
        mPleaseWaitDialog.setCancelable(false);
    }

    @Override
    protected Boolean doInBackground(String... params) {

        String username = params[0];
        String password = params[1];

        NetworkResult<String> loginResult = App.getInstance().getNetworker().doLogin(mContext, username, password);
        if (loginResult.isSuccess() && !TextUtils.isEmpty(loginResult.result)) {
            Log.d(TAG, "Login success. Got token: " + loginResult.result);
            Settings.INSTANCE.setUserId(username);
            Settings.INSTANCE.setPassword(password);
            SyncAlarm.requestSyncAlarm(App.getInstance(), TimeUtil.getTimeWithDelay(1), false);
            return true;
        } else {
            if (loginResult.code == -101) {
                Log.d(TAG, "Can't connect to server. Error: " + loginResult.error);
                errorMessage = mContext.getResources().getString(R.string.error_server_connection);
            } else {
                Log.d(TAG, "Auth failed. Status code: " + loginResult.code + ". Status message: " + loginResult.error);
                errorMessage = mContext.getResources().getString(R.string.error_incorrect_password);
            }
        }
        //return false;
        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        mPleaseWaitDialog.dismiss();
        if (result){
            App.getInstance().openSession();
            mContext.finish();
        } else {
            new MaterialDialog.Builder(mContext).title(R.string.error_title)
                    .content(errorMessage)
                    .positiveText("Ок").show();
        }

    }
}