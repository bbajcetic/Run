package com.bbb.bbdev1.run;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class SignInActivity extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private String PROFILES_FILE = "com.bbb.bbdev1.profiles";

    TextInputEditText emailField;
    TextInputEditText passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        setTitle("Sign in");

        emailField = findViewById(R.id.email_field);
        passwordField = findViewById(R.id.password_field);

        mPreferences = getSharedPreferences(PROFILES_FILE, MODE_PRIVATE);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String response = extras.getString("REGISTRATION_MESSAGE");
            if (response != null) {
                Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void signIn(View view) {
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        boolean isError = false;

        if (email.isEmpty()) {
            emailField.setError("This field is required");
            isError = true;
        } else if (!email.contains("@")) {
            emailField.setError("This email address is invalid");
        }
        if (password.isEmpty()) {
            passwordField.setError("This field is required");
            isError = true;
        } else if (password.length() < 6) {
            passwordField.setError("Password must be at least 6 characters");
        }
        if (isError) {
            return;
        }

        String pass_from_preferences = mPreferences.getString(email, null);
        if (pass_from_preferences != null && pass_from_preferences.equals(password)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            Toast.makeText(this, "Sign in successful!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Email or password is incorrect!", Toast.LENGTH_SHORT).show();
        }
    }

    public void register(View view) {
        Intent registerIntent = new Intent(SignInActivity.this, RegisterActivity.class);
        startActivity(registerIntent);
    }

    public void clearData(View view) {
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.clear();
        preferencesEditor.apply();
    }
}
