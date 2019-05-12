package com.example.transitapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Enroute_Stop_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Enroute_Stop_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Enroute_Stop_Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String stop;

    private OnFragmentInteractionListener mListener;

    private TextView stu_entered_count_TxtView, stu_departed_count_TxtView;
    private EditText stu_entered_count_EditText, stu_departed_count_EditText;
    private TextView current_stop;
    private int entered;
    private int departed;
    private int bikes_used;
    private ImageButton entered_button;
    private ImageButton bike_button;
    private ImageButton departed_button;
    public TripDetails trip_details;


    // TODO: Rename and change types and number of parameters
    public static Enroute_Stop_Fragment newInstance(String stop, int entered, int departed, int bike_used, TripDetails tripDetails) {
        Enroute_Stop_Fragment fragment = new Enroute_Stop_Fragment();
        Bundle args = new Bundle();
        args.putString("Stop", stop);
        args.putInt("Entered", entered);
        args.putInt("Departed", departed);
        args.putInt("Bikes_used", bike_used);
        args.putSerializable("Trip_details", (Serializable) tripDetails);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            stop = getArguments().getString("Stop");
            entered = getArguments().getInt("Entered");
            departed = getArguments().getInt("Departed");
            bikes_used = getArguments().getInt("Bikes_used");
            trip_details = (TripDetails) getArguments().getSerializable("Trip_details");
            trip_details.setBus_number(Enroute_Dashboard.busNumber);
            trip_details.setDriver_name(Enroute_Dashboard.driverName);
            trip_details.setSchedule(Enroute_Dashboard.schedule.replace(":",""));
            trip_details.setStop(stop);
            trip_details.setTrip_start_time(new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss").format(new Date())) ;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View group = inflater.inflate(R.layout.fragment_enroute_stop,
                container, false);

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_enroute_stop, container, false);
        stu_entered_count_EditText = (EditText) group.findViewById(R.id.stu_entered_editText);
        stu_entered_count_TxtView = (TextView) group.findViewById(R.id.stu_entered_textView);

        stu_departed_count_EditText = (EditText) group.findViewById(R.id.stu_depart_EditText);
        stu_departed_count_TxtView = (TextView) group.findViewById(R.id.stu_depart_TextView);


        entered_button = (ImageButton) group.findViewById(R.id.Entered_plus);
        departed_button = (ImageButton) group.findViewById(R.id.departed_plus);
        bike_button = (ImageButton) group.findViewById(R.id.Bike_plus);

        entered_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stu_entered_count_EditText.setText(""+ ++entered);
                trip_details.setStudents_arrived(entered);
            }
        });

        departed_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stu_departed_count_EditText.setText(""+ ++departed);
                trip_details.setStudents_departed(departed);
            }
        });

        bike_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++bikes_used;
                trip_details.setRacks_loaded(bikes_used);
            }
        });


        current_stop = (TextView) group.findViewById(R.id.curr_stop_name_textView);
        current_stop.setText(stop);

        stu_entered_count_EditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                stu_entered_count_TxtView.setText(stu_entered_count_EditText.getText());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        stu_entered_count_EditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == event.KEYCODE_ENTER || actionId == event.KEYCODE_NUMPAD_ENTER) {
                    // Write your logic here that will be executed when user taps next button

                    stu_entered_count_EditText.clearFocus();

                    handled = false;
                }
                return handled;
            }
        });

        stu_departed_count_EditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                stu_departed_count_TxtView.setText(stu_departed_count_EditText.getText());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        stu_departed_count_EditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == event.KEYCODE_ENTER || actionId == event.KEYCODE_NUMPAD_ENTER) {
                    // Write your logic here that will be executed when user taps next button

                    stu_departed_count_EditText.clearFocus();

                    handled = false;
                }
                return handled;
            }
        });

        return group;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        void onFragmentInteraction(Uri uri);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
