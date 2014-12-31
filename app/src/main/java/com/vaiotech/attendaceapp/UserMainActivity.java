package com.vaiotech.attendaceapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bean.User;
import com.google.gson.Gson;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_user_main)
public class UserMainActivity extends BaseActivity {

    @InjectView(R.id.nameTV) TextView nameTV;
    @InjectView(R.id.idColonTV) TextView idColonTV;
    @InjectView(R.id.nameValueTV) TextView nameValueTV;
    @InjectView(R.id.orgNameTV) TextView orgNameTV;
    @InjectView(R.id.orgNameColonTV) TextView orgNameColonTV;
    @InjectView(R.id.orgNameValueTV) TextView orgNameValueTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Calibri.ttf");
        nameTV.setTypeface(font);
        idColonTV.setTypeface(font);
        nameValueTV.setTypeface(font);
        orgNameTV.setTypeface(font);
        orgNameColonTV.setTypeface(font);
        orgNameValueTV.setTypeface(font);
        SharedPreferences sharedPreferences = getSharedPreferences("DIGITAL_ATTENDANCE", Context.MODE_PRIVATE);
        String val = sharedPreferences.getString("USER_DETAILS" , null);
        Gson gson = new Gson();
        User user = gson.fromJson(val , User.class);
        String name = user.getfName() + " " + user.getlName();
        String orgName = user.getCoName();
        nameValueTV.setText(name);
        orgNameValueTV.setText(orgName);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void qrScan(View view) {
        Intent intent = new Intent(this , QRCodeGeneratorActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void voiceScan(View view) {
        Intent intent = new Intent(this , VoiceCodeGeneratorActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
