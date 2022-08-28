package com.hieubui.session;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

public class SessionApplication extends Application {
    public static final String CHANNEL_MEDIA_PLAYBACK = "Media Playback";

    @Override
    public void onCreate() {
        super.onCreate();
        createMediaPlaybackChannel();
    }

    private void createMediaPlaybackChannel() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        String name = "Media Playback";
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel channel = new NotificationChannel(CHANNEL_MEDIA_PLAYBACK, name, importance);

        channel.setSound(null, null);
        channel.enableLights(false);
        channel.setShowBadge(false);
        channel.enableVibration(false);
        notificationManager.createNotificationChannel(channel);
    }
}
