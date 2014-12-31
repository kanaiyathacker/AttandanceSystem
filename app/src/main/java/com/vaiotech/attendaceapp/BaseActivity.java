package com.vaiotech.attendaceapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.bean.User;
import com.google.gson.Gson;
import com.octo.android.robospice.SpiceManager;
import com.services.AttandanceRestService;

import roboguice.activity.RoboActivity;

/**
 * Created by kanaiyalalt on 12/11/2014.
 */
public class BaseActivity extends RoboActivity {
    SpiceManager spiceManager = new SpiceManager(AttandanceRestService.class);
    SharedPreferences sharedPreferences;
    boolean isLogin;
    boolean isUserAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        hideProgressBar();
        sharedPreferences = getSharedPreferences("DIGITAL_ATTENDANCE", Context.MODE_PRIVATE);
        isUserLogedIn();
    }
    @Override
    protected void onStop() {
        super.onStop();
        spiceManager.shouldStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        spiceManager.start(this);
    }

    public void showProgressBar() {
        setProgressBarIndeterminateVisibility(true);
    }

    public void hideProgressBar() {
        setProgressBarIndeterminateVisibility(false);
    }

    public void isUserLogedIn() {
        String val = sharedPreferences.getString("USER_DETAILS" , null);
        Gson gson = new Gson();
        User user = gson.fromJson(val , User.class);
        isLogin = (user != null && user.getUserId() != null && user.getUserId().length() > 0);
        isUserAdmin = (user != null && user.getType() != null && user.getType().length() > 0 && user.getType().equalsIgnoreCase("0"));
    }

}
