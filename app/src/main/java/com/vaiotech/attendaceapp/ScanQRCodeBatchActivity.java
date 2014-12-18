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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import java.util.List;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_scan_qrcode_batch)
public class ScanQRCodeBatchActivity extends BaseActivity implements DialogScanner.OnQRCodeScanListener {

    private static final String TAG = ScanQRCodeBatchActivity.class.getName();

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
    @InjectView(R.id.progressBar) ProgressBar progressBar;
    @InjectView(R.id.counterLableTV) TextView counterLableTV;

    @InjectView(R.id.counterValTV) TextView counterValTV;
    @InjectView(R.id.inBUTTON) Button inBUTTON;
    @InjectView(R.id.outBUTTON) Button outBUTTON;
    private List<String> list;
    private User user;
    private LocationManager lm;
    private Location location;
    private SaveAttandanceRequest saveAttandanceRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qrcode_batch);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Calibri.ttf");
        Typeface digital = Typeface.createFromAsset(getAssets(), "fonts/digital_7_mono.ttf");

        seperateTimeTV.setTypeface(digital);
        hhET.setTypeface(digital);
        mmET.setTypeface(digital);
        timeTV.setTypeface(digital);
        counterLableTV.setTypeface(digital);

        ddET.setTypeface(digital);
        MMET.setTypeface(digital);
        yyET.setTypeface(digital);
        dateTV.setTypeface(digital);

        counterValTV.setTypeface(digital);

        inBUTTON.setTypeface(font);
        outBUTTON.setTypeface(font);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.AM_PM, Calendar.PM);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        hhET.setText("" + hour);
        mmET.setText("" + (min < 10 ? "0" + min : min));

        int date = cal.get(Calendar.DATE);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        ddET.setText("" + date);
        MMET.setText("" + month);
        yyET.setText("" + year);

        inBUTTON.setEnabled(false);
        outBUTTON.setEnabled(false);

        inBUTTON.setAlpha(.5f);
        outBUTTON.setAlpha(.5f);

        SharedPreferences sharedPreferences = getSharedPreferences("DIGITAL_ATTENDANCE", Context.MODE_PRIVATE);
        String val = sharedPreferences.getString("USER_DETAILS", null);
        Gson gson = new Gson();
        user = gson.fromJson(val, User.class);
        adminValueLableTV.setText(user.getfName() + " " + user.getlName());

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        new  HeavyTask(this).execute();
    }

    private class HeavyTask extends AsyncTask<String, Void, Void> {

        private ScanQRCodeBatchActivity scanQRCodeBatchActivity;
        private DialogScanner dialog;
        public HeavyTask(ScanQRCodeBatchActivity scanQRCodeBatchActivity) {
            this.scanQRCodeBatchActivity = scanQRCodeBatchActivity;
        }

        protected Void doInBackground(String... args) {
            dialog = DialogScanner.newInstance(CameraFace.BACK , 1 , progressBar);
            return null;
        }

        protected void onPostExecute(Void results) {
            dialog.show(scanQRCodeBatchActivity.getFragmentManager(), "cameraPreview");
        }
    }


    @Override
    public void onQRCodeScan(Object contents) {
        if (contents != null) {
            list = (List)contents;
            counterValTV.setText(""+list.size());

            inBUTTON.setEnabled(true);
            outBUTTON.setEnabled(true);

            inBUTTON.setAlpha(1f);
            outBUTTON.setAlpha(1f);
        }
    }

    public void save(View view) {
        String type = view.getId() == R.id.inBUTTON ? "IN" : "OUT";
        saveAttandanceRequest = new SaveAttandanceRequest(buildAttandanceTransaction(type));
        spiceManager.execute(saveAttandanceRequest , new SaveAttandanceRequestListener());
        if(list != null)
            list.clear();
        counterValTV.setText("0");
        openDialog(view);
    }

    public AttandanceTransaction buildAttandanceTransaction(String type) {
//        SharedPreferences sharedPreferences = getSharedPreferences("DIGITAL_ATTENDANCE" , Context.MODE_PRIVATE);
//        String val = sharedPreferences.getString("USER_DETAILS" , null);
//        Gson gson = new Gson();
//        User user = gson.fromJson(val , User.class);

        AttandanceTransaction t = new AttandanceTransaction();
        t.setAdminId(user.getUserId());
        t.setCardId(list);
        t.setDate(Util.convertDateToString(new Date()));
        t.setTime(hhET.getText() + ":" + mmET.getText());
        t.setOrgId(user.getCoId());
        t.setType(type);
        if(lm != null) {
            location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location != null) {
                t.setLongitude(""+location.getLongitude());
                t.setLatitude(""+location.getLatitude());
            }
        }
        return t;
    }

    public void openDialog(View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setMessage((view.getId() == R.id.inBUTTON ? "IN Time for " : "OUT Time for ") + counterValTV.getText() + "Users noted as " + hhET.getText() + ":" + mmET.getText());
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

    private void vibrate() {
        Log.d(TAG, "vibrate");

        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE) ;
        vibe.vibrate(500);
    }
}
