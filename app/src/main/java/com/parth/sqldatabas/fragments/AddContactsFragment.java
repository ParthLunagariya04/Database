package com.parth.sqldatabas.fragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.parth.sqldatabas.HomeActivity;
import com.parth.sqldatabas.R;
import com.parth.sqldatabas.data.RoomDBHandler;
import com.parth.sqldatabas.model.RoomDBModel;
import com.parth.sqldatabas.progress_dialog.ProgressDialog;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Objects;

public class AddContactsFragment extends Fragment {
    public static final int CAMERA_REQUEST = 100;
    public static final int STORAGE_REQUEST = 101;
    EditText enterName, mobileNo1, mobileNo2, email;
    Button save, cancel;
    ImageView profileImage, userImageDrawer;
    TextView nameHeaderDrawer, emailHeaderDrawer;
    String[] cameraPermission;
    String[] storagePermission;
    Uri resultUri;
    RoomDBHandler roomDBHandler;
    String emailValidate = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String emailValidate2 = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+\\.+[a-z]+";
    ProgressDialog dialog;

    public AddContactsFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_contacts, container, false);

        dialog = new ProgressDialog(requireContext());

        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        enterName = view.findViewById(R.id.name_editText_addContacts);
        mobileNo1 = view.findViewById(R.id.mobileNo_editText_addContacts);
        mobileNo2 = view.findViewById(R.id.mobileNo2_editText_addContacts);
        email = view.findViewById(R.id.email_editText_addContacts);
        save = view.findViewById(R.id.save_button_addContacts);
        cancel = view.findViewById(R.id.cancel_button_addContacts);
        profileImage = view.findViewById(R.id.profile_image_addContacts);
        userImageDrawer = view.findViewById(R.id.user_image_drawer);
        nameHeaderDrawer = view.findViewById(R.id.full_name_textView_header_drawer);
        emailHeaderDrawer = view.findViewById(R.id.email_textView_header_drawer);

        //cancel button
        cancel.setOnClickListener(view1 -> {
            requireActivity().onBackPressed();
            startActivity(new Intent(getActivity(), HomeActivity.class));
        });

        imagePick();
        saveContacts();

        return view;
    }

    //save contacts in room database
    private void saveContacts() {
        save.setOnClickListener(view -> {
            String name = enterName.getText().toString();
            String moNo1 = mobileNo1.getText().toString();
            String moNo2 = mobileNo2.getText().toString();
            String emails = email.getText().toString();

            if (name.equals("")) {
                Toast.makeText(getContext(), "please enter name", Toast.LENGTH_SHORT).show();

            } else if (moNo1.length() != 10 && moNo2.length() != 10) {
                Toast.makeText(getContext(), "please enter valid numbers", Toast.LENGTH_SHORT).show();

            } else if (!emails.matches(emailValidate) && !emails.matches(emailValidate2)) {
                Toast.makeText(getContext(), "enter valid email !..", Toast.LENGTH_SHORT).show();

            } else {
                //storing data in room DB database
                roomDBHandler = RoomDBHandler.getDB(getContext());
                RoomDBModel roomDBModel = roomDBHandler.roomDbDataAccessObject().isAlreadyTaken(name);

                if (roomDBModel != null) {
                    Toast.makeText(getContext(), "Name is already exists.! please try another name.", Toast.LENGTH_SHORT).show();

                } else {
                    roomDBHandler.roomDbDataAccessObject().addData(new RoomDBModel(name, moNo1, emails, moNo2, imageViewToByte(profileImage)));
                    Toast.makeText(getContext(), "Contact saved successfully", Toast.LENGTH_SHORT).show();
                    requireActivity().onBackPressed();

                    requireActivity().onBackPressed();
                    startActivity(new Intent(getActivity(), HomeActivity.class));
                }
            }
        });
    }

    // Image picker
    private void imagePick() {
        profileImage.setOnClickListener(new View.OnClickListener() {
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
        boolean result = ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        boolean result2 = ContextCompat.checkSelfPermission(requireContext(),
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
                        Toast.makeText(getContext(), "Enable camera and storage permission", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case STORAGE_REQUEST:
                if (grantResults.length > 0) {
                    boolean storage_accept = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storage_accept) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(getContext(), "Please enable storage permission", Toast.LENGTH_SHORT).show();
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
                profileImage.setImageURI(resultUri);
                //Picasso.get().load(resultUri).into(profileImage);
            }
        }
    }
}