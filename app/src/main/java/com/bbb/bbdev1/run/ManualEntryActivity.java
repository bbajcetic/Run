package com.bbb.bbdev1.run;
import com.bbb.bbdev1.run.RunDialogFragment.Type;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.sql.Time;
import java.util.Calendar;

public class ManualEntryActivity extends AppCompatActivity implements View.OnClickListener, RunDialogFragment.OnDialogResponseListener {

    String activity;
    String date;
    String time;
    String duration;
    String distance;
    String calorie;
    String heartbeat;
    String comment;

    TextView activitySubtextView;
    TextView dateSubtextView;
    TextView timeSubtextView;
    TextView durationSubtextView;
    TextView distanceSubtextView;
    TextView calorieSubtextView;
    TextView heartbeatSubtextView;
    TextView commentSubtextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_entry);

        activitySubtextView = findViewById(R.id.activity_info);
        dateSubtextView = findViewById(R.id.date_info);
        timeSubtextView = findViewById(R.id.time_info);
        durationSubtextView = findViewById(R.id.duration_info);
        distanceSubtextView = findViewById(R.id.distance_info);
        calorieSubtextView = findViewById(R.id.calorie_info);
        heartbeatSubtextView = findViewById(R.id.heartbeat_info);
        commentSubtextView = findViewById(R.id.comment_info);

        findViewById(R.id.activity).setOnClickListener(this);
        findViewById(R.id.date).setOnClickListener(this);
        findViewById(R.id.time).setOnClickListener(this);
        findViewById(R.id.duration).setOnClickListener(this);
        findViewById(R.id.distance).setOnClickListener(this);
        findViewById(R.id.calorie).setOnClickListener(this);
        findViewById(R.id.heartbeat).setOnClickListener(this);
        findViewById(R.id.comment).setOnClickListener(this);

        Intent intent = getIntent();
        activity = intent.getStringExtra("ACTIVITY_TYPE");
        initializeDateTime();
        duration = "0 mins";
        distance = "0 kms";
        calorie = "0 cals";
        heartbeat = "0 bpm";
        comment = "";


        if (savedInstanceState != null) {
            activity = savedInstanceState.getString("ACTIVITY");
            date = savedInstanceState.getString("DATE");
            time = savedInstanceState.getString("TIME");
            duration = savedInstanceState.getString("DURATION");
            distance = savedInstanceState.getString("DISTANCE");
            calorie = savedInstanceState.getString("CALORIE");
            heartbeat = savedInstanceState.getString("HEARTBEAT");
            comment = savedInstanceState.getString("COMMENT");
        }

        initializeTexts();

    }

    public void initializeDateTime() {
        //sets date to current day
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        date = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);

        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        time = String.format("%02d:%02d", hourOfDay, minute);
    }

    public void initializeTexts() {
        activitySubtextView.setText(activity);
        dateSubtextView.setText(date);
        timeSubtextView.setText(time);
        durationSubtextView.setText(duration);
        distanceSubtextView.setText(distance);
        calorieSubtextView.setText(calorie);
        heartbeatSubtextView.setText(heartbeat);
        commentSubtextView.setText(comment);
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
    public void onDialogResponse(Type type, String response) {
        switch(type) {
            case DATE:
                dateSubtextView.setText(response);
                date = response;
                break;
            case TIME:
                timeSubtextView.setText(response);
                time = response;
                break;
            case DURATION:
                if (!response.equals("") && !response.equals(".")) {
                    duration = response + " mins";
                    durationSubtextView.setText(duration);
                }
                break;
            case DISTANCE:
                if (!response.equals("") && !response.equals(".")) {
                    distance = response + " kms";
                    distanceSubtextView.setText(distance);
                }
                break;
            case CALORIE:
                if (!response.equals("")) {
                    calorie = response + " cals";
                    calorieSubtextView.setText(calorie);
                }
                break;
            case HEARTBEAT:
                if (!response.equals("")) {
                    heartbeat = response + " bpm";
                    heartbeatSubtextView.setText(heartbeat);
                }
                break;
            case COMMENT:
                if (!response.equals("")) {
                    comment = response;
                    commentSubtextView.setText(comment);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("ACTIVITY", activity);
        outState.putString("DATE", date);
        outState.putString("TIME", time);
        outState.putString("DURATION", duration);
        outState.putString("DISTANCE", distance);
        outState.putString("CALORIE", calorie);
        outState.putString("HEARTBEAT", heartbeat);
        outState.putString("COMMENT", comment);

        super.onSaveInstanceState(outState);
    }
}
