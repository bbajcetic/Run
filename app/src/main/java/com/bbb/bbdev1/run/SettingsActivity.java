package com.bbb.bbdev1.run;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

public class SettingsActivity extends AppCompatActivity {

    public static final String PRIVACY_PREF_KEY = "privacy_setting";
    public static final String UNIT_PREF_KEY = "unit_setting";
    public static final String WEBPAGE_PREF_KEY = "webpage_setting";
    public static final String SIGN_OUT_PREF_KEY = "sign_out_setting";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    public void signOut() {
        Intent signOutIntent = new Intent();
        signOutIntent.putExtra("SIGNOUT", true);
        setResult(RESULT_OK, signOutIntent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //onBackPressed();
            Intent returnIntent = new Intent();
            setResult(RESULT_OK, returnIntent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
