package com.bbb.bbdev1.run;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class StartFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    Spinner inputSpinner;
    Spinner activitySpinner;

    String inputTypeSelected;
    String activityTypeSelected;

    public StartFragment() {
        // Required empty public constructor
    }

    public static StartFragment newInstance(String param1, String param2) {
        StartFragment fragment = new StartFragment();
        /*Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_start, container, false);

        FloatingActionButton startFab = root.findViewById(R.id.start_fab);
        startFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputTypeSelected.equals("GPS") || inputTypeSelected.equals("Automatic")) {
                    Intent mapIntent = new Intent(getActivity(), MapActivity.class);
                    mapIntent.putExtra("ACTIVITY_TYPE", activityTypeSelected);
                    startActivity(mapIntent);
                } else if (inputTypeSelected.equals("Manual")) {
                    Intent manualEntryIntent = new Intent(getActivity(), ManualEntryActivity.class);
                    manualEntryIntent.putExtra("ACTIVITY_TYPE", activityTypeSelected);
                    startActivity(manualEntryIntent);
                }
            }
        });

        inputSpinner = root.findViewById(R.id.input_spinner);
        inputSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> inputAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.input_type_options, android.R.layout.simple_spinner_dropdown_item);
        inputSpinner.setAdapter(inputAdapter);

        activitySpinner = root.findViewById(R.id.activity_spinner);
        activitySpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> activityAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.activity_type_options, android.R.layout.simple_spinner_dropdown_item);
        activitySpinner.setAdapter(activityAdapter);

        if (savedInstanceState != null) {
            inputTypeSelected = savedInstanceState.getString("INPUT_TYPE", "");
            activityTypeSelected = savedInstanceState.getString("ACTIVITY_TYPE", "");
        }

        return root;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("INPUT_TYPE", inputTypeSelected);
        outState.putString("ACTIVITY_TYPE", activityTypeSelected);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch(parent.getId()) {
            case R.id.input_spinner:
                inputTypeSelected = parent.getItemAtPosition(position).toString();
                break;
            case R.id.activity_spinner:
                activityTypeSelected = parent.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
