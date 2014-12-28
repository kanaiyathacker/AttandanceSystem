package com.barcodescannerfordialogs;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.barcodescannerfordialogs.helpers.CameraFace;
import com.barcodescannerfordialogs.qrscanner.ZBarScannerView;
import com.bean.User;
import com.google.gson.Gson;
import com.vaiotech.attendaceapp.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.dm7.barcodescanner.core.DisplayUtils;

import static com.util.Util.getEditViewText;
import static com.util.Util.getTextViewText;

public class DialogScanner extends DialogFragment implements ZBarScannerView.ResultHandler
{
	static final String TAG = DialogScanner.class.getSimpleName();

	CameraFace cameraFace;

	ZBarScannerView mScannerView;

	// Maintain a 4:3 ratio
//	int mWindowWidth = 800;
//	int mWindowHeight = 600;
//	int mViewFinderPadding = 50;

    int mWindowWidth = 480;
    int mWindowHeight = 600;
    int mViewFinderPadding = 0;
    int singleOrBatchFlag =0;
    Set<User> scanResult;
    TextView countTv;

	static final String BUNDLE_CAMERA_FACE = "cameraFace";
	static final String BUNDLE_SINGLE_OR_BATCH = "singleOrBatch";

	OnQRCodeScanListener scanListener;
    ProgressBar progressBar;
	public interface OnQRCodeScanListener
	{
		public void onQRCodeScan(Object contents);
	}

	public static DialogScanner newInstance(CameraFace camera , int singleOrbatch , ProgressBar progressBar)
	{
		DialogScanner dialog = new DialogScanner();
		Bundle args = new Bundle();
		args.putInt(BUNDLE_CAMERA_FACE, camera.ordinal());
		args.putInt(BUNDLE_SINGLE_OR_BATCH, singleOrbatch);
		dialog.setArguments(args);
        dialog.progressBar = progressBar;
		return dialog;
	}

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);

		try
		{
			scanListener = (OnQRCodeScanListener) activity;
		}
		catch (ClassCastException e)
		{
			throw new ClassCastException(activity.getClass().getSimpleName() + " must implement OnQRCodeScanListener");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		Bundle args = getArguments();
		if(args != null)
		{
			cameraFace = CameraFace.values()[args.getInt(BUNDLE_CAMERA_FACE)];
		}
		else
		{
			cameraFace = CameraFace.BACK;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		//View view = inflater.inflate(R.layout.dialog_scanner, container, false);

        Bundle args = getArguments();
        int flag = args.getInt(BUNDLE_SINGLE_OR_BATCH);

		mScannerView = new ZBarScannerView(getActivity(), cameraFace , flag);
		mScannerView.setResultHandler(this);
		mScannerView.setAutoFocus(false);

		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);


		return mScannerView;
	}

	@Override
	public void onStart()
	{
		super.onStart();

		if(DisplayUtils.getScreenOrientation(getActivity()) == Configuration.ORIENTATION_PORTRAIT && mWindowWidth > mWindowHeight)
		{
			getDialog().getWindow().setLayout(mWindowHeight, mWindowWidth);
		}
		else
		{
			getDialog().getWindow().setLayout(mWindowWidth, mWindowHeight);
		}
        Bundle args = getArguments();
        int flag = args.getInt(BUNDLE_SINGLE_OR_BATCH);
        singleOrBatchFlag = flag;
        if(flag == 1) {
            FrameLayout.LayoutParams tvLay = new FrameLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            tvLay.setMargins(0, 0, 0, 0);
            countTv = new TextView(getDialog().getContext());
            countTv.setText("0");
            countTv.setId(R.id.counterValTV);
            countTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            countTv.setTextColor(Color.parseColor("#FFFFFF"));
            countTv.setGravity(Gravity.CENTER);
            countTv.setLayoutParams(tvLay);
            mScannerView.addView(countTv);
            scanResult = new HashSet<User>();
        }

        FrameLayout.LayoutParams lay = new FrameLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        lay.setMargins(0,480,0,0);

        Button button = new Button(getDialog().getContext());
        button.setText(" Done ");
        button.setTextColor(Color.parseColor("#FFFFFF"));
        button.setGravity(Gravity.CENTER);

        button.setLayoutParams(lay);
        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        mScannerView.addView(button);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                scanListener.onQRCodeScan(scanResult != null ? scanResult : null);
                getDialog().dismiss();
                onPause();
            }
        });
	}

	@Override
	public void onResume()
	{
		super.onResume();

		mScannerView.setResultHandler(this);
		mScannerView.startCamera();
//        progressBar.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onPause()
	{
		super.onPause();
		mScannerView.stopCamera();
	}

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void playSound(int notificationType)
	{
		Uri notification = RingtoneManager.getDefaultUri(notificationType);
		Ringtone r = RingtoneManager.getRingtone(getActivity().getApplicationContext(), notification);
		r.play();
	}

	@Override
	public void handleResult(me.dm7.barcodescanner.zbar.Result result)
	{
		if(result.getBarcodeFormat() == me.dm7.barcodescanner.zbar.BarcodeFormat.QRCODE)
		{
			playSound(RingtoneManager.TYPE_NOTIFICATION);
            if(singleOrBatchFlag == 0) {
                scanListener.onQRCodeScan(result.getContents());
                getDialog().dismiss();
            } else {
                int count = Integer.parseInt(getTextViewText(countTv));
                count++;
                countTv.setText(""+count);
                String con = result.getContents();
                Gson gson = new Gson();
                User user= gson.fromJson(con, User.class);
                scanResult.add(user);
            }
		}
	}
}
