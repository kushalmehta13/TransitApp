package com.example.transitapp;


import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UpdateDatabase {
    private FirebaseFirestoreSettings settings;
    private FirebaseFirestore db;
    public UpdateDatabase() {
        settings = new FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build();
        db = FirebaseFirestore.getInstance();
        db.setFirestoreSettings(settings);
    }

    public void sendPreInspectionCheck(HashMap<String, HashMap<String, Boolean>> preInspectionCheckValues, HashMap other, String driverName, int bus_number, String timestamp){
        Map<String, Object> docData = new HashMap<>();
        docData.put("Checks", preInspectionCheckValues);
        if(other!=null){
            docData.put("Others", other);
        }
        docData.put("Driver Name", driverName);
        docData.put("Bus Number", bus_number);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
        Date parsedDate = null;
        try {
            parsedDate = dateFormat.parse(timestamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        docData.put("Timestamp", parsedDate);
        db.collection("Pre Inspection Check").add(docData);
    }
}
