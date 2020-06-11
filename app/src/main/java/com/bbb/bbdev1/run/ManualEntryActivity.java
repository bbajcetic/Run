package com.bbb.bbdev1.run;
import com.bbb.bbdev1.run.RunDialogFragment.Type;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.util.Pair;
import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.sql.Time;
import java.util.Calendar;

import static com.bbb.bbdev1.run.RunDialogFragment.Type.*;

public class ManualEntryActivity extends AppCompatActivity implements View.OnClickListener, RunDialogFragment.OnDialogResponseListener {
    final String TAG = "RUN_TAG";

    String activity;
    String date;
    String time;
    String duration;
    String distance;
    String calorie;
    String heartbeat;
    String comment;
    String units;

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
        Log.d(TAG, String.format("%s onCreate() called", this.getClass().getName()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_entry);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //enable Up navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupFields();
        setupDefaultFieldValues();

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

        setupTexts();

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if      (id == R.id.date)       { showDialog(DATE); }
        else if (id == R.id.time)       { showDialog(TIME); }
        else if (id == R.id.duration)   { showDialog(DURATION); }
        else if (id == R.id.distance)   { showDialog(Type.DISTANCE); }
        else if (id == R.id.calorie)    { showDialog(Type.CALORIE); }
        else if (id == R.id.heartbeat)  { showDialog(Type.HEARTBEAT); }
        else if (id == R.id.comment)    { showDialog(Type.COMMENT); }
    }

    void showDialog(RunDialogFragment.Type type) {
        DialogFragment newFragment = RunDialogFragment
                .newInstance(type);
        newFragment.show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onDialogResponse(Type type, String response) {
        if (type == DATE) {
            dateSubtextView.setText(response);
            date = response;
        } else if   (type == TIME) {
            timeSubtextView.setText(response);
            time = response;
        } else if   (type == DURATION) {
            if (!response.equals("") && !response.equals(".")) {
                duration = response + " mins";
                durationSubtextView.setText(duration);
            }
        } else if   (type == DISTANCE) {
            if (!response.equals("") && !response.equals(".")) {
                distance = response + " kms";
                distanceSubtextView.setText(distance);
            }
        } else if   (type == CALORIE) {
            if (!response.equals("")) {
                calorie = response + " cals";
                calorieSubtextView.setText(calorie);
            }
        } else if   (type == HEARTBEAT) {
            if (!response.equals("")) {
                heartbeat = response + " bpm";
                heartbeatSubtextView.setText(heartbeat);
            }
        } else if   (type == COMMENT) {
            if (!response.equals("")) {
                comment = response;
                commentSubtextView.setText(comment);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d(TAG, String.format("%s onSaveInstanceState() called", this.getClass().getName()));
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

    // Setup methods
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_entry, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_save:
                // add entry to database
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setupFields() {
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
    }
    public void setupDefaultFieldValues() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        units = sharedPref.getString(SettingsActivity.UNIT_PREF_KEY, "kms");

        Intent intent = getIntent();
        activity = intent.getStringExtra("ACTIVITY_TYPE");
        initializeDateTime();
        distance = String.format("0 %s", units);
        duration = "0 mins";
        calorie = "0 cals";
        heartbeat = "0 bpm";
        comment = "";
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
    public void setupTexts() {
        activitySubtextView.setText(activity);
        dateSubtextView.setText(date);
        timeSubtextView.setText(time);
        durationSubtextView.setText(duration);
        distanceSubtextView.setText(distance);
        calorieSubtextView.setText(calorie);
        heartbeatSubtextView.setText(heartbeat);
        commentSubtextView.setText(comment);
    }

    // Logging methods
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, String.format("%s onStart() called", this.getClass().getName()));
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, String.format("%s onPause() called", this.getClass().getName()));
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, String.format("%s onResume() called", this.getClass().getName()));
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, String.format("%s onStop() called", this.getClass().getName()));
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, String.format("%s onDestroy() called", this.getClass().getName()));
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.d(TAG, String.format("%s onLowMemory() called", this.getClass().getName()));
    }
}
