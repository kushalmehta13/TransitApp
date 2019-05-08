package com.example.transitapp;

import org.json.JSONObject;

import java.util.HashMap;

public class PreInspectionDocument {
    private JSONObject obj;

    public PreInspectionDocument(JSONObject obj) {
        this.obj = obj;
    }

    public JSONObject getObj() {
        return obj;
    }

    public void setObj(JSONObject obj) {
        this.obj = obj;
    }
}
