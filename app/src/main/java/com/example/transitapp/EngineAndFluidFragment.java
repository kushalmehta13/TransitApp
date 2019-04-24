package com.example.transitapp;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class EngineAndFluidFragment extends Fragment {

    public EditText other;
    private LinearLayout horizontal;
    private LinearLayout root;
    private CardView card;
    private ToggleButton checkerPositve;
    private HashMap<CardView, ToggleButton> card_toggle_map;
    private HashMap<String, Boolean> label_toggle_map;
    private TextView label;
    private PreInspectionActivity parentActivity;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        assert getArguments() != null;
        ArrayList<String> checks = getArguments().getStringArrayList("EngineChecklist");
        parentActivity = (PreInspectionActivity) getActivity();
        label_toggle_map = new HashMap<>();
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
        ViewGroup.LayoutParams params4 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        ((LinearLayout.LayoutParams) params2).setMargins(0, 50, 0, 0);
//        ((LinearLayout.LayoutParams) params).setMargins(100, 50, 0, 0);
        ((LinearLayout.LayoutParams) params4).setMargins(100, 50, 100, 0);
        ((LinearLayout.LayoutParams) params3).setMargins(100, 50, 0, 100);

        other = new EditText(getContext());
        other.setHint("Other(Alphanumeric)");
        other.setInputType(InputType.TYPE_CLASS_TEXT);
        other.setTag("other_interior");
        other.setTextSize(40);
        other.setLayoutParams(params3);
        card_toggle_map = new HashMap<CardView, ToggleButton>();


        for(String c: checks){
            horizontal = new LinearLayout(getContext());
            horizontal.setOrientation(LinearLayout.HORIZONTAL);
            createCards(c, params4);
            createToggleButtons(c, params);
            createLabels(c, params);
            card_toggle_map.put(card, checkerPositve);
            label_toggle_map.put(c, false);
            horizontal.addView(checkerPositve);
            horizontal.addView(label);
            card.addView(horizontal);
            root.addView(card);
        }
        parentActivity.preInspectionCheckValues.put("Engine Checks", label_toggle_map);
        handleCardClicks(card_toggle_map);
        handleOther(other);
        root.addView(other);
        return view;
    }

    private void handleOther(final EditText other) {
        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentActivity.others.put("Engine Checks", String.valueOf(other.getText()));
            }
        });
    }

    private void handleCardClicks(final HashMap<CardView, ToggleButton> card_toggle_map) {
        Set set = card_toggle_map.entrySet();
        Iterator ct_iterator = set.iterator();
        while(ct_iterator.hasNext()){
            Map.Entry mentry = (Map.Entry) ct_iterator.next();
            CardView c = (CardView) mentry.getKey();
            final String l = (String) c.getTag();
            final ToggleButton b = (ToggleButton) mentry.getValue();
            c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    b.toggle();
                    label_toggle_map.put(l.split("_")[0], true);
                    parentActivity.preInspectionCheckValues.put("Engine Checks", label_toggle_map);
                }
            });
        }
    }

    private void createLabels(String c, ViewGroup.LayoutParams params) {
        label = new TextView(getContext());
        label.setText(c);
        label.setTextSize(40);
        label.setLayoutParams(params);
    }

    private void createToggleButtons(String c, ViewGroup.LayoutParams params) {
        checkerPositve = new ToggleButton(getContext());
        checkerPositve.setTag(c+"_toggle");
        checkerPositve.setText(null);
        checkerPositve.setTextOn(null);
        checkerPositve.setTextOff(null);
        checkerPositve.setTag(c+"pos");
        checkerPositve.setTextSize(40);
        checkerPositve.setBackgroundDrawable(getContext().getDrawable(R.drawable.my_btn_toggle));
        checkerPositve.setLayoutParams(params);

    }

    private void createCards(String c, ViewGroup.LayoutParams params4) {
        card = new CardView(getContext());
        card.setTag(c+"_card");
        card.setLayoutParams(params4);
        card.setRadius(9);
        card.setMaxCardElevation(2);
        card.setElevation(2);
        card.setContentPadding(16,16,16,16);
    }

}
