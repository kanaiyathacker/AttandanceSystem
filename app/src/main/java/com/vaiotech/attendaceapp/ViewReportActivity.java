package com.vaiotech.attendaceapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bean.User;
import com.google.gson.Gson;
import com.listener.SendMessageRequestListener;
import com.listener.ViewAbsenteeDetailsRequestListener;
import com.listener.ViewReportRequestListener;
import com.services.SendMessageRequest;
import com.services.ViewAbsenteeDetailsRequest;
import com.services.ViewReportRequest;
import com.util.Util;

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
    @InjectView(R.id.absentResultLV) ListView absentResultLV;

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
        buildActivitySpinner(user , activitySpinner);
        activitySpinner.setOnItemSelectedListener(this);
    }

    public void viewAbsenteeDetails(View view) {
        if(!Util.isEmpty(searchType) && !Util.isEmpty(searchId)) {
            showProgressBar();
            viewAbsenteeDetailsRequest = new ViewAbsenteeDetailsRequest(searchType, searchId , user.getUserId() , user.getPassword());
            spiceManager.execute(viewAbsenteeDetailsRequest, new ViewAbsenteeDetailsRequestListener(this));
        }
    }

    public void sendMessage(View view) {
        showProgressBar();
        sendMessageRequest = new SendMessageRequest(user.getUserId() , user.getUserId() , user.getPassword());
        spiceManager.execute(sendMessageRequest ,new SendMessageRequestListener(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Map<String, String> branches = user.getBranchs();
        Map<String, String> departs = user.getDeparts();
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
            viewReportRequest = new ViewReportRequest(searchType, key , user.getUserId() , user.getPassword());
            spiceManager.execute(viewReportRequest, new ViewReportRequestListener(this));
        } else {
            this.searchType =  null;
            this.searchId = null;
            totalStrengthValueTV.setText("0");
            absentValueTV.setText("0");
        }
        ArrayAdapter adapter = (ArrayAdapter) absentResultLV.getAdapter();
        if (adapter != null){
            adapter.clear();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}