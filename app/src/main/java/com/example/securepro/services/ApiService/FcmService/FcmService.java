package com.example.securepro.services.ApiService.FcmService;

import com.example.securepro.services.ApiService.ApiService;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface FcmService extends ApiService {
    @POST(FCM_ROUTE + "/save-token")
    Call<ResponseBody> saveFcmToken(@Body Map<String, Object> request);
}
