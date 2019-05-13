package com.example.transitapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class Enroute_Dashboard extends AppCompatActivity implements Enroute_Stop_Fragment.OnFragmentInteractionListener,
        View.OnClickListener, ViewPager.OnPageChangeListener, SeekBar.OnSeekBarChangeListener, LocationListener {

    TextView Marquee_Txt, enroute_dash_TextView;
    ImageButton Previous_Btn, Next_Btn;
    SeekBar seek_Bar;
    ImageButton Finish_Trip;
    public static String route;
    public static String schedule;
    public static String driverName;
    public static int busNumber;
    private String ACTION = "ACTION_FOR_INTENT_CALLBACK_STOPS";

    private ViewPager mViewPager;
    private TextView driverInfo, Loading_msg;


    private BroadcastReceiver stopsReceiver;

    private HashMap<String, LatLng> locations;
    private ArrayList<String> stops;
    private ArrayList<Boolean> visited;
    private ProgressDialog progressDialog;
    private LocationManager locationManager;
    public static ArrayList<TripDetails> tripDetailList;
    private ArrayList<Enroute_Stop_Fragment> fragmentList;
    private int index;
    private ProgressBar progress_Bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroute__dashboard);
        Finish_Trip = (ImageButton) findViewById(R.id.finish_Trip_ImgButton);
        Finish_Trip.setOnClickListener(Enroute_Dashboard.this);
        Loading_msg = (TextView) findViewById(R.id.loading_msg_txtView);
        progress_Bar = (ProgressBar) findViewById(R.id.progressBar);
        //Loading_msg.setVisibility(View.GONE);
        Finish_Trip.setVisibility(View.INVISIBLE);


        RouteScheduleStopRetriever routeScheduleStopRetriever = new RouteScheduleStopRetriever(getApplicationContext(), ACTION);
        routeScheduleStopRetriever.getStops(route);
        progressDialog = ProgressDialog.show(this, "Getting Stops", "Waiting for data...", true);
        stopsReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                stops = intent.getStringArrayListExtra("Stop_names");
                System.out.println(stops.size());
                locations = (HashMap<String, LatLng>) intent.getSerializableExtra("Locations");
                tripDetailList = new ArrayList<>();
                fragmentList = new ArrayList<>();
                for(int i=0;i<stops.size();++i){
                    fragmentList.add(null);
                    tripDetailList.add(null);
                }
                visited = new ArrayList<>();
                Intent myIntent = getIntent();
                String data = myIntent.getStringExtra(DriverDashboard.Key1);

                driverInfo = (TextView) findViewById(R.id.driver_dash_driver_name2);
                Marquee_Txt = (TextView) findViewById(R.id.marquee_TextView);
                enroute_dash_TextView = (TextView) findViewById(R.id.enroutedashboardtxt);
                Marquee_Txt.setSelected(true);

                String info = "Bus No : "+busNumber+" | Route : "+route;
                String welcome = "Welcome, "+driverName;
                Marquee_Txt.setText(info);
                driverInfo.setText(welcome);
/*        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // landscape
            enroute_dash_TextView.setText("Enroute DashBoard");
        } else {
            // portrait
            enroute_dash_TextView.setText("Enroute \nDashBoard");
        }*/


                seek_Bar = (SeekBar) findViewById(R.id.seekBar);
                Previous_Btn = (ImageButton) findViewById(R.id.prev_btn);

                Next_Btn = (ImageButton) findViewById(R.id.next_btn);
                Previous_Btn.setOnClickListener(Enroute_Dashboard.this);

                Next_Btn.setOnClickListener(Enroute_Dashboard.this);


                mViewPager = findViewById(R.id.viewPager);
                mViewPager.setOffscreenPageLimit(stops.size()-1);
                mViewPager.addOnPageChangeListener(Enroute_Dashboard.this);
                setupViewPager(mViewPager, stops);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    seek_Bar.setProgress(0,true);
                }
                seek_Bar.setMax(stops.size()-1);
                seek_Bar.setOnSeekBarChangeListener(Enroute_Dashboard.this);
                index = 0;
                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                startLocationService();

                progressDialog.dismiss();
                Loading_msg.setVisibility(View.GONE);
                progress_Bar.setVisibility(View.GONE);

            }
        };






    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void setupViewPager(ViewPager viewPager, ArrayList<String> stops){
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        int i = 0;
        for(String stop: stops){
            visited.add(false);
            Enroute_Stop_Fragment f =  Enroute_Stop_Fragment.newInstance(stop,0,0,0, new TripDetails());
            fragmentList.set(i++,f);
            adapter.addFragment(f, stop);
        }
        System.out.println(fragmentList);
        viewPager.setAdapter(adapter);
    }


    @Override
    public void onClick(View v) {

        int curr_VPager_Position = mViewPager.getCurrentItem();
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 1);


        switch (v.getId()){
            case R.id.prev_btn:
                myAnim.setInterpolator(interpolator);
                Previous_Btn.startAnimation(myAnim);
                if(curr_VPager_Position ==0){
                    Toast.makeText(Enroute_Dashboard.this, "This is First Stop!!", Toast.LENGTH_LONG).show();
                }else{
                    mViewPager.setCurrentItem((curr_VPager_Position-1), true);
                }
                break;

            case R.id.next_btn:
                myAnim.setInterpolator(interpolator);
                Next_Btn.startAnimation(myAnim);
                if(curr_VPager_Position ==stops.size()-1){
                    Toast.makeText(Enroute_Dashboard.this, "This is Last Stop!!", Toast.LENGTH_LONG).show();
                }else{
                    mViewPager.setCurrentItem((curr_VPager_Position+1), true);
                }
                break;


            case R.id.finish_Trip_ImgButton:
                MyBounceInterpolator interpolator1 = new MyBounceInterpolator(0.2, 20);
                myAnim.setInterpolator(interpolator1);
                Finish_Trip.startAnimation(myAnim);
                sendTripDetails(tripDetailList);
                finish();
                break;


        }

    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {
        int curr_VPager_Position = mViewPager.getCurrentItem();
        tripDetailList.set(curr_VPager_Position, fragmentList.get(curr_VPager_Position).trip_details);
        System.out.println(curr_VPager_Position);
        System.out.println(fragmentList.get(curr_VPager_Position).trip_details);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            seek_Bar.setProgress(curr_VPager_Position,true);
        }


        if(curr_VPager_Position==stops.size()-1){
            final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
            MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
            myAnim.setInterpolator(interpolator);
            Finish_Trip.setVisibility(View.VISIBLE);
            Finish_Trip.startAnimation(myAnim);

/*            Next_Btn.setText("Last Stop!");
            Next_Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendTripDetails(tripDetailList);
                }
            });*/
        }
    }

    private void sendTripDetails(ArrayList<TripDetails> tripDetailList) {
        UpdateDatabase updateDatabase = new UpdateDatabase();
        updateDatabase.sendTripDetails(tripDetailList, route);
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

    @Override
    protected void onResume() {
        super.onResume();
        getApplicationContext().registerReceiver(stopsReceiver, new IntentFilter(ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng loc = locations.get(stops.get(index));
        Location stop_location = new Location(LocationManager.GPS_PROVIDER);
        stop_location.setLatitude(loc.latitude);
        stop_location.setLongitude(loc.longitude);
        double distance = location.distanceTo(stop_location);

        if(distance < 150 && !visited.get(index)){
            mViewPager.setCurrentItem(index);
            visited.set(index, true);
            if(index < stops.size()-1) {
                index+=1;
            }
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    public void startLocationService() {
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if(result == 0){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0, 0, this);
        }

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
