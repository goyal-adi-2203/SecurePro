package com.example.securepro.controllers.DeviceController;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.example.securepro.callbacks.PasswordValidationCallback;
import com.example.securepro.callbacks.SaveDeviceCallback;
import com.example.securepro.domain.model.Device;
import com.example.securepro.domain.model.User;
import com.example.securepro.mappers.DeviceMapper;
import com.example.securepro.presentation.home.DeviceViewModel;
import com.example.securepro.presentation.login.LoginActivity;
import com.example.securepro.services.ApiService.DeviceService.DeviceService;
import com.example.securepro.utils.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeviceController {
    private static String TAG = "DeviceController";
    private List<Device> deviceList;

    public List<Device> fetchDevices(String userId, Context context, DeviceViewModel deviceViewModel) {
        Log.d(TAG, "fetchDevices for : " + userId);
        DeviceService deviceService = RetrofitClient.createDeviceService();

        Call<ResponseBody> call = deviceService.getAllDevices(userId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        Log.d(TAG, "onResponse: " + response.code());
                        try {
                            String responseBody = response.body().string();
                            Log.d(TAG, "onResponse: devices" + responseBody);
                            deviceList = DeviceMapper.deserializer(responseBody);
                            createDevices(deviceList, deviceViewModel);
                        } catch (IOException | JSONException e) {
                            Log.e(TAG, "onResponse: Error");
                            e.printStackTrace();
                        }
                    } else {
                        Log.d(TAG, "onResponse: " + response.code());
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

        return deviceList;
    }

    private void createDevices(List<Device> deviceList, DeviceViewModel deviceViewModel){
        for (int i = 0; i < deviceList.size(); i++) {
            deviceViewModel.insert(deviceList.get(i));
            Log.d(TAG, "createDevices: device inserted with id " + deviceList.get(i).getId());
        }
    }

    public void validatePassword(String password, String deviceId, String userId, Context context, final PasswordValidationCallback callback){

        Log.d(TAG, "validatePassword: " + password);
        Map<String, Object> requestBody = new HashMap(){{
            put("password", password);
        }};

        DeviceService deviceService = RetrofitClient.createDeviceService();

        Call<ResponseBody> call = deviceService.checkPassword(userId, deviceId, requestBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Map<String, Object> responseData = new HashMap();
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        Log.d(TAG, "onResponse: " + response.code());
                        try {
                            String responseBody = response.body().string();
                            Log.d(TAG, "onResponse: " + responseBody);

                            JSONObject jsonObject = new JSONObject(responseBody);
                            boolean check = jsonObject.has("data") && jsonObject.getBoolean("data");
                            String message = jsonObject.has("message") ? jsonObject.getString("message") : "";

                            responseData.put("check", check);
                            responseData.put("message", message);

                            callback.onSuccess(responseData);

                        } catch (IOException | JSONException e) {
                            Log.e(TAG, "onResponse: Error");
                            e.printStackTrace();
                        }
                    } else {
                        Log.d(TAG, "onResponse: " + response.code());
                    }
                } else {
                    Log.e(TAG, "onResponse: Error " + response.code());
                    try {
                        String errorMessage = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorMessage);
                        String message = jsonObject.has("message") ? jsonObject.getString("message") : "";
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                        responseData.put("check", false);
                        responseData.put("message", message);

                        callback.onFailure(responseData);

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

                callback.onFailure(null);
            }
        });
    }

    public void saveDevice(Context context, String userId, Device device, final SaveDeviceCallback callback){
        Log.d(TAG, "saveDevice: " + userId);
        Log.d(TAG, "saveDevice: " + device.toString());

        DeviceService deviceService = RetrofitClient.createDeviceService();

        Call<ResponseBody> call = deviceService.saveDevice(userId, device);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        Log.d(TAG, "Device saved: " + responseBody);

                        JSONObject jsonObject = new JSONObject(responseBody);
                        String message = jsonObject.has("message") ? jsonObject.getString("message") : "no msg";

                        Map<String, Object> responseData = new HashMap<>();
                        responseData.put("message", message);
                        responseData.put("code", response.code());
                        callback.onSuccess(responseData);
                    } catch (IOException | JSONException e) {
                        Log.e(TAG, "onResponse success: Error" + response.code());
                            e.printStackTrace();
                    }
                } else {
                    try {
                        Log.e(TAG, "Server error: " + response.body().string());
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        String message = jsonObject.has("message") ? jsonObject.getString("message") : "";
                        Map<String, Object> errorData = new HashMap<>();
                        errorData.put("message", message);
                        callback.onFailure(errorData);
                    } catch (IOException | JSONException e) {
                        Log.e(TAG, "onResponse: Error" + response.code());
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "onFailure: " + call.request().toString());
                Log.e("RetrofitError", t.getMessage(), t);
                Toast.makeText(context, "Server Error! Please Try Again", Toast.LENGTH_SHORT).show();

                callback.onFailure(null);
            }
        });
    }
}
