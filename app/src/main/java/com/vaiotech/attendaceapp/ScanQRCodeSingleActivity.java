package com.vaiotech.attendaceapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.barcodescannerfordialogs.DialogScanner;
import com.barcodescannerfordialogs.helpers.CameraFace;

import java.util.Calendar;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_scan_qrcode_single)
public class ScanQRCodeSingleActivity extends BaseActivity implements DialogScanner.OnQRCodeScanListener {

    @InjectView(R.id.idLableTV) TextView idLableTV;
    @InjectView(R.id.idValueTV) TextView idValueTV;
    @InjectView(R.id.adminNameLableTV) TextView adminNameLableTV;
    @InjectView(R.id.adminValueLableTV) TextView adminValueLableTV;
    @InjectView(R.id.seperateTimeTV) TextView seperateTimeTV;
    @InjectView(R.id.timeTV) TextView timeTV;
    @InjectView(R.id.hhET) EditText hhET;
    @InjectView(R.id.mmET) EditText mmET;

    @InjectView(R.id.dateTV) TextView dateTV;
    @InjectView(R.id.ddET) EditText ddET;
    @InjectView(R.id.MMET) EditText MMET;
    @InjectView(R.id.yyET) EditText yyET;

    @InjectView(R.id.inBUTTON)  Button inBUTTON;
    @InjectView(R.id.outBUTTON) Button outBUTTON;
    @InjectView(R.id.getInfoBUTTON) Button getInfoBUTTON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qrcode_single);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Calibri.ttf");
        idLableTV.setTypeface(font);
        idValueTV.setTypeface(font);
        adminNameLableTV.setTypeface(font);
        adminValueLableTV.setTypeface(font);

        Typeface digital = Typeface.createFromAsset(getAssets(), "fonts/digital_7_mono.ttf");

        seperateTimeTV.setTypeface(digital);
        hhET.setTypeface(digital);
        mmET.setTypeface(digital);
        timeTV.setTypeface(digital);

        ddET.setTypeface(digital);
        MMET.setTypeface(digital);
        yyET.setTypeface(digital);
        dateTV.setTypeface(digital);

        inBUTTON.setTypeface(font);
        outBUTTON.setTypeface(font);
        getInfoBUTTON.setTypeface(font);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.AM_PM, Calendar.PM);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        hhET.setText(""+hour);
        mmET.setText(""+(min < 10 ? "0"+ min : min));

        int date = cal.get(Calendar.DATE);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        ddET.setText("" + date);
        MMET.setText("" + month);
        yyET.setText("" + year);

        DialogScanner dialog = DialogScanner.newInstance(CameraFace.BACK);
        dialog.show(getFragmentManager(), "cameraPreview");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scan_qrcode_single, menu);
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


    @Override
    public void onQRCodeScan(String contents) {
        idValueTV.setText("content---" + contents);
//        Intent intent = new Intent(this,ScanQRCodeSingleActivity.class);
//        intent.putExtra("SCAN_CONTENT" , contents);
//        startActivity(intent);
    }
}
