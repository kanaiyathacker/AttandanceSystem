package com.vaiotech.attendaceapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_functional_main)
public class FunctionalMainActivity extends BaseActivity {

    @InjectView(R.id.myProfileButton) TextView myProfileButton;
    @InjectView(R.id.aboutiPresenceButton) TextView aboutiPresenceButton;
    @InjectView(R.id.smartScanButton) TextView smartScanButton;
    @InjectView(R.id.viewReportButton) TextView viewReportButton;
    @InjectView(R.id.contactUsButton) TextView contactUsButton;

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
    }

    public void smartScan(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void myProfile(View view) {
        Intent intent = new Intent(this, MyProfileActivity.class);
        startActivity(intent);
    }

    public void aboutIPresence(View view) {
        Intent intent = new Intent(this, AboutIPresenceActivity.class);
        startActivity(intent);
    }

    public void viewReport(View view) {
        Intent intent = new Intent(this, ViewReportActivity.class);
        startActivity(intent);
    }

    public void contactUs(View view) {
        Intent intent = new Intent(this, ContactUsActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_functional_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
