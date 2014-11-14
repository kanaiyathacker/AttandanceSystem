package com.vaiotech.attendaceapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.barcodescannerfordialogs.DialogScanner;
import com.barcodescannerfordialogs.helpers.CameraFace;
import com.bean.Person;
import com.google.gson.Gson;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.services.SaveAttandanceRequest;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_home)
public class HomeActivity extends BaseActivity {

    @InjectView(R.id.personNameTV) TextView personNameTV;
    @InjectView(R.id.personIDTV)   TextView personIDTV;
    @InjectView(R.id.personDeptTV) TextView personDeptTV;
    @InjectView(R.id.inBUTTON) Button inBUTTON;
    @InjectView(R.id.outBUTTON) Button outBUTTON;
    @InjectView(R.id.personLOGOIV) ImageView personLOGOIV;


    private SaveAttandanceRequest saveAttandanceRequest;
    private Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Calibri.ttf");
        personNameTV.setTypeface(font);
        personIDTV.setTypeface(font);
        personDeptTV.setTypeface(font);
        inBUTTON.setTypeface(font);
        outBUTTON.setTypeface(font);
        setData();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void launchScanBack(View view)
    {
        DialogScanner dialog = DialogScanner.newInstance(CameraFace.BACK);
        dialog.show(getFragmentManager(), "dialogScanner");
    }

    public void launchScanFront(View view)
    {
        DialogScanner dialog = DialogScanner.newInstance(CameraFace.FRONT);
        dialog.show(getFragmentManager(), "dialogScanner");
    }

    public void setData()
    {
        String contents = getIntent().getStringExtra("SCAN_CONTENT");
        System.out.println("********** content --- " + contents);
        Gson gson = new Gson();
        person = gson.fromJson(contents , Person.class);
        if(person != null) {
            personNameTV.setText(person.getfName() + " " + person.getmName() + " " + person.getlName());
            personIDTV.setText(person.getId());
            personDeptTV.setText(person.getDepartment());

            if ("12132131".equals(person.getId()))
                personLOGOIV.setImageResource(R.drawable.kanaiya);
            else if ("12132132".equals(person.getId()))
                personLOGOIV.setImageResource(R.drawable.swarnaba);
            else if ("12132133".equals(person.getId()))
                personLOGOIV.setImageResource(R.drawable.najmul);
            else if ("12132134".equals(person.getId()))
                personLOGOIV.setImageResource(R.drawable.amit);
            else if ("12132135".equals(person.getId()))
                personLOGOIV.setImageResource(R.drawable.vikram);
        }
    }

    public void save(View view) {
//        saveAttandanceRequest
//                = new SaveAttandanceRequest(person.getCoId() , person.getId(), person.getfName(), person.getmName(),
//                person.getlName(), person.getDate(),person.getTime(),person.getDesc());
//        spiceManager.execute(saveAttandanceRequest , new SaveAttandanceRequestListener());
        openDialog(view);
    }

    public void openDialog(View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setMessage((view.getId() == R.id.inBUTTON ? "IN Time for" : "OUT Time for ") + (person != null ? person.getfName() : "") + " noted as 12:15");
        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(getApplicationContext(), ScanActivity.class);
                        startActivity(intent);
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private class SaveAttandanceRequestListener implements com.octo.android.robospice.request.listener.RequestListener<Object> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {

        }

        @Override
        public void onRequestSuccess(Object o) {

        }
    }
}
