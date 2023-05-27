package com.ron.keepie.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;
import com.ron.keepie.R;
import com.ron.keepie.objects.KeepieUser;
import com.ron.keepie.whatsup_objects.MyContact;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface UserListener {
        void clicked(MyContact contact, int position);
    }

    private Activity activity;
    private UserListener userListener;

    private ArrayList<MyContact> myContact = new ArrayList<>();
    private List<KeepieUser> keepieUsers = new ArrayList<>();

    public SearchingAdapter(Activity activity, ArrayList<MyContact> myContact) {
        this.activity = activity;
        this.myContact = myContact;
    }

    public SearchingAdapter setChatListener(UserListener userListener) {
        this.userListener = userListener;
        return this;
    }

    public SearchingAdapter setKeepieUsers(List<KeepieUser> keepieUsers) {
        this.keepieUsers = keepieUsers;
        return this;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_searching, parent, false);
        UserHolder userHolder = new UserHolder(view);
        return userHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final UserHolder holder = (UserHolder) viewHolder;
        MyContact myContact = getItem(position);


        holder.listSearching_LBL_user_name.setText(myContact.getName());
        holder.listSearching_LBL_phone.setText(myContact.getPhone());

        keepieUsers.stream().filter(usr -> usr.getPhone().equals(myContact.getPhone())).findAny().ifPresent(v -> {
            Glide.with(activity).load(v.getImage()).into(holder.listSearching_IMG_photo);
        });



    }

    @Override
    public int getItemCount() {
        return myContact.size();
    }

    public MyContact getItem(int position) {
        return myContact.get(position);
    }


    class UserHolder extends RecyclerView.ViewHolder {

        private CircleImageView listSearching_IMG_photo;
        private MaterialTextView listSearching_LBL_user_name;
        private MaterialTextView listSearching_LBL_phone;

        public UserHolder(View itemView) {
            super(itemView);
            listSearching_IMG_photo = itemView.findViewById(R.id.listSearching_IMG_photo);
            listSearching_LBL_user_name = itemView.findViewById(R.id.listSearching_LBL_user_name);
            listSearching_LBL_phone = itemView.findViewById(R.id.listSearching_LBL_phone);

            itemView.setOnClickListener(view -> {
                if (userListener != null) {
                    userListener.clicked(getItem(getAdapterPosition()), getAdapterPosition());
                }
            });
        }
    }
}