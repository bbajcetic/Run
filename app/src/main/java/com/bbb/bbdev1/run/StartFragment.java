package com.bbb.bbdev1.run;

import android.os.Bundle;

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
                Toast.makeText(getActivity(), "Start clicked!", Toast.LENGTH_SHORT).show();
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


        return root;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch(parent.getId()) {
            case R.id.input_spinner:
                String input = parent.getItemAtPosition(position).toString();
                Toast.makeText(getActivity(), "Input: " + input, Toast.LENGTH_SHORT).show();
                break;
            case R.id.activity_spinner:
                String activity = parent.getItemAtPosition(position).toString();
                Toast.makeText(getActivity(), "Activity: " + activity, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
