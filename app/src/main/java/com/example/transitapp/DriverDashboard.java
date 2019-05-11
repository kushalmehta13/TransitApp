package com.example.transitapp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DriverDashboard extends AppCompatActivity implements View.OnClickListener {
    private Button signOut;

    public static final int REQUEST_CODE_PRE_INSPECTION_ACTIVITY = 100;
    public static final int REQUEST_CODE_POST_INSPECTION_ACTIVITY = 101;
    public static final String SHOW_PRE_EDIT = "shouldShowButton";
    public static final String SHOW_POST_EDIT = "shouldShowButton";

    private Intent login;
    private Intent preInspectionIntent;
    private Intent postInspectionIntent;

    private static int bus_number;
    private static String driver_name;



    private CardView preInspection;
    private CardView postInspection;

    private Button editPreinspection;
    private Button editPostInspection;
//    private Button preInspection;
    private Button beginInspection;
    private RelativeLayout layout;
    private RelativeLayout mainLayout;
    private PopupWindow popUp;
    private AutoCompleteTextView bus_num;
    private TextView driverName;
    private View parent, popupView;
    private PopupWindow popupWindow;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_driver_dashboard);
        setContentView(R.layout.material_driver_dashboard);
        login = new Intent(this, FirebaseUIActivity.class);
        signOut = findViewById(R.id.signOut);
        preInspection = findViewById(R.id.pres_inspec_card_view);
        postInspection = findViewById(R.id.post_inspec_card_view);

        signOut.setOnClickListener(this);
        editPreinspection = findViewById(R.id.preInsepctionEdit);
        editPostInspection = findViewById(R.id.postInspectionEdit);

        editPostInspection.setVisibility(View.GONE);
        editPreinspection.setVisibility(View.GONE);

        preInspection.setOnClickListener(this);
        editPreinspection.setOnClickListener(this);
        editPostInspection.setOnClickListener(this);
        driverName = findViewById(R.id.driver_name);
        parent = findViewById(R.id.driverDashboard);

        user = FirebaseAuth.getInstance().getCurrentUser();
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
            case R.id.pres_inspec_card_view:
                editPreinspection.setVisibility(View.GONE);
                editPostInspection.setVisibility(View.GONE);
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

                        if(TextUtils.isEmpty(bus_num.getText())){
                            bus_num.setError("Bus number is required");
                        }
                        else{
                            postInspection.setOnClickListener(DriverDashboard.this);
                            preInspectionIntent = new Intent(DriverDashboard.this, PreInspectionActivity.class);
                            bus_number = Integer.parseInt(String.valueOf(bus_num.getText()));
                            driver_name = user.getDisplayName();
                            Bundle b = new Bundle();
                            b.putBoolean("Edit", false);
                            b.putString("Driver name", driver_name);
                            b.putInt("Bus number", bus_number );
                            b.putString("Timestamp", new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss").format(new Date()));
                            preInspectionIntent.putExtra("editBundle", b);
                            startActivityForResult(preInspectionIntent, REQUEST_CODE_PRE_INSPECTION_ACTIVITY);
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();

                    break;
            case R.id.post_inspec_card_view:
                editPostInspection.setVisibility(View.GONE);
                postInspectionIntent = new Intent(DriverDashboard.this, PostInspectionActivty.class);
                System.out.println(driver_name);
                Bundle b1 = new Bundle();
                b1.putBoolean("Edit", false);
                b1.putString("Driver name", driver_name);
                b1.putInt("Bus number", bus_number);
                b1.putString("Timestamp", new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss").format(new Date()));
                postInspectionIntent.putExtra("editBundle", b1);
                startActivityForResult(postInspectionIntent, REQUEST_CODE_POST_INSPECTION_ACTIVITY);
                break;

            case R.id.preInsepctionEdit:
                preInspectionIntent = new Intent(DriverDashboard.this, PreInspectionActivity.class);
                Bundle b2 = new Bundle();
                b2.putBoolean("Edit", true);
                b2.putString("Driver name", user.getDisplayName());
                b2.putInt("Bus number", Integer.parseInt(String.valueOf(bus_num.getText())));
                b2.putString("Timestamp", new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss").format(new Date()));
                preInspectionIntent.putExtra("editBundle", b2);
                startActivity(preInspectionIntent);
                break;

            case R.id.postInspectionEdit:
                System.out.println("clicked");
                postInspectionIntent = new Intent(DriverDashboard.this, PostInspectionActivty.class);
                Bundle b3 = new Bundle();
                b3.putBoolean("Edit", true);
                b3.putString("Driver name", driver_name);
                b3.putInt("Bus number", bus_number);
                b3.putString("Timestamp", new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss").format(new Date()));
                postInspectionIntent.putExtra("editBundle", b3);
                startActivity(postInspectionIntent);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_PRE_INSPECTION_ACTIVITY && resultCode == RESULT_OK){
            if (data.hasExtra(SHOW_PRE_EDIT) && data.getBooleanExtra(SHOW_PRE_EDIT, false) && editPreinspection.getVisibility() != View.VISIBLE){
                editPreinspection.setVisibility(View.VISIBLE);
            }
        }
        if(requestCode == REQUEST_CODE_POST_INSPECTION_ACTIVITY && resultCode == RESULT_OK){
            if (data.hasExtra(SHOW_POST_EDIT) && data.getBooleanExtra(SHOW_POST_EDIT, false) && editPostInspection.getVisibility() != View.VISIBLE){
                editPostInspection.setVisibility(View.VISIBLE);
            }
        }
    }
}
