package com.example.transitapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.VideoView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class splash extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 3000;
    private BroadcastReceiver routesReciever;
    private RouteScheduleStopRetriever routeScheduleStopRetriever;
    private HashMap<String, Integer> routes;
    private String ACTION = "ACTION_FOR_INTENT_CALLBACK_ROUTES";
    VideoView videoView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        routeScheduleStopRetriever = new RouteScheduleStopRetriever(getApplicationContext(), ACTION);
        routeScheduleStopRetriever.getRoutes();
        routesReciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                routes = (HashMap<String, Integer>) intent.getSerializableExtra("Routes");
                ArrayList<String> routeArray = new ArrayList<>();
                Iterator it = routes.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    String r = (String) pair.getKey();
                    routeArray.add(r);
                }
                DriverDashboard.routeArray = Arrays.copyOf(routeArray.toArray(), routeArray.toArray().length, String[].class);
            }
        };

    }


    @Override
    public void onResume(){
        super.onResume();
        getApplicationContext().registerReceiver(routesReciever, new IntentFilter(ACTION));
        setContentView(R.layout.activity_splash);
        videoView = (VideoView) findViewById(R.id.videoView);

        Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.splash_logo_video_fhd);
        videoView.setVideoURI(video);
        videoView.requestFocus();
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                startNextActivity();
            }
        });



        videoView.start();

    }


    private void startNextActivity() {
        if (isFinishing())
            return;
        startActivity(new Intent(this, FirebaseUIActivity.class));
        finish();
    }
}

    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        videoView = (VideoView) findViewById(R.id.videoView);

        Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.splash_logo_video_fhd);
        videoView.setVideoURI(video);


*//*        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(splash.this, FirebaseUIActivity.class);
                splash.this.startActivity(mainIntent);
                splash.this.finish();

            }
        }, SPLASH_DISPLAY_LENGTH);*//*


        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                startNextActivity();
            }
        });

        videoView.start();
    }

    private void startNextActivity() {
        if (isFinishing())
            return;
        startActivity(new Intent(this, FirebaseUIActivity.class));
        finish();
    }


    }
}
*/