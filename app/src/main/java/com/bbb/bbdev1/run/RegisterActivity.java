package com.bbb.bbdev1.run;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private String PROFILES_FILE = "com.bbb.bbdev1.profiles";

    TextInputEditText emailField;
    TextInputEditText passwordField;
    TextInputEditText nameField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //enable Up navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        emailField = findViewById(R.id.email_field);
        passwordField = findViewById(R.id.password_field);
        nameField = findViewById(R.id.name_field);
        //set focus to first EditText view
        nameField.requestFocus();

        mPreferences = getSharedPreferences(PROFILES_FILE, MODE_PRIVATE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_register) {
            boolean success = saveProfile();
            if (success) {
                Intent intent = new Intent(RegisterActivity.this, SignInActivity.class);
                intent.putExtra("REGISTRATION_MESSAGE", "Successfully registered!");
                startActivity(intent);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean saveProfile() {
        String email_field = emailField.getText().toString();
        String password_field = passwordField.getText().toString();

        String isEmailThere = mPreferences.getString(email_field, null);
        if (isEmailThere != null) {
            Log.d("REGISTER_ACTIVITY", "Email already exists!");
            Toast.makeText(this, "Registration failed (email already exists!)", Toast.LENGTH_SHORT).show();
            return false;
        }
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putString(email_field, password_field);
        preferencesEditor.apply();
        return true;
    }

}
