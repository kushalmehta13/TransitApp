package com.example.transitapp;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

public class PreInspectionActivity extends AppCompatActivity {
    //TODO: @kushal
    // 1. retrieve all the information from the firebase database to populate in the checks.
    // 2. Submit the pre inspection check
    // 3. retrieve the pre-inspection check if edit is clicked (IMPORTANT)

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_inspection);

        Intent i = getIntent();
        Bundle editBundle = i.getBundleExtra("editBundle");
        Boolean isEdit = editBundle.getBoolean("Edit");
        if(isEdit){
            // retrieve the pre-inspection check if edit is clicked (IMPORTANT)
            System.out.println("Must get the edit version");
        }


        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.container);
        mViewPager.setOffscreenPageLimit(2);
        setupViewPager(mViewPager);


        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


    }


    private void setupViewPager(ViewPager viewPager){
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new EngineAndFluidFragment(), "Engine and Fluid Levels");
        adapter.addFragment(new ExteriorChecksFragment(), "Exterior Checks");
        adapter.addFragment(new InteriorChecksFragment(), "Interior Checks");
        viewPager.setAdapter(adapter);
    }


}
