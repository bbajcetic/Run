package com.bbb.bbdev1.run;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        setTitle("Sign in");
    }

    public void signIn(View view) {
    }

    public void register(View view) {
        Intent registerIntent = new Intent(SignInActivity.this, RegisterActivity.class);
        startActivity(registerIntent);
    }
}
