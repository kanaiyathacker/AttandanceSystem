package com.services;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.os.IBinder;
import android.provider.Settings;
import android.widget.Toast;

/**
 * Created by kanaiyalalt on 26/11/2014.
 */
public class ScanCardService extends Service {

    private NfcAdapter mNfcAdapter;
    private PendingIntent mNfcPendingIntent;
    private IntentFilter[] mReadTagFilters;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        mNfcPendingIntent = PendingIntent.getActivity(this, 0, new
                Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TOP), 0);
        IntentFilter ndefDetected = new IntentFilter(
                NfcAdapter.ACTION_NDEF_DISCOVERED);
        ndefDetected.addDataScheme("http");
        IntentFilter techDetected = new IntentFilter(
                NfcAdapter.ACTION_TECH_DISCOVERED);
        mReadTagFilters = new IntentFilter[] { ndefDetected, techDetected };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);


        if (mNfcAdapter != null) {
            if (!mNfcAdapter.isEnabled()) {
                new AlertDialog.Builder(this)
                        .setTitle("NFC Dialog")
                        .setMessage("Please switch on NFC")
                        .setPositiveButton("Settings",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0,
                                                        int arg1) {
                                        Intent setnfc = new Intent(
                                                Settings.ACTION_WIRELESS_SETTINGS);
                                        startActivity(setnfc);
                                    }
                                })
                        .setOnCancelListener(
                                new DialogInterface.OnCancelListener() {
                                    public void onCancel(DialogInterface dialog) {
                                        dialog.dismiss();
                                    }
                                }).create().show();
            }
        } else {
            Toast.makeText(this,
                    "Sorry, NFC adapter not available on your device.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
