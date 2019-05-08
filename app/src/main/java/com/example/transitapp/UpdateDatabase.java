package com.example.transitapp;


import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UpdateDatabase {
    private FirebaseFirestoreSettings settings;
    private FirebaseFirestore db;
    public UpdateDatabase() {
        settings = new FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build();
        db = FirebaseFirestore.getInstance();
        db.setFirestoreSettings(settings);
    }

    public void sendPreInspectionCheck(HashMap<String, HashMap<String, Boolean>> preInspectionCheckValues, HashMap other, String driverName, int bus_number, final String docID, String timestamp){
        Map<String, Object> docData = new HashMap<>();
        docData.put("checks", formatPreInspectionValues(preInspectionCheckValues));
        if(other!=null){
            docData.put("Others", other);
        }
        docData.put("driver_name", driverName);
        docData.put("bus_number", bus_number);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
        Date parsedDate = null;
        try {
            parsedDate = dateFormat.parse(timestamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        docData.put("Timestamp", parsedDate);
        if(docID!=null){
            db.collection("Pre Inspection Check").document(docID).set(docData).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    PreInspectionActivity.sentInspectionID = docID;
                }
            });
        }
        else{
            db.collection("Pre Inspection Check").add(docData).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    PreInspectionActivity.sentInspectionID = task.getResult().getId();
                }
            });
        }
    }

    private Object formatPreInspectionValues(HashMap<String, HashMap<String, Boolean>> preInspectionCheckValues) {
        HashMap<String, HashMap<String, Boolean>> formattedValues = new HashMap<>();
        Iterator it = preInspectionCheckValues.entrySet().iterator();
        while(it.hasNext()) {
            HashMap<String, Boolean> temp = new HashMap<>();
            HashMap<String, Boolean> new_inner = new HashMap<>();
            Map.Entry pair = (Map.Entry) it.next();
            String new_key = pair.getKey().toString().replaceAll("\\s+", "_");
            temp = (HashMap<String, Boolean>) pair.getValue();
            Iterator inner = temp.entrySet().iterator();
            while(inner.hasNext()){
                Map.Entry innerPair = (Map.Entry) inner.next();
                String new_inner_key = innerPair.getKey().toString().replaceAll("\\s+","_");
                new_inner.put(new_inner_key, (Boolean) innerPair.getValue());
            }
            formattedValues.put(new_key, new_inner);
        }
        return formattedValues;
    }
}
