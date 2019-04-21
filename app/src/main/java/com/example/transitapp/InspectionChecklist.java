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
    private RequestQueue queue;
    private Context c;
    private String mAction;
    JSONObject jsonResponse;
    private HashMap<String, ArrayList> pre;
    private HashMap<String, ArrayList> post;
    private String BASE_URL_PRE = "https://us-central1-transitapp-d5956.cloudfunctions.net/GetInspectionCheckList?type=pre";
    private String BASE_URL_POST = "https://us-central1-transitapp-d5956.cloudfunctions.net/GetInspectionCheckList?type=post";
    public  boolean present = false;

    public InspectionChecklist(Context applicationContext, String ACTION){
        c = applicationContext;
        mAction = ACTION;
        queue = Volley.newRequestQueue(applicationContext);
        pre = new HashMap<String, ArrayList>();
        post = new HashMap<String, ArrayList>();
    }

    public HashMap<String, ArrayList> getPre() {
        return pre;
    }

    public void setPre(HashMap<String, ArrayList> pre) {
        this.pre = pre;
    }

    public HashMap<String, ArrayList> getPost() {
        return post;
    }

    public void setPost(HashMap<String, ArrayList> post) {
        this.post = post;
    }

    public void getPreList(){
        HttpGetRequest getRequest = new HttpGetRequest(c, mAction);
        getRequest.execute(BASE_URL_PRE);
    }

}
