package com.example.securepro.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.securepro.MainActivity;
import com.example.securepro.presentation.login.LoginActivity;
import com.example.securepro.services.ApiService.FcmService.FcmService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirebaseMessagingInit {
    private static String TAG = "FirebaseMessagingInitClass";
    private static FcmService fcmService;


    public static void initFirebase(String username, Context context) throws IOException {
        fcmService = RetrofitClient.createFcmService();

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
                        Map<String, Object> request = new HashMap(){{
                            put("userId", username);
                            put("token", token);
                        }};

                        Call<ResponseBody> call = fcmService.saveFcmToken(request);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if(response.isSuccessful()){
                                    if(response.code() == 200){
                                        try {
                                            Log.d(TAG, "onResponse: " + response.body().string());
                                        } catch (IOException e) {
                                            Log.e(TAG, "IoException");
                                            e.printStackTrace();
                                        }
                                    } else {
                                        try {
                                            Log.e(TAG, "onResponse: " + response.body().string());
                                        } catch (IOException e) {
                                            Log.e(TAG, "IoException");
                                            e.printStackTrace();
                                        }
                                    }
                                } else {
                                    Log.e(TAG, "onResponse: " + call.request().toString());
                                    try {
                                        Log.e(TAG, "onResponse: " + response.errorBody().string());
                                    } catch (IOException e) {
                                        Log.e(TAG, "IoException");
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
                });
    }
}
