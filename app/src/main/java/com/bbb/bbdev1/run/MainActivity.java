package com.bbb.bbdev1.run;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    private SharedPreferences mPreferences;

    final int EDIT_PROFILE_REQUEST = 1;
    final int SETTINGS_REQUEST = 2;

    BottomNavigationView navigationView;
    ViewPager pager;
    TabsFragmentPagerAdapter adapter;

    Boolean privacyPref;
    String unitPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPreferences = getSharedPreferences(SignInActivity.PROFILES_FILE, MODE_PRIVATE);

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
            // Default to Start tab
            navigationView.setSelectedItemId(R.id.tab_start);
        }

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        privacyPref = sharedPref.getBoolean(SettingsActivity.PRIVACY_PREF_KEY, false);
        unitPref = sharedPref.getString(SettingsActivity.UNIT_PREF_KEY, "kms");
        Toast.makeText(this, unitPref, Toast.LENGTH_SHORT).show();


    }

    public void signOut() {
        // Remove persistent session information
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.remove("SESSION_EMAIL");
        preferencesEditor.apply();

        Intent signOutIntent = new Intent(this, SignInActivity.class);
        startActivity(signOutIntent);
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
                startActivityForResult(settingsIntent, SETTINGS_REQUEST);
                Toast.makeText(MainActivity.this, "Settings selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_edit_profile:
                Intent profileIntent = new Intent(MainActivity.this, RegisterActivity.class);
                profileIntent.putExtra("EXISTING_USER", true);
                startActivityForResult(profileIntent, EDIT_PROFILE_REQUEST);
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
                break;
            case R.id.tab_history:
                pager.setCurrentItem(1);
                break;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == EDIT_PROFILE_REQUEST && resultCode == RESULT_OK) {
            // show a Toast displaying information about the Registration attempt
            String reply = data.getStringExtra(RegisterActivity.EDIT_PROFILE_REPLY);
            boolean signOut = data.getBooleanExtra("SIGNOUT", false);
            Toast.makeText(MainActivity.this, reply, Toast.LENGTH_SHORT).show();
            if (signOut) {
                signOut();
            }
        } else if (requestCode == SETTINGS_REQUEST && resultCode == RESULT_OK) {
            boolean signOut = data.getBooleanExtra("SIGNOUT", false);
            if (signOut) {
                signOut();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }


}
