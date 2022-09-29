package com.parth.sqldatabas;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.parth.sqldatabas.data.MyDbHandler;
import com.parth.sqldatabas.data.RoomDBHandler;
import com.parth.sqldatabas.model.RoomDBModel;

import java.io.ByteArrayOutputStream;

//developer parth lunagariya
public class RegisterActivity extends AppCompatActivity {
    public static final int CAMERA_REQUEST = 100;
    public static final int STORAGE_REQUEST = 101;
    public static String PREF_NAME = "MyPrefsFile";
    ImageView avatar;
    EditText fullName, userName, mobileNo, password, confirmPassword, email;
    Button signUp, signupUpRoomDb;
    TextView logInTextView;
    MyDbHandler myDbHandler;
    String[] cameraPermission;
    String[] storagePermission;
    Uri resultUri;
    RoomDBHandler roomDBHandler;
    String emailValidate = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String emailValidate2 = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+\\.+[a-z]+";
    String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
    SharedPreferences sharedPreferences;

    @SuppressLint("MissingInflatedId")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        avatar = findViewById(R.id.profile_image_register);
        fullName = findViewById(R.id.full_name_editText_register);
        userName = findViewById(R.id.userName_editText_register);
        mobileNo = findViewById(R.id.mobileNo_editText_register);
        password = findViewById(R.id.password_editText_register);
        email = findViewById(R.id.email_editText_register);
        signUp = findViewById(R.id.signUp_button_register);
        logInTextView = findViewById(R.id.logIn_text_register);
        signupUpRoomDb = findViewById(R.id.signUp_button_roomDb_register);
        confirmPassword = findViewById(R.id.confirm_password_editText_register);

        //preferenceManager for getting image
        //preferenceManager=PreferenceManager.getInstance(this);

        // initialize MyDbHandler class
        myDbHandler = new MyDbHandler(this);
        imagePick();

        //signUp submit button
        signUp.setOnClickListener(view -> {
            String fName = fullName.getText().toString();
            String uName = userName.getText().toString();
            String emails = email.getText().toString();
            String mNumber = mobileNo.getText().toString();
            String psw = password.getText().toString();
            String confiPassword = confirmPassword.getText().toString();

            if (fName.equals("") || uName.equals("") || emails.equals("") || mNumber.equals("") || psw.equals("") || confiPassword.equals("")) {
                Toast.makeText(RegisterActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();

            } else if (resultUri == null) {
                Toast.makeText(this, "Please upload image", Toast.LENGTH_SHORT).show();

            } else if (!emails.matches(emailValidate) && !emails.matches(emailValidate2)) {
                Toast.makeText(this, "enter valid email !..", Toast.LENGTH_SHORT).show();

            } else if (mNumber.length() != 10) {
                Toast.makeText(this, "please enter valid numbers", Toast.LENGTH_SHORT).show();

            } else if (!psw.matches(PASSWORD_PATTERN)) {
                Toast.makeText(RegisterActivity.this, "Password must contain Uppercase, lowercase and Special characters ‼", Toast.LENGTH_SHORT).show();

            } else if (!psw.equals(confiPassword) || psw.length() != confiPassword.length()) {
                Toast.makeText(this, "required password must be same!!..", Toast.LENGTH_SHORT).show();

            } else {
                if (psw != null) {
                    Boolean checkUser = myDbHandler.checkUserName(uName);

                    if (!checkUser) {
                        Boolean insert = myDbHandler.insertData(fName, uName, emails, mNumber, psw, imageViewToByte(avatar));

                        if (insert) {

                            // sharedPreferences
                            sharedPreferences = getSharedPreferences(RegisterActivity.PREF_NAME, 0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("hasLoggedIn", true);
                            editor.apply();

                            Toast.makeText(RegisterActivity.this, "Sign Up Success, Welcome " + uName, Toast.LENGTH_LONG).show();
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                            finish();

                        } else {
                            Toast.makeText(RegisterActivity.this, "Registration failed!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "User already exists! please Log In", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Password is incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        });

        roomDBHandler = RoomDBHandler.getDB(getBaseContext());

        //sign up with room db
        signupUpRoomDb.setOnClickListener(view -> {
            String fName = fullName.getText().toString();
            String uName = userName.getText().toString();
            String emails = email.getText().toString();
            String mNumber = mobileNo.getText().toString();
            String psw = password.getText().toString();
            String confiPassword = confirmPassword.getText().toString();

            if (fName.equals("") || uName.equals("") || emails.equals("") || mNumber.equals("") || psw.equals("")) {
                Toast.makeText(RegisterActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();

            } else if (resultUri == null) {
                Toast.makeText(RegisterActivity.this, "Please upload image", Toast.LENGTH_SHORT).show();

            } else if (!emails.matches(emailValidate) && !emails.matches(emailValidate2)) {
                Toast.makeText(RegisterActivity.this, "enter valid email !..", Toast.LENGTH_SHORT).show();

            } else if (mNumber.length() != 10) {
                Toast.makeText(RegisterActivity.this, "please enter valid numbers", Toast.LENGTH_SHORT).show();

            } else if (!psw.matches(PASSWORD_PATTERN)) {
                Toast.makeText(RegisterActivity.this, "Password must contain Uppercase, lowercase and Special characters ‼", Toast.LENGTH_SHORT).show();

            } else if (!psw.equals(confiPassword) || psw.length() != confiPassword.length()) {
                Toast.makeText(RegisterActivity.this, "required password must be same!!..", Toast.LENGTH_SHORT).show();

            } else {
                roomDBHandler = RoomDBHandler.getDB(getApplicationContext());
                RoomDBModel roomDBModel = roomDBHandler.roomDbDataAccessObject().isTaken(uName);

                if (roomDBModel != null) {
                    Toast.makeText(RegisterActivity.this, "User already exists! please Log In", Toast.LENGTH_SHORT).show();
                } else {
                    roomDBHandler.roomDbDataAccessObject().addData(
                            new RoomDBModel(fName, uName, emails, mNumber, psw, imageViewToByte(avatar)));

                    // sharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences(RegisterActivity.PREF_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("fullName", fName);
                    editor.putString("email", emails);
                    editor.putBoolean("hasLoggedIn2", true);
                    editor.apply();

                    Log.d("parth", "ROOM DB REGISTER");

                    Toast.makeText(RegisterActivity.this, "Sign Up Success, Welcome " + uName, Toast.LENGTH_LONG).show();
                    startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                    finish();
                }
            }
        });

        //move to logIn Activity
        logInTextView.setOnClickListener(view -> startActivity(new Intent(RegisterActivity.this, LogInActivity.class)));
    }

    // Image picker
    private void imagePick() {
        avatar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                int avatar = 0;
                if (avatar == 0) {
                    if (!checkCameraPermission()) {
                        requestCameraPermission();

                        Log.d("parth", " AVATAR 0");
                    } else {
                        pickFromGallery();

                        Log.d("parth", " PICK FROM GALLERY ");
                    }
                } else if (avatar == 1) {
                    if (!checkStoragePermission()) {
                        requestStoragePermission();

                    } else {
                        pickFromGallery();
                    }
                }
            }
        });
    }

    //request storage permission
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestStoragePermission() {
        requestPermissions(storagePermission, STORAGE_REQUEST);
    }

    //check storage permission
    private boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
    }

    //Here we will pick image from gallery or camera
    private void pickFromGallery() {
        ImagePicker.with(this)
                .crop()
                .start();
    }

    //request camera permission
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCameraPermission() {
        requestPermissions(cameraPermission, CAMERA_REQUEST);
    }

    //check camera permission
    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        boolean result2 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        return result && result2;
    }

    // convert image to byte
    private byte[] imageViewToByte(ImageView avatar) {
        Bitmap bitmap = ((BitmapDrawable) avatar.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        byte[] bytes = stream.toByteArray();
        String encodedImage = Base64.encodeToString(bytes, Base64.DEFAULT);

        //store image in sharedPreferences
        sharedPreferences = getSharedPreferences(RegisterActivity.PREF_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_photo", encodedImage).apply();

        return bytes;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST:
                if (grantResults.length > 0) {
                    boolean camera_accept = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storage_accept = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (camera_accept && storage_accept) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, "Enable camera and storage permission", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case STORAGE_REQUEST:
                if (grantResults.length > 0) {
                    boolean storage_accept = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storage_accept) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, "Please enable storage permission", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImagePicker.REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
                assert data != null;
                resultUri = data.getData();
                avatar.setImageURI(resultUri);

                Log.d("parth", "IMAGE PICKER result CODE");
            }
        }
    }
}




