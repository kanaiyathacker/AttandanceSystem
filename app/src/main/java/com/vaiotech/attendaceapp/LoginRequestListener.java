package com.vaiotech.attendaceapp;

import android.content.Intent;

import com.bean.Admin;
import com.google.gson.Gson;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

/**
 * Created by kanaiyalalt on 14/11/2014.
 */
public class LoginRequestListener implements RequestListener<Object> {

    private LoginActivity loginActivity;

    public LoginRequestListener(LoginActivity loginActivity) {
        this.loginActivity = loginActivity;
    }

    @Override
    public void onRequestFailure(SpiceException spiceException) {

    }

    @Override
    public void onRequestSuccess(Object o) {
        Intent intent = new Intent(loginActivity,ScanActivity.class);
        Gson gson = new Gson();
        Admin admin = new Admin();
        admin.setfName("Admin");
        admin.setCoId("1212121");
        intent.putExtra("ADMIN_DETAILS" , gson.toJson(admin));
        loginActivity.startActivity(intent);
    }
}
