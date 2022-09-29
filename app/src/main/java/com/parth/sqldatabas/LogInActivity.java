package com.parth.sqldatabas;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parth.sqldatabas.data.MyDbHandler;
import com.parth.sqldatabas.data.RoomDBHandler;
import com.parth.sqldatabas.data.RoomDbDataAccessObject;
import com.parth.sqldatabas.model.RoomDBModel;

public class LogInActivity extends AppCompatActivity {
    EditText userName, password;
    Button logInButton, logInRoomDb;
    TextView signUp;
    MyDbHandler myDbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        userName = findViewById(R.id.userName_editText_login);
        password = findViewById(R.id.password_editText_login);
        logInButton = findViewById(R.id.login_button_login);
        signUp = findViewById(R.id.signUp_text_login);
        logInRoomDb = findViewById(R.id.login_button_roomDb_login);

        // initialize MyDbHandler class
        myDbHandler = new MyDbHandler(this);

        //login button
        logInButton.setOnClickListener(view -> {
            String uName = userName.getText().toString();
            String psw = password.getText().toString();

            if (uName.equals("")) {
                Toast.makeText(this, "User Name is required", Toast.LENGTH_SHORT).show();
            } else if (psw.equals("")) {
                Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show();
            } else {
                Boolean checkUserPassword = myDbHandler.checkUserNamePassword(uName, psw);
                if (checkUserPassword) {
                    Toast.makeText(this, "Welcome " + uName, Toast.LENGTH_LONG).show();
                    startActivity(new Intent(LogInActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, "Invalid credentials !. enter valid details", Toast.LENGTH_SHORT).show();
                }
            }
        });

        logInRoomDb.setOnClickListener(view -> {
            String uName = userName.getText().toString();
            String psw = password.getText().toString();

            if (uName.equals("")) {
                Toast.makeText(this, "User Name is required", Toast.LENGTH_SHORT).show();
            } else if (psw.equals("")) {
                Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show();
            } else {
                RoomDBHandler roomDBHandler = RoomDBHandler.getDB(getApplicationContext());
                final RoomDbDataAccessObject object = roomDBHandler.roomDbDataAccessObject();
                RoomDBModel roomDBModel = roomDBHandler.roomDbDataAccessObject().logIn(uName, psw);

                if (roomDBModel == null) {
                    Toast.makeText(this, "Invalid credentials !. enter valid details", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Welcome " + uName, Toast.LENGTH_LONG).show();
                    startActivity(new Intent(LogInActivity.this, HomeActivity.class));
                    finish();
                }
            }
        });

        //move to Register/ signUp Activity
        signUp.setOnClickListener(view -> startActivity(new Intent(LogInActivity.this, RegisterActivity.class)));
    }
}