package com.example.transitapp;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PreInspectionActivity extends AppCompatActivity {


    //TODO: @kushal
    // 1. retrieve all the information from the firebase database to populate in the checks.
    // 2. Submit the pre inspection check
    // 3. retrieve the pre-inspection check if edit is clicked (IMPORTANT)

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private InspectionChecklist inspectionChecklist;
    private String ACTION = "ACTION_FOR_INTENT_CALLBACK";
    private ProgressDialog progressDialog;
    private Fragment engineAndFluidFragment;
    private Fragment exteriorCheckFragment;
    private Fragment interiorCheckFragment;



    private BroadcastReceiver receiver;


    private String driverName;
    private int bus_number;
    private String timestamp;

    private ArrayList interiorChecks;
    private ArrayList exteriorChecks;
    private ArrayList engineChecks;
    private Bundle inBundle;
    private Bundle exBundle;
    private Bundle enBundle;

    private UpdateDatabase updateDatabase;


    private FloatingActionButton send;
    public HashMap<String, HashMap<String, Boolean>> preInspectionCheckValues;
    public HashMap<String, String> others;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_inspection);
        preInspectionCheckValues = new HashMap<>();
        others = new HashMap<>();
        updateDatabase = new UpdateDatabase();
        send = findViewById(R.id.submit);
        inspectionChecklist = new InspectionChecklist(getApplicationContext(), ACTION);
        inspectionChecklist.getPreList();
        progressDialog = ProgressDialog.show(this, "Getting Data", "Waiting for results...", true);

        receiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                Intent i = getIntent();
                Bundle b = i.getBundleExtra("editBundle");
                Boolean isEdit = b.getBoolean("Edit");
                driverName = b.getString("Driver name");
                bus_number = b.getInt("Bus number");
                if(isEdit){
                    // retrieve the pre-inspection check if edit is clicked (IMPORTANT)
                    System.out.println("Must get the edit version");
                    getLatestPreInspectionCheck(driverName, bus_number);
                    // Will give an error right now. Need to populate the bundle to be sent to the fragments.
                }
                else {
                    timestamp = b.getString("Timestamp");


                    interiorChecks = intent.getStringArrayListExtra("IntCheck");
                    exteriorChecks = intent.getStringArrayListExtra("ExtCheck");
                    engineChecks = intent.getStringArrayListExtra("EngCheck");
                    inBundle = new Bundle();
                    exBundle = new Bundle();
                    enBundle = new Bundle();
                    inBundle.putStringArrayList("InteriorChecklist", interiorChecks);
                    exBundle.putStringArrayList("ExteriorChecklist", exteriorChecks);
                    enBundle.putStringArrayList("EngineChecklist", engineChecks);
                    engineAndFluidFragment = new EngineAndFluidFragment();
                    exteriorCheckFragment = new ExteriorChecksFragment();
                    interiorCheckFragment = new InteriorChecksFragment();
                    engineAndFluidFragment.setArguments(enBundle);
                    exteriorCheckFragment.setArguments(exBundle);
                    interiorCheckFragment.setArguments(inBundle);
                }

                System.out.println(driverName);
                System.out.println(bus_number);
                System.out.println(timestamp);

                mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

                mViewPager = findViewById(R.id.container);
                mViewPager.setOffscreenPageLimit(2);
                setupViewPager(mViewPager);



                TabLayout tabLayout = findViewById(R.id.tabs);
                tabLayout.setupWithViewPager(mViewPager);

            }
        };


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(preInspectionCheckValues);
                updateDatabase.sendPreInspectionCheck(preInspectionCheckValues, others, driverName, bus_number, timestamp);
            }
        });


    }

//    private void prepareJSON(HashMap<String, HashMap<CardView, ToggleButton>> preInspectionCheckValues) {
//        HashMap<String, HashMap<String, Boolean>> finalValues  = new HashMap<>();
//        HashMap<CardView, ToggleButton> innerChecks = new HashMap<>();
//        HashMap<String, Boolean> innerCheckValues = new HashMap<>();
//        Iterator it = preInspectionCheckValues.entrySet().iterator();
//        while(it.hasNext()){
//            Map.Entry pair = (Map.Entry) it.next();
//            innerChecks = (HashMap<CardView, ToggleButton>) pair.getValue();
//            Iterator ot = innerChecks.entrySet().iterator();
//            while(ot.hasNext()){
//                Map.Entry innerPair = (Map.Entry) ot.next();
//                CardView c = innerPair.getKey();
//                ToggleButton b = innerPair.getValue();
//                innerCheckValues.put(c.getTag());
//            }
//
//
//        }
//    }

    private void getLatestPreInspectionCheck(String driverName, int bus_number) {
        //TODO: Use the two arguments to get the latest record of the pre-inspection check
    }


    private void setupViewPager(ViewPager viewPager){
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(engineAndFluidFragment, "Engine and Fluid Levels");
        adapter.addFragment(exteriorCheckFragment, "Exterior Checks");
        adapter.addFragment(interiorCheckFragment, "Interior Checks");
        viewPager.setAdapter(adapter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        getApplicationContext().registerReceiver(receiver, new IntentFilter(ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        getApplicationContext().unregisterReceiver(receiver);
    }
}
