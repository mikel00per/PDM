package com.example.festivaly.Peticion;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import com.example.festivaly.ActividadPrincipal;
import com.example.festivaly.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import androidx.core.app.NotificationCompat;


public class MyFirebaseMenssagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("NEW_TOKEN",s);
    }
    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        Map<String, String> data = remoteMessage.getData();

        sendNotification(notification, data);
    }

    /**
     * Create and show a custom notification containing the received FCM message.
     *
     * @param notification FCM notification payload received.
     * @param data FCM data payload received.
     */
    private void sendNotification(RemoteMessage.Notification notification, Map<String, String> data) {

        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_notifications_black_24dp);

        Intent intentAceptar = new Intent(this, ActividadPrincipal.class);
        intentAceptar.setAction("aceptar");
        intentAceptar.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntentAceptar = PendingIntent.getActivity(this, 0, intentAceptar, PendingIntent.FLAG_ONE_SHOT);

        Intent intentRechazar= new Intent(this, ActividadPrincipal.class);
        intentAceptar.setAction("rechazar");
        intentAceptar.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntentRechazar = PendingIntent.getActivity(this, 0, intentAceptar, PendingIntent.FLAG_ONE_SHOT);

        Intent intentMostrar= new Intent(this, ActividadPrincipal.class);
        intentAceptar.setAction("mostrar");
        intentAceptar.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntentMostrar = PendingIntent.getActivity(this, 0, intentAceptar, PendingIntent.FLAG_ONE_SHOT);


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "channel_id")
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntentMostrar)
                .setContentInfo(notification.getTitle())
                .setLargeIcon(icon)
                .setColor(Color.BLACK)
                .setLights(Color.RED, 1000, 300)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSmallIcon(R.mipmap.ic_launcher)
                .addAction(R.drawable.ic_add_friend,"Aceptar",pendingIntentAceptar)
                .addAction(R.drawable.ic_eliminar_peticion,"Rechazar",pendingIntentRechazar);

        try {
            String picture_url = data.get("picture_url");
            if (picture_url != null && !"".equals(picture_url)) {
                URL url = new URL(picture_url);
                Bitmap bigPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                notificationBuilder.setStyle(
                        new NotificationCompat.BigPictureStyle().bigPicture(bigPicture).setSummaryText(notification.getBody())
                );
                Log.d("Noty:",picture_url);
            }else{
                Log.d("Noty 2:",picture_url);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Notification Channel is required for Android O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "channel_id", "channel_name", NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("channel description");
            channel.setShowBadge(true);
            channel.canShowBadge();
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500});
            notificationManager.createNotificationChannel(channel);
        }


        notificationManager.notify(1, notificationBuilder.build());
    }
}