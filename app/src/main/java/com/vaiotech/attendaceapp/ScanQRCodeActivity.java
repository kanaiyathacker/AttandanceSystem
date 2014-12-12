package com.vaiotech.attendaceapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.barcodescannerfordialogs.DialogScanner;
import com.barcodescannerfordialogs.helpers.CameraFace;
import com.bean.Admin;
import com.google.gson.Gson;

import java.io.InputStream;
import java.net.URL;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_scan_qrcode)
public class ScanQRCodeActivity extends RoboActivity implements DialogScanner.OnQRCodeScanListener {

//    @InjectView(R.id.companyAdminNameTV) TextView companyAdminNameTV;
//    @InjectView(R.id.companyAdminCodeTV) TextView companyAdminCodeTV;
//    @InjectView(R.id.companyLOGOIV) ImageView companyLOGOIV;
    @InjectView(R.id.scanBUTTON) Button scanBUTTON;
    Bitmap bitmap;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qrcode);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Calibri.ttf");
        scanBUTTON.setTypeface(font);
        String adminDetails = getIntent().getStringExtra("ADMIN_DETAILS");
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
//        onQRCodeScan("");
//        DialogScanner dialog = DialogScanner.newInstance(CameraFace.BACK);
//        dialog.show(getFragmentManager(), "dialogScanner");
    }

    @Override
    public void onQRCodeScan(Object contents) {
        Intent intent = new Intent(this,HomeActivity.class);
//        intent.putExtra("SCAN_CONTENT" , contents);
        startActivity(intent);
    }
}
