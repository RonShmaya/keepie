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
import com.ron.keepie.objects.Notification;
import com.ron.keepie.whatsup_objects.MyContact;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface UserListener {
        void delete(Notification notification, int position);
    }

    private Activity activity;
    private UserListener userListener;
    private List<Notification> notifications = new ArrayList<>();

    public NotificationAdapter(Activity activity, ArrayList<Notification> notifications) {
        this.notifications = notifications;
    }

    public NotificationAdapter setChatListener(UserListener userListener) {
        this.userListener = userListener;
        return this;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_notification, parent, false);
        UserHolder userHolder = new UserHolder(view);
        return userHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final UserHolder holder = (UserHolder) viewHolder;
        Notification notification = getItem(position);

        holder.list_notification_LBL_title.setText(notification.getTitle());
        holder.list_notification_LBL_body.setText(notification.getBody());

    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public Notification getItem(int position) {
        return notifications.get(position);
    }


    class UserHolder extends RecyclerView.ViewHolder {
        private MaterialTextView list_notification_LBL_title;
        private MaterialTextView list_notification_LBL_body;
        private CircleImageView list_notification_IMG_cancel;


        public UserHolder(View itemView) {
            super(itemView);
            list_notification_LBL_title = itemView.findViewById(R.id.list_notification_LBL_title);
            list_notification_LBL_body = itemView.findViewById(R.id.list_notification_LBL_body);
            list_notification_IMG_cancel = itemView.findViewById(R.id.list_notification_IMG_cancel);

            list_notification_IMG_cancel.setOnClickListener(view -> {
                if (userListener != null) {
                    userListener.delete(getItem(getAdapterPosition()), getAdapterPosition());
                }
            });
        }
    }
}