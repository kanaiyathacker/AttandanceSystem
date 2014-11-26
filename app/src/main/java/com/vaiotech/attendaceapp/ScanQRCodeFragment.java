package com.vaiotech.attendaceapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.barcodescannerfordialogs.DialogScanner;

import roboguice.fragment.RoboFragment;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScanQRCodeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScanQRCodeFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ScanQRCodeFragment extends Fragment implements DialogScanner.OnQRCodeScanListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    TextView companyAdminNameTV;
    TextView companyAdminCodeTV;
    Button scanBUTTON;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScanQRCodeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScanQRCodeFragment newInstance(String param1, String param2) {
        ScanQRCodeFragment fragment = new ScanQRCodeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }
    public ScanQRCodeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        companyAdminNameTV = (TextView) getView().findViewById(R.id.companyAdminNameTV);
        companyAdminCodeTV = (TextView) getView().findViewById(R.id.companyAdminCodeTV);
        scanBUTTON = (Button) getView().findViewById(R.id.scanBUTTON);

        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Calibri.ttf");

        companyAdminNameTV.setTypeface(font);
        companyAdminCodeTV.setTypeface(font);
        scanBUTTON.setTypeface(font);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scan_qrcode, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onQRCodeScan(String contents) {
        Intent intent = new Intent(getActivity(),HomeActivity.class);
        intent.putExtra("SCAN_CONTENT" , contents);
        startActivity(intent);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
