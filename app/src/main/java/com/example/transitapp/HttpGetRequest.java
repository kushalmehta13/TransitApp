package com.example.transitapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

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
        HashMap<String, ArrayList> pre = new HashMap<>();
        try {
            JSONObject jsonResponse = new JSONObject(result);
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
