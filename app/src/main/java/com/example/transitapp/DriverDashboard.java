package com.example.transitapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class DriverDashboard extends AppCompatActivity implements View.OnClickListener {

    public static final int REQUEST_CODE_PRE_INSPECTION_ACTIVITY = 100;
    public static final int REQUEST_CODE_POST_INSPECTION_ACTIVITY = 101;
    private String ACTION = "ACTION_FOR_INTENT_CALLBACK_SCHEDULES";
    public static final String SHOW_PRE_EDIT = "shouldShowButton";
    public static final String SHOW_POST_EDIT = "shouldShowButton";

    private Intent login;
    private Intent preInspectionIntent;
    private Intent postInspectionIntent;
    private ImageView preIns_Image, postIns_Image;
    private static int bus_number;
    private static String driver_name;
    private String routeSelected;
    private String scheduleSelected;

    private CardView preInspectionCV;
    private CardView postInspectionCV;
    private CardView startCardView;

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
    ArrayList<String> schedulesArray;

    private Button pre_Ins_Button, post_Ins_Button, startTrip, signOut ;
    private ImageView start_trip_btn_img, signout_btn_img;


    public static final String Key1 = "Data";
    private Button beginTrip;
    private AutoCompleteTextView routes;
    private AutoCompleteTextView schedules;
    public static String[] routeArray;
    BroadcastReceiver scheduleReceiver;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_dashboard);

        login = new Intent(this, FirebaseUIActivity.class);
        signOut = (Button) findViewById(R.id.signOut_Btn);
        preInspectionCV = (CardView) findViewById(R.id.pres_inspec_card_view);
        postInspectionCV = (CardView) findViewById(R.id.post_inspec_card_view);
        startCardView = (CardView) findViewById(R.id.start_cardView);
        editPreinspection = (FloatingActionButton) findViewById(R.id.pre_ins_edit_btn);
        editPostInspection = (FloatingActionButton) findViewById(R.id.post_ins_edit_btn);
        pre_Ins_Button = (Button) findViewById(R.id.pre_Ins_Btn);
        post_Ins_Button =(Button) findViewById(R.id.post_Ins_Btn);

        pre_Ins_Button.setOnClickListener(this);
        post_Ins_Button.setOnClickListener(this);

        editPostInspection.hide();
        editPreinspection.hide();
        startCardView.setVisibility(View.GONE);
        postInspectionCV.setVisibility(View.GONE);

        startTrip = (Button) findViewById(R.id.start_trip_Btn);
        preIns_Image = (ImageView) findViewById(R.id.pre_ins_imageView);
        postIns_Image = (ImageView) findViewById(R.id.post_ins_imageView);


        signOut.setOnClickListener(this);
        startTrip.setOnClickListener(this);

        editPreinspection.setOnClickListener(this);
        editPostInspection.setOnClickListener(this);

        parent = findViewById(R.id.driverDashboard);
        driverName = findViewById(R.id.driver_dash_driver_name2);

        user = FirebaseAuth.getInstance().getCurrentUser();
        driverName.setText("Hello, " + user.getDisplayName());

        start_trip_btn_img = findViewById(R.id.start_imageView);
        signout_btn_img = findViewById(R.id.signout_imageView);

        driver_name = user.getDisplayName();

    }

    @Override
    public void onClick(View v) {

        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);

        switch (v.getId()){

            case R.id.pre_Ins_Btn:
                myAnim.setInterpolator(interpolator);
                preInspectionCV.startAnimation(myAnim);
                editPreinspection.hide();
                editPostInspection.hide();
                final Dialog dialog = new Dialog(DriverDashboard.this, R.style.Dialog);
                dialog.setContentView(R.layout.busn_number_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //dialog.setTitle("Select Bus Number");
                //ImageView image = dialog.findViewById(R.id.busIcon);
                //image.setImageResource(R.drawable.ic_bus_black_36dp);
                bus_num = dialog.findViewById(R.id.bus_numbers);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                        R.array.bus_numbers, R.layout.spinner_item);
                adapter.setDropDownViewResource(R.layout.spinner_item);
                bus_num.setAdapter(adapter);
                //bus_num.setHint("Bus number");
                beginInspection = dialog.findViewById(R.id.beginInspection);
                beginInspection.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyBounceInterpolator interpolator1 = new MyBounceInterpolator(0.2, 10);
                        myAnim.setInterpolator(interpolator1);
                        beginInspection.startAnimation(myAnim);

                        if(TextUtils.isEmpty(bus_num.getText())){
                            bus_num.setError("Bus number is required");
                        }
                        else{
                            //postInspectionCV.setOnClickListener(DriverDashboard.this);
                            preInspectionIntent = new Intent(DriverDashboard.this, PreInspectionActivity.class);
                            bus_number = Integer.parseInt(String.valueOf(bus_num.getText()));
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
                final Dialog routeScheduleSelector = new Dialog(DriverDashboard.this, R.style.Dialog);
                routeScheduleSelector.setContentView(R.layout.bus_route_schedule);
                routeScheduleSelector.setTitle("Select Route and Schedule");;
                routes = routeScheduleSelector.findViewById(R.id.route);
                schedules = routeScheduleSelector.findViewById(R.id.schedule);


                ArrayAdapter<String> route_adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, routeArray);
//                ArrayAdapter<CharSequence> route_adapter = ArrayAdapter.createFromResource(this,
//                        R.array.routes, R.layout.spinner_item);
                route_adapter.setDropDownViewResource(R.layout.spinner_item);

                routes.setAdapter(route_adapter);
                beginTrip = routeScheduleSelector.findViewById(R.id.beginTrip);
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                routes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        routes.showDropDown();

                    }
                });

                schedules.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        schedules.showDropDown();
                    }
                });

                schedules.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        scheduleSelected = Arrays.copyOf(schedulesArray.toArray(), schedulesArray.toArray().length, String[].class)[position];
                    }
                });

                routes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        routeSelected = routeArray[position];
                        populateSchedules(routeArray[position]);

                        progressDialog = ProgressDialog.show(DriverDashboard.this, "Getting Schedules", "Waiting for Results...", true);
                        scheduleReceiver = new BroadcastReceiver() {
                            @Override
                            public void onReceive(Context context, Intent intent) {
                                System.out.println("Received");
                                schedulesArray = intent.getStringArrayListExtra("Schedule");
                                ArrayAdapter<String> schedule_adapter= new ArrayAdapter<String>(DriverDashboard.this, R.layout.spinner_item, Arrays.copyOf(schedulesArray.toArray(), schedulesArray.toArray().length, String[].class));
                                schedule_adapter.setDropDownViewResource(R.layout.spinner_item);
                                schedules.setAdapter(schedule_adapter);
                                progressDialog.dismiss();

                            }
                        };

                        getApplicationContext().registerReceiver(scheduleReceiver, new IntentFilter(ACTION));
                    }
                });



                beginTrip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(TextUtils.isEmpty(routes.getText()) || TextUtils.isEmpty(schedules.getText())){
                            routes.setError("Select a route");
                            schedules.setError("Select a route and then Select a schedule");
                        }
                        else{
                            Enroute_Dashboard.busNumber = bus_number;
                            Enroute_Dashboard.driverName = driver_name;
                            Enroute_Dashboard.route = routeSelected;
                            Enroute_Dashboard.schedule = scheduleSelected;

                            Intent myIntent = new Intent(DriverDashboard.this, Enroute_Dashboard.class);
                            myIntent.putExtra(Key1,"MyPranavKey");
                            startActivity(myIntent);
                            routeScheduleSelector.dismiss();
                        }
                    }
                });
                routeScheduleSelector.show();
                break;

            case R.id.post_Ins_Btn:
                editPostInspection.hide();
                myAnim.setInterpolator(interpolator);
                postInspectionCV.startAnimation(myAnim);

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

    private void populateSchedules(String route) {
        RouteScheduleStopRetriever routeScheduleStopRetriever = new RouteScheduleStopRetriever(getApplicationContext(), ACTION);
        // Time hardcoded for demo purposes
        // "930" is 9:30 AM
        // "0" is Monday
        routeScheduleStopRetriever.getSchedules(route, "930", "0");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_PRE_INSPECTION_ACTIVITY && resultCode == RESULT_OK){
            if (data.hasExtra(SHOW_PRE_EDIT) && data.getBooleanExtra(SHOW_PRE_EDIT, false) && editPreinspection.getVisibility() != View.VISIBLE){
                final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                myAnim.setInterpolator(interpolator);
                editPreinspection.show();
                editPreinspection.setAnimation(myAnim);
                final Animation myAnim1 = AnimationUtils.loadAnimation(this, R.anim.bounce);
                MyBounceInterpolator interpolator1 = new MyBounceInterpolator(0.2, 5);
                myAnim1.setInterpolator(interpolator1);
                startCardView.setVisibility(View.VISIBLE);
                startCardView.setAnimation(myAnim1);
                postInspectionCV.setVisibility(View.VISIBLE);
                postInspectionCV.setAnimation(myAnim1);

            }
        }
        if(requestCode == REQUEST_CODE_POST_INSPECTION_ACTIVITY && resultCode == RESULT_OK){
            if (data.hasExtra(SHOW_POST_EDIT) && data.getBooleanExtra(SHOW_POST_EDIT, false) && editPostInspection.getVisibility() != View.VISIBLE){
                final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                myAnim.setInterpolator(interpolator);
                editPostInspection.show();
                editPostInspection.setAnimation(myAnim);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
