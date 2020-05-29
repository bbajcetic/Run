package com.bbb.bbdev1.run;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.sql.Time;

public class ManualEntryActivity extends AppCompatActivity implements RunDialogFragment.OnDialogResponseListener {

    String activity;
    Date date;
    Time time;
    double duration;
    double distance;
    double calories;
    double heartRate;
    String comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_entry);

        View tv = findViewById(R.id.text);
        ((TextView) tv).setText("Example of displaying an alert dialog with a DialogFragment");
        // Watch for button clicks.
        Button button = findViewById(R.id.show);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog();
            }
        });


        if (savedInstanceState != null) {
            date = (Date) savedInstanceState.getSerializable("DATE");
            time = (Time) savedInstanceState.getSerializable("TIME");
            duration = savedInstanceState.getDouble("DURATION");
            distance = savedInstanceState.getDouble("DISTANCE");
            calories = savedInstanceState.getDouble("CALORIES");
            heartRate = savedInstanceState.getDouble("HEART_RATE");
            comment = savedInstanceState.getString("COMMENT");
        }

    }
    void showDialog() {
        DialogFragment newFragment = RunDialogFragment
                .newInstance(7);
        newFragment.show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onDialogResponse(String response) {
        Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable("DATE", date);
        outState.putSerializable("TIME", time);
        outState.putDouble("DURATION", duration);
        outState.putDouble("DISTANCE", distance);
        outState.putDouble("CALORIES", calories);
        outState.putDouble("HEART_RATE", heartRate);
        outState.putString("COMMENT", comment);

        super.onSaveInstanceState(outState);
    }
}
