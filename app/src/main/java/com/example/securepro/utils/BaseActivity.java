package com.example.securepro.utils;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.securepro.MainActivity;
import com.example.securepro.R;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.securepro.domain.model.User;
import com.example.securepro.presentation.home.DeviceListActivity;
import com.example.securepro.presentation.home.DeviceViewModel;
import com.example.securepro.presentation.login.LoginActivity;
import com.example.securepro.presentation.login.UserViewModel;
import com.example.securepro.presentation.profile.EditProfileActivity;
import com.example.securepro.presentation.profile.ProfileActivity;
import com.example.securepro.services.ApiService.AuthService.AuthService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseActivity extends AppCompatActivity {

    private static final int REQUEST_NOTIFICATION_PERMISSION = 1;
    private String TAG = "SecureProBase";

    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    protected Toolbar toolbar;

    protected TextView nameTextView, usernameTextView;
    protected ShapeableImageView profilePicShapeableImageView;

//    private static boolean isAuthenticated = false;
//    private static boolean isAppInBackground = false;
//    private static String className = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupOnBackPressedCallback();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Log.d(TAG, "onResume: resume " + getLocalClassName());
//
//        if(!isAuthenticated){
//            checkUserLogin();
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//        Log.d(TAG, "onStart: start " + getLocalClassName());
////        Log.d(TAG, "onStart: isAuthenticated " + isAuthenticated + ", isBg : " + isAppInBackground);
//        if(className != null && !className.equals(getLocalClassName())){
//            className = null;
//        }
    }

    @Override
    protected void onStop() {
        super.onStop();
//        Log.d(TAG, "onStop: stop " + getLocalClassName());
////        Log.d(TAG, "onStart: isAuthenticated " + isAuthenticated + ", isBg : " + isAppInBackground);
//        if(className != null && className.equals(getLocalClassName())){
//            isAuthenticated = false;
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        Log.d(TAG, "onPause: pause " + getLocalClassName());
//        className = getLocalClassName();
    }

    protected void setupNavigation() {
        Context context = getApplicationContext();
        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        DeviceViewModel deviceViewModel = new ViewModelProvider(this).get(DeviceViewModel.class);

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

        View headerView = navigationView.getHeaderView(0);

        nameTextView = headerView.findViewById(R.id.name_nav_header);
        usernameTextView = headerView.findViewById(R.id.username_nav_header);
        profilePicShapeableImageView = headerView.findViewById(R.id.profile_picture_nav_header);

        userViewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user != null){
                    nameTextView.setText(user.getName());
                    usernameTextView.setText(user.getUsername());
                    String profilePictureUrl = user.getProfilePicturePath();
                    GlideClientUtil.getGlide(context, profilePictureUrl, profilePicShapeableImageView);
                } else {
                    Log.e(TAG, "onChanged: user not found from here");
                }
            }
        });



        // Set Navigation item click listener
        navigationView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
//            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();

            if (itemId == R.id.profile_menu_item){
                Log.d(TAG, "setupNavigation: go to profile activity");
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
//                finish();
            } else if (itemId == R.id.edit_profile_menu_item) {
                Log.d(TAG, "setupNavigation: go to edit profile activity");
                Intent intent = new Intent(this, EditProfileActivity.class);
                startActivity(intent);
//                finish();
            } else if (itemId == R.id.device_list_menu_item) {
                Log.d(TAG, "setupNavigation: go to device list");
                Intent intent = new Intent(this, DeviceListActivity.class);
                startActivity(intent);
//                finish();
            } else if (itemId == R.id.get_help_menu_item) {
                Log.d(TAG, "setupNavigation: go to get help activity");
            } else if(itemId == R.id.logout_menu_item){
                Log.d(TAG, "setupNavigation: logout user");

                userViewModel.getUser().observe(this, new Observer<User>() {
                    @Override
                    public void onChanged(User user) {
                        if (user != null) {
                            String username = user.getUsername();
                            Log.d(TAG, "onChanged: logout" + username);
                            logout(username, context, userViewModel, deviceViewModel);
                        } else {
                            Log.e(TAG, "onChanged: User not found");
                        }
                    }
                });
            } else {
                Log.e(TAG, "setupNavigation: unknown item id");
            }

            drawerLayout.closeDrawers();  // Close the drawer after item click
            return true;
        });
    }

    public void logout(String username, Context context, UserViewModel userViewModel, DeviceViewModel deviceViewModel){
        AuthService authService = RetrofitClient.createAuthService();
        Map<String, Object> requestBody = new HashMap(){{
            put("username", username);
        }};

        Call<ResponseBody> call = authService.logout(requestBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Toast.makeText(context, "Goodbye " + username + "!", Toast.LENGTH_SHORT).show();

                    userViewModel.delete();
                    deviceViewModel.deleteAllDevices();

                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e(TAG, "onResponse: Error " + response.code());
                    try {
                        String errorMessage = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorMessage);
                        String message = jsonObject.has("message") ? jsonObject.getString("message") : "";
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                        Log.e(TAG, "onResponse: Error " + response.errorBody().string());
                    } catch (IOException | JSONException e) {
                        Log.e(TAG, "onResponse: Error");
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "onFailure: " + call.request().toString());
                Log.e("RetrofitError", t.getMessage(), t);
                Toast.makeText(context, "Server Error! Please Try Again", Toast.LENGTH_SHORT).show();
            }
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

    public void checkUserLogin(){
        Context context = getApplicationContext();
        UserViewModel userViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(UserViewModel.class);
        userViewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user == null) {
                    Intent intent = new Intent(context, LoginActivity.class);
                    startActivity(intent);
                } else {
                    getBiometric(context);
                }
            }
        });

    }

    public void getBiometric(Context context) {

        Log.d(TAG, "getBiometric: fetched");

        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                // Device supports biometric authentication
                showBiometricPrompt(context);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                // No biometric hardware available
                Toast.makeText(this, "No biometric hardware found.", Toast.LENGTH_SHORT).show();
                showPasswordDialog(context);
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                // Biometric hardware is unavailable
                Toast.makeText(this, "Biometric hardware is currently unavailable.", Toast.LENGTH_SHORT).show();
                showPasswordDialog(context);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                // No biometric data enrolled, ask the user to set it up
                Toast.makeText(this, "No biometric enrolled. Please set up biometric authentication in settings or use password.", Toast.LENGTH_SHORT).show();
                showPasswordDialog(context);
                break;
        }


    }

    private void showBiometricPrompt(Context context) {
        Executor executor = ContextCompat.getMainExecutor(context);
        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                // Handle error
                if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                    showPasswordDialog(context);
                } else {
                    Toast.makeText(context, "Authentication Failed Try Again", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                // Authentication succeeded, proceed with your app logic
//                isAuthenticated = true;
                Intent intent = new Intent(context, DeviceListActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                // Handle failed authentication
                Toast.makeText(context, "Authentication Failed Try Again", Toast.LENGTH_SHORT).show();
            }
        });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Authentication")
                .setSubtitle("Use your fingerprint to unlock the app")
                .setNegativeButtonText("Use password")
                .setConfirmationRequired(false)
                .build();

        biometricPrompt.authenticate(promptInfo);
    }

    private void showPasswordDialog(Context context) {
//        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText passwordInput = new EditText(this);
        passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        passwordInput.setHint("Password");

        final EditText confirmPasswordInput = new EditText(this);
        confirmPasswordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        confirmPasswordInput.setHint("Confirm Password");

        final LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(passwordInput);

        if (!isPasswordSet()) {
            layout.addView(confirmPasswordInput);
        }

        builder.setView(layout)
                .setTitle(isPasswordSet() ? "Enter Password" : "Set New Password")
                .setPositiveButton(isPasswordSet() ? "OK" : "Set", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String enteredPassword = passwordInput.getText().toString();

                        if (TextUtils.isEmpty(enteredPassword)) {
                            Toast.makeText(context, "Password cannot be empty", Toast.LENGTH_SHORT).show();
                        } else if (isPasswordSet()) {
                            if (validatePassword(enteredPassword)) {
                                Intent intent = new Intent(context, DeviceListActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(context, "Incorrect password", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Set new password
                            String confirmPassword = confirmPasswordInput.getText().toString();
                            if (TextUtils.isEmpty(confirmPassword)) {
                                Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show();
                            } else {
                                savePassword(enteredPassword);
                                Toast.makeText(context, "Password set successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(context, DeviceListActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        Intent intent = new Intent(context, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private boolean isPasswordSet() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        String savedPassword = sharedPreferences.getString("AppPassword", null);
        return savedPassword != null;
    }

    private boolean validatePassword(String enteredPassword) {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        String savedPassword = sharedPreferences.getString("AppPassword", null);
        return savedPassword != null && savedPassword.equals(enteredPassword);
    }

    // Save the password to SharedPreferences
    private void savePassword(String password) {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("AppPassword", password);
        editor.apply();
    }
}
