package com.example.transitapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class HttpGetRequest extends AsyncTask<String, Void, String> {

    private Context mContext;
    private String mAction;
    public HttpGetRequest(Context c, String mAction){
        mContext = c;
        this.mAction = mAction;
    }
    @Override
    protected String doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        String response = null;
        try {
            URL url = new URL(params[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder res = new StringBuilder();
            for (String line; (line = r.readLine()) != null; ) {
                res.append(line).append('\n');
            }
            response = res.toString();
        } catch (Exception e){
            e.printStackTrace();
            response = null;
        }
        finally {
            urlConnection.disconnect();
        }
        return response;
    }

    protected void onPostExecute(String result){
        super.onPostExecute(result);
        // TODO: check what the result is for. Ideally, check a field for the URL and check its parameters
        try {
            JSONObject jsonResponseChecker = new JSONObject(result);
            if(jsonResponseChecker.has("checkListType"))
            {
                String type = (String) jsonResponseChecker.get("checkListType");
                if (type.equals("Pre_Inspection_Check")) {
                    handlePreInspectionCheck(result);
                } else if(type.equals("Post_Inspection_Check")) {
                    handlePostInspectionCheck(result);
                }
            }
            else{
                if(jsonResponseChecker.has("UMBC")){
                    handleRoutes(result);
                }
            }
        } catch (JSONException e){
            e.printStackTrace();
        }

        

    }

    private void handleRoutes(String result) {
        HashMap<String, Integer> routes = new HashMap<>();
        try {
            JSONObject jsonResponse = new JSONObject(result);
            Iterator<String> keys = jsonResponse.keys();
            while(keys.hasNext()){
                String key = keys.next();
                if(key.equals("UMBC")){
                    continue;
                }
                routes.put(key, (Integer) jsonResponse.get(key));
            }
            Intent i = new Intent(mAction);
            i.putExtra("Routes", routes);
            mContext.sendBroadcast(i);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void handlePostInspectionCheck(String result) {
        HashMap<String, ArrayList> post = new HashMap<>();
        try{
            JSONObject jsonResponse = new JSONObject(result);
            JSONArray postTripChecks = (JSONArray) jsonResponse.getJSONObject("values").get("Post-Trip");
            System.out.println(postTripChecks);
            post.put("Post Trip Check", new ArrayList());
            for(int i=0; i<postTripChecks.length(); ++i){
                post.get("Post Trip Check").add(postTripChecks.get(i));
            }


            Intent i = new Intent(mAction);
            i.putExtra("postCheck", post.get("Post Trip Check"));
            mContext.sendBroadcast(i);
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void handlePreInspectionCheck(String result) {
        HashMap<String, ArrayList> pre = new HashMap<>();
        try {
            JSONObject jsonResponse = new JSONObject(result);
            JSONArray InteriorChecks = (JSONArray) jsonResponse.getJSONObject("values").get("interior_checks");
            System.out.println(InteriorChecks);
            // Adding interior checklist
            pre.put("Interior Checks", new ArrayList());
            for(int i=0;i<InteriorChecks.length();++i){
                pre.get("Interior Checks").add(InteriorChecks.get(i));
            }

            JSONArray ExteriorChecks = (JSONArray) jsonResponse.getJSONObject("values").get("exterior_checks");
            // Adding exterior checklist
            pre.put("Exterior Checks", new ArrayList());
            for(int i=0;i<ExteriorChecks.length();++i){
                pre.get("Exterior Checks").add(ExteriorChecks.get(i));
            }

            JSONArray EngineChecks = (JSONArray) jsonResponse.getJSONObject("values").get("engine_and_fluids_levels");
            // Adding Engine checklist
            pre.put("Engine Checks", new ArrayList());
            for(int i=0;i<EngineChecks.length();++i){
                pre.get("Engine Checks").add(EngineChecks.get(i));
            }
            Intent i = new Intent(mAction);
            i.putExtra("IntCheck", pre.get("Interior Checks"));
            i.putExtra("ExtCheck", pre.get("Exterior Checks"));
            i.putExtra("EngCheck", pre.get("Engine Checks"));
            mContext.sendBroadcast(i);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
