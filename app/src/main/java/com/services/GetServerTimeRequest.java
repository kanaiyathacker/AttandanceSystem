package com.services;

import android.os.AsyncTask;
import android.view.View;
import android.widget.EditText;

import com.util.Util;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by kanaiyalalt on 01/01/2015.
 */
public class GetServerTimeRequest extends AsyncTask<String, Integer, String> {

    EditText hhET;
    EditText mmET;
    EditText ddET;
    EditText MMET;
    EditText yyET;

    public GetServerTimeRequest(EditText hhET , EditText mmET , EditText ddET , EditText MMET , EditText yyET ) {
        this.hhET = hhET;
        this.mmET = mmET;
        this.ddET = ddET;
        this.MMET = MMET;
        this.yyET = yyET;
    }

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
        hhET.setText(Util.paddingZero(hour));
        mmET.setText(Util.paddingZero(min));

        int date = cal.get(Calendar.DATE);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);

        ddET.setText(Util.paddingZero(date));
        MMET.setText(Util.paddingZero(month));
        yyET.setText(Util.paddingZero(year));
    }
}
