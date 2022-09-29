package com.parth.sqldatabas;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class UserGuideActivity extends AppCompatActivity {
    VideoView videoView;
    TextView textView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_guide);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        videoView = findViewById(R.id.video);
        textView = findViewById(R.id.user_guide_text);

        textView.setText("✦ Userfriendly UI \n✦ End to end secure \n✦ RecyclerView animation " +
                "\n✦ Your data will be secure until your device has \n     been not hacked" +
                "\n✦ Supporting multiple user Sign Up" +
                "\n✦ Delete single/multiple user \n✦ User can edit their details" +
                "\n✦ Supporting image upload during Sign Up and \n     Edit user profile" +
                "\n✦ User name must be unique" +
                "\n✦ For more information please watch video \n     below");

        videoView.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.user_guid_video);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
    }
}