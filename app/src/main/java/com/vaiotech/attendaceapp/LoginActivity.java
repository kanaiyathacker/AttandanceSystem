package com.vaiotech.attendaceapp;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.listener.LoginRequestListener;
import com.services.LoginRequest;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_login)
public class LoginActivity  extends BaseActivity {

    @InjectView(R.id.loginIDTV)   TextView loginIDTV;
    @InjectView(R.id.loginIDET)   EditText loginIDET;
    @InjectView(R.id.passwordTV)  TextView passwordTV;
    @InjectView(R.id.passwordET)  EditText passwordET;
    @InjectView(R.id.loginBUTTON) Button loginBUTTON;

    private LoginRequest loginRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Calibri.ttf");
        loginIDTV.setTypeface(font);
        loginIDET.setTypeface(font);
        passwordTV.setTypeface(font);
        passwordET.setTypeface(font);
        loginBUTTON.setTypeface(font);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    public void login(View view) {
        loginRequest = new LoginRequest(loginIDET.getText().toString() , passwordET.getText().toString() , "DEV ID");
        spiceManager.execute(loginRequest ,new LoginRequestListener(this));
    }
}
