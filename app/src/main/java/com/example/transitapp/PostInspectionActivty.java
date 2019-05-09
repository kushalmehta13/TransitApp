package com.example.transitapp;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class PostInspectionActivty extends AppCompatActivity {

    private SectionsPagerAdapter mSectioPagerAdapter;
    private ViewPager mViewPager;
    private InspectionChecklist inspectionChecklist;
    private String ACTION = "ACTION_FOR_INTENT_CALLBACK_POST";

    private Fragment PostTripFrag;
    private Fragment FuelAndProblems;
    private Fragment BodyCondition;
    private Button send;
    private ProgressDialog progressDialog;
    public HashMap<String, Boolean> postInspectionCheckValues;
    private BroadcastReceiver receiver;

    private FirebaseFirestore db;

    private ArrayList<String> post;
    private UpdateDatabase updateDatabase;

    private String driverName;
    private String timestamp;
    private int bus_number;
    private Bundle postBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_inspection_activty);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        postInspectionCheckValues = new HashMap<>();
        updateDatabase = new UpdateDatabase();
        inspectionChecklist = new InspectionChecklist(getApplicationContext(), ACTION);
        inspectionChecklist.getPostList();
        progressDialog = ProgressDialog.show(this, "Getting Data", "Waiting for results...", true);
        db = FirebaseFirestore.getInstance();
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                System.out.println("Recieived in postinspection");
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                Intent i = getIntent();
                final Bundle b = i.getBundleExtra("editBundle");
                Boolean isEdit = b.getBoolean("Edit");
                driverName = b.getString("Diver Name");
                bus_number = b.getInt("Bus Number");

                if(isEdit){
                    //do edit stuff
                } else{
                    timestamp = b.getString("Timestamp");
                    post = intent.getStringArrayListExtra("postCheck");
                    postBundle = new Bundle();
                    postBundle.putStringArrayList("postTripChecklist", post);

                    PostTripFrag = new PostTripFragment();
                    BodyCondition = new BodyCondition();
                    FuelAndProblems = new FuelAndProblems();
                    PostTripFrag.setArguments(postBundle);



                    mSectioPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

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




        FloatingActionButton fab = findViewById(R.id.submit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
