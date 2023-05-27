package com.ron.keepie.activities;


import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.reflect.TypeToken;
import com.ron.keepie.R;
import com.ron.keepie.mytools.MySP;
import com.ron.keepie.objects.Notification;

import java.util.ArrayList;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Handle incoming message here
        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();

            ArrayList<Notification> notifications = MySP.get_my_SP().getArray(MySP.NOTIFICATION,new TypeToken<ArrayList<Notification>>() {} );
            notifications.add(new Notification(title,body));
            MySP.get_my_SP().putArray(MySP.NOTIFICATION, notifications);

            // Process the notification
            processNotification(title, body);
        }
    }

    private void processNotification(String title, String body) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
                .setSmallIcon(R.drawable.ic_new_logo)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0, builder.build());    }


    @Override
    public void onNewToken(String token) {
        sendTokenToServer(token);
    }

    private void sendTokenToServer(String token) {
        // Send the token to your server for further processing if needed
    }

    public static void add_topic(String topic_name){
        FirebaseMessaging.getInstance().subscribeToTopic(topic_name)
                .addOnCompleteListener(listener);
    }
    public static void remove_topic(String topic_name){
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic_name)
                .addOnCompleteListener(listener);
    }

    private static OnCompleteListener<Void> listener = task -> {
        if (task.isSuccessful()) {
            Log.d("MyLog", "Successfully subscribed to topic");
        } else {
            Log.d("MyLog", "Subscription to topic failed");
        }
    };
}
