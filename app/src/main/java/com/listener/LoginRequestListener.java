package com.listener;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.TextView;

import com.bean.User;
import com.bean.UserMappingBean;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.vaiotech.attendaceapp.FunctionalMainActivity;
import com.vaiotech.attendaceapp.LoginActivity;
import com.vaiotech.attendaceapp.MainActivity;
import com.vaiotech.attendaceapp.MyProfileActivity;
import com.vaiotech.attendaceapp.R;
import com.vaiotech.attendaceapp.ViewReportActivity;

import java.util.ArrayList;
import java.util.List;

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
        TextView tv = (TextView)loginActivity.findViewById(R.id.loginIDTV);
        tv.setText(spiceException.getMessage());
    }


    @Override
    public void onRequestSuccess(Object o) {
        loginActivity.hideProgressBar();
        if(o != null) {
            LinkedTreeMap map = (LinkedTreeMap)o;
            Gson gson = new Gson();
            User user = new User();
            if(map.get("userId") != null) {
                String userId = map.get("userId").toString();
                user.setfName(map.get("fName") != null ? map.get("fName").toString() : "");
                user.setlName(map.get("lName") != null ? map.get("lName").toString() : "");
                user.setmName(map.get("mName") != null ? map.get("mName").toString() : "");

                user.setUserId(map.get("userId") != null ? map.get("userId").toString() : "");
                user.setStatus(map.get("userStatus") != null ? map.get("userStatus").toString() : "");
                user.setCoName(map.get("orgName") != null ? map.get("orgName").toString() : "");
                user.setCoId(map.get("orgId") != null ? map.get("orgId").toString() : "");
                user.setPassword(map.get("password") != null ? map.get("password").toString() : "");
                user.setCardId(map.get("cardId") != null ? map.get("cardId").toString() : "");

                user.setId(map.get("id").toString());

                user.setBranchs((java.util.Map<String, String>) map.get("branchs"));
                user.setDeparts((java.util.Map<String, String>) map.get("departs"));
                List<LinkedTreeMap> userMappingList = (List<LinkedTreeMap>) map.get("userMappingList");
                user.setUserMappingList(buildUserMappingList(userMappingList));
                String type = map.get("userType").toString();
                user.setType(type);
                Intent intent = getIntend(type);
                String userDetail = gson.toJson(user);
                //            intent.putExtra("USER_DETAILS", userDetail);
                SharedPreferences.Editor editor = loginActivity.getSharedPreferences("DIGITAL_ATTENDANCE", Context.MODE_PRIVATE).edit();
                editor.putString("USER_DETAILS", userDetail).apply();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                loginActivity.startActivity(intent);
            } else {
                String errorMsg = "Invalid Login ID OR Password";
                if(map.get("msgField") != null) {
                    String erroMsg = (String) map.get("msgField");
                    String[] split = erroMsg.split("-");
                    if(split.length == 2) {
                        errorMsg = split[1];
                    }
                }
                TextView errorTV = (TextView)loginActivity.findViewById(R.id.errorTV);
                errorTV.setText(errorMsg);
            }
        } else {
            TextView errorTV = (TextView)loginActivity.findViewById(R.id.errorTV);
            errorTV.setText("Invalid Login ID OR Password");
        }
    }

    private List<UserMappingBean> buildUserMappingList(List<LinkedTreeMap> userMappingList) {
        List<UserMappingBean> retVal = new ArrayList<UserMappingBean>();
        for(LinkedTreeMap curr : userMappingList) {
            String branchId = (String) curr.get("branchId");
            String departId = (String) curr.get("departId");
            retVal.add(new UserMappingBean(branchId , departId));
        }
        return retVal;
    }

    public Intent getIntend(String type) {
        Intent intent = null;
        if("0".equalsIgnoreCase(type)) {
            String activitySelected = loginActivity.getIntent().getStringExtra("ACTIVITY_SELECTED");
            if("SMART_SCAN".equals(activitySelected)) {
                intent = new Intent(loginActivity, MainActivity.class);
            } else if("MY_PROFILE".equals(activitySelected)) {
                intent = new Intent(loginActivity, MyProfileActivity.class);
            } else if("VIEW_REPORT".equals(activitySelected)) {
                intent = new Intent(loginActivity, ViewReportActivity.class);
            } else {
                intent = new Intent(loginActivity, FunctionalMainActivity.class);
            }
        } else {
            intent = new Intent(loginActivity, FunctionalMainActivity.class);
        }
        return intent;
    }
}
