package com.example.transitapp;


import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UpdateDatabase {
    private FirebaseFirestoreSettings settings;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    public UpdateDatabase() {
        settings = new FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build();
        db = FirebaseFirestore.getInstance();
        db.setFirestoreSettings(settings);
        storage = FirebaseStorage.getInstance();
    }

    public void sendPreInspectionCheck(HashMap<String, HashMap<String, Object>> preInspectionCheckValues, HashMap other, String driverName, int bus_number, final String docID, String timestamp){
        Map<String, Object> docData = new HashMap<>();
        docData.put("checks", formatInspectionValues(preInspectionCheckValues));
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

    private Object formatInspectionValues(HashMap<String, HashMap<String, Object>> InspectionCheckValues) {
        HashMap<String, HashMap<String, Object>> formattedValues = new HashMap<>();
        Iterator it = InspectionCheckValues.entrySet().iterator();
        while(it.hasNext()) {
            HashMap<String, Object> temp = new HashMap<>();
            HashMap<String, Object> new_inner = new HashMap<>();
            Map.Entry pair = (Map.Entry) it.next();
            String new_key = pair.getKey().toString().replaceAll("\\s+", "_");
            temp = (HashMap<String, Object>) pair.getValue();
            Iterator inner = temp.entrySet().iterator();
            while(inner.hasNext()){
                Map.Entry innerPair = (Map.Entry) inner.next();
                String new_inner_key = innerPair.getKey().toString().replaceAll("\\s+","_");
                new_inner.put(new_inner_key, innerPair.getValue());
            }
            formattedValues.put(new_key, new_inner);
        }
        return formattedValues;
    }


    public void sendPostInspectionCheck(final HashMap<String, HashMap<String, Object>> postInspectionCheckValues, String driverName, final int bus_number, final String docID, final String timestamp, final ArrayList<Uri> defects) {
        final Map<String, Object> docData = new HashMap<>();
        docData.put("checks", formatInspectionValues(postInspectionCheckValues));
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
        final ArrayList<String> urls = new ArrayList<>();
        if(docID!=null){
            if(defects.size()>0)
            {
                handleImageUploads(docID, defects, bus_number, timestamp, urls, docData);
            }
            db.collection("Post Inspection Check").document(docID).set(docData).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    PostInspectionActivty.sentInspectionID = docID;
                }
            });

        }
        else{
            db.collection("Post Inspection Check").add(docData).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    final String id = task.getResult().getId();
                    PostInspectionActivty.sentInspectionID = id;
                    System.out.println(id);
                    if(defects.size()>0){
                        handleImageUploads(id, defects, bus_number, timestamp, urls, docData);
                    }

                }
            });
        }


        
    }

    private void handleImageUploads(final String docID, ArrayList<Uri> defects, final int bus_number, final String timestamp, final ArrayList<String> urls, final Map<String, Object> docData) {
            final StorageReference imgRef = storage.getReference();
            for(Uri image: defects){
                imgRef.child(bus_number+"_"+timestamp).putFile(image).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        imgRef.child(bus_number+"_"+timestamp).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                urls.add(uri.toString());
                                docData.put("Image_urls", urls);
                                db.collection("Post Inspection Check").document(docID).set(docData);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                e.printStackTrace();
                            }
                        });
                    }
                });
            }
        }

    public void sendTripDetails(ArrayList<TripDetails> tripDetailList, String route) {
        System.out.println(tripDetailList);
        for(TripDetails tripDetails: tripDetailList){
            db.collection("Interim/Spring2019/Routes/"+route+"/TripDetails").add(tripDetails);
        }
    }
}
