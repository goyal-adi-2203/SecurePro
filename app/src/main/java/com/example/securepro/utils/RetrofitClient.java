package com.example.securepro.utils;

import com.example.securepro.BuildConfig;
import com.example.securepro.services.ApiService.AuthService.AuthService;
import com.example.securepro.services.ApiService.UserService.UserService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
//    private static final String baseUrl = BuildConfig.BASE_URL3 + "api/";
    private static final String baseUrl = BuildConfig.BASE_URL2 + "api/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
        
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }

    public static AuthService createAuthService(){
        return getClient().create(AuthService.class);
    }

    public static UserService createUserService(){
        return getClient().create(UserService.class);
    }
}
