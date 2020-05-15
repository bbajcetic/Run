package com.bbb.bbdev1.run;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class SignInFragment extends Fragment implements View.OnClickListener {

    private OnSignInSelectedListener listener;

    private SharedPreferences mPreferences;
    private String PROFILES_FILE = "com.bbb.bbdev1.profiles";
    final int REGISTER_REQUEST = 1;

    private TextInputEditText emailField;
    private TextInputEditText passwordField;
    private Button registerButton;
    private Button signInButton;
    private Button clearButton;

    private String emailState;
    private String passwordState;

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.register_button:
                register(v);
                break;
            case R.id.signin_button:
                try {
                    signIn(v);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.clear_button:
                clearData(v);
                break;
        }
    }

    public interface OnSignInSelectedListener {
        public void onSignInSelected(boolean success);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnSignInSelectedListener) context;
        } catch (ClassCastException e) { throw new ClassCastException(context.toString() + " must implement OnSignInSelectedListener"); }
    }

    public SignInFragment() {
        // Required empty public constructor
    }


    public static SignInFragment newInstance(int signin_status) {
        /*
        This method takes a parameter signin_status which can take on the following values:
            0: no signin was attempted, the activity was just created
            1: signin was attempted and was successful
            2: signin was attempted and failed
        */

        SignInFragment fragment = new SignInFragment();

        Bundle args = new Bundle();
        args.putInt("SIGNIN_STATUS", signin_status);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();
        int signin_status = args.getInt("SIGNIN_STATUS");

        mPreferences = getActivity().getSharedPreferences(PROFILES_FILE, MODE_PRIVATE);
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_sign_in, container, false);
        emailField = root.findViewById(R.id.email_field);
        passwordField = root.findViewById(R.id.password_field);
        registerButton = root.findViewById(R.id.register_button);
        registerButton.setOnClickListener(this);
        signInButton = root.findViewById(R.id.signin_button);
        signInButton.setOnClickListener(this);
        clearButton = root.findViewById(R.id.clear_button);
        clearButton.setOnClickListener(this);


        //restore state of email or password on configuration change
        if (savedInstanceState != null) {
            signin_status = 0; //this is because we don't want to send a response every config change
            emailField.setText(savedInstanceState.getString("EMAIL_STATE"));
            passwordField.setText(savedInstanceState.getString("PASSWORD_STATE"));
        }

        //set focus to first EditText view
        emailField.requestFocus();
        emailField.setSelection(emailField.getText().length());


        // if loading just finished, take action and correctly respond to the sign in attempt
        if (signin_status == 1) {
            signinResponse(true);
        } else if (signin_status == 2) {
            signinResponse(false);
        }

        return root;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        try {
            outState.putString("EMAIL_STATE", emailField.getText().toString());
            outState.putString("PASSWORD_STATE", passwordField.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void signIn(View view) throws InterruptedException {

        String email_field = emailField.getText().toString();
        String password_field = passwordField.getText().toString();

        // Check for input errors
        boolean emailError = false;
        boolean passwordError = false;
        if (email_field.isEmpty()) {
            emailField.setError("This field is required");
            emailError = true;
        } else if (!email_field.contains("@")) {
            emailField.setError("This email address is invalid");
            emailError = true;
        }
        if (password_field.isEmpty()) {
            passwordField.setError("This field is required");
            passwordError = true;
        } else if (password_field.length() < 6) {
            passwordField.setError("Password must be at least 6 characters");
            passwordError = true;
        }

        // select the EditText View so that the user can correct it
        if (emailError) {
            emailField.requestFocus();
            emailField.setSelection(emailField.getText().length());
        } else if (passwordError) {
            passwordField.requestFocus();
            passwordField.setSelection(passwordField.getText().length());
        }

        // don't attempt to sign in if there is an input error
        if (emailError || passwordError) { return; }

        //check if signin attempt was successful
        String pass_from_preferences = mPreferences.getString(email_field, null);
        boolean signin_success =
                (pass_from_preferences != null && pass_from_preferences.equals(password_field));

        // save email and password state
        saveUIState();

        //start the loading to show the user the signin is in process
        listener.onSignInSelected(signin_success);

    }
    public void signinResponse(boolean signin_success) {
        loadUIState();
        if (signin_success) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
            Toast.makeText(getActivity(), "Sign in successful!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Email or password is incorrect!", Toast.LENGTH_SHORT).show();
        }
    }

    public void register(View view) {
        Intent registerIntent = new Intent(getActivity(), RegisterActivity.class);
        registerIntent.putExtra("EMAIL_STATE", emailField.getText().toString());
        registerIntent.putExtra("PASSWORD_STATE", passwordField.getText().toString());
        startActivityForResult(registerIntent, REGISTER_REQUEST);
    }

    public void clearData(View view) {
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.clear();
        preferencesEditor.apply();
    }

    public void saveUIState() {
        String email_field = emailField.getText().toString();
        String password_field = passwordField.getText().toString();
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putString("EMAIL_STATE", email_field);
        preferencesEditor.putString("PASSWORD_STATE", password_field);
        preferencesEditor.apply();
    }

    public void loadUIState() {
        emailField.setText(mPreferences.getString("EMAIL_STATE", ""));
        passwordField.setText(mPreferences.getString("PASSWORD_STATE", ""));
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.remove("EMAIL_STATE");
        preferencesEditor.remove("PASSWORD_STATE");
        preferencesEditor.apply();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REGISTER_REQUEST && resultCode == RESULT_OK) {
            // show a Toast displaying information about the Registration attempt
            String reply = data.getStringExtra(RegisterActivity.SIGN_IN_REPLY);
            emailState = data.getStringExtra("EMAIL_STATE");
            passwordState = data.getStringExtra("PASSWORD_STATE");
            emailField.setText(emailState);
            passwordField.setText(passwordState);
            Toast.makeText(getActivity(), reply, Toast.LENGTH_SHORT).show();
            /*Bundle extras = getActivity().getIntent().getExtras();
            if (extras != null) {
                String response = extras.getString("REGISTRATION_MESSAGE");
                if (response != null) {
                    Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                }
            }*/
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
