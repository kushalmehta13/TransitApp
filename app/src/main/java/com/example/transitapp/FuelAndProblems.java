package com.example.transitapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.HashMap;


public class FuelAndProblems extends Fragment {


    private HashMap<String, Object> toEdit;
    private PostInspectionActivty parentActivity;
    private HashMap<String, Object> fieldValues;
    private View root;
    private LinearLayout vertical;
    private EditText fuelLevel;
    private ToggleButton problemChecker;
    private ToggleButton supervisorChecker;
    private EditText problemReport;
    private CardView problemCheckerCard;
    private CardView supervisorCheckerCard;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null)
        {
            toEdit = (HashMap<String, Object>) getArguments().getSerializable("toEdit");
        }
        parentActivity = (PostInspectionActivty) getActivity();
        fieldValues = new HashMap<>();
        View view = inflater.inflate(R.layout.fragment_fuel_and_problems, container, false);
        fuelLevel = (EditText) view.findViewById(R.id.fuelLevel);
        problemChecker = (ToggleButton) view.findViewById(R.id.problemChecker);
        problemCheckerCard = (CardView) view.findViewById(R.id.problemCheckerCard);
        supervisorChecker = (ToggleButton) view.findViewById(R.id.supervisorChecker);
        supervisorCheckerCard = (CardView) view.findViewById(R.id.supervisorCheckerCard);
        problemReport = (EditText) view.findViewById(R.id.problemReport);
        fieldValues.put("Fuel Level", 0.0f);
        fieldValues.put("Severe Problem Detected", false);
        fieldValues.put("Supervisor Notified", false);
        fieldValues.put("Problem Report", "");
        parentActivity.postInspectionCheckValues.put("FuelAndOtherProblems", fieldValues);
        if(toEdit!=null){
            fuelLevel.setText( new Double((Double) toEdit.get("Fuel Level")).toString());
            fieldValues.put("Fuel Level", toEdit.get("Fuel Level"));
            System.out.println(toEdit.get("Severe Problem Detected"));
            if((Boolean) toEdit.get("Severe Problem Detected")){
                problemChecker.setChecked(true);
                fieldValues.put("Severe Problem Detected", true);
            }
            if((Boolean) toEdit.get("Supervisor Notified")){
                supervisorChecker.setChecked(true);
                fieldValues.put("Supervisor Notififed", true);
            }
            problemReport.setText((CharSequence) toEdit.get("Problem Report"));
            fieldValues.put("Problem Report", toEdit.get("Problem Report"));
        }



        fuelLevel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(fuelLevel.getText())){
                    fuelLevel.setError("Please enter the fuel level");
                }
                else{
                    fieldValues.put("Fuel Level", Float.parseFloat(fuelLevel.getText().toString()));
                    parentActivity.postInspectionCheckValues.put("FuelAndOtherProblems", fieldValues);
                }
            }
        });


        problemCheckerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                problemChecker.performClick();
            }
        });

        problemChecker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (problemChecker.isChecked()){
                    fieldValues.put("Severe Problem Detected", true);
                }
                else{
                    fieldValues.put("Severe Problem Detected", false);
                }
                parentActivity.postInspectionCheckValues.put("FuelAndOtherProblems", fieldValues);
            }
        });

        supervisorCheckerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supervisorChecker.performClick();
            }
        });
        supervisorChecker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (supervisorChecker.isChecked()){
                    fieldValues.put("Supervisor Notified", true);
                }
                else{
                    fieldValues.put("Supervisor Notified", false);
                }
                parentActivity.postInspectionCheckValues.put("FuelAndOtherProblems", fieldValues);
            }
        });

        problemReport.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                fieldValues.put("Problem Report", problemReport.getText().toString());
                parentActivity.postInspectionCheckValues.put("FuelAndOtherProblems", fieldValues);
            }
        });


        return view;
    }
}
