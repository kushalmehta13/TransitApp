package com.example.transitapp;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PreInspectionActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private InspectionChecklist inspectionChecklist;
    private String ACTION = "ACTION_FOR_INTENT_CALLBACK";
    private ProgressDialog progressDialog;
    private Fragment engineAndFluidFragment;
    private Fragment exteriorCheckFragment;
    private Fragment interiorCheckFragment;
    private TextView bus_num;


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
    public HashMap<String, HashMap<String, Object>> preInspectionCheckValues;
    public HashMap<String, String> others;

    private FirebaseFirestore db;


    public static String sentInspectionID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_inspection);
        bus_num = findViewById(R.id.bus_num);
        preInspectionCheckValues = new HashMap<>();
        others = new HashMap<>();
        updateDatabase = new UpdateDatabase();
        send = findViewById(R.id.submit);
        inspectionChecklist = new InspectionChecklist(getApplicationContext(), ACTION);
        inspectionChecklist.getPreList();
        progressDialog = ProgressDialog.show(this, "Getting Data", "Waiting for results...", true);
        db = FirebaseFirestore.getInstance();

        receiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, final Intent intent)
            {
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                Intent i = getIntent();
                final Bundle b = i.getBundleExtra("editBundle");
                Boolean isEdit = b.getBoolean("Edit");
                driverName = b.getString("Driver name");
                bus_number = b.getInt("Bus number");
                bus_num.setText("Bus Number: "+ bus_number);
                if(isEdit){
                    // retrieve the pre-inspection check if edit is clicked (IMPORTANT)
                    progressDialog.show();
                    db.collection("Pre Inspection Check").document(sentInspectionID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            handleResponse(documentSnapshot, b, intent);
                            progressDialog.dismiss();


                            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

                            mViewPager = findViewById(R.id.container);
                            mViewPager.setOffscreenPageLimit(2);
                            setupViewPager(mViewPager);



                            TabLayout tabLayout = findViewById(R.id.tabs);
                            tabLayout.setupWithViewPager(mViewPager);

                        }
                    });
//                    getLatestPreInspectionCheck(driverName, bus_number);
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

                    mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

                    mViewPager = findViewById(R.id.container);
                    mViewPager.setOffscreenPageLimit(2);
                    setupViewPager(mViewPager);



                    TabLayout tabLayout = findViewById(R.id.tabs);
                    tabLayout.setupWithViewPager(mViewPager);
                }


            }
        };


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDatabase.sendPreInspectionCheck(preInspectionCheckValues, others, driverName, bus_number, sentInspectionID, timestamp);
                Intent intent = new Intent();
                intent.putExtra(DriverDashboard.SHOW_PRE_EDIT, true);
                setResult(RESULT_OK, intent);
                finish();
            }
        });


    }

    private void handleResponse(DocumentSnapshot documentSnapshot, Bundle b, Intent intent) {
        HashMap checks = (HashMap<String, HashMap<String, Boolean>>) documentSnapshot.getData().get("checks");
        HashMap PreInspectionCompleted = new HashMap<String, HashMap<String, Boolean>>();
        Iterator it = checks.entrySet().iterator();
        while(it.hasNext()){
            HashMap innerChecks = new HashMap<String, Boolean>();
            Map.Entry pair = (Map.Entry) it.next();
            String new_key = pair.getKey().toString().replaceAll("_"," ");
            HashMap<String, Boolean> inner_temp = (HashMap) pair.getValue();
            Iterator inner = inner_temp.entrySet().iterator();
            while(inner.hasNext()){
                Map.Entry innerPair = (Map.Entry) inner.next();
                String new_inner_key = innerPair.getKey().toString().replaceAll("_"," ");
                innerChecks.put(new_inner_key, innerPair.getValue());
            }
            preInspectionCheckValues.put(new_key, innerChecks);
        }
        timestamp = b.getString("Timestamp");
        interiorChecks = intent.getStringArrayListExtra("IntCheck");
        exteriorChecks = intent.getStringArrayListExtra("ExtCheck");
        engineChecks = intent.getStringArrayListExtra("EngCheck");
        inBundle = new Bundle();
        exBundle = new Bundle();
        enBundle = new Bundle();
        inBundle.putStringArrayList("InteriorChecklist", interiorChecks);
        inBundle.putSerializable("toEdit", preInspectionCheckValues.get("Interior Checks"));
        exBundle.putStringArrayList("ExteriorChecklist", exteriorChecks);
        exBundle.putSerializable("toEdit", preInspectionCheckValues.get("Exterior Checks"));
        enBundle.putStringArrayList("EngineChecklist", engineChecks);
        enBundle.putSerializable("toEdit", preInspectionCheckValues.get("Engine Checks"));
        engineAndFluidFragment = new EngineAndFluidFragment();
        exteriorCheckFragment = new ExteriorChecksFragment();
        interiorCheckFragment = new InteriorChecksFragment();
        engineAndFluidFragment.setArguments(enBundle);
        exteriorCheckFragment.setArguments(exBundle);
        interiorCheckFragment.setArguments(inBundle);
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
