package com.tarapogancev.denoise.service;

import static com.tarapogancev.denoise.service.App.CHANNEL_ID;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

    public void resume() {
        if (!isPlaying()) {
            player.start();
        }
    }

    public void close() {
        if (isPlaying()) {
            player.stop();
            player.release();
        }
    }

    public void stopService() {
        close();
        stopForeground(true);
        //stopSelf();
    }

    public void next(Context context) {
        Boolean wasPlaying = isPlaying();
        close();
        currentSong++;
        if (currentSong == 3) {
            currentSong = 0;
        }
        setSong(currentSong);
        if (wasPlaying) {
            play(context);
        }
    }

    public void previous(Context context) {
        Boolean wasPlaying = isPlaying();
        close();
        currentSong--;
        if (currentSong == -1) {
            currentSong = 2;
        }
        setSong(currentSong);
        startForeground(1, getNotification(getCurrentSoundName(), wasPlaying));
        if (wasPlaying) {
            play(context);
        }
    }

    public void setSong(int i) {
        currentSong = i;
    }

    public String getCurrentSoundName() {
        return songNames[currentSong];
    }

    public Boolean isPlaying() {
        return player != null && player.isPlaying();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String soundName = intent.getStringExtra("soundName");
        Boolean playingState = intent.getBooleanExtra("playingState", true);
        startForeground(1, getNotification(soundName, playingState));
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        close();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public Notification getNotification(String sound, Boolean isPlaying) {

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        Intent broadcastIntent1 = new Intent(this, NotificationReceiver.class).setAction("PREVIOUS");
        PendingIntent actionIntent1 = PendingIntent.getBroadcast(this, 0, broadcastIntent1, PendingIntent.FLAG_MUTABLE);

        int playPauseIcon;
        Intent broadcastIntent2 = new Intent(this, NotificationReceiver.class);
        if (isPlaying) {
            broadcastIntent2.setAction("PAUSE");
            playPauseIcon = R.drawable.ic_baseline_pause_24;
        } else {
            broadcastIntent2.setAction("PLAY");
            playPauseIcon = R.drawable.ic_baseline_play_arrow_24;
        }
        PendingIntent actionIntent2 = PendingIntent.getBroadcast(this, 0, broadcastIntent2, PendingIntent.FLAG_MUTABLE);

        Intent broadcastIntent3 = new Intent(this, NotificationReceiver.class).setAction("NEXT");
        PendingIntent actionIntent3 = PendingIntent.getBroadcast(this, 0, broadcastIntent3, PendingIntent.FLAG_MUTABLE);

        Intent broadcastIntent0 = new Intent(this, NotificationReceiver.class).setAction("CLOSE");
        PendingIntent actionIntent0 = PendingIntent.getBroadcast(this, 0, broadcastIntent0, PendingIntent.FLAG_MUTABLE);

        Bitmap picture = BitmapFactory.decodeResource(getResources(), R.drawable.waterfall);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("DeNoise")
                .setContentText(sound)
                .setSmallIcon(R.drawable.logo)
                .setLargeIcon(picture)
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.ic_baseline_skip_previous_24, "Previous", actionIntent1)
                .addAction(playPauseIcon, "Play/Pause", actionIntent2)
                .addAction(R.drawable.ic_baseline_skip_next_24, "Next", actionIntent3)
                .addAction(R.drawable.ic_baseline_cancel_24, "Close", actionIntent0)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(0, 1, 2))
                .setSilent(true)
                .build();

        return notification;
    }

}
