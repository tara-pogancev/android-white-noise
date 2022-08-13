package com.tarapogancev.denoise.service;

import android.content.Context;
import android.media.MediaPlayer;
import android.provider.Settings;

import com.tarapogancev.denoise.R;

public class MediaPlayerService {

    public static MediaPlayerService instance;
    int currentSong = 0;
    MediaPlayer player;

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
        player.setLooping(true);
        player.start();
    }

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
