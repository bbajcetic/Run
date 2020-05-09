package com.bbb.bbdev1.run;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private String PROFILES_FILE = "com.bbb.bbdev1.profiles";


    final int PERMISSION_REQUEST_CAMERA = 1;
    final int PERMISSION_REQUEST_READ_EXTERNAL = 2;
    final int PERMISSION_REQUEST_REGISTER_BEGIN = 0;

    final int GALLERY_REQUEST = 0;
    final int CAMERA_REQUEST = 1;

    TextInputEditText nameField;
    RadioGroup genderField;
    TextInputEditText emailField;
    TextInputEditText passwordField;
    TextInputEditText phoneField;
    TextInputEditText majorField;
    TextInputEditText classField;
    ImageView displayPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //enable Up navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nameField = findViewById(R.id.name_field);
        //set focus to first EditText view
        nameField.requestFocus();
        genderField = findViewById(R.id.gender_field);
        emailField = findViewById(R.id.email_field);
        passwordField = findViewById(R.id.password_field);
        phoneField = findViewById(R.id.phone_field);
        majorField = findViewById(R.id.major_field);
        classField = findViewById(R.id.class_field);
        displayPic = findViewById(R.id.display_pic);


        mPreferences = getSharedPreferences(PROFILES_FILE, MODE_PRIVATE);

        if (savedInstanceState != null) {
            nameField.setText(savedInstanceState.getString("NAME"));
            emailField.setText(savedInstanceState.getString("EMAIL"));
            passwordField.setText(savedInstanceState.getString("PASSWORD"));
            phoneField.setText(savedInstanceState.getString("PHONE"));
            majorField.setText(savedInstanceState.getString("MAJOR"));
            classField.setText(savedInstanceState.getString("CLASS"));
        }

        requestPermissionsBeforeRegister();
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

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("NAME", nameField.getText().toString());
        outState.putString("EMAIL", emailField.getText().toString());
        outState.putString("PASSWORD", passwordField.getText().toString());
        outState.putString("PHONE", phoneField.getText().toString());
        outState.putString("MAJOR", majorField.getText().toString());
        outState.putString("CLASS", classField.getText().toString());
        super.onSaveInstanceState(outState);
    }

    public void changeDisplayPicture(View view) {
        String dialogTitle = "Profile Picture Picker";
        String[] dialogOptions = { "Take from camera", "Select from gallery" };
        new AlertDialog.Builder(this).setTitle(dialogTitle).setItems(dialogOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) { //take from camera
                    requestCameraPermissions();
                } else if (which == 1) { //select from gallery;
                    requestGalleryPermissions();
                }
            }
        }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap)extras.get("data");
            displayPic.setImageBitmap(imageBitmap);
        } else if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            //Bitmap imageBitmap = (Bitmap)extras.get("data");
            //displayPic.setImageBitmap(imageBitmap);
            Uri image_uri = data.getData();
            if (image_uri != null) {
                displayPic.setImageURI(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startCameraIntent() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }
    private void startGalleryIntent() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (galleryIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(galleryIntent, GALLERY_REQUEST);
        }
    }
    private void requestPermissionsBeforeRegister() {
        ArrayList<String> permissions = new ArrayList<String>();
        if (!isCameraEnabled()) {
            permissions.add(Manifest.permission.CAMERA);
        }
        if (!isGalleryEnabled()) {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (permissions.size() > 0) {
            ActivityCompat.requestPermissions(this, permissions.toArray(new String[0]), PERMISSION_REQUEST_REGISTER_BEGIN);
        }
    }
    private void requestCameraPermissions() {
        if (!isCameraEnabled()) {
            //explanation
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
        }
        else {
            startCameraIntent();
        }
    }
    private void requestGalleryPermissions() {
        if (!isGalleryEnabled()) {
            //explanation
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_READ_EXTERNAL);
        }
        else {
            startGalleryIntent();
        }
    }

    private boolean isCameraEnabled() {
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);
    }
    private boolean isGalleryEnabled() {
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case PERMISSION_REQUEST_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(this, "Camera request successful", Toast.LENGTH_SHORT).show();
                    startCameraIntent();
                } else {
                    Toast.makeText(this, "Camera request unsuccessful", Toast.LENGTH_SHORT).show();
                }
                break;
            case PERMISSION_REQUEST_READ_EXTERNAL:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(this, "Read external memory request successful", Toast.LENGTH_SHORT).show();
                    startGalleryIntent();
                } else {
                    Toast.makeText(this, "Read external memory request unsuccessful", Toast.LENGTH_SHORT).show();
                }
                break;
            case PERMISSION_REQUEST_REGISTER_BEGIN:
                //no action taken
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
