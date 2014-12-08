package com.vaiotech.attendaceapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.barcodescannerfordialogs.DialogScanner;
import com.barcodescannerfordialogs.helpers.CameraFace;
import com.bean.AttandanceTransaction;
import com.bean.User;
import com.google.gson.Gson;
import com.listener.GetInfoRequestListener;
import com.listener.SaveAttandanceRequestListener;
import com.services.GetInfoRequest;
import com.services.SaveAttandanceRequest;
import com.util.Util;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

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

    private SaveAttandanceRequest saveAttandanceRequest;
    private GetInfoRequest getInfoRequest;
    private LocationManager lm;
    private Location location;
    private User user;
    private String cardId;

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

        inBUTTON.setEnabled(false);
        outBUTTON.setEnabled(false);
        getInfoBUTTON.setEnabled(false);

        inBUTTON.setAlpha(.5f);
        outBUTTON.setAlpha(.5f);
        getInfoBUTTON.setAlpha(.5f);

        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        SharedPreferences sharedPreferences = getSharedPreferences("DIGITAL_ATTENDANCE" , Context.MODE_PRIVATE);
        String val = sharedPreferences.getString("USER_DETAILS" , null);
        Gson gson = new Gson();
        user = gson.fromJson(val , User.class);
        adminValueLableTV.setText(user.getfName() + " " +  user.getlName());


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
        this.cardId = contents;

        inBUTTON.setEnabled(true);
        outBUTTON.setEnabled(true);
        getInfoBUTTON.setEnabled(true);


        inBUTTON.setAlpha(1f);
        outBUTTON.setAlpha(1f);
        getInfoBUTTON.setAlpha(1f);

//        Intent intent = new Intent(this,ScanQRCodeSingleActivity.class);
//        intent.putExtra("SCAN_CONTENT" , contents);
//        startActivity(intent);
    }

    public void save(View view) {
        String type = view.getId() == R.id.inBUTTON ? "IN" : "OUT";
        saveAttandanceRequest = new SaveAttandanceRequest(buildAttandanceTransaction(type));
        spiceManager.execute(saveAttandanceRequest , new SaveAttandanceRequestListener());
        openDialog(view);
    }

    public void getInfo(View view) {
        getInfoRequest = new GetInfoRequest(cardId);
        spiceManager.execute(getInfoRequest, new GetInfoRequestListener(this));
    }

    public AttandanceTransaction buildAttandanceTransaction(String type) {
//        SharedPreferences sharedPreferences = getSharedPreferences("DIGITAL_ATTENDANCE" , Context.MODE_PRIVATE);
//        String val = sharedPreferences.getString("USER_DETAILS" , null);
//        Gson gson = new Gson();
//        User user = gson.fromJson(val , User.class);

        AttandanceTransaction t = new AttandanceTransaction();
        t.setAdminId(user.getUserId());
        t.setCardId(Arrays.asList(cardId));
        t.setDate(Util.convertDateToString(new Date()));
        t.setTime(hhET.getText() + ":" + mmET.getText());
        t.setTrasTable(user.getTranTable());
        t.setType(type);
        if(lm != null) {
            location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location != null) {
                t.setLongitude(location.getLongitude());
                t.setLatitude(location.getLatitude());
            }
        }
        return t;
    }

    public void openDialog(View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setMessage((view.getId() == R.id.inBUTTON ? "IN Time for " : "OUT Time for ") + cardId + " noted as " + hhET.getText() + ":" + mmET.getText());
        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
