package com.example.crudapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.DELETE;
import retrofit2.http.Path;

public interface ApiService {
    @POST("Postregister.php")
    Call<RegisterResponse> registerUser(@Body User user);

    @POST("Postlogin.php")
    Call<LoginResponse> loginUser(@Body User user);

    @GET("http://192.168.1.33/CRUD/CRUD/Getregister.php")
    Call<List<User>> getUsers();

    @POST("DeleteUser.php")
    Call<ApiResponse> deleteUser(@Body UserDeleteRequest request);

}
