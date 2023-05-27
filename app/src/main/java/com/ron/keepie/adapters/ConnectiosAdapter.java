package com.ron.keepie.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;
import com.ron.keepie.R;
import com.ron.keepie.objects.KeepieUser;
import com.ron.keepie.objects.Track;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConnectiosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


//    public interface ClickedUser {
//        void clicked(KeepieUser contact, int position);
//        void delete(KeepieUser contact, int position);
//    }

    private Activity activity;
    //private ClickedUser userListener;

    private Map<KeepieUser, Track> user_tracks = new HashMap<>();
    private List<KeepieUser> myUsers = new ArrayList<>();

    public ConnectiosAdapter(Activity activity, ArrayList<KeepieUser> myUsers) {
        this.activity = activity;
        this.myUsers = myUsers;
    }

//    public void setChatListener(ClickedUser userListener) {
//        this.userListener = userListener;
//
//    }

    public ConnectiosAdapter setUser_tracks(Map<KeepieUser, Track> user_tracks) {
        this.user_tracks = user_tracks;
        return this;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_connectors, parent, false);
        UserHolder userHolder = new UserHolder(view);
        return userHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final UserHolder holder = (UserHolder) viewHolder;
        KeepieUser myUser = getItem(position);
        holder.list_connect_LBL_name.setText("Name: "+myUser.getName());
        holder.list_connect_LBL_phone.setText("Phone: "+myUser.getPhone());
        Glide.with(activity).load(myUser.getImage()).placeholder(R.drawable.ic_user).into(holder.list_connect_IMG);
        Track track = user_tracks.get(myUser);
        holder.profile_IMG_photo_ok.setVisibility(View.GONE);
        holder.profile_IMG_photo_denied.setVisibility(View.GONE);
        holder.profile_IMG_photo_wait.setVisibility(View.VISIBLE);
        if(track == null){
            return;
        }
        if(track.isDenied()){
            holder.profile_IMG_photo_ok.setVisibility(View.GONE);
            holder.profile_IMG_photo_denied.setVisibility(View.VISIBLE);
            holder.profile_IMG_photo_wait.setVisibility(View.GONE);
        }
        else if(track.isApproved()){
            holder.profile_IMG_photo_ok.setVisibility(View.VISIBLE);
            holder.profile_IMG_photo_denied.setVisibility(View.GONE);
            holder.profile_IMG_photo_wait.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return myUsers.size();
    }

    public KeepieUser getItem(int position) {
        return myUsers.get(position);
    }


    class UserHolder extends RecyclerView.ViewHolder {

        private AppCompatImageView list_connect_IMG;
        private MaterialTextView list_connect_LBL_name;
        private MaterialTextView list_connect_LBL_phone;
        private CircleImageView profile_IMG_photo_ok;
        private CircleImageView profile_IMG_photo_denied;
        private CircleImageView profile_IMG_photo_wait;

        public UserHolder(View itemView) {
            super(itemView);
            list_connect_IMG = itemView.findViewById(R.id.list_connect_IMG);
            list_connect_LBL_name = itemView.findViewById(R.id.list_connect_LBL_name);
            list_connect_LBL_phone = itemView.findViewById(R.id.list_connect_LBL_phone);
            profile_IMG_photo_ok = itemView.findViewById(R.id.profile_IMG_photo_ok);
            profile_IMG_photo_denied = itemView.findViewById(R.id.profile_IMG_photo_denied);
            profile_IMG_photo_wait = itemView.findViewById(R.id.profile_IMG_photo_wait);

//            itemView.setOnClickListener(view -> {
//                if (userListener != null) {
//                    userListener.clicked(getItem(getAdapterPosition()), getAdapterPosition());
//                }
//            });
//            listFollowers_IMG_delete.setOnClickListener(view -> {
//                if (userListener != null) {
//                    userListener.delete(getItem(getAdapterPosition()), getAdapterPosition());
//                }
//            });
        }
    }
}

