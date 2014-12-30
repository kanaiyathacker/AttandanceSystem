package com.listener;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import com.bean.User;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.vaiotech.attendaceapp.FunctionalMainActivity;
import com.vaiotech.attendaceapp.MainActivity;
import com.vaiotech.attendaceapp.MyProfileActivity;


/**
 * Created by kanaiyalalt on 12/12/2014.
 */
public class ChangePasswordRequestListener implements RequestListener<Object> {

    private MyProfileActivity myProfileActivity;
    private SharedPreferences sharedPreferences;

    public ChangePasswordRequestListener(MyProfileActivity myProfileActivity) {
        this.myProfileActivity = myProfileActivity;
    }


    @Override
    public void onRequestFailure(SpiceException spiceException) {

    }

    @Override
    public void onRequestSuccess(Object o) {
        if(o != null) {
            LinkedTreeMap map = (LinkedTreeMap) o;
            Gson gson = new Gson();
            User user = new User();
            user.setfName(map.get("fName").toString());
            user.setlName(map.get("lName").toString());
            user.setmName(map.get("mName").toString());
            user.setUserId(map.get("userId").toString());
            user.setStatus(map.get("status").toString());
            user.setCoName(map.get("orgName").toString());
            user.setCoId(map.get("orgId").toString());
            user.setPassword(map.get("password").toString());
            user.setId(map.get("id").toString());
            String type = map.get("type").toString();
            user.setType(type);
            String userDetail = gson.toJson(user);

            SharedPreferences.Editor editor = myProfileActivity.getSharedPreferences("DIGITAL_ATTENDANCE", Context.MODE_PRIVATE).edit();
            editor.putString("USER_DETAILS", userDetail).apply();
            myProfileActivity.hideProgressBar();
            dialog("Password updated Successfully");
        }
    }

    public void dialog(String msg) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(myProfileActivity);

        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setPositiveButton("OK",

                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(myProfileActivity, FunctionalMainActivity.class);
                        myProfileActivity.startActivity(intent);
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}