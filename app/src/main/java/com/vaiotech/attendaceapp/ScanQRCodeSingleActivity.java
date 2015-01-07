package com.vaiotech.attendaceapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.barcodescannerfordialogs.DialogScanner;
import com.barcodescannerfordialogs.helpers.CameraFace;
import com.bean.AttandanceTransaction;
import com.bean.User;
import com.google.gson.Gson;
import com.listener.GetInfoRequestListener;
import com.listener.SaveAttandanceRequestListener;
import com.services.GetInfoRequest;
import com.services.GetServerTimeRequest;
import com.services.SaveAttandanceRequest;
import com.util.Util;

import java.util.Arrays;
import java.util.Date;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_scan_qrcode_single)
public class ScanQRCodeSingleActivity extends BaseActivity implements DialogScanner.OnQRCodeScanListener , AdapterView.OnItemSelectedListener{

    @InjectView(R.id.idLableTV) TextView idLableTV;
    @InjectView(R.id.idValueTV) TextView idValueTV;
    @InjectView(R.id.adminNameLableTV) TextView adminNameLableTV;
    @InjectView(R.id.adminValueLableTV) TextView adminValueLableTV;
    @InjectView(R.id.seperateTimeTV) TextView seperateTimeTV;
    @InjectView(R.id.seperateDateTV) TextView seperateDateTV;
    @InjectView(R.id.seperateDateMMTV) TextView seperateDateMMTV;
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
    @InjectView(R.id.activitySpinner) Spinner activitySpinner;



    private SaveAttandanceRequest saveAttandanceRequest;
    private GetInfoRequest getInfoRequest;
    private LocationManager lm;
    private User user;
    private String cardId;

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String selItem = (String) activitySpinner.getSelectedItem();
        if(!selItem.equals("Select") && this.cardId != null && this.cardId.length() > 0) {
            inBUTTON.setEnabled(true);
            outBUTTON.setEnabled(true);
            getInfoBUTTON.setEnabled(true);

            inBUTTON.setAlpha(1f);
            outBUTTON.setAlpha(1f);
            getInfoBUTTON.setAlpha(1f);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qrcode_single);
        new GetServerTimeRequest(hhET , mmET , ddET , MMET , yyET ).execute();
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Calibri.ttf");
        idLableTV.setTypeface(font);
        idValueTV.setTypeface(font);
        adminNameLableTV.setTypeface(font);
        adminValueLableTV.setTypeface(font);

        Typeface digital = Typeface.createFromAsset(getAssets(), "fonts/digital_7_mono.ttf");

        seperateTimeTV.setTypeface(digital);
        seperateDateTV.setTypeface(digital);
        hhET.setTypeface(digital);
        mmET.setTypeface(digital);
        timeTV.setTypeface(digital);
        seperateDateMMTV.setTypeface(digital);

        ddET.setTypeface(digital);
        MMET.setTypeface(digital);
        yyET.setTypeface(digital);
        dateTV.setTypeface(digital);

        inBUTTON.setTypeface(font);
        outBUTTON.setTypeface(font);
        getInfoBUTTON.setTypeface(font);

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
        adminValueLableTV.setText(user.getfName() + " " + user.getlName());
        showProgressBar();
        new  HeavyTask(this).execute();
        getEventData();
        buildActivitySpinner(user , activitySpinner);
        activitySpinner.setOnItemSelectedListener(this);
    }

    private void getEventData() {
    }

    private class HeavyTask extends AsyncTask<String, Void, Void> {

        private ScanQRCodeSingleActivity scanQRCodeSingleActivity;
        private DialogScanner dialog;
        public HeavyTask(ScanQRCodeSingleActivity scanQRCodeSingleActivity) {
            this.scanQRCodeSingleActivity = scanQRCodeSingleActivity;
        }

        protected Void doInBackground(String... args) {
            dialog = DialogScanner.newInstance(CameraFace.BACK , 0 , null , null);
            dialog.show(scanQRCodeSingleActivity.getFragmentManager(), "cameraPreview");
            return null;
        }

        protected void onPostExecute(Void results) {
            hideProgressBar();
        }
    }

    @Override
    public void onQRCodeScan(Object contents) {
        if(contents != null) {
            Gson gson = new Gson();
            User user = gson.fromJson("" + contents, User.class);
            String userCardId = user.getCardId();
            if(userCardId != null && userCardId.length()> 0) {
                idValueTV.setText(userCardId);

                this.cardId = userCardId;


                String selItem = (String) activitySpinner.getSelectedItem();
                if(!selItem.equals("Select")) {
                    inBUTTON.setEnabled(true);
                    outBUTTON.setEnabled(true);
                    getInfoBUTTON.setEnabled(true);


                    inBUTTON.setAlpha(1f);
                    outBUTTON.setAlpha(1f);
                    getInfoBUTTON.setAlpha(1f);
                }
            }
        }

    }

    public void save(View view) {
        onItemSelected(user , activitySpinner);
        showProgressBar();
        String type = view.getId() == R.id.inBUTTON ? "IN" : "OUT";
        AttandanceTransaction t = buildAttandanceTransaction(type , user , Arrays.asList(cardId) , hhET.getText().toString()
                , mmET.getText().toString() , lm);
        saveAttandanceRequest = new SaveAttandanceRequest(t);
        spiceManager.execute(saveAttandanceRequest , new SaveAttandanceRequestListener(this));
        String msg = (view.getId() == R.id.inBUTTON ? "IN Time for " : "OUT Time for ") + cardId + " noted as " + hhET.getText() + ":" + mmET.getText();
        openDialog(msg);
        cardId = null;
    }

    public void getInfo(View view) {
        getInfoRequest = new GetInfoRequest("CARD_ID" , cardId , user.getUserId() , user.getPassword());
        spiceManager.execute(getInfoRequest, new GetInfoRequestListener(this));
    }

//    public AttandanceTransaction buildAttandanceTransaction(String type) {
//        AttandanceTransaction t = new AttandanceTransaction();
//        t.setAdminId(user.getUserId());
//        t.setCardId(Arrays.asList(cardId));
//        t.setDate(Util.convertDateToString(new Date()));
//        t.setTime(hhET.getText() + ":" + mmET.getText());
//        t.setOrgId(user.getCoId());
//        t.setType(type);
//        if(lm != null) {
//            location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            if(location != null) {
//                t.setLongitude(""+location.getLongitude());
//                t.setLatitude(""+location.getLatitude());
//            }
//        }
//        return t;
//    }
}
