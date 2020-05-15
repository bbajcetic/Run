package com.bbb.bbdev1.run;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class SignInActivity extends AppCompatActivity
        implements SignInFragment.OnSignInSelectedListener, LoadingFragment.OnLoadingCompleteListener {

    boolean signin_success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        signin_success = false;
        if (savedInstanceState != null) {
            signin_success = savedInstanceState.getBoolean("SIGNIN_SUCCESS");
            return;
        }
        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) == null) {
            loadSignInFragment();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("SIGNIN_SUCCESS", signin_success);
    }

    public void loadSignInFragment() {
        SignInFragment signInFragment = SignInFragment.newInstance(0);
        FragmentManager fragmentManager = getSupportFragmentManager();
        //carry out the fragment transaction
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, signInFragment);
        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    @Override
    public void onSignInSelected(boolean signin_success) {
        //set signin status to use after loading

        //this.signin_success = signin_success;
        int signin_status = signin_success ? 1 : 2;

        LoadingFragment loadingFragment = LoadingFragment.newInstance(signin_status);
        FragmentManager fragmentManager = getSupportFragmentManager();
        //transaction
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, loadingFragment);
        fragmentTransaction.commit();
    }


    @Override
    public void onLoadingComplete(int signin_status) {
        //int signin_status = this.signin_success ? 1 : 2;

        SignInFragment signInFragment = SignInFragment.newInstance(signin_status);
        FragmentManager fragmentManager = getSupportFragmentManager();
        //transaction
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, signInFragment);
        fragmentTransaction.commitAllowingStateLoss();

        signin_success = false;
    }
}
