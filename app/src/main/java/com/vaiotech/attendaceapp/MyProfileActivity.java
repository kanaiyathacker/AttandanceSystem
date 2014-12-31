package com.vaiotech.attendaceapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.bean.ChangePassword;
import com.bean.User;
import com.google.gson.Gson;
import com.listener.ChangePasswordRequestListener;
import com.services.ChangePasswordRequest;
import com.services.LoginRequest;
import com.util.Util;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import static com.util.Util.getEditViewText;

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
    private User user;

    private ChangePasswordRequest changePasswordRequest;

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

        changePasswordButton.setAlpha(.5f);
        changePasswordButton.setEnabled(false);

        sharedPreferences = getSharedPreferences("DIGITAL_ATTENDANCE" , Context.MODE_PRIVATE);
        String val = sharedPreferences.getString("USER_DETAILS" , null);
        Gson gson = new Gson();
        if(val != null) {
             user = gson.fromJson(val, User.class);

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
    public boolean onOptionsItemSelected(MenuItem item) {
        if ("Log Out" == item.getTitle()) {
            sharedPreferences.edit().remove("USER_DETAILS").commit();
            isUserLogedIn();
            item.setTitle("Log In");
        } else {
            Intent intent = new Intent(this , LoginActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_functional_main, menu);
        menu.getItem(0).setTitle(isLogin ? "Log Out" : "Log In");
        return true;
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {

        boolean enableButton = currentPasswordEV.getText().length() > 0 && newPasswordEV.getText().length() > 0 && retypePasswordEV.getText().length() > 0;
        enableButton = enableButton ? newPasswordEV.getText().toString().equals(retypePasswordEV.getText().toString()) : enableButton;
        changePasswordButton.setEnabled(enableButton);
        changePasswordButton.setAlpha(enableButton ? 1f : 0.5f);

        return false;
    }

    public void save(View view) {
        String password = user.getPassword();
        if(!password.equals(Util.getEditViewText(currentPasswordEV))) {
            dialog(" Wrong current password " + Util.getEditViewText(currentPasswordEV));
        } else {
            showProgressBar();
            changePasswordRequest = new ChangePasswordRequest(new ChangePassword(user.getUserId() , getEditViewText(currentPasswordEV) , getEditViewText(newPasswordEV) , getEditViewText(retypePasswordEV)));
            spiceManager.execute(changePasswordRequest , new ChangePasswordRequestListener(this));
        }
    }



    public void dialog(String msg) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setPositiveButton("OK",

                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
