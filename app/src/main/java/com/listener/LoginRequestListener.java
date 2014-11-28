package com.listener;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.bean.Admin;
import com.bean.User;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.vaiotech.attendaceapp.LoginActivity;
import com.vaiotech.attendaceapp.MainActivity;

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
//        Intent intent = new Intent(loginActivity,MainActivity.class);
//        Gson gson = new Gson();
//        Admin admin = new Admin();
//        admin.setfName("Admin");
//        admin.setCoId("1212121");
//        intent.putExtra("ADMIN_DETAILS" , gson.toJson(admin));
//        loginActivity.startActivity(intent);
        if(o != null) {
            LinkedTreeMap map = (LinkedTreeMap)o;
            Gson gson = new Gson();
            User user = new User();
            user.setfName(map.get("fName").toString());
            user.setlName(map.get("lName").toString());
            user.setmName(map.get("mName").toString());
            user.setId(map.get("id").toString());
            String type = map.get("type").toString();
            user.setType(type);
            Intent intent = getIntend(type);
            String userDetail = gson.toJson(user);
            intent.putExtra("USER_DETAILS", userDetail);
            SharedPreferences.Editor editor = loginActivity.getSharedPreferences("DIGITAL_ATTENDANCE" , Context.MODE_PRIVATE).edit();
            editor.putString("USER_DETAILS" , userDetail).apply();
            loginActivity.startActivity(intent);
        }
    }

    public Intent getIntend(String type) {
        Intent intent = null;
        if(type.equalsIgnoreCase("ADMIN")) {
            intent = new Intent(loginActivity,MainActivity.class);
        } else {

        }
        return intent;
    }
}
