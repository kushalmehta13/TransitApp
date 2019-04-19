package com.example.transitapp;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

public class EngineAndFluidFragment extends Fragment {

    private EditText other;
    private LinearLayout horizontal;
    private LinearLayout root;
    private ToggleButton checkerPositve;
//    private ToggleButton checkerNegative;
    private TextView label;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println("here");
        String[] checks = getResources().getStringArray(R.array.engineChecks);
        View view = inflater.inflate(R.layout.engine_and_fluids_fragment, container, false);
        root = view.findViewById(R.id.root);

        ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        ViewGroup.LayoutParams params2 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        ViewGroup.LayoutParams params3 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        ((LinearLayout.LayoutParams) params2).setMargins(0, 50, 0, 0);
        ((LinearLayout.LayoutParams) params).setMargins(100, 50, 0, 0);
        ((LinearLayout.LayoutParams) params3).setMargins(100, 50, 0, 100);

        other = new EditText(getContext());
        other.setHint("Other(Alphanumeric)");
        other.setInputType(InputType.TYPE_CLASS_TEXT);
        other.setTag("other_engine");
        other.setTextSize(40);
        other.setLayoutParams(params3);


        for(String c: checks){
            horizontal = new LinearLayout(getContext());
            horizontal.setOrientation(LinearLayout.HORIZONTAL);
            checkerPositve = new ToggleButton(getContext());
            checkerPositve.setText(null);
            checkerPositve.setTextOn(null);
            checkerPositve.setTextOff(null);
            checkerPositve.setTag(c+"pos");
            checkerPositve.setTextSize(40);
            checkerPositve.setBackgroundDrawable(getContext().getDrawable(R.drawable.my_btn_toggle));

//            checkerNegative = new ToggleButton(getContext());
//            checkerNegative.setTag(c+"neg");
//            checkerNegative.setTextSize(40);

            checkerPositve.setLayoutParams(params);
//            checkerNegative.setLayoutParams(params2);
            label = new TextView(getContext());
            label.setText(c);
            label.setTextSize(40);
            label.setLayoutParams(params);
            horizontal.addView(checkerPositve);
//            horizontal.addView(checkerNegative);
            horizontal.addView(label);
            root.addView(horizontal);
        }

        root.addView(other);
        return view;
    }


}
