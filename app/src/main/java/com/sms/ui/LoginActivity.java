package com.sms.ui;


/*
 * sms
 * Created by A.Kolchev  1.3.2016
 */

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import com.sms.R;
import com.sms.Settings;
import com.sms.service.UserLoginTask;
import com.sms.util.NetworkUtil;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private TextView mLogin;
    private TextView mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        mLogin = (TextView) findViewById(R.id.login);
        mLogin.setText(Settings.INSTANCE.getUserId());

        findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        mPassword = (TextView) findViewById(R.id.password);
        mPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    attemptLogin();
                    return false;
                }
                return false;
            }
        });

    }

    private void attemptLogin() {

        String login = mLogin.getText().toString();
        String password = mPassword.getText().toString();

        if (TextUtils.isEmpty(login)) {
            mLogin.animate().translationX(20).setInterpolator(new CycleInterpolator(4)).setDuration(450)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mLogin.requestFocus();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }
                    });
            return;
        }
        if (TextUtils.isEmpty(password)) {
            mPassword.animate().translationX(20).setInterpolator(new CycleInterpolator(4)).setDuration(450)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mPassword.requestFocus();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }
                    });
            return;
        }
        if (!NetworkUtil.isAnyNetworkConnected(LoginActivity.this)) {
            new MaterialDialog.Builder(this).title(R.string.error_title)
                    .content(R.string.error_need_connection)
                    .positiveText("Ок").show();
        }
        new UserLoginTask(this).execute(login, password);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


}
