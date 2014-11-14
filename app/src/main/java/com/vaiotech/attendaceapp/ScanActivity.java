package com.vaiotech.attendaceapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.barcodescannerfordialogs.DialogScanner;
import com.bean.Person;
import com.google.gson.Gson;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_scan)
public class ScanActivity extends RoboActivity implements DialogScanner.OnQRCodeScanListener {

    @InjectView(R.id.companyAdminNameTV) TextView companyAdminNameTV;
    @InjectView(R.id.companyAdminCodeTV) TextView companyAdminCodeTV;
    @InjectView(R.id.scanBUTTON) Button scanBUTTON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Calibri.ttf");

        companyAdminNameTV.setTypeface(font);
        companyAdminCodeTV.setTypeface(font);
        scanBUTTON.setTypeface(font);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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

    public void scan(View view) {
        Intent intent = new Intent(this,HomeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onQRCodeScan(String contents) {
        Intent intent = new Intent(this,HomeActivity.class);
        intent.putExtra("SCAN_CONTENT" , contents);
        startActivity(intent);
    }
}
