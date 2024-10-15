package com.example.securepro.services.ApiService.DeviceService;

import com.example.securepro.services.ApiService.ApiService;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface DeviceService extends ApiService {
    @GET(DEVICE_ROUTE + "/fetch/{userId}")
    Call<ResponseBody> getAllDevices(@Path("userId") String userId);

    @POST(DEVICE_ROUTE + "/check-password/{userId}/{deviceId}")
    Call<ResponseBody> checkPassword(@Path("userId") String userid, @Path("deviceId") String deviceId, @Body Map<String, Object> requestBody);
}
