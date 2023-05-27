package com.ron.keepie.server;

import android.util.Log;


import com.ron.keepie.objects.KeepieUser;
import com.ron.keepie.objects.UsersPhones;
import com.ron.keepie.server.server_callbacks.GetUserCallback;
import com.ron.keepie.server.server_callbacks.GetUserslist;
import com.ron.keepie.server.server_callbacks.SetUserCallback;

import java.util.Arrays;
import java.util.List;

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
    private GetUserslist getUsersListCallback;

    public UserServerCommunicator setGetUserCallback(GetUserCallback getUserCallback) {
        this.getUserCallback = getUserCallback;
        return this;
    }

    public UserServerCommunicator setSetUserCallback(SetUserCallback setUserCallback) {
        this.setUserCallback = setUserCallback;
        return this;
    }

    public UserServerCommunicator setGetUsersListCallback(GetUserslist getUsersListCallback) {
        this.getUsersListCallback = getUsersListCallback;
        return this;
    }

    private Callback<KeepieUser> getUserDetails_callback = new Callback<KeepieUser>() {
        @Override
        public void onResponse(Call<KeepieUser> call, Response<KeepieUser> response) {
            Log.d("myLog", response.code() + "");
            if(response.code() == STATUS_OK){
                if (response.body() == null){
                    Log.d("MyLog", "Get user null");
                    getUserCallback.failed(response.code(), "failed - Wrong details");
                    return;
                }
                Log.d("myLog", "Get user OK"+response.body().toString());
                getUserCallback.get_user(response.body());
            }
            else if(response.code() == NOT_FOUND){
                Log.d("myLog", "Get user not found");
                getUserCallback.not_found_user();
            }
            else {
                Log.d("myLog", "Get user failed"+response.code());
                getUserCallback.failed(response.code(), "failed - Wrong details");
            }
        }
        @Override
        public void onFailure(Call<KeepieUser> call, Throwable t) {
            Log.d("myLog", "Get user failed -1 "+t.getMessage());
            getUserCallback.failed(-1, t.getMessage());
        }
    };

    private Callback<List<KeepieUser>> getUsersList_callback = new Callback<List<KeepieUser>>() {
        @Override
        public void onResponse(Call<List<KeepieUser>> call, Response<List<KeepieUser>> response) {
            Log.d("myLog", response.code() + "");
            if(response.code() == STATUS_OK){
                if (response.body() == null){
                    Log.d("MyLog", "Get user list null");
                    getUsersListCallback.failed(response.code(), "failed - Wrong details");
                    return;
                }
                Log.d("myLog", "Get user OK "+response.body().toString());
                getUsersListCallback.get_users_list(response.body());
            }
            else {
                Log.d("myLog", "Get user failed ");
                getUsersListCallback.failed(response.code(), "failed - Wrong details");
            }
        }
        @Override
        public void onFailure(Call<List<KeepieUser>> call, Throwable t) {
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
            else if(response.code() == NOT_FOUND){
                setUserCallback.failed(response.code(), "Not found user");
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
    public void getUsersList(UsersPhones usersPhones) {
        myApiServer.getUsersList(usersPhones )
                .enqueue(getUsersList_callback);
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
