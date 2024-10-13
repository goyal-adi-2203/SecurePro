package com.example.securepro.presentation.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.securepro.R;
import com.example.securepro.domain.model.User;
import com.example.securepro.presentation.login.LoginActivity;
import com.example.securepro.presentation.login.UserViewModel;
import com.example.securepro.services.ApiService.UserService.UserService;
import com.example.securepro.utils.BaseActivity;
import com.example.securepro.utils.GlideClientUtil;
import com.example.securepro.utils.RetrofitClient;
import com.google.android.material.imageview.ShapeableImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends BaseActivity {

    private String TAG = "editing";

    private UserViewModel userViewModel;
    private EditText etName, etEmail, etAddress, etMobileNo, etGender, etAge;
    private TextView tvUserName,  tvAccessLevel;
    private ShapeableImageView profileImageView;
    private Button btnSave, btnCancel;
    private Context context;
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);
        getLayoutInflater().inflate(R.layout.activity_edit_profile, findViewById(R.id.content_frame));
        setupNavigation();

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userService = RetrofitClient.createUserService();

        context = getApplicationContext();

        // Initialize views
        profileImageView = findViewById(R.id.profileImageViewEdit);
        etName = findViewById(R.id.etName);
        tvUserName = findViewById(R.id.tvUsernameEdit);
        tvAccessLevel = findViewById(R.id.tvAccessLevel);
        etEmail = findViewById(R.id.etEmail);
        etMobileNo = findViewById(R.id.etMobileNo);
        etAddress = findViewById(R.id.etAddress);
        etGender = findViewById(R.id.etGender);
        etAge = findViewById(R.id.etAge);

        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        userViewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user != null){
                    etName.setText(user.getName());
                    tvUserName.setText(user.getUsername());
                    tvAccessLevel.setText(user.getAccessLevel());
                    etEmail.setText(user.getEmail());
                    etAddress.setText(user.getAddress());
                    etMobileNo.setText(user.getMobileNo());
                    etGender.setText(user.getGender());
                    etAge.setText(user.getAge() != null ? user.getAge().toString() : "N/A");

                    GlideClientUtil.getGlide(context, user.getProfilePicturePath(), profileImageView);
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = userViewModel.getUser().getValue();
                if(user != null){
//                    Log.d(TAG, "onClick: " + user.toString());

                    if(checkData()){
                        user.setName(etName.getText().toString());
                        user.setEmail(etEmail.getText().toString());
                        user.setMobileNo(etMobileNo.getText().toString());
                        user.setAddress(etAddress.getText().toString());
                        user.setGender(etGender.getText().toString());
                        user.setAge(Integer.parseInt(etAge.getText().toString()));

                        updateApi(user);
                    }

                } else {
                    Log.e(TAG, "onClick: Error");
                }
            }
        });
    }

    public boolean checkData(){
        if(etName.getText().toString().isEmpty()){
            Toast.makeText(context, "Name can not be empty!", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private void updateApi(User user){
        Log.d(TAG, "updateApi: " + user.toString());

        Call<ResponseBody> call = userService.updateUser(user.getUsername(), user);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                Log.d(TAG, "onResponse: " + call.request().toString());
                if(response.isSuccessful()){
                    try {
                        String responseMessage = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseMessage);
                        String message = jsonObject.has("message") ? jsonObject.getString("message") : "";
                        Toast.makeText(EditProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                        userViewModel.update(user);
                        Log.d(TAG, "onClick: user updated " + user.toString());

                        Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        finish();
                    } catch (IOException | JSONException e) {
                        Log.e(TAG, "onResponse: error response body is null");
                        e.printStackTrace();
                    }


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
}