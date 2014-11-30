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
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bean.AttandanceTransaction;
import com.bean.User;
import com.google.gson.Gson;
import com.listener.SaveAttandanceRequestListener;
import com.services.SaveAttandanceRequest;
import com.util.NdefMessageParser;
import com.util.ParsedNdefRecord;
import com.util.Util;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_scan_card_batch)
public class ScanCardBatchActivity extends BaseActivity {


    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "NfcDemo";

    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mIntentFilters;
    private String[][] mNFCTechLists;
    private SaveAttandanceRequest saveAttandanceRequest;
    private String cardId;
    private NdefMessage mNdefPushMessage;

    @InjectView(R.id.timeTV) TextView timeTV;
    @InjectView(R.id.timeTV) TextView hhET;
    @InjectView(R.id.timeTV) TextView seperateTimeTV;
    @InjectView(R.id.timeTV) TextView mmET;
    @InjectView(R.id.timeTV) TextView counterValTV;
    @InjectView(R.id.inBUTTON)  Button inBUTTON;
    @InjectView(R.id.outBUTTON) Button outBUTTON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_card_batch);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Calibri.ttf");
        Typeface digital = Typeface.createFromAsset(getAssets(), "fonts/digital_7_mono.ttf");

        seperateTimeTV.setTypeface(digital);
        hhET.setTypeface(digital);
        mmET.setTypeface(digital);
        timeTV.setTypeface(digital);
        counterValTV.setTypeface(digital);

        inBUTTON.setTypeface(font);
        outBUTTON.setTypeface(font);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.AM_PM, Calendar.PM);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        hhET.setText(""+hour);
        mmET.setText("" + (min < 10 ? "0" + min : min));

        if (mNfcAdapter != null) {
            Toast.makeText(this, "Read an NFC tag", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "This phone is not NFC enabled", Toast.LENGTH_SHORT).show();
        }
//        // create an intent with tag data and deliver to this activity
//        mPendingIntent = PendingIntent.getActivity(this, 0,
//                new Intent(this, ScanCardBatchActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
//
//        // set an intent filter for all MIME data
//        IntentFilter ndefIntent = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
//        try {
//            ndefIntent.addDataType("*/*");
//            mIntentFilters = new IntentFilter[] { ndefIntent };
//        } catch (Exception e) {
//        }
//
//        mNFCTechLists = new String[][] { new String[] { NfcF.class.getName() } };


        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, ScanCardBatchActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        mNdefPushMessage = new NdefMessage(new NdefRecord[] { newTextRecord(
                "Message from NFC Reader :-)", Locale.ENGLISH, true) });
    }

    private void showMessage(int title, int message) {
//        mDialog.setTitle(title);
//        mDialog.setMessage(getText(message));
//        mDialog.show();
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
            if (!mNfcAdapter.isEnabled()) {
                showWirelessSettingsDialog();
            }
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
            mNfcAdapter.enableForegroundNdefPush(this, mNdefPushMessage);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mNfcAdapter != null) {
            mNfcAdapter.disableForegroundDispatch(this);
            mNfcAdapter.disableForegroundNdefPush(this);
        }
    }

    private void showWirelessSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("dsds");
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.create().show();
        return;
    }

    private void resolveIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs;
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            } else {
                // Unknown tag type
//                byte[] empty = new byte[0];
                byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
                long cardId = getReversed(id);
                String cardIdStr= new String(id);
                System.out.println(cardIdStr);
                System.out.println(ByteArrayToHexString(id));
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                System.out.println(ByteArrayToHexString(tag.getId()));
                System.out.println(bytesToHexString(tag.getId()));
//                Parcelable tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//                byte[] payload = dumpTagData(tag).getBytes();
//                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload);
//                NdefMessage msg = new NdefMessage(new NdefRecord[] { record });
//                msgs = new NdefMessage[] { msg };
            }
            // Setup the views
//            buildTagViews(msgs);
        }
    }


    String ByteArrayToHexString(byte [] inarray)
    {
        int i, j, in;
        String [] hex = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
        String out= "";

        for(j = 0 ; j < inarray.length ; ++j)
        {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }

    private String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("0x");
        if (src == null || src.length <= 0) {
            return null;
        }

        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
            System.out.println(buffer);
            stringBuilder.append(buffer);
        }

        return stringBuilder.toString();
    }

    private String dumpTagData(Parcelable p) {
        StringBuilder sb = new StringBuilder();
        Tag tag = (Tag) p;
        byte[] id = tag.getId();
        sb.append("Tag ID (hex): ").append(getHex(id)).append("\n");
        sb.append("Tag ID (dec): ").append(getDec(id)).append("\n");
        sb.append("ID (reversed): ").append(getReversed(id)).append("\n");

        String prefix = "android.nfc.tech.";
        sb.append("Technologies: ");
        for (String tech : tag.getTechList()) {
            sb.append(tech.substring(prefix.length()));
            sb.append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        for (String tech : tag.getTechList()) {
            if (tech.equals(MifareClassic.class.getName())) {
                sb.append('\n');
                MifareClassic mifareTag = MifareClassic.get(tag);
                String type = "Unknown";
                switch (mifareTag.getType()) {
                    case MifareClassic.TYPE_CLASSIC:
                        type = "Classic";
                        break;
                    case MifareClassic.TYPE_PLUS:
                        type = "Plus";
                        break;
                    case MifareClassic.TYPE_PRO:
                        type = "Pro";
                        break;
                }
                sb.append("Mifare Classic type: ");
                sb.append(type);
                sb.append('\n');

                sb.append("Mifare size: ");
                sb.append(mifareTag.getSize() + " bytes");
                sb.append('\n');

                sb.append("Mifare sectors: ");
                sb.append(mifareTag.getSectorCount());
                sb.append('\n');

                sb.append("Mifare blocks: ");
                sb.append(mifareTag.getBlockCount());
            }

            if (tech.equals(MifareUltralight.class.getName())) {
                sb.append('\n');
                MifareUltralight mifareUlTag = MifareUltralight.get(tag);
                String type = "Unknown";
                switch (mifareUlTag.getType()) {
                    case MifareUltralight.TYPE_ULTRALIGHT:
                        type = "Ultralight";
                        break;
                    case MifareUltralight.TYPE_ULTRALIGHT_C:
                        type = "Ultralight C";
                        break;
                }
                sb.append("Mifare Ultralight type: ");
                sb.append(type);
            }
        }

        return sb.toString();
    }

    private String getHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = bytes.length - 1; i >= 0; --i) {
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
            if (i > 0) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    private long getDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = 0; i < bytes.length; ++i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
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

    void buildTagViews(NdefMessage[] msgs) {
        if (msgs == null || msgs.length == 0) {
            return;
        }
//        List<ParsedNdefRecord> records =NdefMessageParser.parse(msgs[0]);
//        LayoutInflater inflater = LayoutInflater.from(this);
//        LinearLayout content = mTagContent;
//
//        // Parse the first message in the list
//        // Build views for all of the sub records
//        Date now = new Date();
//        List<ParsedNdefRecord> records = NdefMessageParser.parse(msgs[0]);
//        final int size = records.size();
//        for (int i = 0; i < size; i++) {
//            TextView timeView = new TextView(this);
//            timeView.setText(TIME_FORMAT.format(now));
//            content.addView(timeView, 0);
//            ParsedNdefRecord record = records.get(i);
//            content.addView(record.getView(this, inflater, content, i), 1 + i);
//            content.addView(inflater.inflate(R.layout.tag_divider, content, false), 2 + i);
//        }i
    }

    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent);
        resolveIntent(intent);
    }

//    @Override
//    public void onNewIntent(Intent intent) {
//        String action = intent.getAction();
//        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//
//        String s = action + "\n\n" + tag.toString();
//
//        // parse through all NDEF messages and their records and pick text type only
//        Parcelable[] data = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
//        if (data != null) {
//            try {
//                for (int i = 0; i < data.length; i++) {
//                    NdefRecord[] recs = ((NdefMessage)data[i]).getRecords();
//                    for (int j = 0; j < recs.length; j++) {
//                        if (recs[j].getTnf() == NdefRecord.TNF_WELL_KNOWN &&
//                                Arrays.equals(recs[j].getType(), NdefRecord.RTD_TEXT)) {
//                            byte[] payload = recs[j].getPayload();
//                            String textEncoding = ((payload[0] & 0200) == 0) ? "UTF-8" : "UTF-16";
//                            int langCodeLen = payload[0] & 0077;
//
//                            s += ("\n\nNdefMessage[" + i + "], NdefRecord[" + j + "]:\n\"" +
//                                    new String(payload, langCodeLen + 1, payload.length - langCodeLen - 1,
//                                            textEncoding) + "\"");
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        cardId = "Kanaiya Test";
//        Toast.makeText(this, "data -- " + s , Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        if (mNfcAdapter != null)
//            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, mIntentFilters, mNFCTechLists);
//
////        if (mNfcAdapter != null)
////            setupForegroundDispatch(this, mNfcAdapter);
//           // mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, mIntentFilters, mNFCTechLists);
//    }
//
//    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
//        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
//        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//
//        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);
//
//        IntentFilter[] filters = new IntentFilter[1];
//        String[][] techList = new String[][]{};
//
//        // Notice that this is the same filter as in our manifest.
//        filters[0] = new IntentFilter();
//        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
//        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
//        try {
//            filters[0].addDataType(MIME_TEXT_PLAIN);
//        } catch (IntentFilter.MalformedMimeTypeException e) {
//            throw new RuntimeException("Check your mime type.");
//        }
//
//        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//
//        if (mNfcAdapter != null)
//            mNfcAdapter.disableForegroundDispatch(this);
//    }
//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.scan_card_batch, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

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
        t.setTrasTable(t.getTrasTable());
        t.setType(type);
        return t;
    }

//    public String getAdminId() {
//        SharedPreferences sharedPreferences = getSharedPreferences("DIGITAL_ATTENDANCE" , Context.MODE_PRIVATE);
//        String val = sharedPreferences.getString("USER_DETAILS" , null);
//        Gson gson = new Gson();
//        User user = gson.fromJson(val , User.class);
//        return "21";
////        return user.getId();
//    }

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

}
