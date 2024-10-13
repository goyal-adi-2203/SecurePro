package com.example.securepro;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.example.securepro.presentation.login.UserViewModel;
import com.example.securepro.utils.BaseActivity;

public class MainActivity extends BaseActivity {

    private static final int REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS = 1;
    String TAG = "mainTag";
    UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkUserLogin();

        initFirebase();
        // Request notification permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkNotificationPermission();
        }

    }

    //    private void authenticateWithDeviceCredentials() {
//        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && keyguardManager.isKeyguardSecure()) {
//            Intent intent = keyguardManager.createConfirmDeviceCredentialIntent("Unlock App", "Use your screen lock to unlock the app");
//            if (intent != null) {
//                startActivityForResult(intent, REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS);
//            }
//        } else {
//            // Device does not have a secure lock screen
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS) {
            if (resultCode == RESULT_OK) {
                // Authentication was successful
            } else {
                // Authentication failed
            }
        }
    }
}