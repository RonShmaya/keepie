package com.ron.keepie.server;

import android.util.Log;


import com.ron.keepie.objects.KeepieUser;
import com.ron.keepie.server.server_callbacks.GetUserCallback;
import com.ron.keepie.server.server_callbacks.SetUserCallback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserServerCommunicator {
    private static UserServerCommunicator serverCommunicator = new UserServerCommunicator();
    private final int STATUS_OK = 200;
    private final int FOUND = 302;
    private final int NOT_FOUND = 404;
    private UserApiHandler myApiServer = RetrofitService.getInstance().getRetrofit().create(UserApiHandler.class);
    private GetUserCallback getUserCallback;
    private SetUserCallback setUserCallback;

    public UserServerCommunicator setGetUserCallback(GetUserCallback getUserCallback) {
        this.getUserCallback = getUserCallback;
        return this;
    }

    public UserServerCommunicator setSetUserCallback(SetUserCallback setUserCallback) {
        this.setUserCallback = setUserCallback;
        return this;
    }

    private Callback<KeepieUser> getUserDetails_callback = new Callback<KeepieUser>() {
        @Override
        public void onResponse(Call<KeepieUser> call, Response<KeepieUser> response) {
            Log.d("myLog", response.code() + "");
            if(response.code() == STATUS_OK){
                if (response.body() == null){
                    getUserCallback.failed(response.code(), "failed - Wrong details");
                    return;
                }
                Log.d("myLog", response.body().toString());
                getUserCallback.get_user(response.body());
            }
            else if(response.code() == NOT_FOUND){
                getUserCallback.not_found_user();
            }
            else {
                getUserCallback.failed(response.code(), "failed - Wrong details");
            }
        }
        @Override
        public void onFailure(Call<KeepieUser> call, Throwable t) {
            Log.d("myLog", t.getMessage());
            getUserCallback.failed(-1, t.getMessage());
        }
    };


    private Callback<Void> create_user_callback = new Callback<Void>() {
        @Override
        public void onResponse(Call<Void> call, Response<Void> response) {
            if(response.code() == STATUS_OK){
                setUserCallback.user_merged();
            }
            else if(response.code() == FOUND){
                setUserCallback.failed(FOUND, "User already exists");
            }
            else {
                setUserCallback.failed(response.code(), "failed - Wrong details");
            }
        }
        @Override
        public void onFailure(Call<Void> call, Throwable t) {
            Log.d("myLog", t.getMessage());
            setUserCallback.failed(-1, t.getMessage());
        }
    };
    private Callback<Void> update_user_callback = new Callback<Void>() {
        @Override
        public void onResponse(Call<Void> call, Response<Void> response) {
            if(response.code() == STATUS_OK){
                setUserCallback.user_merged();
            }
            else {
                setUserCallback.failed(response.code(), "failed - Wrong details");
            }
        }
        @Override
        public void onFailure(Call<Void> call, Throwable t) {
            Log.d("myLog", t.getMessage());
            getUserCallback.failed(-1, t.getMessage());
        }
    };



    private UserServerCommunicator() {

    }


    public void getUserDetails(String userId) {
        myApiServer.getUser(userId )
                .enqueue(getUserDetails_callback);
    }


    public void createUser(KeepieUser user) {
        myApiServer.createUser(user)
                .enqueue(create_user_callback);
    }



    public void updateUser( KeepieUser user){
        myApiServer.updateUserDetails(user)
                .enqueue(update_user_callback);
    }


    public static UserServerCommunicator getInstance(){
        return serverCommunicator;
    }


}
