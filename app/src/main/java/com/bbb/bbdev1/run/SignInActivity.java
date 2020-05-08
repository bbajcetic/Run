package com.bbb.bbdev1.run;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;

public class SignInActivity extends AppCompatActivity {

    TextInputEditText emailField;
    TextInputEditText passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        setTitle("Sign in");

        emailField = findViewById(R.id.email_field);
        passwordField = findViewById(R.id.password_field);
    }

    public void signIn(View view) {
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
    }

    public void register(View view) {
        Intent registerIntent = new Intent(SignInActivity.this, RegisterActivity.class);
        startActivity(registerIntent);
    }
}
