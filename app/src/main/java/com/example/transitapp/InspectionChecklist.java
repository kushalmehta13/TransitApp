package com.example.transitapp;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class InspectionChecklist {
    private RequestQueue queue;
    JSONObject jsonResponse;
    private HashMap<String, ArrayList> pre;
    private HashMap<String, ArrayList> post;
    private String BASE_URL_PRE = "https://us-central1-transitapp-d5956.cloudfunctions.net/GetInspectionCheckList?type=pre";
    private String BASE_URL_POST = "https://us-central1-transitapp-d5956.cloudfunctions.net/GetInspectionCheckList?type=post";
    public  boolean present = false;

    public InspectionChecklist(Context applicationContext){
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
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(BASE_URL_PRE);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String response = IOUtils.toString(in, StandardCharsets.UTF_8.name());
            jsonResponse = new JSONObject(response);
            JSONArray InteriorChecks = (JSONArray) jsonResponse.getJSONObject("_fieldsProto").getJSONObject("Interior Checks").getJSONObject("arrayValue").get("values");
            // Adding interior checklist
            pre.put("Interior Checks", new ArrayList());
            for(int i=0;i<InteriorChecks.length();++i){
                pre.get("Interior Checks").add(InteriorChecks.getJSONObject(i).get("stringValue"));
            }

            JSONArray ExteriorChecks = (JSONArray) jsonResponse.getJSONObject("_fieldsProto").getJSONObject("Exterior Checks").getJSONObject("arrayValue").get("values");
            // Adding exterior checklist
            pre.put("Exterior Checks", new ArrayList());
            for(int i=0;i<ExteriorChecks.length();++i){
                pre.get("Exterior Checks").add(ExteriorChecks.getJSONObject(i).get("stringValue"));
            }

            JSONArray EngineChecks = (JSONArray) jsonResponse.getJSONObject("_fieldsProto").getJSONObject("Engine & Fluids Levels").getJSONObject("arrayValue").get("values");
            // Adding Engine checklist
            pre.put("Engine Checks", new ArrayList());
            for(int i=0;i<EngineChecks.length();++i){
                pre.get("Engine Checks").add(EngineChecks.getJSONObject(i).get("stringValue"));
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        finally {
            urlConnection.disconnect();
        }


//        RequestFuture<JSONObject> future = RequestFuture.newFuture();
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, BASE_URL_PRE, new JSONObject(), future, future);
//        queue.add(request);
//
//        try {
////            JSONObject response = future.get(); // this will block
//            if(future.isDone()){
//                future.onResponse(jsonResponse);
//            }
//            System.out.println(jsonResponse);
//            JSONArray InteriorChecks = (JSONArray) jsonResponse.getJSONObject("_fieldsProto").getJSONObject("Interior Checks").getJSONObject("arrayValue").get("values");
//            // Adding interior checklist
//            pre.put("Interior Checks", new ArrayList());
//            for(int i=0;i<InteriorChecks.length();++i){
//                pre.get("Interior Checks").add(InteriorChecks.getJSONObject(i).get("stringValue"));
//            }
//
//            JSONArray ExteriorChecks = (JSONArray) jsonResponse.getJSONObject("_fieldsProto").getJSONObject("Exterior Checks").getJSONObject("arrayValue").get("values");
//            // Adding exterior checklist
//            pre.put("Exterior Checks", new ArrayList());
//            for(int i=0;i<ExteriorChecks.length();++i){
//                pre.get("Exterior Checks").add(ExteriorChecks.getJSONObject(i).get("stringValue"));
//            }
//
//            JSONArray EngineChecks = (JSONArray) jsonResponse.getJSONObject("_fieldsProto").getJSONObject("Engine & Fluids Levels").getJSONObject("arrayValue").get("values");
//            // Adding Engine checklist
//            pre.put("Engine Checks", new ArrayList());
//            for(int i=0;i<EngineChecks.length();++i){
//                pre.get("Engine Checks").add(EngineChecks.getJSONObject(i).get("stringValue"));
//            }
//
//        } catch (InterruptedException e) {
//            // exception handling
//        } catch (ExecutionException e) {
//            // exception handling
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }


//        StringRequest stringRequest = new StringRequest(Request.Method.GET ,BASE_URL_PRE, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    jsonResponse = new JSONObject(response);
//                    JSONArray InteriorChecks = (JSONArray) jsonResponse.getJSONObject("_fieldsProto").getJSONObject("Interior Checks").getJSONObject("arrayValue").get("values");
//                    // Adding interior checklist
//                    pre.put("Interior Checks", new ArrayList());
//                    for(int i=0;i<InteriorChecks.length();++i){
//                        pre.get("Interior Checks").add(InteriorChecks.getJSONObject(i).get("stringValue"));
//                    }
//
//                    JSONArray ExteriorChecks = (JSONArray) jsonResponse.getJSONObject("_fieldsProto").getJSONObject("Exterior Checks").getJSONObject("arrayValue").get("values");
//                    // Adding exterior checklist
//                    pre.put("Exterior Checks", new ArrayList());
//                    for(int i=0;i<ExteriorChecks.length();++i){
//                        pre.get("Exterior Checks").add(ExteriorChecks.getJSONObject(i).get("stringValue"));
//                    }
//
//                    JSONArray EngineChecks = (JSONArray) jsonResponse.getJSONObject("_fieldsProto").getJSONObject("Engine & Fluids Levels").getJSONObject("arrayValue").get("values");
//                    // Adding Engine checklist
//                    pre.put("Engine Checks", new ArrayList());
//                    for(int i=0;i<EngineChecks.length();++i){
//                        pre.get("Engine Checks").add(EngineChecks.getJSONObject(i).get("stringValue"));
//                    }
//
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                System.out.println("That did not work");
//            }
//        });
//        queue.add(stringRequest);
//        System.out.println(pre);
    }

}
