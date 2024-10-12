package com.example.securepro.services.ApiService.AuthService;

import com.example.securepro.services.ApiService.ApiService;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService extends ApiService {
    @POST(AUTH_ROUTE + "/login")
    Call<ResponseBody> login(@Body Map<String, Object> request);

    @POST(AUTH_ROUTE + "/signup")
    Call<ResponseBody> signup(@Body Map<String, Object> request);
}
