package com.vaiotech.attendaceapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bean.User;
import com.google.gson.Gson;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_my_profile)
public class MyProfileActivity extends BaseActivity implements View.OnKeyListener{

    @InjectView(R.id.idTV) TextView idTV;
    @InjectView(R.id.idValueTV) TextView idValueTV;

    @InjectView(R.id.nameTV) TextView nameTV;
    @InjectView(R.id.nameValueTV) TextView nameValueTV;

    @InjectView(R.id.orgTV) TextView orgTV;
    @InjectView(R.id.orgValueTV) TextView orgValueTV;

    @InjectView(R.id.statusTV) TextView statusTV;
    @InjectView(R.id.statusValueTV) TextView statusValueTV;

    @InjectView(R.id.existingPasswordTV) TextView existingPasswordTV;
    @InjectView(R.id.currentPasswordEV) EditText currentPasswordEV;

    @InjectView(R.id.newPasswordTV) TextView newPasswordTV;
    @InjectView(R.id.newPasswordEV) EditText newPasswordEV;

    @InjectView(R.id.retypePasswordTV) TextView retypePasswordTV;
    @InjectView(R.id.retypePasswordEV) EditText retypePasswordEV;
    @InjectView(R.id.changePasswordButton) Button changePasswordButton;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Calibri.ttf");

        idTV.setTypeface(font);
        idValueTV.setTypeface(font);
        nameTV.setTypeface(font);
        nameValueTV.setTypeface(font);
        orgTV.setTypeface(font);
        orgValueTV.setTypeface(font);
        statusTV.setTypeface(font);
        statusValueTV.setTypeface(font);
        existingPasswordTV.setTypeface(font);
        currentPasswordEV.setTypeface(font);
        newPasswordTV.setTypeface(font);
        newPasswordEV.setTypeface(font);
        retypePasswordTV.setTypeface(font);
        retypePasswordEV.setTypeface(font);

        sharedPreferences = getSharedPreferences("DIGITAL_ATTENDANCE" , Context.MODE_PRIVATE);
        String val = sharedPreferences.getString("USER_DETAILS" , null);
        Gson gson = new Gson();
        if(val != null) {
            User user = gson.fromJson(val, User.class);

            idValueTV.setText(user.getUserId());
            nameValueTV.setText(user.getfName() + " " + user.getmName() + " " + user.getlName());
            orgValueTV.setText(user.getCoName());
            statusValueTV.setText(user.getStatus());
        }

        currentPasswordEV.setOnKeyListener(this);
        newPasswordEV.setOnKeyListener(this);
        retypePasswordEV.setOnKeyListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_profile, menu);
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
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        System.out.println(currentPasswordEV.getText().length());
        System.out.println(newPasswordEV.getText().length());
        System.out.println(retypePasswordEV.getText().length());

        boolean enableButton = currentPasswordEV.getText().length() > 0 && newPasswordEV.getText().length() > 0 && retypePasswordEV.getText().length() > 0;
        enableButton = enableButton ? newPasswordEV.getText().toString().equals(retypePasswordEV.getText().toString()) : enableButton;
        changePasswordButton.setEnabled(enableButton);

        return false;
    }
}
