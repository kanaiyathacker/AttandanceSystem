package com.vaiotech.attendaceapp;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.listener.ContactUsRequestListener;
import com.services.ContactUsRequest;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_contact_us)
public class ContactUsActivity extends BaseActivity {

    @InjectView(R.id.emailTV) TextView emailTV;
    @InjectView(R.id.emailET) EditText emailET;
    @InjectView(R.id.detailsTV) TextView detailsTV;
    @InjectView(R.id.detailsET) EditText detailsET;
    @InjectView(R.id.submitButton) Button submitButton;

    private ContactUsRequest contactUsRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Calibri.ttf");

        emailTV.setTypeface(font);
        emailET.setTypeface(font);
        detailsTV.setTypeface(font);
        detailsET.setTypeface(font);
        submitButton.setTypeface(font);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_us, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void save(View view) {
        showProgressBar();
        contactUsRequest = new ContactUsRequest(emailET.getText().toString() , detailsET.getText().toString());
        spiceManager.execute(contactUsRequest ,new ContactUsRequestListener(this));
    }
}
