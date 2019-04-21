package com.example.transitapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

public class DriverDashboard extends AppCompatActivity implements View.OnClickListener {
    //This is testing comment
    //This is testing comment2
    private Intent login;
    private Intent preInspectionIntent;
    private RelativeLayout layout, mainLayout;
    private PopupWindow popUp;
    private AutoCompleteTextView bus_num;
    private TextView driverName, dashboardTextView;
    private View parent, popupView;
    private PopupWindow popupWindow;

    private Button /*preInspection, postInspection, beginInspection, */ startTrip, signOut ;
    private ImageView /*pre_ins_btn_img, post_ins_btn_img,*/ start_trip_btn_img, signout_btn_img;

    public static final String Key1 = "Data";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_dashboard);


        login = new Intent(this, FirebaseUIActivity.class);

/*        preInspection = findViewById(R.id.pre_insp_check_Btn);*/
        startTrip = findViewById(R.id.start_trip_Btn);
        /*postInspection = findViewById(R.id.post_insp_check_Btn);*/
        signOut = findViewById(R.id.signOut_Btn);

      /*  preInspection.setOnClickListener(this);*/
        startTrip.setOnClickListener(this);
        /*postInspection.setOnClickListener(this);*/
        signOut.setOnClickListener(this);



        driverName = findViewById(R.id.driver_dash_driver_name2);
        //dashboardTextView = findViewById(R.id.driver_dashboard_txt);

        parent = findViewById(R.id.driverDashboard);
/*        pre_ins_btn_img = findViewById(R.id.pre_insp_imageView);
        post_ins_btn_img = findViewById(R.id.post_insp_imageView);*/
        start_trip_btn_img = findViewById(R.id.start_imageView);
        signout_btn_img = findViewById(R.id.signout_imageView);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        driverName.setText("Hello, " + user.getDisplayName());


        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        driverName.startAnimation(myAnim);
/*        pre_ins_btn_img.startAnimation(myAnim);*/
        start_trip_btn_img.startAnimation(myAnim);
        /*post_ins_btn_img.startAnimation(myAnim);*/
        signout_btn_img.startAnimation(myAnim);


       /* if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // landscape
            dashboardTextView.setText("Driver DashBoard");
        } else {
            // portrait
            dashboardTextView.setText("Driver \nDashBoard");
        }*/


    }

    @Override
    public void onClick(View v) {

        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);

        switch (v.getId()){


            /*case R.id.pre_insp_check_Btn:

                // Use bounce interpolator with amplitude 0.2 and frequency 20
                myAnim.setInterpolator(interpolator);
                pre_ins_btn_img.startAnimation(myAnim);


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
*/
            case R.id.start_trip_Btn:

                // Use bounce interpolator with amplitude 0.2 and frequency 20
                myAnim.setInterpolator(interpolator);
                start_trip_btn_img.startAnimation(myAnim);

                Intent myIntent = new Intent(this, Enroute_Dashboard.class);
                myIntent.putExtra(Key1,"MyPranavKey");
                startActivity(myIntent);

                break;

  /*          case R.id.post_insp_check_Btn:

                // Use bounce interpolator with amplitude 0.2 and frequency 20
                myAnim.setInterpolator(interpolator);
                post_ins_btn_img.startAnimation(myAnim);

                break;
*/
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
}
