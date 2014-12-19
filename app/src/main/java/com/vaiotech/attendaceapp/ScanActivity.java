package com.vaiotech.attendaceapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.barcodescannerfordialogs.DialogScanner;
import com.google.gson.Gson;

import java.io.InputStream;
import java.net.URL;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_scan)
public class ScanActivity extends BaseActivity implements DialogScanner.OnQRCodeScanListener {

    @InjectView(R.id.scanBUTTON) Button scanBUTTON;
    Bitmap bitmap;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Calibri.ttf");
        scanBUTTON.setTypeface(font);
        String adminDetails = getIntent().getStringExtra("ADMIN_DETAILS");
        Gson gson = new Gson();
//        DialogScanner dialog = DialogScanner.newInstance(CameraFace.BACK);
//        dialog.show(getFragmentManager(), "cameraPreview");

    }

    private class LoadImage extends AsyncTask<String, String, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ScanActivity.this);
            pDialog.setMessage("Loading Image ....");
            pDialog.show();
        }
        protected Bitmap doInBackground(String... args) {
            try {
                bitmap = BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }
        protected void onPostExecute(Bitmap image) {
            if(image != null){
//                companyLOGOIV.setImageBitmap(image);
                pDialog.dismiss();
            }else{
                pDialog.dismiss();
                Toast.makeText(ScanActivity.this, "Image Does Not exist or Network Error", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void scan(View view) {
//        onQRCodeScan("");
//        DialogScanner dialog = DialogScanner.newInstance(CameraFace.BACK);
//        dialog.show(getFragmentManager(), "dialogScanner");
    }

    @Override
    public void onQRCodeScan(Object contents) {
        Intent intent = new Intent(this,ScanQRCodeSingleActivity.class);
//        intent.putExtra("SCAN_CONTENT" , contents);
        startActivity(intent);
    }
}
