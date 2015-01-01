package com.vaiotech.attendaceapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bean.User;
import com.google.gson.Gson;
import com.util.Util;

import java.io.IOException;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_functional_main)
public class FunctionalMainActivity extends BaseActivity {

    public static final String TAG = "NTPSync API Demo";

    // messages that are returned from service
    public static final int RETURN_GENERIC_ERROR = 0;
    public static final int RETURN_OKAY = 1;
    public static final int RETURN_SERVER_TIMEOUT = 2;
    public static final int RETURN_NO_ROOT = 3;

    @InjectView(R.id.myProfileButton) TextView myProfileButton;
    @InjectView(R.id.aboutiPresenceButton) TextView aboutiPresenceButton;
    @InjectView(R.id.smartScanButton) TextView smartScanButton;
    @InjectView(R.id.viewReportButton) TextView viewReportButton;
    @InjectView(R.id.contactUsButton) TextView contactUsButton;
    Context context ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_functional_main);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Calibri.ttf");
        myProfileButton.setTypeface(font);
        aboutiPresenceButton.setTypeface(font);
        smartScanButton.setTypeface(font);
        viewReportButton.setTypeface(font);
        contactUsButton.setTypeface(font);
        hideProgressBar();
    }



    public void smartScan(View view) {
        Intent intent =     null;
        if(isLogin) {
            if(isUserAdmin) {
                intent = new Intent(this, MainActivity.class);
            } else {
                intent = new Intent(this, UserMainActivity.class);
            }
        } else {
                intent = new Intent(this, LoginActivity.class);
                intent.putExtra("ACTIVITY_SELECTED", "SMART_SCAN");
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
//        finish();
    }

    public void myProfile(View view) {
        Intent intent = null;
        if(isLogin) {
            intent = new Intent(this, MyProfileActivity.class);
        } else {
            intent = new Intent(this, LoginActivity.class);
            intent.putExtra("ACTIVITY_SELECTED" , "MY_PROFILE");
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
//        finish();
    }

    public void aboutIPresence(View view) {
        Intent intent = new Intent(this, AboutIPresenceActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
//        finish();
    }

    public void viewReport(View view) {
        Intent intent = null;
        if(isLogin) {
            if(isUserAdmin) {
                intent = new Intent(this, ViewReportActivity.class);
            } else {
                intent = new Intent(this, ViewUserReportActivity.class);
            }
        } else {
            intent = new Intent(this, LoginActivity.class);
            intent.putExtra("ACTIVITY_SELECTED", "VIEW_REPORT");
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
//        finish();
    }

    public void contactUs(View view) {
        Intent intent = new Intent(this, ContactUsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isUserLogedIn();
    }
}
