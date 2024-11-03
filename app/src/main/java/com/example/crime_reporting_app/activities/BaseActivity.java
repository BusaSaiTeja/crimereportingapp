package com.example.crime_reporting_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import com.example.crime_reporting_app.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
public class BaseActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    private final static int NAV_HOME = R.id.nav_home;
    private final static int NAV_PROFILE = R.id.nav_profile;
    private final static int NAV_LOGOUT = R.id.nav_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            // User is not logged in, redirect to LoginActivity
            Intent loginIntent = new Intent(this, LoginActivity.class);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginIntent);
            finish(); // Close this activity
            return; // Exit the onCreate method
        }

        setContentView(R.layout.activity_base);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);


        // Initialize DrawerLayout and NavigationView
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // Set up ActionBarDrawerToggle
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();



        // Handle navigation item selection
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.nav_home) {
                    Intent homeIntent = new Intent(BaseActivity.this, MainActivity.class); // Replace with your HomeActivity class
                    startActivity(homeIntent);
                }
                else if (item.getItemId() == R.id.nav_upload_crime) {
                    Intent uploadCrimeIntent = new Intent(BaseActivity.this, UploadCrimeActivity.class);
                    startActivity(uploadCrimeIntent);
                } else if (item.getItemId() == R.id.nav_crime_visualization) {
                    Intent crimeVisualizationIntent = new Intent(BaseActivity.this, CrimeVisualizationActivity.class);
                    startActivity(crimeVisualizationIntent);
                } else if (item.getItemId() == R.id.nav_profile) {
                    // Navigate to ProfileActivity
                    Intent profileIntent = new Intent(BaseActivity.this, ProfileActivity.class);
                    startActivity(profileIntent);
                } else if (item.getItemId() == R.id.nav_logout) {
                    FirebaseAuth.getInstance().signOut();
                    Intent logoutIntent = new Intent(BaseActivity.this, LoginActivity.class);
                    logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(logoutIntent);
                    finish();
                }
                drawerLayout.closeDrawers(); // Close drawer after selection
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }
}
