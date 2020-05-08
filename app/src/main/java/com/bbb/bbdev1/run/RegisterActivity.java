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
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private String PROFILES_FILE = "com.bbb.bbdev1.profiles";

    TextInputEditText emailField;
    TextInputEditText passwordField;
    TextInputEditText nameField;
    RadioGroup genderField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //enable Up navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        genderField = findViewById(R.id.gender_field);
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
        String name_field = nameField.getText().toString();
        int gender_field = genderField.getCheckedRadioButtonId();

        // Check for input errors
        boolean isError = false;
        if (email_field.isEmpty()) {
            emailField.setError("This field is required");
            isError = true;
        } else if (!email_field.contains("@")) {
            emailField.setError("This email address is invalid");
            isError = true;
        }
        if (password_field.isEmpty()) {
            passwordField.setError("This field is required");
            isError = true;
        } else if (password_field.length() < 6) {
            passwordField.setError("Password must be at least 6 characters");
            isError = true;
        }
        if (name_field.isEmpty()) {
            nameField.setError("This field is required");
            isError = true;
        }
        if (gender_field == -1) {
            Toast.makeText(this, "Gender is required!", Toast.LENGTH_SHORT).show();
            isError = true;
        }
        if (isError) {
            return false;
        }

        // Check for existing email
        if (mPreferences.getString(email_field, null) != null) {
            Log.d("REGISTER_ACTIVITY", "Email already exists!");
            Toast.makeText(this, "Registration failed (email already exists!)", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Save email/password combination
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putString(email_field, password_field);
        preferencesEditor.apply();
        return true;
    }

}
