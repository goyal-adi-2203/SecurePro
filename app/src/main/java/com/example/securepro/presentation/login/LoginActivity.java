package com.example.securepro.presentation.login;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.securepro.R;
import com.example.securepro.controllers.DeviceController.DeviceController;
import com.example.securepro.domain.model.Device;
import com.example.securepro.domain.model.User;
import com.example.securepro.presentation.home.DeviceViewModel;
import com.example.securepro.services.ApiService.AuthService.AuthService;
import com.example.securepro.services.ApiService.DeviceService.DeviceService;
import com.example.securepro.utils.FirebaseMessagingInit;
import com.example.securepro.utils.RetrofitClient;
import com.google.firebase.FirebaseApp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText usernameEditText, passwordEditText;
    Button loginBtn;
    String TAG = "loginTag";

    private String userId = "1";
    private Context context;
    private Application application;
    private UserViewModel userViewModel;
    private DeviceViewModel deviceViewModel;
    private DeviceController deviceController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = getApplicationContext();
        application = getApplication();

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        deviceViewModel = new ViewModelProvider(this).get(DeviceViewModel.class);
        deviceController = new DeviceController();
//        createUserIfNotExists();

        usernameEditText = findViewById(R.id.usernameInput);
        passwordEditText = findViewById(R.id.passwordInput);
        loginBtn = findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                login(username, password);

            }
        });
    }

    public void login(String username, String password) {

        Log.d(TAG, "login: " + username + " " + password);
        AuthService authService = RetrofitClient.createAuthService();
        Map<String, Object> request = new HashMap() {{
            put("username", username);
            put("password", password);
        }};

        Call<ResponseBody> call = authService.login(request);
        call.enqueue(new Callback<ResponseBody>() {
                         @Override
                         public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                             if (response.isSuccessful()) {
                                 if (response.code() == 200) {
                                     Log.d(TAG, "onResponse: " + response.code());
                                     Toast.makeText(LoginActivity.this, "Welcome " + username, Toast.LENGTH_SHORT).show();

                                     try {
                                         String responseBody = response.body().string();

                                         createUser(responseBody, username, password);
                                         deviceController.fetchDevices(username, context, deviceViewModel);
                                         fetchAndSaveFcmToken(username);
                                     } catch (IOException | JSONException e) {
                                         Log.e(TAG, "onResponse: Error");
                                         e.printStackTrace();
                                     }

                                     finish();
                                 } else {
                                     Log.d(TAG, "onResponse: " + response.code());
                                 }
                             } else {
                                 Log.e(TAG, "onResponse: Error " + response.code());
                                 handleLoginFailure(response);
                             }
                         }

                         @Override
                         public void onFailure(Call<ResponseBody> call, Throwable t) {
                             Log.e(TAG, "onFailure: " + call.request().toString());
                             Log.e("RetrofitError", t.getMessage(), t);
                             Toast.makeText(LoginActivity.this, "Server Error! Please Try Again", Toast.LENGTH_SHORT).show();
                         }
                     }
        );
    }

    private void createUser(String responseBody, String username, String password) throws JSONException {
        JSONObject jsonObject = new JSONObject(responseBody);
        Log.d(TAG, "createUser: response Body " + responseBody);

        if (jsonObject.has("data")) {
            JSONObject data = (JSONObject) jsonObject.get("data");
            String accessLevel = data.has("accessLevel") ? data.getString("accessLevel") : "";
            String address = data.has("address") ? data.getString("address") : "";
            String gender = data.has("gender") ? data.getString("gender") : "";
            String name = data.has("name") ? data.getString("name") : "";
            String mobileNo = data.has("mobileNo") ? data.getString("mobileNo") : "";
            Integer age = Integer.parseInt(data.has("age") ? data.getString("age") : null);
            String email = data.has("email") ? data.getString("email") : "";
            String profilePicturePath = data.has("profilePicturePath") ? data.getString("profilePicturePath") : "";

            User user = new User(
                    username,
                    username,
                    password,
                    name,
                    email,
                    profilePicturePath,
                    accessLevel,
                    address,
                    gender,
                    mobileNo,
                    age
            );
            userViewModel.insert(user);
            Log.d(TAG, "createUser: user inserted" + user.toString());
        } else {
            throw new JSONException("No data");
        }
    }

    private void fetchAndSaveFcmToken(String username) throws IOException {
//        if(FirebaseApp.getApps(this).isEmpty()){
            FirebaseMessagingInit.initFirebase(username, context);
//        }
    }

    private void handleLoginFailure(Response<ResponseBody> response) {
        try {
            String errorMessage = response.errorBody().string();
            JSONObject jsonObject = new JSONObject(errorMessage);
            String message = jsonObject.optString("message", "Login failed");
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            Log.e(TAG, "onResponse: Error " + errorMessage);
        } catch (IOException | JSONException e) {
            Log.e(TAG, "onResponse: Error parsing error body", e);
        }
    }

}