package com.tarapogancev.denoise.service;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class TimerService extends Service {

    private static final long startTimeInMillis = 5000;
    private static TimerService instance;
    Thread timerThread;
    private CountDownTimer countDownTimer;
    private Boolean timerRunning = false;
    private long timeLeftInMillis = startTimeInMillis;


    public static TimerService getInstance() {
        if (instance == null) {
            instance = new TimerService();
        }
        return instance;
    }

    public void openTimerDialog() {

    }

    public Boolean isTimerRunning() {
        return timerRunning;
    }

    public void startTimer() {
        timerRunning = true;
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisLeft) {
                timeLeftInMillis = millisLeft;
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                stopSounds();
            }
        }.start();
    }

    private void stopSounds() {
        MediaPlayerService.getInstance().close();
    }

    public void stopTimer() {
        if (timerThread.isAlive()) {
            timerThread.stop();
            timerThread.destroy();
        }
        stopSelf();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
