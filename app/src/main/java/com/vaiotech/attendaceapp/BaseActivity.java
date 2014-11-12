package com.vaiotech.attendaceapp;

import com.octo.android.robospice.SpiceManager;
import com.services.AttandanceRestService;

import roboguice.activity.RoboActivity;

/**
 * Created by kanaiyalalt on 12/11/2014.
 */
public class BaseActivity extends RoboActivity {
    SpiceManager spiceManager = new SpiceManager(AttandanceRestService.class);

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
