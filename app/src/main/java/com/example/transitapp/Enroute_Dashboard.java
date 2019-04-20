package com.example.transitapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class Enroute_Dashboard extends AppCompatActivity implements Enroute_Stop_Fragment.OnFragmentInteractionListener,
        View.OnClickListener, ViewPager.OnPageChangeListener, SeekBar.OnSeekBarChangeListener {

    TextView Marquee_Txt, enroute_dash_TextView;
    Button Previous_Btn, Next_Btn;
    SeekBar seek_Bar;

    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroute__dashboard);

        Intent myIntent = getIntent();
        String data = myIntent.getStringExtra(DriverDashboard.Key1);

        Marquee_Txt = (TextView) findViewById(R.id.marquee_TextView);
        enroute_dash_TextView = (TextView) findViewById(R.id.enroutedashboardtxt);
        Marquee_Txt.setSelected(true);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // landscape
            enroute_dash_TextView.setText("Enroute DashBoard");
        } else {
            // portrait
            enroute_dash_TextView.setText("Enroute \nDashBoard");
        }


        seek_Bar = (SeekBar) findViewById(R.id.seekBar);
        Previous_Btn = (Button) findViewById(R.id.prev_btn);
        Next_Btn = (Button) findViewById(R.id.next_btn);
        Previous_Btn.setOnClickListener(this);
        Next_Btn.setOnClickListener(this);

        mViewPager = findViewById(R.id.viewPager);
        mViewPager.setOffscreenPageLimit(6);
        mViewPager.addOnPageChangeListener(this);
        setupViewPager(mViewPager);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            seek_Bar.setProgress(0,true);
        }
        seek_Bar.setMax(5);
        seek_Bar.setOnSeekBarChangeListener(this);


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void setupViewPager(ViewPager viewPager){
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Enroute_Stop_Fragment(), "Stop 1");
        adapter.addFragment(new Enroute_Stop_Fragment(), "Stop 2");
        adapter.addFragment(new Enroute_Stop_Fragment(), "Stop 3");
        adapter.addFragment(new Enroute_Stop_Fragment(), "Stop 4");
        adapter.addFragment(new Enroute_Stop_Fragment(), "Stop 5");
        adapter.addFragment(new Enroute_Stop_Fragment(), "Stop 6");
        viewPager.setAdapter(adapter);
    }


    @Override
    public void onClick(View v) {

        int curr_VPager_Position = mViewPager.getCurrentItem();


        switch (v.getId()){
            case R.id.prev_btn:
                if(curr_VPager_Position ==0){
                    Toast.makeText(Enroute_Dashboard.this, "This is First Stop!!", Toast.LENGTH_LONG).show();
                }else{
                    mViewPager.setCurrentItem((curr_VPager_Position-1), true);
                }
                break;

            case R.id.next_btn:
                if(curr_VPager_Position ==5){
                    Toast.makeText(Enroute_Dashboard.this, "This is Last Stop!!", Toast.LENGTH_LONG).show();
                }else{
                    mViewPager.setCurrentItem((curr_VPager_Position+1), true);
                }
                break;

        }

    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {
        int curr_VPager_Position = mViewPager.getCurrentItem();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            seek_Bar.setProgress(curr_VPager_Position,true);
        }
    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    //SeekBar Listener Methods
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mViewPager.setCurrentItem(progress,true);

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
