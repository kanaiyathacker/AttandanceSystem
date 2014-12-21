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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bean.User;
import com.google.gson.Gson;
import com.listener.GetUserAbsenteeRequestListener;
import com.listener.ViewAbsenteeDetailsRequestListener;
import com.octo.android.robospice.SpiceManager;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;
import com.services.AttandanceRestService;
import com.services.GetUserAbsenteeRequest;
import com.services.ViewReportRequest;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class ViewUserReportActivity extends FragmentActivity {

    private GetUserAbsenteeRequest getUserAbsenteeRequest;
    private CaldroidFragment caldroidFragment;
    private SpiceManager spiceManager = new SpiceManager(AttandanceRestService.class);
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_report);
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
        getUserAbsenteeRequest = new GetUserAbsenteeRequest(user.getUserId() , String.valueOf((cal.get(Calendar.MONTH) + 1)));
        caldroidFragment.setCaldroidListener(new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {

            }

            @Override
            public void onChangeMonth(int month, int year) {
                String text = "month: " + month + " year: " + year;
                Toast.makeText(getApplicationContext(), text,
                        Toast.LENGTH_SHORT).show();
                getUserAbsenteeRequest = new GetUserAbsenteeRequest(user.getUserId() , String.valueOf(month));
                spiceManager.execute(getUserAbsenteeRequest ,new GetUserAbsenteeRequestListener(caldroidFragment));
            }

        });
//        HashMap<Date, Integer> mapVal = new HashMap<java.util.Date, Integer>();
//        mapVal.put(new Date("11/11/2014"), R.color.event_color_02);
//        caldroidFragment.setBackgroundResourceForDates(mapVal);
//        caldroidFragment.refreshView();


//        caldroidFragment.setBackgroundResourceForDate("#1C5EBB", greenDate);
//        caldroidFragment.setTextColorForDate(R.color.white, greenDate);

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.fraContainer, caldroidFragment);
        t.commit();
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

}
