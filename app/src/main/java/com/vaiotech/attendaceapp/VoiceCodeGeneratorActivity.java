package com.vaiotech.attendaceapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bean.User;
import com.google.gson.Gson;
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
    @InjectView(R.id.hhET) TextView hhET;
    @InjectView(R.id.mmET) TextView mmET;
    @InjectView(R.id.dateTV) TextView dateTV;
    @InjectView(R.id.ddET) TextView ddET;
    @InjectView(R.id.MMET) TextView MMET;
    @InjectView(R.id.yyET) TextView yyET;
    @InjectView(R.id.refreshButton) Button refreshButton;
    private User user;
    private ProgressBar progressBar ;
    private TextView progressDisplay;

    private class PushRequest extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... server) {
            String result = null;
            try {
                result = Util.query("3.in.pool.ntp.org");
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        protected void onPostExecute(String result) {
            Date dateTime = Util.convertStringToDate(result, Util.NTC_DATETIME_FORMAT);
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateTime);
            cal.set(Calendar.AM_PM, Calendar.PM);
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int min = cal.get(Calendar.MINUTE);
            hhET.setText(""+hour);
            mmET.setText(""+(min < 10 ? "0"+ min : min));

            int date = cal.get(Calendar.DATE);
            int month = cal.get(Calendar.MONTH);
            int year = cal.get(Calendar.YEAR);

            ddET.setText("" + date);
            MMET.setText("" + month);
            yyET.setText("" + year);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_code_generator);
        new PushRequest().execute();
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_voice_code_generator, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void refresh(View view) {
        initData();

    }
}
