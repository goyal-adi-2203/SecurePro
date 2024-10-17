package com.example.securepro.services;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

import android.app.Notification;

import com.example.securepro.MainActivity;
import com.example.securepro.R;
import com.example.securepro.domain.model.User;
import com.example.securepro.presentation.login.UserViewModel;
import com.example.securepro.presentation.login.UserViewModelFactory;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private String TAG = "NotifMessagingService";
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

        Application application = getApplication();
        UserViewModelFactory factory = new UserViewModelFactory(application);
        UserViewModel userViewModel = new ViewModelProvider(
                new ViewModelStoreOwner() {
                    @NonNull
                    @Override
                    public ViewModelStore getViewModelStore() {
                        return new ViewModelStore();
                    }
                },
                new UserViewModelFactory(getApplication()))
                .get(UserViewModel.class);

        String notificationUserId = remoteMessage.getData().get("userId");
        Log.d(TAG, "onMessageReceived: " + notificationUserId);

        new Handler(Looper.getMainLooper()).post(() -> {
            userViewModel.getUser().observeForever(user -> {
                if (user != null) {
                    if (notificationUserId != null && !notificationUserId.equals(user.getUserId())) {
                        Log.d(TAG, "onMessageReceived: Notif ignored for user " + user.getUserId());
                        return;
                    }

                    if (remoteMessage.getNotification() != null) {
                        RemoteMessage.Notification notification = remoteMessage.getNotification();
                        Log.d(TAG, "Message Notification Title: " + notification.getTitle());
                        Log.d(TAG, "Message Notification Body: " + notification.getBody());
                        Log.d(TAG, "Message Notification Icon: " + notification.getIcon());
                        Log.d(TAG, "Message Notification Sound: " + notification.getSound());

                        // Show the notification using the notification payload
                        notificationService.showNotification(notification.getTitle(), notification.getBody(), CHANNEL_ID);
                    }

                    if (!remoteMessage.getData().isEmpty()) {
                        Log.d(TAG, "Message data payload: " + remoteMessage.getData().toString());

                        // Iterate through each data key-value pair
//                        for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
//                            Log.d(TAG, "Key: " + entry.getKey() + " Value: " + entry.getValue());
//                        }

                        String title = remoteMessage.getData().get("title");
                        String body = remoteMessage.getData().get("body");
//                        Log.d(TAG, "Notification Title: " + title);
//                        Log.d(TAG, "Notification Body: " + body);

                        notificationService.showNotification(title, body, CHANNEL_ID);
                    }
                }
            });
        });


    }

}
