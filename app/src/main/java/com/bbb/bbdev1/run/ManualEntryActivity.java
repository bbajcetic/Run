package com.bbb.bbdev1.run;
import com.bbb.bbdev1.run.RunDialogFragment.Type;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.sql.Time;

public class ManualEntryActivity extends AppCompatActivity implements View.OnClickListener, RunDialogFragment.OnDialogResponseListener {

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

        findViewById(R.id.activity).setOnClickListener(this);
        findViewById(R.id.date).setOnClickListener(this);
        findViewById(R.id.time).setOnClickListener(this);
        findViewById(R.id.duration).setOnClickListener(this);
        findViewById(R.id.distance).setOnClickListener(this);
        findViewById(R.id.calorie).setOnClickListener(this);
        findViewById(R.id.heartbeat).setOnClickListener(this);
        findViewById(R.id.comment).setOnClickListener(this);

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

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.date:
                showDialog(Type.DATE);
                break;
            case R.id.time:
                showDialog(Type.TIME);
                break;
            case R.id.duration:
                showDialog(Type.DURATION);
                break;
            case R.id.distance:
                showDialog(Type.DISTANCE);
                break;
            case R.id.calorie:
                showDialog(Type.CALORIE);
                break;
            case R.id.heartbeat:
                showDialog(Type.HEARTBEAT);
                break;
            case R.id.comment:
                showDialog(Type.COMMENT);
                break;
            default:
                break;
        }
    }

    void showDialog(RunDialogFragment.Type type) {
        DialogFragment newFragment = RunDialogFragment
                .newInstance(type);
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
