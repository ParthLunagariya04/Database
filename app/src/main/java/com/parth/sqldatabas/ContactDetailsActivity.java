package com.parth.sqldatabas;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class ContactDetailsActivity extends AppCompatActivity {
    ImageButton imageButtonEdit, backButton;
    ImageView profileImage;
    TextView tvname, mobileNo, tvemail;
    byte[] image;
    String mobilno1;
    String mobilno2;
    String email;
    String name;
    int id;
    SwipeRefreshLayout refreshLayout;

    @SuppressLint({"MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        imageButtonEdit = findViewById(R.id.edit_icon_imageView_contactDetails);
        profileImage = findViewById(R.id.profile_image_contactDetail);
        tvname = findViewById(R.id.name_textView_contactDetail);
        mobileNo = findViewById(R.id.mobileNo_textView_contactDetails);
        tvemail = findViewById(R.id.email_textView_contactDetails);
        backButton = findViewById(R.id.back_imageButton_contactDetail);
        refreshLayout = findViewById(R.id.refresh_layout_contactDetail);

        backImageButton();
        editProfile();
        fetchDataOnRecyclerView();

        //for refreshing whole activity
        refreshLayout.setOnRefreshListener(() -> {
            refreshLayout.setRefreshing(false);

            backImageButton();
            editProfile();
            fetchDataOnRecyclerView();
        });

    }

    @SuppressLint("SetTextI18n")
    private void fetchDataOnRecyclerView() {
        try {
            Intent intent = getIntent();

            image = intent.getByteArrayExtra("image");
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);

            mobilno1 = intent.getStringExtra("mobileNO");
            mobilno2 = intent.getStringExtra("userName");
            name = intent.getStringExtra("name");
            email = intent.getStringExtra("email");
            id = intent.getIntExtra("id", 0);

            if (mobilno1.isEmpty()) {
                mobileNo.setText("+91 " + mobilno2);
            } else {
                mobileNo.setText("+91 " + mobilno1);
            }

            profileImage.setImageBitmap(bitmap);
            tvname.setText(name);
            tvemail.setText(email);

        } catch (Exception e) {
            Log.d("PARTH_DATA", "ERROR " + e);
        }
    }

    //edit profile button
    private void editProfile() {
        imageButtonEdit.setOnClickListener(view -> {
            Intent intent = new Intent(ContactDetailsActivity.this, EditContactActivity.class);
            intent.putExtra("name1", name);
            intent.putExtra("email", email);
            intent.putExtra("mobile1", mobilno1);
            intent.putExtra("mobile2", mobilno2);
            intent.putExtra("image", image);
            intent.putExtra("id", id);
            startActivity(intent);
        });
    }

    private void backImageButton() {
        backButton.setOnClickListener(view -> {
            onBackPressed();
        });
    }
}