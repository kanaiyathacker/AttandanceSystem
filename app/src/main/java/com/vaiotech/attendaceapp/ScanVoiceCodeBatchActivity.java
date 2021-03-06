package com.vaiotech.attendaceapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.bean.AttandanceTransaction;
import com.bean.User;
import com.google.gson.Gson;
import com.listener.SaveAttandanceRequestListener;
import com.services.GetServerTimeRequest;
import com.services.SaveAttandanceRequest;
import com.util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_scan_voice_code_batch)
public class ScanVoiceCodeBatchActivity extends BaseActivity {

    @InjectView(R.id.adminNameLableTV)
    TextView adminNameLableTV;
    @InjectView(R.id.adminValueLableTV) TextView adminValueLableTV;
    @InjectView(R.id.seperateTimeTV) TextView seperateTimeTV;
    @InjectView(R.id.seperateDateTV) TextView seperateDateTV;
    @InjectView(R.id.timeTV) TextView timeTV;
    @InjectView(R.id.hhET)
    EditText hhET;
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
    private SaveAttandanceRequest saveAttandanceRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_voice_code_batch);
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
    }

    public void save(View view) {
        onItemSelected(user , activitySpinner);
        showProgressBar();
        String type = view.getId() == R.id.inBUTTON ? "IN" : "OUT";
        AttandanceTransaction t = buildAttandanceTransaction(type , user , getCardIdList(scanSet) , hhET.getText().toString()
                , mmET.getText().toString() , lm);
        saveAttandanceRequest = new SaveAttandanceRequest(t , user.getUserId() , user.getPassword());
        spiceManager.execute(saveAttandanceRequest , new SaveAttandanceRequestListener(this));
        String msg = (view.getId() == R.id.inBUTTON ? "IN Time for " : "OUT Time for ") + counterValTV.getText() + "Users noted as " + hhET.getText() + ":" + mmET.getText();
        openDialog(msg);
        if(scanSet != null)
            scanSet.clear();
        counterValTV.setText("0");
    }

//    public AttandanceTransaction buildAttandanceTransaction(String type) {
////        SharedPreferences sharedPreferences = getSharedPreferences("DIGITAL_ATTENDANCE" , Context.MODE_PRIVATE);
////        String val = sharedPreferences.getString("USER_DETAILS" , null);
////        Gson gson = new Gson();
////        User user = gson.fromJson(val , User.class);
//
//        AttandanceTransaction t = new AttandanceTransaction();
//        t.setAdminId(user.getUserId());
//        t.setCardId(getCardIdList(scanSet));
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

    private List<String> getCardIdList(Set<User> scanSet) {
        List<String> retVal = new ArrayList<String>();
        for(User user : scanSet) {
            retVal.add(user.getCardId());
        }
        return retVal;
    }

    private void vibrate() {
        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE) ;
        vibe.vibrate(500);
    }
}
