package com.ron.keepie.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;
import com.ron.keepie.R;
import com.ron.keepie.objects.MyUser;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FollowersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    public interface ClickedUser {
        void clicked(MyUser contact, int position);
        void delete(MyUser contact, int position);
    }

    private Activity activity;
    private ClickedUser userListener;

    private ArrayList<MyUser> myUsers = new ArrayList<>();

    public FollowersAdapter(Activity activity, ArrayList<MyUser> myUsers) {
        this.activity = activity;
        this.myUsers = myUsers;
    }

    public void setChatListener(ClickedUser userListener) {
        this.userListener = userListener;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_followers, parent, false);
        UserHolder userHolder = new UserHolder(view);
        return userHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final UserHolder holder = (UserHolder) viewHolder;
        MyUser myUser = getItem(position);


        holder.listFollowers_LBL_user_name.setText(myUser.getNick_name());
        holder.listFollowers_LBL_phone.setText(myUser.getPhone());
        Glide.with(activity).load(myUser.getImg_uri()).placeholder(R.drawable.ic_user).into(holder.listFollowers_IMG_photo);

    }

    @Override
    public int getItemCount() {
        return myUsers.size();
    }

    public MyUser getItem(int position) {
        return myUsers.get(position);
    }


    class UserHolder extends RecyclerView.ViewHolder {

        private CircleImageView listFollowers_IMG_photo;
        private CircleImageView listFollowers_IMG_delete;
        private MaterialTextView listFollowers_LBL_user_name;
        private MaterialTextView listFollowers_LBL_phone;


        public UserHolder(View itemView) {
            super(itemView);
            listFollowers_IMG_photo = itemView.findViewById(R.id.listFollowers_IMG_photo);
            listFollowers_IMG_delete = itemView.findViewById(R.id.listFollowers_IMG_delete);
            listFollowers_LBL_user_name = itemView.findViewById(R.id.listFollowers_LBL_user_name);
            listFollowers_LBL_phone = itemView.findViewById(R.id.listFollowers_LBL_phone);

            itemView.setOnClickListener(view -> {
                if (userListener != null) {
                    userListener.clicked(getItem(getAdapterPosition()), getAdapterPosition());
                }
            });
            listFollowers_IMG_delete.setOnClickListener(view -> {
                if (userListener != null) {
                    userListener.delete(getItem(getAdapterPosition()), getAdapterPosition());
                }
            });
        }
    }
}

