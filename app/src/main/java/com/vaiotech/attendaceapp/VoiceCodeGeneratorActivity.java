package com.vaiotech.attendaceapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bean.User;
import com.google.gson.Gson;
import com.services.GetServerTimeRequest;
import com.util.Util;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_voice_code_generator)
public class VoiceCodeGeneratorActivity extends BaseActivity {

    @InjectView(R.id.seperateTimeTV) TextView seperateTimeTV;
    @InjectView(R.id.seperateDateTV) TextView seperateDateTV;
    @InjectView(R.id.seperateDateMMTV) TextView seperateDateMMTV;
    @InjectView(R.id.timeTV) TextView timeTV;
    @InjectView(R.id.hhET)
    EditText hhET;
    @InjectView(R.id.mmET) EditText mmET;
    @InjectView(R.id.dateTV) EditText dateTV;
    @InjectView(R.id.ddET) EditText ddET;
    @InjectView(R.id.MMET) EditText MMET;
    @InjectView(R.id.yyET) EditText yyET;
    @InjectView(R.id.refreshButton) Button refreshButton;
    private User user;
    private ProgressBar progressBar ;
    private TextView progressDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_code_generator);
        new GetServerTimeRequest(hhET , mmET , ddET , MMET , yyET ).execute();
        SharedPreferences sharedPreferences = getSharedPreferences("DIGITAL_ATTENDANCE", Context.MODE_PRIVATE);
        String val = sharedPreferences.getString("USER_DETAILS" , null);
        Gson gson = new Gson();
        user = gson.fromJson(val , User.class);
        Calendar cal = Calendar.getInstance();
        long time = cal.getTimeInMillis();

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Calibri.ttf");
        Typeface digital = Typeface.createFromAsset(getAssets(), "fonts/digital_7_mono.ttf");

        seperateTimeTV.setTypeface(digital);
        seperateDateTV.setTypeface(digital);
        seperateDateMMTV.setTypeface(digital);
        hhET.setTypeface(digital);
        mmET.setTypeface(digital);
        timeTV.setTypeface(digital);

        ddET.setTypeface(digital);
        MMET.setTypeface(digital);
        yyET.setTypeface(digital);
        dateTV.setTypeface(digital);
        refreshButton.setTypeface(font);

        init();
        initData();
    }


    /**
     * Initialize UI view components
     */
    private void init() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressDisplay = (TextView) findViewById(R.id.progressDisplay);

    }

    /**
     * Initialize data || start actions
     */
    private void initData() {

        new ShowCustomProgressBarAsyncTask().execute();

    }

    /**
     * Progress bar increment and display current state
     */
    public class ShowCustomProgressBarAsyncTask extends AsyncTask<Void, Integer, Void> {

        int myProgress;

        @Override
        protected void onPreExecute() {
            myProgress = 0;
        }

        @Override
        protected Void doInBackground(Void... params) {
            while(myProgress<100){
                myProgress++;
                publishProgress(myProgress);
                SystemClock.sleep(5);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
            progressBar.setSecondaryProgress(values[0] + 1);
            progressDisplay.setText(String.valueOf(myProgress)+"%");
        }

        @Override
        protected void onPostExecute(Void result) {

        }
    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_voice_code_generator, menu);
//        menu.getItem(0).setTitle(isLogin ? "Log Out" : "Log In");
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if ("Log Out" == item.getTitle()) {
//            sharedPreferences.edit().remove("USER_DETAILS").commit();
//            isUserLogedIn();
//            item.setTitle("Log In");
//        } else {
//            Intent intent = new Intent(this , LoginActivity.class);
//            startActivity(intent);
//        }
//        return super.onOptionsItemSelected(item);
//    }

    public void refresh(View view) {
        initData();

    }
}
