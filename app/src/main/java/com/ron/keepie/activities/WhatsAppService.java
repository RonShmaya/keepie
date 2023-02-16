package com.ron.keepie.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;





import com.google.gson.Gson;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.ron.keepie.R;


public class WhatsAppService extends Service {
    public static final String START_FOREGROUND_SERVICE = "START_FOREGROUND_SERVICE";
    public static final String STOP_FOREGROUND_SERVICE = "STOP_FOREGROUND_SERVICE";
    public static final int NOTIFICATION_ID = 5;
    private boolean isServiceRunningRightNow = false;
    private NotificationCompat.Builder builder;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent == null) {
            stopForeground(true);
            return START_NOT_STICKY;
        }
        if (intent.getAction().equals(START_FOREGROUND_SERVICE)) {
            if (isServiceRunningRightNow) {
                return START_STICKY;
            }

            isServiceRunningRightNow = true;
            make_first_notification();
            startListen();
            return START_STICKY;
        }
        if (intent.getAction().equals(STOP_FOREGROUND_SERVICE)) {
            if (!isServiceRunningRightNow) {
                return START_NOT_STICKY;
            }
            isServiceRunningRightNow = false;
            stopListen();
            stopForeground(true);
            stopSelf();
            return START_NOT_STICKY;
        }
        return START_NOT_STICKY;

    }

    private void stopListen() {

    }

    private void make_first_notification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the notification channel
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", "Suspicious call detected", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
        // TODO: 08/01/2023 add app icon
        builder = new NotificationCompat.Builder(this, "CHANNEL_ID")
                .setSmallIcon(R.drawable.rounded)
                .setContentTitle("Suspicious call detected")
                .setContentText("found a suspicious call in account 0542262097")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        startForeground(NOTIFICATION_ID, builder.build());
    }


    private void startListen() {
        new Thread(() -> {
            while (isServiceRunningRightNow) {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
//        SelendroidCapabilities caps = new SelendroidCapabilities("com.example.app:1.0");
//        caps.setModel("Nexus 5");
//        caps.setPlatform(Platform.ANDROID);
//        caps.setEmulator(false);

//
//        // Set the desired capabilities for the AndroidDriver
//        DesiredCapabilities capabilities = new DesiredCapabilities();
//        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "MyDevice");
//        capabilities.setCapability("appPackage", "com.whatsapp");
//        capabilities.setCapability("appActivity", "com.whatsapp.Main");
//
//        // Create a new instance of the AndroidDriver
//        AndroidDriver<AndroidElement> driver = null;
//        try {
//            driver = new AndroidDriver<>(new URL("http://localhost:4723/wd/hub"), capabilities);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//
//        // Wait for the chat list to be displayed
//        WebDriverWait wait = new WebDriverWait(driver, 10);
//        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.whatsapp:id/conversations_row_message")));
//
//        // Select the chat that you want to export
//        driver.findElement(By.id("com.whatsapp:id/conversations_row_message")).click();
//
//        // Scroll to the bottom of the chat
//        JavascriptExecutor js = (JavascriptExecutor) driver;
//        String script = "window.scrollTo(0, document.body.scrollHeight);";
//        js.executeScript(script);
//
//        // Wait for the chat messages to be loaded
//        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.whatsapp:id/message_text")));
//
//        // Get the list of chat messages
//        List<AndroidElement> messages = driver.findElements(By.id("com.whatsapp:id/message_text"));
//
//        // Create a list to hold the chat messages
//        List<String> chatMessages = new ArrayList<>();
//
//        // Iterate through the messages and add them to the list
//        for (AndroidElement message : messages) {
//            chatMessages.add(message.getText());
//        }
//
//        // Create a new Gson instance
//        Gson gson = new Gson();
//
//        // Convert the list of messages to a JSON string
//        String json = gson.toJson(chatMessages);


        //.start();
    }

    //    public void updateNotification(String title,String message) {
//        // Update the notification message
//        builder.setContentTitle(title)
//                .setContentText(message);
//
//        // Show the updated notification
//        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        notificationManager.notify(NOTIFICATION_ID, builder.build());
//    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
