package com.example.transitapp;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PostInspectionActivty extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private InspectionChecklist inspectionChecklist;
    private String ACTION = "ACTION_FOR_INTENT_CALLBACK_POST";

    private Fragment PostTripFrag;
    private Fragment FuelAndProblems;
    private Fragment BodyCondition;
    private Button send;
    private ProgressDialog progressDialog;
    public HashMap<String, HashMap<String, Object>> postInspectionCheckValues;
    public ArrayList<Uri> defects;
    private BroadcastReceiver receiver;

    private FirebaseFirestore db;

    private ArrayList<String> postCheck;
    private ArrayList<String> FuelAndOthers;
    private UpdateDatabase updateDatabase;

    private String driverName;
    private String timestamp;
    private int bus_number;
    private Bundle postBundle;
    private Bundle fuelBundle;

    public static String sentInspectionID;
    private Bundle bodyBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_inspection_activty);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        postInspectionCheckValues = new HashMap<String, HashMap<String, Object>>();
        defects = new ArrayList<>();
        updateDatabase = new UpdateDatabase();
        inspectionChecklist = new InspectionChecklist(getApplicationContext(), ACTION);
        inspectionChecklist.getPostList();
        progressDialog = ProgressDialog.show(this, "Getting Data", "Waiting for results...", true);
        db = FirebaseFirestore.getInstance();
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, final Intent intent) {
                System.out.println("Recieived in postinspection");
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                Intent i = getIntent();
                final Bundle b = i.getBundleExtra("editBundle");
                Boolean isEdit = b.getBoolean("Edit");
                driverName = b.getString("Driver name");
                System.out.println(driverName);
                bus_number = b.getInt("Bus number");
                System.out.println(bus_number);

                if(isEdit){
                    // retrieve the post-inspection check if edit is clicked (IMPORTANT)
                    progressDialog.show();
                    db.collection("Post Inspection Check").document(sentInspectionID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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


                    //do edit stuff

                } else{
                    timestamp = b.getString("Timestamp");
                    postCheck = intent.getStringArrayListExtra("postCheck");
                    postBundle = new Bundle();
                    fuelBundle = new Bundle();
                    bodyBundle = new Bundle();
                    postBundle.putStringArrayList("postTripChecklist", postCheck);
                    bodyBundle.putInt("bus_number", bus_number);

                    PostTripFrag = new PostTripFragment();
                    BodyCondition = new BodyCondition();
                    FuelAndProblems = new FuelAndProblems();
                    PostTripFrag.setArguments(postBundle);
                    FuelAndProblems.setArguments(fuelBundle);
                    BodyCondition.setArguments(bodyBundle);


                    mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

                    mViewPager = findViewById(R.id.container);
                    mViewPager.setOffscreenPageLimit(2);
                    setupViewPager(mViewPager);

                    TabLayout tabLayout = findViewById(R.id.tabs);
                    tabLayout.setupWithViewPager(mViewPager);

                }


            }
        };



        /* TODO: @KUSHAL
            1. CREATE POST INSPECTION ACTIVITY SKELETON - DONE
            2. IMPLEMENT POST INSPECTION ACTIVITY FUNCTIONS - MAIN
        */




        FloatingActionButton send = findViewById(R.id.submit);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(postInspectionCheckValues);
                System.out.println(defects);
                updateDatabase.sendPostInspectionCheck(postInspectionCheckValues, driverName, bus_number, sentInspectionID, timestamp, defects);
                System.out.println(sentInspectionID);
                Intent intent = new Intent();
                intent.putExtra(DriverDashboard.SHOW_POST_EDIT, true);
                setResult(RESULT_OK, intent);
                finish();
            }
        });


    }

    private void setupViewPager(ViewPager viewPager){
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(PostTripFrag,"Post trip checklist");
        adapter.addFragment(BodyCondition, "Body Condition");
        adapter.addFragment(FuelAndProblems, "Fuel levels and other problems");
        viewPager.setAdapter(adapter);

    }

    private void handleResponse(DocumentSnapshot documentSnapshot, Bundle b, Intent intent) {
        HashMap checks = (HashMap<String, HashMap<String, Boolean>>) documentSnapshot.getData().get("checks");
        HashMap PostInspectionCompleted = new HashMap<String, HashMap<String, Boolean>>();
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
            postInspectionCheckValues.put(new_key, innerChecks);
        }
        timestamp = b.getString("Timestamp");
        postCheck = intent.getStringArrayListExtra("postCheck");
        postBundle = new Bundle();
        fuelBundle = new Bundle();
        bodyBundle = new Bundle();
        postBundle.putStringArrayList("postTripChecklist", postCheck);
        bodyBundle.putInt("bus_number", bus_number);
        postBundle.putSerializable("toEdit", postInspectionCheckValues.get("PostTripChecks"));
        fuelBundle.putSerializable("toEdit", postInspectionCheckValues.get("FuelAndOtherProblems"));

        PostTripFrag = new PostTripFragment();
        BodyCondition = new BodyCondition();
        FuelAndProblems = new FuelAndProblems();
        PostTripFrag.setArguments(postBundle);
        FuelAndProblems.setArguments(fuelBundle);
        BodyCondition.setArguments(bodyBundle);
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
