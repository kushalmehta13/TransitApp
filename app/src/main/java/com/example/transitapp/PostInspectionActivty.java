package com.example.transitapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class PostInspectionActivty extends AppCompatActivity {

    private SectionsPagerAdapter mSectioPagerAdapter;
    private ViewPager mViewPager;

    private Fragment PostTrip;
    private Fragment FuelAndProblems;
    private Fragment BodyCondition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_inspection_activty);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        /* TODO: @KUSHAL
            1. CREATE POST INSPECTION ACTIVITY SKELETON - DONE
            2. IMPLEMENT POST INSPECTION ACTIVITY FUNCTIONS - MAIN
        */
        PostTrip = new PostTrip();
        BodyCondition = new BodyCondition();
        FuelAndProblems = new FuelAndProblems();


        mSectioPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.container);
        mViewPager.setOffscreenPageLimit(2);
        setupViewPager(mViewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);




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
        adapter.addFragment(PostTrip,"Post trip checklist");
        adapter.addFragment(BodyCondition, "Body Condition");
        adapter.addFragment(FuelAndProblems, "Fuel levels and other problems");
        viewPager.setAdapter(adapter);

    }

}
