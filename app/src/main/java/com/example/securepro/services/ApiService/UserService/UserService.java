package com.example.securepro.services.ApiService.UserService;

import com.example.securepro.domain.model.User;
import com.example.securepro.services.ApiService.ApiService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface UserService extends ApiService {
    @PUT(USER_ROUTE + "/{userId}")
    Call<ResponseBody> updateUser(@Path("userId") String userId, @Body User user);
}
