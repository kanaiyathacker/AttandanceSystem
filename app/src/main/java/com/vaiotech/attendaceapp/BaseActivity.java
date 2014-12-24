package com.vaiotech.attendaceapp;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.WindowManager;

import com.octo.android.robospice.SpiceManager;
import com.services.AttandanceRestService;

import roboguice.activity.RoboActivity;

/**
 * Created by kanaiyalalt on 12/11/2014.
 */
public class BaseActivity extends RoboActivity {
    SpiceManager spiceManager = new SpiceManager(AttandanceRestService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
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

}
