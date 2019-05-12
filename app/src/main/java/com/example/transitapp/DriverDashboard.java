package com.example.transitapp;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

    private FloatingActionButton editPreinspection;
    private FloatingActionButton editPostInspection;

    private Button beginInspection;
    private RelativeLayout layout;
    private RelativeLayout mainLayout;
    private PopupWindow popUp;
    private AutoCompleteTextView bus_num;
    private TextView driverName;
    private View parent, popupView;
    private PopupWindow popupWindow;
    private FirebaseUser user;

    private Button pre_Ins_Button, post_Ins_Button, startTrip, signOut ;
    private ImageView /*pre_ins_btn_img, post_ins_btn_img,*/ start_trip_btn_img, signout_btn_img;


    public static final String Key1 = "Data";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_dashboard);
        login = new Intent(this, FirebaseUIActivity.class);
        signOut = (Button) findViewById(R.id.signOut_Btn);
        preInspection = (CardView) findViewById(R.id.pres_inspec_card_view);
        postInspection = (CardView) findViewById(R.id.post_inspec_card_view);
        editPreinspection = (FloatingActionButton) findViewById(R.id.pre_ins_edit_btn);
        editPostInspection = (FloatingActionButton) findViewById(R.id.post_ins_edit_btn);
        pre_Ins_Button = (Button) findViewById(R.id.pre_Ins_Btn);
        post_Ins_Button =(Button) findViewById(R.id.post_Ins_Btn);

        pre_Ins_Button.setOnClickListener(this);
        post_Ins_Button.setOnClickListener(this);

        editPostInspection.hide();
        editPreinspection.hide();

        startTrip = (Button) findViewById(R.id.start_trip_Btn);

        signOut.setOnClickListener(this);
        startTrip.setOnClickListener(this);

        preInspection.setOnClickListener(this);

        editPreinspection.setOnClickListener(this);
        editPostInspection.setOnClickListener(this);

        parent = findViewById(R.id.driverDashboard);
        driverName = findViewById(R.id.driver_dash_driver_name2);

        user = FirebaseAuth.getInstance().getCurrentUser();
        driverName.setText("Hello, " + user.getDisplayName());

        start_trip_btn_img = findViewById(R.id.start_imageView);
        signout_btn_img = findViewById(R.id.signout_imageView);


    }

    @Override
    public void onClick(View v) {

        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);

        switch (v.getId()){

            case R.id.pre_Ins_Btn:
                editPreinspection.hide();
                editPostInspection.hide();
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

            case R.id.start_trip_Btn:

                // Use bounce interpolator with amplitude 0.2 and frequency 20
                myAnim.setInterpolator(interpolator);
                start_trip_btn_img.startAnimation(myAnim);

                Intent myIntent = new Intent(this, Enroute_Dashboard.class);
                myIntent.putExtra(Key1,"MyPranavKey");
                startActivity(myIntent);

                break;

            case R.id.post_Ins_Btn:
                editPostInspection.hide();
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

            case R.id.pre_ins_edit_btn:
                preInspectionIntent = new Intent(DriverDashboard.this, PreInspectionActivity.class);
                Bundle b2 = new Bundle();
                b2.putBoolean("Edit", true);
                b2.putString("Driver name", user.getDisplayName());
                b2.putInt("Bus number", Integer.parseInt(String.valueOf(bus_num.getText())));
                b2.putString("Timestamp", new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss").format(new Date()));
                preInspectionIntent.putExtra("editBundle", b2);
                startActivity(preInspectionIntent);
                break;

            case R.id.post_ins_edit_btn:
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



            case R.id.signOut_Btn:
                // Use bounce interpolator with amplitude 0.2 and frequency 20
                myAnim.setInterpolator(interpolator);
                signout_btn_img.startAnimation(myAnim);
                AuthUI.getInstance().signOut(getApplicationContext()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    startActivity(login);
                    finish();
                }
            });
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
