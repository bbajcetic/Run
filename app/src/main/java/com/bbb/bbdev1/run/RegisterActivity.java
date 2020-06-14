package com.bbb.bbdev1.run;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.PathUtils;
import androidx.core.util.Pair;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

//crop library
import com.soundcloud.android.crop.Crop;

import com.google.android.material.textfield.TextInputEditText;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

public class RegisterActivity extends AppCompatActivity {

    private SharedPreferences mPreferences;

    // Register screen or Edit Profile screen
    boolean existingUser;

    String emailState;
    String passwordState;

    final int PERMISSION_REQUEST_CAMERA = 1;
    final int PERMISSION_REQUEST_READ_EXTERNAL = 2;
    final int PERMISSION_REQUEST_REGISTER_BEGIN = 0;

    final int GALLERY_REQUEST = 0;
    final int CAMERA_REQUEST = 1;
    final int CROP_REQUEST = 2;

    final String BEFORE_CROP_FILE_NAME = "before_crop.jpg";
    final String AFTER_CROP_FILE_NAME = "after_crop.jpg";
    final String SAVED_FILE_NAME = "_display_pic.jpg";
    public static final String SIGN_IN_REPLY = "sign_in_reply";
    public static final String EDIT_PROFILE_REPLY = "edit_profile_reply";

    public static final String KEY_DISPLAY_PIC = "_display_pic";
    public static final String KEY_NAME = "_name";
    public static final String KEY_GENDER = "_gender";
    public static final String KEY_PHONE = "_phone";
    public static final String KEY_MAJOR = "_major";
    public static final String KEY_CLASS = "_class";

    File beforeCropFile;
    File afterCropFile;

    Uri afterCropUri = null;
    Uri beforeCropUri = null;

    TextInputEditText nameField;
    RadioGroup genderField;
    TextInputEditText emailField;
    TextInputEditText passwordField;
    TextInputEditText phoneField;
    TextInputEditText majorField;
    TextInputEditText classField;
    ImageView displayPic;

    // for the Edit Profile screen
    String session_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //enable Up navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPreferences = getSharedPreferences(SignInActivity.PROFILES_FILE, MODE_PRIVATE);

        handleIntent();
        setupFields();
        
        if (existingUser) {
            emailField.setInputType(InputType.TYPE_NULL);
            emailField.setTextIsSelectable(false);
            emailField.setEnabled(false);
            emailField.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keycode, KeyEvent event) {
                    return true;
                }
            });
            if (savedInstanceState == null) {
                setupEditProfile();
            }
        }
        if (savedInstanceState != null) {
            existingUser = savedInstanceState.getBoolean("EXISTING_USER");
            nameField.setText(savedInstanceState.getString("NAME"));
            emailField.setText(savedInstanceState.getString("EMAIL"));
            passwordField.setText(savedInstanceState.getString("PASSWORD"));
            phoneField.setText(savedInstanceState.getString("PHONE"));
            majorField.setText(savedInstanceState.getString("MAJOR"));
            classField.setText(savedInstanceState.getString("CLASS"));
            if (savedInstanceState.getParcelable("AFTER_PICTURE_URI") != null) {
                afterCropUri = savedInstanceState.getParcelable("AFTER_PICTURE_URI");
                displayPic.setImageURI(afterCropUri);
            }
            if (savedInstanceState.getParcelable("BEFORE_PICTURE_URI") != null) {
                beforeCropUri = savedInstanceState.getParcelable("BEFORE_PICTURE_URI");
            }
        }

        requestPermissionsBeforeRegister();
    }
    private void handleIntent() {
        // save state of email/password fields from SignInActivity and set correct title
        Intent intent = getIntent();
        emailState = intent.getStringExtra("EMAIL_STATE");
        passwordState = intent.getStringExtra("PASSWORD_STATE");
        // existingUser = true : Registering    |   existingUser = false : Edit Profile
        existingUser = intent.getBooleanExtra("EXISTING_USER", false);
        setTitle(existingUser ? "Profile" : "Sign up");
    }
    private void setupFields() {
        afterCropFile = new File(this.getFilesDir(), AFTER_CROP_FILE_NAME);
        beforeCropFile = new File(this.getFilesDir(), BEFORE_CROP_FILE_NAME);

        nameField = findViewById(R.id.name_field);
        genderField = findViewById(R.id.gender_field);
        emailField = findViewById(R.id.email_field);
        passwordField = findViewById(R.id.password_field);
        phoneField = findViewById(R.id.phone_field);
        majorField = findViewById(R.id.major_field);
        classField = findViewById(R.id.class_field);
        displayPic = findViewById(R.id.display_pic);
        //set focus to first EditText view
        nameField.requestFocus();
    }
    private void setupEditProfile() {
        //Edit Profile screen populate fields with saved user data
        session_email = mPreferences.getString("SESSION_EMAIL", null);
        emailField.setText(session_email);
        passwordField.setText(mPreferences.getString(session_email, ""));
        nameField.setText(mPreferences.getString(session_email + KEY_NAME, ""));
        phoneField.setText(mPreferences.getString(session_email + KEY_PHONE, ""));
        majorField.setText(mPreferences.getString(session_email + KEY_MAJOR, ""));
        classField.setText(mPreferences.getString(session_email + KEY_CLASS, ""));
        genderField.check(mPreferences.getInt(session_email + KEY_GENDER, -1));

        String uriString = mPreferences.getString(session_email + KEY_DISPLAY_PIC, null);
        if (uriString != null) {
            afterCropUri = Uri.parse(uriString);
            displayPic.setImageURI(afterCropUri);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (existingUser) {
            getMenuInflater().inflate(R.menu.menu_profile, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_register, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.action_save && existingUser) {
            boolean hasPasswordChanged = hasPasswordChanged();
            boolean success = saveProfile();
            if (success) {
                Intent replyIntent = new Intent();
                replyIntent.putExtra(EDIT_PROFILE_REPLY, "Successfully saved!");
                replyIntent.putExtra("SIGNOUT", hasPasswordChanged);
                setResult(RESULT_OK, replyIntent);
                finish();
            }
        } else if (item.getItemId() == R.id.action_register && !existingUser) {
            boolean success = registerProfile();
            if (success) {
                Intent replyIntent = new Intent();
                replyIntent.putExtra(SIGN_IN_REPLY, "Successfully registered!");
                // these next 2 extras just keep the state of the user fields from the SignIn activity
                replyIntent.putExtra("EMAIL_STATE", emailState);
                replyIntent.putExtra("PASSWORD_STATE", passwordState);
                setResult(RESULT_OK, replyIntent);
                finish();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Registers a profile (Register screen)
     * @return boolean success, where success indicates if the profile was registered successfully
     */
    private boolean registerProfile() {
        if (isErrorInFields()) {
            return false;
        }
        String email_field = emailField.getText().toString();
        String password_field = passwordField.getText().toString();
        if (isExistingEmail(email_field)) {
            Log.d("REGISTER_ACTIVITY", "Email already exists!");
            Toast.makeText(this, "Registration failed (email already exists!)", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Save email/password combination
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putString(email_field, password_field);
        preferencesEditor.putInt(email_field + KEY_GENDER, genderField.getCheckedRadioButtonId());
        preferencesEditor.putString(email_field + KEY_NAME, nameField.getText().toString());
        preferencesEditor.putString(email_field + KEY_PHONE, phoneField.getText().toString());
        preferencesEditor.putString(email_field + KEY_MAJOR, majorField.getText().toString());
        preferencesEditor.putString(email_field + KEY_CLASS, classField.getText().toString());
        Toast.makeText(this, "Registered! Email: " + email_field + ", Password: " + password_field, Toast.LENGTH_LONG).show();

        if (afterCropUri != null) {
            File displayPicFile = new File(this.getFilesDir(), email_field + SAVED_FILE_NAME);
            //transfer file at uri: afterCropUri to file: displayPicFile and store Uri in savedUri
            Uri savedUri = transferToInternalFile(afterCropUri, displayPicFile);
            preferencesEditor.putString(email_field + KEY_DISPLAY_PIC, savedUri.toString());
            //if editing profile and changing email, delete last pic
        }
        preferencesEditor.apply();
        return true;
    }

    /**
     * Saves a profile (Edit Profile screen)
     * @return boolean success, where success indicates if the profile was saved successfully
     */
    private boolean saveProfile() {
        // get current user's email
        String session_email = mPreferences.getString("SESSION_EMAIL", null);
        assert (session_email != null);
        String password_field = passwordField.getText().toString();

        if (isErrorInFields()) {
            return false;
        }

        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putString(session_email, password_field);
        Toast.makeText(this, "Saved! Email: " + session_email + ", Password: " + password_field, Toast.LENGTH_LONG).show();
        preferencesEditor.putInt(session_email + KEY_GENDER, genderField.getCheckedRadioButtonId());
        preferencesEditor.putString(session_email + KEY_NAME, nameField.getText().toString());
        preferencesEditor.putString(session_email + KEY_PHONE, phoneField.getText().toString());
        preferencesEditor.putString(session_email + KEY_MAJOR, majorField.getText().toString());
        preferencesEditor.putString(session_email + KEY_CLASS, classField.getText().toString());
        // get URI string of saved display pic if user has added one yet
        String uriString = mPreferences.getString(session_email + KEY_DISPLAY_PIC, null);

        // convert the URI string to a URI (null if the String was null)
        Uri previousUri = (uriString == null) ? null : Uri.parse(uriString);
        // if the current URI doesn't match the previous one, that means the user updated their
        // display picture and it needs to be saved
        if (afterCropUri != null && !afterCropUri.equals(previousUri)) {
            // update the display picture file with the newly updated display picture
            File displayPicFile = new File(this.getFilesDir(), session_email + SAVED_FILE_NAME);
            //transfer file at uri: afterCropUri to file: displayPicFile and store Uri in savedUri
            Uri savedUri = transferToInternalFile(afterCropUri, displayPicFile);
            preferencesEditor.putString(session_email + KEY_DISPLAY_PIC, savedUri.toString());
        }
        preferencesEditor.apply();
        return true;
    }
    /**
     * Returns true if the password field has been changed
     * @return A value indicating if the password field has been changed or not
     */
    private boolean hasPasswordChanged() {
        String session_email = mPreferences.getString("SESSION_EMAIL", null);
        String oldPassword = mPreferences.getString(session_email, null);
        String password_field = passwordField.getText().toString();

        // Check if password has changed
        if (!oldPassword.equals(password_field)) {
            return true;
        }
        return false;
    }

    /**
     * Returns true if the email field matches the email of an existing user account
     * @param email A string containing the email to check
     * @return A value indicating if the email belongs to an existing account
     */
    private boolean isExistingEmail(String email) {
        if (mPreferences.getString(email, null) != null) {
            return true;
        }
        return false;
    }
    private boolean isErrorInFields() {
        // Check for input errors
        boolean isError = false;

        String email_field = emailField.getText().toString();
        String password_field = passwordField.getText().toString();
        String name_field = nameField.getText().toString();
        int gender_field = genderField.getCheckedRadioButtonId();

        if (password_field.isEmpty()) {
            passwordField.setError("This field is required");
            passwordField.requestFocus();
            passwordField.setSelection(passwordField.getText().length());
            isError = true;

        } else if (password_field.length() < 6) {
            passwordField.setError("Password must be at least 6 characters");
            passwordField.requestFocus();
            passwordField.setSelection(passwordField.getText().length());
            isError = true;
        }
        if (email_field.isEmpty()) {
            emailField.setError("This field is required");
            emailField.requestFocus();
            emailField.setSelection(emailField.getText().length());
            isError = true;
        } else if (!email_field.contains("@")) {
            emailField.setError("This email address is invalid");
            emailField.requestFocus();
            emailField.setSelection(emailField.getText().length());
            isError = true;
        }
        if (name_field.isEmpty()) {
            nameField.setError("This field is required");
            nameField.requestFocus();
            nameField.setSelection(nameField.getText().length());
            isError = true;
        }
        if (gender_field == -1) {
            Toast.makeText(this, "Gender is required!", Toast.LENGTH_SHORT).show();
            isError = true;
        }
        if (isError) {
            return true;
        }
        return false;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean("EXISTING_USER", existingUser);
        outState.putString("NAME", nameField.getText().toString());
        outState.putString("EMAIL", emailField.getText().toString());
        outState.putString("PASSWORD", passwordField.getText().toString());
        outState.putString("PHONE", phoneField.getText().toString());
        outState.putString("MAJOR", majorField.getText().toString());
        outState.putString("CLASS", classField.getText().toString());
        if (afterCropUri != null) {
            outState.putParcelable("AFTER_PICTURE_URI", afterCropUri);
        }
        if (beforeCropUri != null) {
            outState.putParcelable("BEFORE_PICTURE_URI", beforeCropUri);
        }
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

    public int getOrientationFromFile(File path) {
        try {
            int rotation = 0;
            Bitmap bitmap = BitmapFactory.decodeFile(path.getAbsolutePath());
            ExifInterface ei = new ExifInterface(path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            switch(orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotation = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotation = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotation = 270;
                    break;
                case ExifInterface.ORIENTATION_NORMAL:
                    break;
            }
            return rotation;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public Uri transferToInternalFile(Uri uri, File file) {
        try {
            InputStream in = new BufferedInputStream(getContentResolver().openInputStream(uri));
            OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
            byte buffer[] = new byte[1024];
            int length = 0;
            while((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            out.close();
            in.close();
            return FileProvider.getUriForFile(RegisterActivity.this,
                    getPackageName() + ".provider", file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void rotateFile(Uri uri, File file, float rotation) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);
            Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
            OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
            newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            if (beforeCropUri == null || beforeCropFile == null) { return; }

            //beforeCropUri holds the uri of the File: beforeCropFile
            float rotation = getOrientationFromFile(beforeCropFile);
            rotateFile(beforeCropUri, beforeCropFile, rotation);
            afterCropUri = FileProvider.getUriForFile(RegisterActivity.this,
                    "com.bbb.bbdev1.run.provider", afterCropFile);
            Crop.of(beforeCropUri, afterCropUri).asSquare().start(this);
        }
        else if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            Uri image_uri = data.getData();
            if (image_uri == null) {
                return; }

            //transfer file at uri: image_uri to file: beforeCropFile and store Uri in beforeCropUri
            beforeCropUri = transferToInternalFile(image_uri, beforeCropFile);
            float rotation = getOrientationFromFile(beforeCropFile);
            rotateFile(beforeCropUri, beforeCropFile, rotation);

            afterCropUri = FileProvider.getUriForFile(RegisterActivity.this,
                    "com.bbb.bbdev1.run.provider", afterCropFile);
            Crop.of(beforeCropUri, afterCropUri).asSquare().start(this);
        }
        else if (requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK) {
            displayPic.setImageResource(0);

            if (afterCropUri != null) {
                displayPic.setImageURI(afterCropUri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startCameraIntent() {
        beforeCropUri = FileProvider.getUriForFile(RegisterActivity.this,
                this.getPackageName() + ".provider", beforeCropFile);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, beforeCropUri);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }
    private void startGalleryIntent() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        galleryIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
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
        } else {
            startCameraIntent();
        }
    }
    private void requestGalleryPermissions() {
        if (!isGalleryEnabled()) {
            //explanation
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_READ_EXTERNAL);
        } else {
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
