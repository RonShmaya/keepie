package com.ron.keepie.server;

import com.ron.keepie.objects.KeepieUser;
import com.ron.keepie.objects.UsersPhones;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserApiHandler {

    @GET("user/{id}")
    Call<KeepieUser> getUser(@Path("id") String userId );

    @PUT("/user/list")
    Call<List<KeepieUser>> getUsersList(@Body UsersPhones phonesList );

    @POST("/user")
    Call<Void> createUser(@Body KeepieUser newUser);

    @PUT("/user")
    Call<Void> updateUserDetails(@Body KeepieUser user);

    @GET("/admin/data")
    Call<Void> admin_data();

    @GET("/admin/note")
    Call<Void> admin_alert();


}
