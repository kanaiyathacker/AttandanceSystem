package com.vaiotech.attendaceapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bean.User;
import com.google.gson.Gson;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_functional_main)
public class FunctionalMainActivity extends BaseActivity {

    @InjectView(R.id.myProfileButton) TextView myProfileButton;
    @InjectView(R.id.aboutiPresenceButton) TextView aboutiPresenceButton;
    @InjectView(R.id.smartScanButton) TextView smartScanButton;
    @InjectView(R.id.viewReportButton) TextView viewReportButton;
    @InjectView(R.id.contactUsButton) TextView contactUsButton;

    private SharedPreferences sharedPreferences;
    boolean isLogin;
    boolean isUserAdmin;

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
        sharedPreferences = getSharedPreferences("DIGITAL_ATTENDANCE" , Context.MODE_PRIVATE);
        isUserLogedIn();
    }

    public void isUserLogedIn() {
        String val = sharedPreferences.getString("USER_DETAILS" , null);
        Gson gson = new Gson();
        User user = gson.fromJson(val , User.class);
        isLogin = (user != null && user.getUserId() != null && user.getUserId().length() > 0);
        isUserAdmin = (user != null && user.getType() != null && user.getType().length() > 0 && user.getType().equalsIgnoreCase("Admin"));
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
        startActivity(intent);
//        finish();
    }

    public void aboutIPresence(View view) {
        Intent intent = new Intent(this, AboutIPresenceActivity.class);
        startActivity(intent);
//        finish();
    }

    public void viewReport(View view) {
        Intent intent = null;
        if(isLogin) {
            intent = new Intent(this, ViewUserReportActivity.class);
        } else {
            intent = new Intent(this, LoginActivity.class);
            intent.putExtra("ACTIVITY_SELECTED", "VIEW_REPORT");
        }
        startActivity(intent);
//        finish();
    }

    public void contactUs(View view) {
        Intent intent = new Intent(this, ContactUsActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_functional_main, menu);
        menu.getItem(0).setTitle(isLogin ? "Log Out" : "Log In");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if ("Log Out" == item.getTitle()) {
            sharedPreferences.edit().remove("USER_DETAILS").commit();
            isUserLogedIn();
            item.setTitle("Log In");
        } else {
            Intent intent = new Intent(this , LoginActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isUserLogedIn();
    }
}
