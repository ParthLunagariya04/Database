package com.parth.sqldatabas;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.parth.sqldatabas.data.RoomDBHandler;
import com.parth.sqldatabas.model.RoomDBModel;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class EditContactActivity extends AppCompatActivity {
    public static final int CAMERA_REQUEST = 100;
    public static final int STORAGE_REQUEST = 101;
    ImageButton backImageButton;
    ImageView image;
    EditText name, moNo1, moNo2, email;
    Button save, cancel;
    Uri resultUri;
    String[] cameraPermission;
    String[] storagePermission;
    byte[] byteBuffer;
    RoomDBHandler roomDBHandler;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        backImageButton = findViewById(R.id.back_imageButton_editContact);
        image = findViewById(R.id.profile_image_editContact);
        name = findViewById(R.id.name_editText_editContact);
        moNo1 = findViewById(R.id.mobileNo_editText_editContact);
        moNo2 = findViewById(R.id.mobileNo2_editText_editContact);
        email = findViewById(R.id.email_editText_editContact);
        save = findViewById(R.id.save_button_editContact);
        cancel = findViewById(R.id.cancel_button_editContact);

        buttons();
        imagePick();
        dataShowing();

    }

    // set data on all fields
    private void dataShowing() {
        Intent intent = getIntent();

        try {
            byteBuffer = intent.getByteArrayExtra("image");
            Bitmap bitmap = BitmapFactory.decodeByteArray(byteBuffer, 0, byteBuffer.length);
            image.setImageBitmap(bitmap);

            String name1 = intent.getStringExtra("name1");
            String mobileNo1 = intent.getStringExtra("mobile1");
            String mobileNo2 = intent.getStringExtra("mobile2");
            String email1 = intent.getStringExtra("email");

            name.setText(name1);
            moNo1.setText(mobileNo1);
            moNo2.setText(mobileNo2);
            email.setText(email1);

        } catch (Exception e) {
            Log.d("PARTH_DATA", " my data ERROR - " + e);
        }
    }

    // two buttons
    private void buttons() {

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);

        //back image button
        backImageButton.setOnClickListener(view -> {
            /*startActivity(new Intent(EditContactActivity.this, ContactDetailsActivity.class));
            finish();*/
            onBackPressed();
        });

        // cancel button
        cancel.setOnClickListener(view -> {
            /*startActivity(new Intent(EditContactActivity.this, ContactDetailsActivity.class));
            finish();*/
            onBackPressed();
        });

        //save button
        save.setOnClickListener(view -> {
            String nameUpdate = name.getText().toString();
            String mobileNo1Update = moNo1.getText().toString();
            String mobileNo2Update = moNo2.getText().toString();
            String emailUpdate = email.getText().toString();

            roomDBHandler = RoomDBHandler.getDB(EditContactActivity.this);
            roomDBHandler.roomDbDataAccessObject().update(nameUpdate,
                    mobileNo1Update, emailUpdate, mobileNo2Update, imageViewToByte(image), id);
            startActivity(new Intent(EditContactActivity.this, HomeActivity.class));
            finish();
        });
    }

    // Image picker
    private void imagePick() {
        image.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                //int avatar = 0;
                if (!checkCameraPermission()) {
                    requestCameraPermission();

                } else {
                    pickFromGallery();
                }
            }
        });
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
        boolean result = ContextCompat.checkSelfPermission(EditContactActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        boolean result2 = ContextCompat.checkSelfPermission(EditContactActivity.this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        return result && result2;
    }

    // convert image to byte
    private byte[] imageViewToByte(ImageView avatar) {
        Bitmap bitmap = ((BitmapDrawable) avatar.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        byte[] bytes = stream.toByteArray();
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
                        Toast.makeText(EditContactActivity.this, "Enable camera and storage permission", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case STORAGE_REQUEST:
                if (grantResults.length > 0) {
                    boolean storage_accept = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storage_accept) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(EditContactActivity.this, "Please enable storage permission", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImagePicker.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                resultUri = data.getData();
                image.setImageURI(resultUri);
                //Picasso.get().load(resultUri).into(profileImage);
            }
        }
    }
}