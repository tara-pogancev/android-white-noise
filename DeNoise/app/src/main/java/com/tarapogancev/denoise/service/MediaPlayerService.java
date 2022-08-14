package com.tarapogancev.denoise.service;

import android.content.Context;
import android.media.MediaPlayer;
import android.provider.Settings;

import com.tarapogancev.denoise.R;

public class MediaPlayerService {

    public static MediaPlayerService instance;
    int currentSong = 0;
    MediaPlayer player, playerNext;
    Context mContext;

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
}
