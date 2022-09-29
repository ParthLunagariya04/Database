package com.parth.sqldatabas;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.parth.sqldatabas.adapter.MyAdapter;
import com.parth.sqldatabas.data.MyDbHandler;
import com.parth.sqldatabas.model.DetailsModel;
import com.parth.sqldatabas.parameters.Parameters;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ImageButton menuButton;
    MyDbHandler myDbHandler;
    SQLiteDatabase sqLiteDatabase;
    MyAdapter myAdapter;
    RecyclerView recyclerView;
    ArrayList<DetailsModel> detailsModels;

    @SuppressLint({"NotifyDataSetChanged", "NonConstantResourceId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        menuButton = findViewById(R.id.menu_button_main);
        recyclerView = findViewById(R.id.recyclerView_mainActivity);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        try {
            myDbHandler = new MyDbHandler(MainActivity.this);
            sqLiteDatabase = myDbHandler.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery(" SELECT * FROM " + Parameters.TABLE_NAME, null);

            detailsModels = new ArrayList<>();
            while (cursor.moveToNext()) {
                //int id = cursor.getInt(0);
                String fullName = cursor.getString(1);
                String userName = cursor.getString(2);
                String email = cursor.getString(3);
                String mobileNo = cursor.getString(4);
                byte[] avatar = cursor.getBlob(6);
                detailsModels.add(new DetailsModel(fullName, userName, email, mobileNo, avatar));

                Log.d("parth", "IM GETTING DATA " + userName);
            }
            cursor.close();
            myAdapter = new MyAdapter(MainActivity.this, R.layout.user_cards, detailsModels, sqLiteDatabase);
            recyclerView.setAdapter(myAdapter);
            myAdapter.notifyDataSetChanged();

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("parth", " THIS MY ERRORS");
        }

        //menu button
        menuButton.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(getBaseContext(), menuButton);
            popupMenu.inflate(R.menu.main_menu);

            popupMenu.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId()) {

                    case R.id.register:
                        startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                        break;

                    case R.id.user_guid:
                        startActivity(new Intent(MainActivity.this, UserGuideActivity.class));
                        break;

                    case R.id.log_out:
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("This will delete your whole database. \nDo you want to delete it ? ");
                        builder.setTitle("Wipe out all data").setCancelable(true);
                        builder.setPositiveButton("Yes", (dialogInterface, i) -> {

                            MyDbHandler myDbHandler = new MyDbHandler(MainActivity.this);
                            sqLiteDatabase = myDbHandler.getWritableDatabase();
                            //sqLiteDatabase.delete(Parameters.TABLE_NAME,  "1", null);
                            sqLiteDatabase.execSQL(" DELETE FROM " + Parameters.TABLE_NAME);
                            sqLiteDatabase.close();

                            SharedPreferences preferences = getSharedPreferences(RegisterActivity.PREF_NAME , 0);
                            preferences.edit().clear().apply();

                            Toast.makeText(MainActivity.this, "Data has been deleted..", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                            finish();

                        }).setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                        break;

                    default:
                        return false;
                }
                return false;
            });
            popupMenu.show();
        });
    }
}