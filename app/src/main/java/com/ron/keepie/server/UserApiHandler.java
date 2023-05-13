package com.ron.keepie.server;

import com.ron.keepie.objects.KeepieUser;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserApiHandler {

    @GET("user/{id}")
    Call<KeepieUser> getUser(@Path("id") String userId );

    @POST("/user")
    Call<Void> createUser(@Body KeepieUser newUser);

    @PUT("/user")
    Call<Void> updateUserDetails(@Body KeepieUser user);


}
