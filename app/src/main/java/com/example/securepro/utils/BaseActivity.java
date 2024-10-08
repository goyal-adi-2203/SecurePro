package com.example.securepro.utils;

import android.os.Bundle;
import android.widget.Toast;

import com.example.securepro.R;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

public class BaseActivity extends AppCompatActivity {

    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    protected Toolbar toolbar;
//    private int CONTENT_FRAME = R.id.content_frame;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupOnBackPressedCallback();
    }

    protected void setupNavigation() {
        // Initialize Toolbar and set it as the app bar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize DrawerLayout and NavigationView
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // Setup Drawer Toggle to handle open/close logic
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Set Navigation item click listener
        navigationView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if(itemId == R.id.nav_item1){
                Toast.makeText(getApplicationContext(), "nav item 1", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "nav item 2", Toast.LENGTH_SHORT).show();
            }
            drawerLayout.closeDrawers();  // Close the drawer after item click
            return true;
        });
    }

    // New onBackPressed behavior using OnBackPressedDispatcher
    private void setupOnBackPressedCallback() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // If the drawer is open, close it when back button is pressed
                if (drawerLayout != null && drawerLayout.isDrawerOpen(navigationView)) {
                    drawerLayout.closeDrawer(navigationView);
                } else {
                    // Otherwise, use the default back behavior
                    if (isEnabled()) {
                        setEnabled(false);
                        getOnBackPressedDispatcher().onBackPressed();
                    }
                }
            }
        });
    }

//    protected void inflateActivity(int resource){
//        getLayoutInflater().inflate(resource, findViewById(CONTENT_FRAME));
//    }
}
