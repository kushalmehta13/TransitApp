package com.example.transitapp;

import android.content.Context;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class RouteScheduleStopRetriever {
    private Context c;
    private String mAction;
    private String BASE_URL_ROUTES = "https://us-central1-transitapp-d5956.cloudfunctions.net/api/getAllRoutes/Spring2019";
    private String BASE_URL_SCHEDULES = "https://us-central1-transitapp-d5956.cloudfunctions.net/api/getSchedule/Spring2019/:route?current_time=930&week_day=0" ;

    public RouteScheduleStopRetriever(Context applicationContext, String ACTION){
        c = applicationContext;
        mAction = ACTION;
    }

    public void getRoutes(){
        HttpGetRequest getRequest = new HttpGetRequest(c, mAction);
        getRequest.execute(BASE_URL_ROUTES);
    }
    
    public void getSchedules(){
        HttpGetRequest getRequest = new HttpGetRequest(c, mAction);
        getRequest.execute(BASE_URL_SCHEDULES);
    }
}
