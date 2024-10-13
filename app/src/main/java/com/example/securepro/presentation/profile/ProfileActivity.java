package com.example.securepro.presentation.profile;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.securepro.R;
import com.example.securepro.utils.BaseActivity;

public class ProfileActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);
        getLayoutInflater().inflate(R.layout.activity_profile, findViewById(R.id.content_frame));
        setupNavigation();


    }
}