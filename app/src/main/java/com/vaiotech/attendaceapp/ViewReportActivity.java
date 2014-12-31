package com.vaiotech.attendaceapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.bean.User;
import com.bean.UserMappingBean;
import com.google.gson.Gson;
import com.listener.SendMessageRequestListener;
import com.listener.ViewAbsenteeDetailsRequestListener;
import com.listener.ViewReportRequestListener;
import com.services.SendMessageRequest;
import com.services.ViewAbsenteeDetailsRequest;
import com.services.ViewReportRequest;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_view_report)
public class ViewReportActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {

    @InjectView(R.id.vadButton) Button vadButton;
    @InjectView(R.id.sendMSGButton) Button sendMSGButton;
    @InjectView(R.id.totalStrengthTV)   TextView totalStrengthTV;
    @InjectView(R.id.totalStrengthValueTV)   TextView totalStrengthValueTV;

    @InjectView(R.id.absentTV)   TextView absentTV;
    @InjectView(R.id.absentValueTV)   TextView absentValueTV;
    @InjectView(R.id.activityTV)   TextView activityTV;
    @InjectView(R.id.activityColonTV)   TextView activityColonTV;
    @InjectView(R.id.totalStrengthColonTV)   TextView totalStrengthColonTV;
    @InjectView(R.id.absentColonTV)   TextView absentColonTV;
    @InjectView(R.id.activitySpinner) Spinner activitySpinner;

    private SendMessageRequest sendMessageRequest;
    private ViewReportRequest viewReportRequest;
    private ViewAbsenteeDetailsRequest viewAbsenteeDetailsRequest;
    private SharedPreferences sharedPreferences;
    private User user;
    private String searchType;
    private String searchId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Calibri.ttf");
        Typeface digital = Typeface.createFromAsset(getAssets(), "fonts/digital_7_mono.ttf");

        totalStrengthValueTV.setTypeface(digital);
        absentValueTV.setTypeface(digital);


        vadButton.setTypeface(font);
        sendMSGButton.setTypeface(font);
        totalStrengthTV.setTypeface(font);
        absentTV.setTypeface(font);
        activityTV.setTypeface(font);
        activityColonTV.setTypeface(font);
        totalStrengthColonTV.setTypeface(font);
        absentColonTV.setTypeface(font);

        sharedPreferences = getSharedPreferences("DIGITAL_ATTENDANCE" , Context.MODE_PRIVATE);
        String val = sharedPreferences.getString("USER_DETAILS" , null);

        Gson gson = new Gson();
        user = gson.fromJson(val , User.class);
        buildActivitySpinner();
        activitySpinner.setOnItemSelectedListener(this);
    }

    private void buildActivitySpinner() {
        List<UserMappingBean> userMappingList = user.getUserMappingList();
        List<String> list = new LinkedList<String>();
        list.add("Select");

        Map<String, String> branches = user.getBranchs();
        Map<String, String> departs = user.getDeparts();

        for(UserMappingBean curr : userMappingList) {
            String branch = curr.getBranchId();
            String depart = curr.getDepartId();
            if(branch != null && depart != null && branch.length()> 0 && depart.length() > 0) {
                String val = branches.get(branch) + " - " + departs.get(depart);
                list.add(val);
            }
            if(branch != null && depart.length() > 0 && depart == null ) {
                String val = branches.get(branch);
                list.add(val);
            }
        }
        if(list.isEmpty()) {
            list.add(user.getCoName());
        }
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//                this, android.R.layout.simple_spinner_item, list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, R.layout.spinner_text, list);
        activitySpinner.setAdapter(adapter);
        if(list.size() == 2) {
            activitySpinner.setSelection(1);
        }
    }

    public void viewAbsenteeDetails(View view) {
        showProgressBar();
        viewAbsenteeDetailsRequest = new ViewAbsenteeDetailsRequest(searchType , searchId);
        spiceManager.execute(viewAbsenteeDetailsRequest ,new ViewAbsenteeDetailsRequestListener(this));
    }

    public void sendMessage(View view) {
        showProgressBar();
        sendMessageRequest = new SendMessageRequest(user.getUserId());
        spiceManager.execute(sendMessageRequest ,new SendMessageRequestListener(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        System.out.println(i);
        Map<String, String> branches = user.getBranchs();
        Map<String, String> departs = user.getDeparts();
        System.out.println(activitySpinner.getSelectedItem());
        String selItem = (String) activitySpinner.getSelectedItem();
        if(!selItem.equals("Select")) {
            String[] split = selItem.split(" - ");
            String searchType = null;
            String key = null;
            if (split.length > 1) {
                for (String val : departs.keySet()) {
                    if(departs.get(val).equalsIgnoreCase(split[1])) {
                        key = val;
                        searchType = "DEPART";
                        break;
                    }
                }
            } else {
                for (String val : branches.keySet()) {
                    if(branches.get(val).equalsIgnoreCase(split[1])) {
                        key = val;
                        searchType = "BRANCH";
                        break;
                    }
                }
            }
            if (key == null) {
                // set the org id
                key = user.getCoId();
                searchType = "ORG";
            }
            this.searchType =  searchType;
            this.searchId = key;
            showProgressBar();
            viewReportRequest = new ViewReportRequest(searchType, key);
            spiceManager.execute(viewReportRequest, new ViewReportRequestListener(this));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_report, menu);
        menu.getItem(0).setTitle(isLogin ? "Log Out" : "Log In");
        return true;
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
}