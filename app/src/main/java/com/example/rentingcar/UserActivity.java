package com.example.rentingcar;

import static com.example.rentingcar.R.id.bookingCar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import com.example.rentingcar.R;

import android.widget.Button;
import androidx.appcompat.widget.Toolbar; // Ensure this is correctly imported

import com.example.rentingcar.R; // Ensure this is correctly imported

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class UserActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar; // Use androidx.appcompat.widget.Toolbar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        /* ---- Hooks -----*/
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        /*---Tool Bar---*/
        setSupportActionBar(toolbar); // This should now work correctly


        /*---Navigation----*/
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.bookingCar) {
            // Handle booking car action
            startActivity(new Intent(UserActivity.this, MainActivity.class));
    }else if (id == R.id.YourCart) {
            // Handle cart action
            startActivity(new Intent(UserActivity.this, CartMainActivity.class));
        }else if (id == R.id.usersetting) {
            // Handle setting action
            startActivity(new Intent(UserActivity.this, UpdateUserActivity.class));
        }else if (id == R.id.userLogout) {
            startActivity(new Intent(UserActivity.this, MainActivity.class));


        }

        return true;
    }
}