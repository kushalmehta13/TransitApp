package com.example.transitapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Enroute_Dashboard extends AppCompatActivity implements Enroute_Stop_Fragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroute__dashboard);

        Intent myIntent = getIntent();
        String data = myIntent.getStringExtra(DriverDashboard.Key1);


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
