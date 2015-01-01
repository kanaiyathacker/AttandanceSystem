package com.vaiotech.attendaceapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @InjectView(R.id.button1) Button button1;
    @InjectView(R.id.button2) Button button2;
    @InjectView(R.id.button3) Button button3;
    @InjectView(R.id.button4) Button button4;
    @InjectView(R.id.modeRadioGroup) RadioGroup modeRadioGroup;

    @InjectView(R.id.singleModeRB)   RadioButton singleModeRB;
    @InjectView(R.id.batchModeRB) RadioButton batchModeRB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Calibri.ttf");
        button1.setTypeface(font);
        button2.setTypeface(font);
        button3.setTypeface(font);
        button4.setTypeface(font);
    }

    public void scanQRCode(View view) {
        Intent intent = R.id.singleModeRB == modeRadioGroup.getCheckedRadioButtonId() ?
                new Intent(this, ScanQRCodeSingleActivity.class) : new Intent(this, ScanQRCodeBatchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void scanCard(View view) {
        Intent intent = R.id.singleModeRB == modeRadioGroup.getCheckedRadioButtonId() ?
                new Intent(this, ScanCardSingleActivity.class) : new Intent(this, ScanCardBatchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void scanVoice(View view) {
        Intent intent = R.id.singleModeRB == modeRadioGroup.getCheckedRadioButtonId() ?
                new Intent(this, ScanVoiceCodeSingleActivity.class) : new Intent(this, ScanVoiceCodeBatchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void manualEntry(View view) {
        Intent intent = new Intent(this , ManualEntryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
