package com.vaiotech.attendaceapp;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_about_ipresence)
public class AboutIPresenceActivity extends BaseActivity {

    @InjectView(R.id.aboutiPresenceTV) TextView aboutiPresenceTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_ipresence);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Calibri.ttf");
        aboutiPresenceTV.setTypeface(font);
        aboutiPresenceTV.setText(Html.fromHtml(aboutiPresenceTV.getText().toString()));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about_ipresence, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
