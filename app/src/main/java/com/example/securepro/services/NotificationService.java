package com.example.securepro.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.securepro.MainActivity;
import com.example.securepro.R;

public class NotificationService {
    private String TAG = "notifServiceTag";
    private String CHANNEL_ID = "esp32_alerts_channel";
    private String ESP32_ALERTS = "ESP 32 Alerts";

    private Context context;

    public NotificationService(Context context) {
        this.context = context;
        createNotificationChannels();
    }

    public void createNotificationChannels(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = ESP32_ALERTS;
            String description = "Channel for Esp32 Alerts";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.setBypassDnd(true);  // Enable DND override for high-priority notifications
            channel.enableVibration(true);
            channel.enableLights(true);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    public void showNotification(String title, String message, String channelId) {
        // Create an Intent to launch your app when the user taps the notification
        Log.d(TAG, "showNotification: " + title);
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        // Create the NotificationCompat builder
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(R.drawable.ic_shield_24)  // Replace with your app's icon
                        .setContentTitle(title)
                        .setContentText(message)
                        .setAutoCancel(true)  // Automatically remove the notification when tapped
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setCategory(NotificationCompat.CATEGORY_ALARM)
                        .setContentIntent(pendingIntent);  // Intent to launch the app

        // Get the NotificationManager service
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Show the notification
        int notificationId = (int) System.currentTimeMillis();
        Log.d(TAG, "Displaying notif: " + notificationId);
        notificationManager.notify(notificationId, notificationBuilder.build());

    }
}
