package com.example.securepro.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import android.app.Notification;

import com.example.securepro.MainActivity;
import com.example.securepro.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private String TAG = "SecurePro";
    private final String CHANNEL_ID = "esp32_alerts_channel";

    private NotificationService notificationService;
    private Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        notificationService = new NotificationService(context);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (!remoteMessage.getData().isEmpty()) {
            String title = remoteMessage.getData().get("title");
            String body = remoteMessage.getData().get("body");
            Log.d(TAG, "Notification Title: " + title);
            Log.d(TAG, "Notification Body: " + body);

            notificationService.showNotification(title, body, CHANNEL_ID);
        }

    }

}
