package com.parth.sqldatabas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.parth.sqldatabas.data.MyDbHandler;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class EditUserInfoActivity extends AppCompatActivity {
    ImageView profileImage;
    EditText fullName, userName, email, mobileNo, password;
    Button saveButton;
    int id = 0;
    String[] cameraPermission;
    String[] storagePermission;
    Uri resultUri;
    public static final int CAMERA_REQUEST = 100;
    public static final int STORAGE_REQUEST = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        profileImage = findViewById(R.id.profile_image_edit);
        fullName = findViewById(R.id.full_name_editText_edit);
        userName = findViewById(R.id.userName_editText_edit);
        email = findViewById(R.id.email_editText_edit);
        mobileNo = findViewById(R.id.mobileNo_editText_edit);
        password = findViewById(R.id.password_editText_edit);
        saveButton = findViewById(R.id.save_button_edit);

        MyDbHandler myDbHandler = new MyDbHandler(this);
        SQLiteDatabase sqLiteDatabase = myDbHandler.getWritableDatabase();
        ContentValues contentValues = new ContentValues();


        //get data function
        getData();

        //pick image
        imagePick();

        //save button - to upgrade database
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fName = fullName.getText().toString();
                String uName = userName.getText().toString();
                String email1 = email.getText().toString();
                String mobileNo1 = mobileNo.getText().toString();
                String password1 = password.getText().toString();

                myDbHandler.upgradeData(fName, uName, email1, mobileNo1, password1, imageViewToByte(profileImage));
                Toast.makeText(EditUserInfoActivity.this, "Data has been Successfully updated..", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(EditUserInfoActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    //getting data from recycler view
    private void getData(){
        if (getIntent().getBundleExtra("userData") != null) {
            Bundle bundle = getIntent().getBundleExtra("userData");
            id = bundle.getInt("id");
            byte[] bytes = bundle.getByteArray("avatar");
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            profileImage.setImageBitmap(bitmap);

            fullName.setText(bundle.getString("fullName"));
            userName.setText(bundle.getString("userName"));
            email.setText(bundle.getString("email"));
            mobileNo.setText(bundle.getString("mobilNo"));
        }
    }

    private void imagePick() {
        profileImage.setOnClickListener(new View.OnClickListener() {
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
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    //Here we will pick image from gallery or camera
    private void pickFromGallery() {
        ImagePicker.with(this).crop().start();
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

        Log.d("parth", "IMAGE TO BYTE");
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
            //CropImage.ActivityResult result = CropImage.getActivityResult(data);

            Log.d("parth", "IMAGE PICKER REQUEST CODE");

            if (resultCode == RESULT_OK) {
                resultUri = data.getData();
                Picasso.get().load(resultUri).into(profileImage);

                Log.d("parth", "IMAGE PICKER result CODE");
            }
        }
    }
}