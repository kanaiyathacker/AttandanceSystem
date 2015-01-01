package com.vaiotech.attendaceapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
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
import com.listener.SaveAttandanceRequestListener;
import com.services.GetServerTimeRequest;
import com.services.SaveAttandanceRequest;
import com.util.Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_scan_qrcode_batch)
public class ScanQRCodeBatchActivity extends BaseActivity implements DialogScanner.OnQRCodeScanListener , AdapterView.OnItemSelectedListener{

    private static final String TAG = ScanQRCodeBatchActivity.class.getName();

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
    @InjectView(R.id.counterLableTV) TextView counterLableTV;

    @InjectView(R.id.counterValTV) TextView counterValTV;
    @InjectView(R.id.inBUTTON) Button inBUTTON;
    @InjectView(R.id.outBUTTON) Button outBUTTON;
    @InjectView(R.id.activitySpinner)
    Spinner activitySpinner;
    private Set<User> scanSet;
    private User user;
    private LocationManager lm;
    private Location location;
    private SaveAttandanceRequest saveAttandanceRequest;

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String selItem = (String) activitySpinner.getSelectedItem();
        if(!selItem.equals("Select") && scanSet!= null &&  scanSet.size() > 0) {
            counterValTV.setText("" + scanSet.size());
            inBUTTON.setEnabled(true);
            outBUTTON.setEnabled(true);
            inBUTTON.setAlpha(1f);
            outBUTTON.setAlpha(1f);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qrcode_batch);
        new GetServerTimeRequest(hhET , mmET , ddET , MMET , yyET ).execute();

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Calibri.ttf");
        Typeface digital = Typeface.createFromAsset(getAssets(), "fonts/digital_7_mono.ttf");

        seperateTimeTV.setTypeface(digital);
        seperateDateTV.setTypeface(digital);
        hhET.setTypeface(digital);
        mmET.setTypeface(digital);
        timeTV.setTypeface(digital);
        counterLableTV.setTypeface(digital);

        ddET.setTypeface(digital);
        MMET.setTypeface(digital);
        yyET.setTypeface(digital);
        dateTV.setTypeface(digital);
        seperateDateMMTV.setTypeface(digital);

        counterValTV.setTypeface(digital);

        inBUTTON.setTypeface(font);
        outBUTTON.setTypeface(font);

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
        buildActivitySpinner(user , activitySpinner);
        activitySpinner.setOnItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new  HeavyTask(this).execute();
    }

    private class HeavyTask extends AsyncTask<String, Void, Void> {

        private ScanQRCodeBatchActivity scanQRCodeBatchActivity;
        private DialogScanner dialog;
        public HeavyTask(ScanQRCodeBatchActivity scanQRCodeBatchActivity) {
            this.scanQRCodeBatchActivity = scanQRCodeBatchActivity;
        }

        protected Void doInBackground(String... args) {
            dialog = DialogScanner.newInstance(CameraFace.BACK , 1 , null , Typeface.createFromAsset(getAssets(), "fonts/digital_7_mono.ttf"));
            dialog.show(scanQRCodeBatchActivity.getFragmentManager(), "cameraPreview");
            return null;
        }

        protected void onPostExecute(Void results) {
//            dialog.show(scanQRCodeBatchActivity.getFragmentManager(), "cameraPreview");
            hideProgressBar();
        }

    }


    @Override
    public void onQRCodeScan(Object contents) {
        String selItem = (String) activitySpinner.getSelectedItem();
        if (contents != null) {
            scanSet = (Set)contents;
            int size = scanSet.size();
            if(!selItem.equals("Select") && size > 0) {
                counterValTV.setText("" + size);
                inBUTTON.setEnabled(true);
                outBUTTON.setEnabled(true);
                inBUTTON.setAlpha(1f);
                outBUTTON.setAlpha(1f);
            }
        }
    }

    public void save(View view) {
        showProgressBar();
        String type = view.getId() == R.id.inBUTTON ? "IN" : "OUT";
        saveAttandanceRequest = new SaveAttandanceRequest(buildAttandanceTransaction(type));
        spiceManager.execute(saveAttandanceRequest , new SaveAttandanceRequestListener(this));
        String msg = (view.getId() == R.id.inBUTTON ? "IN Time for " : "OUT Time for ") + counterValTV.getText() + "Users noted as " + hhET.getText() + ":" + mmET.getText();
        openDialog(msg);
        if(scanSet != null)
            scanSet.clear();
        counterValTV.setText("0");
    }

    public AttandanceTransaction buildAttandanceTransaction(String type) {
        AttandanceTransaction t = new AttandanceTransaction();
        t.setAdminId(user.getUserId());
        t.setCardId(getCardIdList(scanSet));
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

    private List<String> getCardIdList(Set<User> scanSet) {
        List<String> retVal = new ArrayList<String>();
        for(User user : scanSet) {
            retVal.add(user.getCardId());
        }
        return retVal;
    }

    private void vibrate() {
        Log.d(TAG, "vibrate");

        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE) ;
        vibe.vibrate(500);
    }
}
