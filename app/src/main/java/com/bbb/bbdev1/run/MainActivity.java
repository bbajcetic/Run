package com.bbb.bbdev1.run;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView navigationView;
    ViewPager pager;
    TabsFragmentPagerAdapter adapter;

    Boolean privacyPref;
    String unitPref;

    String session_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.navigation_main);
        navigationView.setOnNavigationItemSelectedListener((BottomNavigationView.OnNavigationItemSelectedListener) this);

        //pageAdapter = new TabsFragmentPagerAdapter(getSupportFragmentManager());
        ArrayList<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(new StartFragment());
        fragments.add(new HistoryFragment());
        pager = findViewById(R.id.viewpager);
        adapter = new TabsFragmentPagerAdapter(getSupportFragmentManager(), fragments, fragments.size());
        pager.setAdapter(adapter);


        if (savedInstanceState == null) {
            navigationView.setSelectedItemId(R.id.tab_start);
            Intent intent = getIntent();
            session_email = intent.getStringExtra("SESSION_EMAIL");
        } else {
            session_email = savedInstanceState.getString("SESSION_EMAIL");
        }

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        privacyPref = sharedPref.getBoolean(SettingsActivity.PRIVACY_PREF_KEY, false);
        unitPref = sharedPref.getString(SettingsActivity.UNIT_PREF_KEY, "");
        Toast.makeText(this, unitPref, Toast.LENGTH_SHORT).show();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_settings:
                Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
                Toast.makeText(MainActivity.this, "Settings selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_edit_profile:
                Intent profileIntent = new Intent(MainActivity.this, RegisterActivity.class);
                profileIntent.putExtra("EXISTING_USER", true);
                profileIntent.putExtra("SESSION_EMAIL", session_email);
                startActivity(profileIntent);
                Toast.makeText(MainActivity.this, "Edit Profile selected", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.tab_start:
                pager.setCurrentItem(0);
                Toast.makeText(MainActivity.this, "On Start tab", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tab_history:
                pager.setCurrentItem(1);
                Toast.makeText(MainActivity.this, "On History tab", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("SESSION_EMAIL", session_email);
        super.onSaveInstanceState(outState);
    }
}
