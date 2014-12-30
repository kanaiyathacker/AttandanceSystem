package com.vaiotech.attendaceapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.bean.User;
import com.bean.UserMappingBean;
import com.google.gson.Gson;
import com.listener.GetUserAbsenteeRequestListener;
import com.listener.ViewAbsenteeDetailsRequestListener;
import com.listener.ViewReportRequestListener;
import com.octo.android.robospice.SpiceManager;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;
import com.services.AttandanceRestService;
import com.services.GetUserAbsenteeRequest;
import com.services.ViewReportRequest;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import roboguice.inject.InjectView;


public class ViewUserReportActivity extends FragmentActivity implements AdapterView.OnItemSelectedListener {

    private GetUserAbsenteeRequest getUserAbsenteeRequest;
    private CaldroidFragment caldroidFragment;
    private SpiceManager spiceManager = new SpiceManager(AttandanceRestService.class);
    private User user;
    private Spinner activitySpinner;
    private String searchType;
    private String searchId;
    private String monthSel;
    private String yearSel;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_report);
        this.context = this;
        activitySpinner = (Spinner) findViewById(R.id.activitySpinner);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        args.putBoolean(CaldroidFragment.ENABLE_CLICK_ON_DISABLED_DATES, true);
        SharedPreferences sharedPreferences = getSharedPreferences("DIGITAL_ATTENDANCE", Context.MODE_PRIVATE);
        String val = sharedPreferences.getString("USER_DETAILS" , null);
        Gson gson = new Gson();
        user = gson.fromJson(val , User.class);
        caldroidFragment.setArguments(args);
        caldroidFragment.setCaldroidListener(new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {

            }

            @Override
            public void onChangeMonth(int month, int year) {
                String monthStr  = month < 10 ? "0"+month : ""+month;
                String text = "month: " + monthStr + " : " + user.getCardId();
                Toast.makeText(getApplicationContext(), text,
                        Toast.LENGTH_SHORT).show();

                monthSel = monthStr;
                yearSel = ""+year;
                if(searchType != null && searchId != null) {
                    getUserAbsenteeRequest = new GetUserAbsenteeRequest(searchType , searchId , user.getCardId(), monthStr, "" + year);
                    spiceManager.execute(getUserAbsenteeRequest, new GetUserAbsenteeRequestListener(caldroidFragment));
                }
            }
        });
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.fraContainer, caldroidFragment);
        t.commit();
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
            list.add("Add Org");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, list);
        activitySpinner.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        spiceManager.shouldStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        spiceManager.start(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_user_report, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
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
                key = "";
                searchType = "ORG";
            }
            this.searchType =  searchType;
            this.searchId = key;

            getUserAbsenteeRequest = new GetUserAbsenteeRequest(searchType , searchId ,user.getCardId() , monthSel , ""+ yearSel);
            spiceManager.execute(getUserAbsenteeRequest ,new GetUserAbsenteeRequestListener(caldroidFragment));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
