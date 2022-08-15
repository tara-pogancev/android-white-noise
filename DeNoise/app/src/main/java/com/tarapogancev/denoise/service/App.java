package com.tarapogancev.denoise.service;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {
    public static final String CHANNEL_ID = "musicServiceChannel";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();
    }

    private void createNotificationChannel() {
        NotificationChannel channel1 = new NotificationChannel(CHANNEL_ID, "Music Service Channel", NotificationManager.IMPORTANCE_DEFAULT);
        channel1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        channel1.enableLights(true);

        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel1);
    }


}
