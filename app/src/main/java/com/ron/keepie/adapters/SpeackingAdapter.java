package com.ron.keepie.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.ron.keepie.R;
import com.ron.keepie.whatsup_objects.Message;

import java.util.ArrayList;

public class SpeackingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String my_user_color = "#E7FFDB";
    private final String other_user_color = "#FFFFFF";
    private Activity activity;
    private String phone1;
    private String phone2;
    private ArrayList<Message> messages = new ArrayList<>();

    public SpeackingAdapter(Activity activity, ArrayList<Message> messages, String phone1,String phone2) {
        this.activity = activity;
        this.messages = messages;
        this.phone1 = phone1;
        this.phone2 = phone2;
    }

    public SpeackingAdapter setPhone1(String phone1) {
        this.phone1 = phone1;
        return this;
    }

    public SpeackingAdapter setPhone2(String phone2) {
        this.phone2 = phone2;
        return this;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_speaking, parent, false);
        MessagesHolder messagesHolder = new MessagesHolder(view);
        messagesHolder.setIsRecyclable(false);
        return messagesHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final MessagesHolder holder = (MessagesHolder) viewHolder;
        Message meassage = getItem(position);


        if (meassage.getSender().equals(phone1)) {
            holder.listSpeaking_PAR.setGravity(Gravity.START);
            holder.listSpeaking_MTV.setCardBackgroundColor(Color.parseColor(my_user_color));
            holder.listSpeaking_LBL_phone.setText(phone1);
        } else {
            holder.listSpeaking_PAR.setGravity(Gravity.END);
            holder.listSpeaking_MTV.setCardBackgroundColor(Color.parseColor(other_user_color));
            holder.listSpeaking_LBL_phone.setText(phone2);

        }
        holder.listSpeaking_LBL_msg.setText(meassage.getContent());
        holder.listSpeaking_LBL_calender.setText(meassage.get_hour_minutes_str());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public Message getItem(int position) {
        return messages.get(position);
    }


    class MessagesHolder extends RecyclerView.ViewHolder {
        private MaterialCardView listSpeaking_MTV;
        private LinearLayout listSpeaking_PAR;
        private MaterialTextView listSpeaking_LBL_msg;
        private MaterialTextView listSpeaking_LBL_calender;
        private MaterialTextView listSpeaking_LBL_phone;



        public MessagesHolder(View itemView) {
            super(itemView);
            listSpeaking_PAR = itemView.findViewById(R.id.listSpeaking_PAR);
            listSpeaking_MTV = itemView.findViewById(R.id.listSpeaking_MTV);
            listSpeaking_LBL_msg = itemView.findViewById(R.id.listSpeaking_LBL_msg);
            listSpeaking_LBL_calender = itemView.findViewById(R.id.listSpeaking_LBL_calender);
            listSpeaking_LBL_phone = itemView.findViewById(R.id.listSpeaking_LBL_phone);
        }
    }
}