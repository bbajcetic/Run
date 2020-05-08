package com.bbb.bbdev1.run;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
        //set focus to first EditText view
        emailField.requestFocus();

        mPreferences = getSharedPreferences(PROFILES_FILE, MODE_PRIVATE);

        if (savedInstanceState != null) {
            emailField.setText(savedInstanceState.getString("EMAIL"));
            passwordField.setText(savedInstanceState.getString("PASSWORD"));
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String response = extras.getString("REGISTRATION_MESSAGE");
            if (response != null) {
                Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("EMAIL", emailField.getText().toString());
        outState.putString("PASSWORD", passwordField.getText().toString());
        super.onSaveInstanceState(outState);
    }

    public void signIn(View view) {
        String email_field = emailField.getText().toString();
        String password_field = passwordField.getText().toString();

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
        if (isError) {
            return;
        }

        // Check if email/password combination exists
        String pass_from_preferences = mPreferences.getString(email_field, null);
        if (pass_from_preferences != null && pass_from_preferences.equals(password_field)) {
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
