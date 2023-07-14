package com.example.automoto.Notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.automoto.AcceptedBooking;
import com.example.automoto.Booking;
import com.example.automoto.Chat;
import com.example.automoto.Model.Accepted;
import com.example.automoto.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class FirebaseService extends FirebaseMessagingService {

    private final String CHANNEL_ID = "channel_id";
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        Intent intent = new Intent(this, AcceptedBooking.class);
        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationId = new Random().nextInt();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            createNotificationChannel(manager);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent intent1 = PendingIntent.getActivities(this, 0 , new Intent[]{intent}, PendingIntent.FLAG_MUTABLE);

        Notification notification;

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle(message.getData().get("title"))
                    .setContentText(message.getData().get("message"))
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.ic_booking)
                    .setContentIntent(intent1)
                    .setVibrate(new long[] { 1000, 1000})
                    .setSound(alarmSound)
                    .build();


        manager.notify(notificationId, notification);

    }
    @RequiresApi(api = Build.VERSION_CODES.S)
    private void createNotificationChannel(NotificationManager manager){
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "channelName", NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription("My Description");
        channel.enableLights(true);
        channel.setLightColor(Color.WHITE);

        manager.createNotificationChannel(channel);
    }
}
