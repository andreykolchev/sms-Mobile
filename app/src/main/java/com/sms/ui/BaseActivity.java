package com.sms.ui;

/*
 * sms
 * Created by A.Kolchev  26.2.2016
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sms.App;
import com.sms.service.SyncAlarm;
import com.sms.util.Log;
import com.sms.util.TimeUtil;

/**
 * sms
 * Created by A.Kolchev on 26.02.16.
 */
public class BaseActivity extends AppCompatActivity {

    public final String TAG = getClass().getSimpleName();


    @Override
    protected void onResume() {
        super.onResume();
        if (!App.getInstance().resetSessionTime()) {
            Log.d(TAG, "Session expired. Showing login screen.");
            showLoginActivity();
        }
    }

    private void showLoginActivity() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void onClickHome(View view) {
        startActivity(new Intent(this, RoutsActivity.class));
    }

    public void onClickMap(View view) {
        startActivity(new Intent(this, MapActivity.class));
    }

    public void onClickOrders(View view) {
    }

    public void onClickSync(View view) {
        SyncAlarm.requestSyncAlarm(this, TimeUtil.getTimeWithDelay(5), false);
    }

    public void onClickPresenter(View view) {
        startActivity(new Intent(this, PresenterActivity.class));
    }
}
