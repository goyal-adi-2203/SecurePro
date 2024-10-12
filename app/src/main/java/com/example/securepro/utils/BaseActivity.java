package com.example.securepro.utils;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.securepro.R;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

public class BaseActivity extends AppCompatActivity {

    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    protected Toolbar toolbar;
    String TAG = "SecureProBase";
    private static final int REQUEST_NOTIFICATION_PERMISSION = 1;

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


    protected void initFirebase(){
        // Initialize Firebase
        FirebaseMessaging.getInstance().subscribeToTopic("esp32_notifications")
                .addOnCompleteListener(task -> {
                    String msg = task.isSuccessful() ? "Subscribed" : "Subscription failed";
                    Log.d(TAG, msg);
                });

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        String token = task.getResult();
                        Log.d(TAG, "FCM Registration Token: " + token);
                        // You can send this token to your ESP32 or backend for notifications
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    protected void checkNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    REQUEST_NOTIFICATION_PERMISSION
            );
        }
    }

    // Handle the result of the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, notifications can be shown
                Log.d(TAG, "permission granted");
            } else {
                // Permission denied, you can show a message or disable notification-related features
                grantResults[0] = PackageManager.PERMISSION_GRANTED;
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    public void getBiometric(){
        Log.d(TAG, "getBiometric: fetched");
    }
}
