package com.example.securepro.presentation.profile;

import android.content.Context;
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.securepro.R;
import com.example.securepro.domain.model.User;
import com.example.securepro.presentation.login.UserViewModel;
import com.example.securepro.utils.BaseActivity;
import com.example.securepro.utils.GlideClientUtil;
import com.google.android.material.imageview.ShapeableImageView;

public class ProfileActivity extends BaseActivity {

    private UserViewModel userViewModel;
    private EditText etName;
    private TextView tvName, tvUserName, tvAccessLevel, tvEmail, tvAddress, tvMobileNo, tvGender, tvAge;
    private ShapeableImageView profileImageView;
    private String TAG = "viewing";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);
        getLayoutInflater().inflate(R.layout.activity_profile, findViewById(R.id.content_frame));
        setupNavigation();

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        Context context = getApplicationContext();

        // Initialize views
        profileImageView = findViewById(R.id.profileImageView);

        tvName = findViewById(R.id.tvName);
        tvUserName = findViewById(R.id.tvUserName);
        tvAccessLevel = findViewById(R.id.tvAccessLevel);
        tvEmail = findViewById(R.id.tvEmail);
        tvAddress = findViewById(R.id.tvAddress);
        tvMobileNo = findViewById(R.id.tvMobileNo);
        tvGender = findViewById(R.id.tvGender);
        tvAge = findViewById(R.id.tvAge);

        userViewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user != null){
                    tvName.setText(user.getName());
                    tvUserName.setText(user.getUsername());
                    tvAccessLevel.setText(user.getAccessLevel());
                    tvEmail.setText(user.getEmail());
                    tvAddress.setText(user.getAddress());
                    tvMobileNo.setText(user.getMobileNo());
                    tvGender.setText(user.getGender());
                    tvAge.setText(user.getAge() != null ? user.getAge().toString() : "N/A");

                    GlideClientUtil.getGlide(context, user.getProfilePicturePath(), profileImageView);

                }
            }
        });


    }
}