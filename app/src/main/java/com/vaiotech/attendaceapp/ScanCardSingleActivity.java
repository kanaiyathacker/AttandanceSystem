package com.vaiotech.attendaceapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bean.AttandanceTransaction;
import com.bean.User;
import com.google.gson.Gson;
import com.listener.SaveAttandanceRequestListener;
import com.services.SaveAttandanceRequest;
import com.util.Util;

import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_scan_card_single)
public class ScanCardSingleActivity extends BaseActivity {

    @InjectView(R.id.idLableTV) TextView idLableTV;
    @InjectView(R.id.idValueTV) TextView idValueTV;
    @InjectView(R.id.seperateTimeTV) TextView seperateTimeTV;
    @InjectView(R.id.timeTV) TextView timeTV;
    @InjectView(R.id.hhET) EditText hhET;
    @InjectView(R.id.mmET) EditText mmET;
    @InjectView(R.id.inBUTTON) Button inBUTTON;
    @InjectView(R.id.outBUTTON) Button outBUTTON;

    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mIntentFilters;
    private String[][] mNFCTechLists;
    private String cardId;
    private NdefMessage mNdefPushMessage;
    private SaveAttandanceRequest saveAttandanceRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_card_single);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Calibri.ttf");
        idLableTV.setTypeface(font);
        idValueTV.setTypeface(font);

        Typeface digital = Typeface.createFromAsset(getAssets(), "fonts/digital_7_mono.ttf");

        seperateTimeTV.setTypeface(digital);
        hhET.setTypeface(digital);
        mmET.setTypeface(digital);
        timeTV.setTypeface(digital);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        inBUTTON.setTypeface(font);
        outBUTTON.setTypeface(font);


        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.AM_PM, Calendar.PM);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        hhET.setText(""+hour);
        mmET.setText(""+(min < 10 ? "0"+ min : min));

        if (mNfcAdapter != null) {
            Toast.makeText(this, "Read an NFC tag", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "This phone is not NFC enabled", Toast.LENGTH_SHORT).show();
        }
        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, ScanCardSingleActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        mNdefPushMessage = new NdefMessage(new NdefRecord[] { newTextRecord(
                "Message from NFC Reader :-)", Locale.ENGLISH, true) });

    }

    private NdefRecord newTextRecord(String text, Locale locale, boolean encodeInUtf8) {
        byte[] langBytes = locale.getLanguage().getBytes(Charset.forName("US-ASCII"));

        Charset utfEncoding = encodeInUtf8 ? Charset.forName("UTF-8") : Charset.forName("UTF-16");
        byte[] textBytes = text.getBytes(utfEncoding);

        int utfBit = encodeInUtf8 ? 0 : (1 << 7);
        char status = (char) (utfBit + langBytes.length);

        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        data[0] = (byte) status;
        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);

        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mNfcAdapter != null) {
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
            mNfcAdapter.setNdefPushMessage(mNdefPushMessage, this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mNfcAdapter != null) {
            mNfcAdapter.disableForegroundDispatch(this);
            mNfcAdapter.setNdefPushMessage(mNdefPushMessage, this);
        }
    }

    private void resolveIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
            long cardId = getReversed(id);
            this.cardId = ""+cardId;
            idValueTV.setText(this.cardId);
        }
    }

    private long getReversed(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = bytes.length - 1; i >= 0; --i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent);
        resolveIntent(intent);
    }

    public void save(View view) {
        String type = view.getId() == R.id.inBUTTON ? "IN" : "OUT";
        saveAttandanceRequest = new SaveAttandanceRequest(buildAttandanceTransaction(type));
        spiceManager.execute(saveAttandanceRequest , new SaveAttandanceRequestListener());
        openDialog(view);
    }

    public AttandanceTransaction buildAttandanceTransaction(String type) {
        SharedPreferences sharedPreferences = getSharedPreferences("DIGITAL_ATTENDANCE" , Context.MODE_PRIVATE);
        String val = sharedPreferences.getString("USER_DETAILS" , null);
        Gson gson = new Gson();
        User user = gson.fromJson(val , User.class);

        AttandanceTransaction t = new AttandanceTransaction();
        t.setAdminId(user.getUserId());
        t.setCardId(cardId);
        t.setDate(Util.convertDateToString(new Date()));
        t.setTime(hhET.getText() + ":" + mmET.getText());
        t.setTrasTable(user.getTranTable());
        t.setType(type);
        return t;
    }

    public void openDialog(View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setMessage((view.getId() == R.id.inBUTTON ? "IN Time for" : "OUT Time for ") + "id1231231" + " noted as 12:15");
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.scan_card_single, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}