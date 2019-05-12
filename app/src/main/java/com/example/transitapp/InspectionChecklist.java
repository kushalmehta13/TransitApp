package com.example.transitapp;

import android.content.Context;
import android.content.Intent;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class InspectionChecklist {
    private Context c;
    private String mAction;
    private String BASE_URL_PRE = "https://us-central1-transitapp-d5956.cloudfunctions.net/api/GetInspectionCheckList?type=pre";
    private String BASE_URL_POST = "https://us-central1-transitapp-d5956.cloudfunctions.net/api/GetInspectionCheckList?type=post";

    public InspectionChecklist(Context applicationContext, String ACTION){
        c = applicationContext;
        mAction = ACTION;
    }
    public void getPreList(){
        HttpGetRequest getRequest = new HttpGetRequest(c, mAction);
        getRequest.execute(BASE_URL_PRE);
    }

    public void getPostList(){
        HttpGetRequest getRequest = new HttpGetRequest(c, mAction);
        getRequest.execute(BASE_URL_POST);
    }
}
