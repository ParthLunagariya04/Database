package com.parth.sqldatabas;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.parth.sqldatabas.data.RoomDBHandler;
import com.parth.sqldatabas.data.RoomDbDataAccessObject;
import com.parth.sqldatabas.fragments.AddContactsFragment;
import com.parth.sqldatabas.fragments.ContactFragment;
import com.parth.sqldatabas.model.RoomDBModel;

import java.util.List;

public class HomeActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    FloatingActionButton floatingActionButton;
    TextView fullNameDrawer, emailDrawer;
    ImageView drawerProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        floatingActionButton = findViewById(R.id.fab);

        View headerView = navigationView.getHeaderView(0);
        fullNameDrawer = headerView.findViewById(R.id.full_name_textView_header_drawer);
        emailDrawer = headerView.findViewById(R.id.email_textView_header_drawer);
        drawerProfileImage = headerView.findViewById(R.id.user_image_drawer);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        //load default fragment contacts
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.contact_frameLayout, new ContactFragment())
                .commit();

        //NavigationView
        setNavigationView();
        displaySharedPrefData();

        //floating action button
        floatingActionButton.setOnClickListener(view -> {
            getSupportFragmentManager().beginTransaction().replace(R.id.contact_frameLayout, new AddContactsFragment()).commit();

            floatingActionButton.hide();
        });
    }

    //navigation menu items
    private void setNavigationView() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint({"NonConstantResourceId", "CommitPrefEdits"})
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.user_guide_drawer_menu:
                        Toast.makeText(HomeActivity.this, "user guide", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.signUp_drawer_menu:
                        startActivity(new Intent(HomeActivity.this, RegisterActivity.class));
                        break;

                    case R.id.logOut_drawer_menu:
                        Dialog dialog = new Dialog(HomeActivity.this);
                        dialog.setContentView(R.layout.alert_dialog);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();

                        // cancel dialog
                        Button no = dialog.findViewById(R.id.no_button_dialog);
                        no.setOnClickListener(view -> dialog.dismiss());

                        // Yes button on dialog to delete all data
                        Button yes = dialog.findViewById(R.id.yes_button_dialog);
                        yes.setOnClickListener(view -> {

                           /* try {
                                //delete all data from room db
                                roomDBHandler.roomDbDataAccessObject().deleteAllData();
                            }catch (Exception e){
                                Log.d("PARTH_DATA", "ROOM DB DELETE ERROR "+ e);
                            }*/

                            // clear all data from shared preferences
                            SharedPreferences preferences = getSharedPreferences(RegisterActivity.PREF_NAME, 0);
                            preferences.edit().clear().apply();

                            Toast.makeText(HomeActivity.this, "Data has been deleted..", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(HomeActivity.this, RegisterActivity.class));
                            //dialog.dismiss();
                            finish();
                        });
                        break;

                    default:
                        return false;
                }
                return true;
            }
        });
    }

    //get shared pref data and show them in drawer
    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    public void displaySharedPrefData() {
        try {
            SharedPreferences getSharedPref = getSharedPreferences(RegisterActivity.PREF_NAME, MODE_PRIVATE);
            String fullname = getSharedPref.getString("fullName", "Parth Lunagariya");
            String email = getSharedPref.getString("email", "thunderbeast@gmail.com");
            String profileCardImage = getSharedPref.getString("user_photo", null);

            if (!fullname.isEmpty() && !email.isEmpty() ) {

                //display shared pref image in drawer header
                byte[] imageAsBytes = Base64.decode(profileCardImage.getBytes(), Base64.DEFAULT);
                drawerProfileImage.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));

                //display rest of the data (full name and email) to drawer header
                fullNameDrawer.setText(fullname);
                emailDrawer.setText(email);
            } else {
                //drawerProfileImage.setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.profile_image));
                fullNameDrawer.setText("Parth Lunagariya");
                emailDrawer.setText("thunderbeast11@gmail.com");
            }
        } catch (Exception e) {
            Log.d("PARTH_DATA", "SHARED PREF GETTING DATA ERROR " + e);
        }
    }
}