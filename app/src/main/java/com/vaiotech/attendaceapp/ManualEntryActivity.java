package com.vaiotech.attendaceapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

import static com.util.Util.getEditViewText;

@ContentView(R.layout.activity_manual_entry)
public class ManualEntryActivity extends BaseActivity implements View.OnKeyListener{

    @InjectView(R.id.idLableTV) TextView idLableTV;
    @InjectView(R.id.userIdValueET) EditText userIdValueET;

    @InjectView(R.id.adminNameLableTV) TextView adminNameLableTV;
    @InjectView(R.id.adminValueLableTV) TextView adminValueLableTV;

    @InjectView(R.id.userNameLableTV) TextView userNameLableTV;
    @InjectView(R.id.userNameValueTV) TextView userNameValueTV;


    @InjectView(R.id.seperateTimeTV) TextView seperateTimeTV;
    @InjectView(R.id.timeTV) TextView timeTV;
    @InjectView(R.id.hhET) EditText hhET;
    @InjectView(R.id.mmET) EditText mmET;

    @InjectView(R.id.dateTV) TextView dateTV;
    @InjectView(R.id.ddET) EditText ddET;
    @InjectView(R.id.MMET) EditText MMET;
    @InjectView(R.id.yyET) EditText yyET;
    @InjectView(R.id.inBUTTON) Button inBUTTON;

    @InjectView(R.id.outBUTTON) Button outBUTTON;
    @InjectView(R.id.getInfoBUTTON) Button getInfoBUTTON;

    private SaveAttandanceRequest saveAttandanceRequest;
    private GetInfoRequest getInfoRequest;
    private LocationManager lm;
    private Location location;
    private User user;
    private String cardId;

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_entry);
        new GetServerTimeRequest(hhET , mmET , ddET , MMET , yyET ).execute();
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Calibri.ttf");

        idLableTV.setTypeface(font);
        userIdValueET.setTypeface(font);
        userIdValueET.requestFocus();

        adminNameLableTV.setTypeface(font);
        adminValueLableTV.setTypeface(font);

        userNameLableTV.setTypeface(font);
        userNameValueTV.setTypeface(font);


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
        userIdValueET.setOnKeyListener(this);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(userIdValueET, InputMethodManager.SHOW_IMPLICIT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    public AttandanceTransaction buildAttandanceTransaction(String type) {
        AttandanceTransaction t = new AttandanceTransaction();
        t.setAdminId(user.getUserId());
        t.setDate(Util.convertDateToString(new Date()));
        t.setTime(hhET.getText() + ":" + mmET.getText());
        t.setOrgId(user.getCoId());
        t.setType(type);
        t.setCardId(Arrays.asList(getCardId()));
        if(lm != null) {
            location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location != null) {
                t.setLongitude(""+location.getLongitude());
                t.setLatitude(""+location.getLatitude());
            }
        }
        return t;
    }

    public void save(View view) {
        showProgressBar();
        String type = view.getId() == R.id.inBUTTON ? "IN" : "OUT";
        saveAttandanceRequest = new SaveAttandanceRequest(buildAttandanceTransaction(type) , user.getUserId() , user.getPassword());
        spiceManager.execute(saveAttandanceRequest , new SaveAttandanceRequestListener(this));
        String msg = (view.getId() == R.id.inBUTTON ? "IN Time for " : "OUT Time for ") + userIdValueET.getText() + " noted as " + hhET.getText() + ":" + mmET.getText();
        openDialog(msg);
        cardId = null;
    }

    public void getInfo(View view) {
        showProgressBar();
        getInfoRequest = new GetInfoRequest("USER_ID" , getEditViewText(userIdValueET) , user.getUserId() , user.getPassword());
        spiceManager.execute(getInfoRequest, new GetInfoRequestListener(this));
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        boolean enableButton = getEditViewText(userIdValueET).length() > 0 ;
        if(enableButton) {
            getInfoBUTTON.setEnabled(true);
            getInfoBUTTON.setAlpha(1f);
        } else {
            inBUTTON.setEnabled(false);
            outBUTTON.setEnabled(false);
            getInfoBUTTON.setEnabled(false);

            inBUTTON.setAlpha(.5f);
            outBUTTON.setAlpha(.5f);
            getInfoBUTTON.setAlpha(.5f);
        }
        return false;
    }
}
