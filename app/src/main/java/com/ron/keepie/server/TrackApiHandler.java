package com.ron.keepie.server;

import com.ron.keepie.objects.KeepieUser;
import com.ron.keepie.objects.Track;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface TrackApiHandler {

    @GET("tracking/{id}/{is_child}")
    Call<List<Track>> getTracks(@Path("id") String trackId, @Path("is_child") boolean is_child );

    @POST("/tracking")
    Call<Void> createTrack(@Body Track track);

    @PUT("/tracking")
    Call<Void> updateTrack(@Body Track track);

    @DELETE("/tracking/{child_phone}/{adult_phone}")
    Call<Void> deleteTrack(@Path("child_phone") String child_phone, @Path("adult_phone") String adult_phone);


}
