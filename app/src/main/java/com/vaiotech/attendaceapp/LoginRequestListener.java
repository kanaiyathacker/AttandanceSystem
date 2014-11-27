package com.vaiotech.attendaceapp;

import android.content.Intent;

import com.bean.Admin;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
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
//        Intent intent = new Intent(loginActivity,ScanActivity.class);
//        Intent intent = new Intent(loginActivity,TabActivity.class);
        Intent intent = new Intent(loginActivity,MainActivity.class);
        Gson gson = new Gson();
        Admin admin = new Admin();
        admin.setfName("Admin");
        admin.setCoId("1212121");
        intent.putExtra("ADMIN_DETAILS" , gson.toJson(admin));
        loginActivity.startActivity(intent);
//        if(o != null) {
//            LinkedTreeMap map = (LinkedTreeMap)o;
//            Gson gson = new Gson();
//            Admin admin = new Admin();
//            admin.setfName(map.get("fName").toString());
//            admin.setlName(map.get("lName").toString());
//            admin.setmName(map.get("mName").toString());
//            admin.setfName(map.get("fName").toString());
//            admin.setCoId(map.get("coId").toString());
//            admin.setCoName(map.get("coName").toString());
//            intent.putExtra("ADMIN_DETAILS", gson.toJson(admin));
//            loginActivity.startActivity(intent);
//        }
    }
}
