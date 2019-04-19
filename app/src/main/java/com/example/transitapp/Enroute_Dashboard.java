package com.example.transitapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Enroute_Dashboard extends AppCompatActivity implements Enroute_Stop_Fragment.OnFragmentInteractionListener {

    TextView Marquee_Txt, enroute_dash_TextView;

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

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
