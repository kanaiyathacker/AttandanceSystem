package com.vaiotech.attendaceapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bean.User;
import com.bean.ViewReport;
import com.google.gson.Gson;
import com.listcomponent.Item;
import com.listcomponent.ItemAdapter;
import com.listener.LoginRequestListener;
import com.listener.SendMessageRequestListener;
import com.listener.ViewAbsenteeDetailsRequestListener;
import com.listener.ViewReportRequestListener;
import com.services.LoginRequest;
import com.services.SendMessageRequest;
import com.services.ViewAbsenteeDetailsRequest;
import com.services.ViewReportRequest;

import java.util.ArrayList;
import java.util.List;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_view_report)
public class ViewReportActivity extends BaseActivity {

    @InjectView(R.id.vadButton) Button vadButton;
    @InjectView(R.id.totalStrengthValueTV)   TextView totalStrengthValueTV;
    @InjectView(R.id.absentValueTV)   TextView absentValueTV;

    private SendMessageRequest sendMessageRequest;
    private ViewReportRequest viewReportRequest;
    private ViewAbsenteeDetailsRequest viewAbsenteeDetailsRequest;
    private SharedPreferences sharedPreferences;
    private User user;
    private ProgressDialog progressDialog;

    public ProgressDialog getProgressDialog() {
        return progressDialog;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Calibri.ttf");
        Typeface digital = Typeface.createFromAsset(getAssets(), "fonts/digital_7_mono.ttf");

        totalStrengthValueTV.setTypeface(digital);
        absentValueTV.setTypeface(digital);


        sharedPreferences = getSharedPreferences("DIGITAL_ATTENDANCE" , Context.MODE_PRIVATE);
        String val = sharedPreferences.getString("USER_DETAILS" , null);
        Gson gson = new Gson();
        user = gson.fromJson(val , User.class);
        viewReportRequest = new ViewReportRequest(user.getUserId() , user.getCoId());
        progressDialog = new ProgressDialog(this);
    }

    public void viewAbsenteeDetails(View view) {
        progressDialog.show();
        viewAbsenteeDetailsRequest = new ViewAbsenteeDetailsRequest(user.getUserId() , user.getCoId());
        spiceManager.execute(viewAbsenteeDetailsRequest ,new ViewAbsenteeDetailsRequestListener(this));
    }

    public void sendMessage(View view) {
        progressDialog.show();
        sendMessageRequest = new SendMessageRequest(user.getUserId());
        spiceManager.execute(sendMessageRequest ,new SendMessageRequestListener(this));
    }




    @Override
    protected void onStart() {
        super.onStart();
        spiceManager.execute(viewReportRequest ,new ViewReportRequestListener(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_report, menu);
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
