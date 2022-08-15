package com.tarapogancev.denoise.service;

import static com.tarapogancev.denoise.service.App.CHANNEL_ID;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.tarapogancev.denoise.MainActivity;
import com.tarapogancev.denoise.R;

public class MediaPlayerService extends Service {

    public static MediaPlayerService instance;
    int currentSong = 0;
    MediaPlayer player, playerNext;
    Context mContext;

    private NotificationManagerCompat notificationManagerCompat;

    String[] songNames = {
            "White Noise",
            "Pink Noise",
            "Brown Noise"
    };

    int[] songs = {
            R.raw.whitenoise,
            R.raw.pinknoise,
            R.raw.brownnoise
    };

    public static MediaPlayerService getInstance() {
        if (instance == null) {
            instance = new MediaPlayerService();
        }
        return instance;
    }

    public void play(Context context) {
        player = MediaPlayer.create(context, songs[currentSong]);
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                player.start();
            }
        });

        createNextMediaPlayer(context);

//        notificationManagerCompat = NotificationManagerCompat.from(context);
//        Notification channel = new NotificationCompat.Builder(context.getApplicationContext(), App.CHANNEL_ID_1)
//                .setSmallIcon(R.drawable.logo)
//                .setContentTitle("Currently Playing: " + getCurrentSongName())
//                .setContentText("Return to DeNoise")
//                .setAutoCancel(true)
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                //.addAction(R.drawable.previous, "previous", null)
//                //.addAction(R.drawable.pause_button, "playPause", null)
//                //.addAction(R.drawable.next, "next", null)
//                //.setStyle(new androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(1, 2, 3))
//                .build();
//
//        notificationManagerCompat.notify(1, channel);
    }

    private void createNextMediaPlayer(Context context) {
        playerNext = MediaPlayer.create(context, songs[currentSong]);
        player.setNextMediaPlayer(playerNext);
        mContext = context;
        player.setOnCompletionListener(onCompletionListener);
    }

    private MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.release();
            player = playerNext;
            createNextMediaPlayer(mContext);
        }
    };

    public void pause() {
        if (isPlaying()) {
            player.pause();
        }
    }

    public void close() {
        if (isPlaying()) {
            player.stop();
            player.release();
        }
    }

    public void next(Context context) {
        close();
        currentSong++;
        if (currentSong == 3) {
            currentSong = 0;
        }
        setSong(currentSong);
        play(context);
    }

    public void previous(Context context) {
        close();
        currentSong--;
        if (currentSong == -1) {
            currentSong = 2;
        }
        setSong(currentSong);
        play(context);
    }

    public void setSong(int i) {
        currentSong = i;
    }

    public String getCurrentSongName() {
        return songNames[currentSong];
    }

    public Boolean isPlaying() {
        return player != null && player.isPlaying();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String title = intent.getStringExtra("inputExtra");

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_MUTABLE);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("DeNoise is currently running.")
                .setContentText("Tap to revisit the application.")
                .setSmallIcon(R.drawable.logo)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);
        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
