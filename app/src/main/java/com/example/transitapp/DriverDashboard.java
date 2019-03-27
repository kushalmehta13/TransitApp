package com.example.transitapp;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DriverDashboard extends AppCompatActivity implements View.OnClickListener {
    private Button signOut;
    private Intent login;
    private Intent preInspectionIntent;
    private Button preInspection;
    private Button beginInspection;
    private RelativeLayout layout;
    private RelativeLayout mainLayout;
    private PopupWindow popUp;
    private AutoCompleteTextView bus_num;
    private TextView driverName;
    private View parent, popupView;
    private PopupWindow popupWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_dashboard);


        login = new Intent(this, FirebaseUIActivity.class);
        signOut = findViewById(R.id.signOut);
        preInspection = findViewById(R.id.pre_insp_check_Btn);
        signOut.setOnClickListener(this);
        preInspection.setOnClickListener(this);
        driverName = findViewById(R.id.driver_name);
        parent = findViewById(R.id.driverDashboard);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        driverName.setText("Hello, " + user.getDisplayName());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signOut: AuthUI.getInstance().signOut(getApplicationContext()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    startActivity(login);
                    finish();
                }
            });
                    break;
            case R.id.pre_insp_check_Btn:
                final Dialog dialog = new Dialog(DriverDashboard.this, R.style.Dialog);
                dialog.setContentView(R.layout.busn_number_dialog);
                dialog.setTitle("Select Bus Number");
                ImageView image = dialog.findViewById(R.id.busIcon);
                image.setImageResource(R.drawable.ic_bus_black_36dp);
                bus_num = dialog.findViewById(R.id.bus_numbers);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                        R.array.planets_array, R.layout.spinner_item);
                adapter.setDropDownViewResource(R.layout.spinner_item);
                bus_num.setAdapter(adapter);
                bus_num.setHint("Bus number");
                beginInspection = dialog.findViewById(R.id.beginInspection);
                beginInspection.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

                    break;
        }

    }
}
