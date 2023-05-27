package com.ron.keepie.server;

import android.util.Log;

import com.ron.keepie.objects.KeepieUser;
import com.ron.keepie.objects.Track;
import com.ron.keepie.server.server_callbacks.TrackCallback;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TrackServerCommunicator {
    private static TrackServerCommunicator serverCommunicator = new TrackServerCommunicator();
    private final int STATUS_OK = 200;
    private final int FOUND = 302;
    private final int NOT_FOUND = 404;
    private TrackApiHandler myApiServer = RetrofitService.getInstance().getRetrofit().create(TrackApiHandler.class);
    private TrackCallback trackCallback;

    public TrackServerCommunicator setTrackCallback(TrackCallback trackCallback) {
        this.trackCallback = trackCallback;
        return this;
    }

    private Callback<List<Track>> getTrackCallback = new Callback<List<Track>>() {
        @Override
        public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
            Log.d("myLog", response.code() + "");
            if(response.code() == STATUS_OK){
                if (response.body() == null){
                    trackCallback.failed(response.code(), "failed - Wrong details");
                    return;
                }
                Log.d("myLog", response.body().toString());
                trackCallback.get_tracks(response.body());
            }
            else {
                trackCallback.failed(response.code(), "failed - Wrong details");
            }
        }
        @Override
        public void onFailure(Call<List<Track>> call, Throwable t) {
            Log.d("myLog", t.getMessage());
            trackCallback.failed(-1, t.getMessage());
        }
    };


    private Callback<Void> create_track_callback = new Callback<Void>() {
        @Override
        public void onResponse(Call<Void> call, Response<Void> response) {
            if(response.code() == STATUS_OK){
                trackCallback.track_created();
            }
            else if(response.code() == FOUND){
                trackCallback.failed(FOUND, "Track already exists");
            }
            else {
                trackCallback.failed(response.code(), "failed - Wrong details");
            }
        }
        @Override
        public void onFailure(Call<Void> call, Throwable t) {
            Log.d("myLog", t.getMessage());
            trackCallback.failed(-1, t.getMessage());
        }
    };

    private Callback<Void> update_track_callback = new Callback<Void>() {
        @Override
        public void onResponse(Call<Void> call, Response<Void> response) {
            if(response.code() == STATUS_OK){
                trackCallback.track_updated();
            }
            else {
                trackCallback.failed(response.code(), "failed - Wrong details");
            }
        }
        @Override
        public void onFailure(Call<Void> call, Throwable t) {
            Log.d("myLog", t.getMessage());
            trackCallback.failed(-1, t.getMessage());
        }
    };


    private Callback<Void> deleteTrackCallback = new Callback<Void>() {
        @Override
        public void onResponse(Call<Void> call, Response<Void> response) {
            Log.d("myLog", response.code() + "");
            if(response.code() == STATUS_OK){
                Log.d("myLog", "delete track ok");
                trackCallback.track_deleted();
            }
            else {
                trackCallback.failed(response.code(), "failed on delete");
            }
        }
        @Override
        public void onFailure(Call<Void> call, Throwable t) {
            Log.d("myLog", t.getMessage());
            trackCallback.failed(-1, t.getMessage());
        }
    };


    private TrackServerCommunicator() {

    }


    public void getTracks(String id, boolean is_child) {
        myApiServer.getTracks(id , is_child)
                .enqueue(getTrackCallback);
    }


    public void createTrack(Track track) {
        myApiServer.createTrack(track)
                .enqueue(create_track_callback);
    }



    public void updateTrack( Track track){
        myApiServer.updateTrack(track)
                .enqueue(update_track_callback);
    }

    public void deleteTrack( String child_phone,String adult_phone){
        myApiServer.deleteTrack(child_phone, adult_phone)
                .enqueue(deleteTrackCallback);
    }


    public static TrackServerCommunicator getInstance(){
        return serverCommunicator;
    }


}
