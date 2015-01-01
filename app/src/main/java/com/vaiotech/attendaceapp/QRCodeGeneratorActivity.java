package com.vaiotech.attendaceapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bean.User;
import com.google.gson.Gson;
import com.services.GetServerTimeRequest;
import com.util.Util;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_qrcode_generator)
public class QRCodeGeneratorActivity extends BaseActivity {

    @InjectView(R.id.qrCodeimageView) ImageView qrCodeimageView;

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
    @InjectView(R.id.refreshButton) Button refreshButton;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_generator);
        new GetServerTimeRequest(hhET , mmET , ddET , MMET , yyET ).execute();
        SharedPreferences sharedPreferences = getSharedPreferences("DIGITAL_ATTENDANCE", Context.MODE_PRIVATE);
        String val = sharedPreferences.getString("USER_DETAILS" , null);
        Gson gson = new Gson();
        user = gson.fromJson(val , User.class);
        Calendar cal = Calendar.getInstance();
        long time = cal.getTimeInMillis();
        qrCodeimageView.setImageBitmap(Util.generateQRImage(user, time));

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Calibri.ttf");
        Typeface digital = Typeface.createFromAsset(getAssets(), "fonts/digital_7_mono.ttf");

        seperateTimeTV.setTypeface(digital);
        seperateDateTV.setTypeface(digital);
        seperateDateMMTV.setTypeface(digital);
        hhET.setTypeface(digital);
        mmET.setTypeface(digital);
        timeTV.setTypeface(digital);

        ddET.setTypeface(digital);
        MMET.setTypeface(digital);
        yyET.setTypeface(digital);
        dateTV.setTypeface(digital);
        refreshButton.setTypeface(font);
    }


    public void refresh(View view) {
        Calendar cal = Calendar.getInstance();
        long time = cal.getTimeInMillis();
        qrCodeimageView.setImageBitmap(Util.generateQRImage(user, time));
    }
}
