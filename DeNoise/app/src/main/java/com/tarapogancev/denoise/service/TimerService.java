package com.tarapogancev.denoise.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class TimerService {

    private static final long startTimeInMillis = 5000;
    private static TimerService instance;
    private CountDownTimer countDownTimer;
    private Boolean timerRunning = false;
    private long timeLeftInMillis = startTimeInMillis;
    private Context mContext;

    public static TimerService getInstance() {
        if (instance == null) {
            instance = new TimerService();
        }
        return instance;
    }

    public Boolean isTimerRunning() {
        return timerRunning;
    }

    public void startTimer(Context context) {
        timerRunning = true;
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisLeft) {
                timeLeftInMillis = millisLeft;
                setupTimerText(context);
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                MediaPlayerService.getInstance().pause();
                Intent in = new Intent("RefreshPlayPause");
                context.sendBroadcast(in);
            }
        }.start();

    }

    private void pauseSound() {
        MediaPlayerService.getInstance().pause();
    }

    public void stopTimer() {

    }

    public long getMillisRemaining() {
        return timeLeftInMillis;
    }

    private void setupTimerText(Context context) {
        if (!TimerService.getInstance().isTimerRunning()) {
        } else {
            long timeInMillis = TimerService.getInstance().getMillisRemaining();
            String text = "";

            long hours = timeInMillis / 3600000;
            long minutes = timeInMillis % 60000 / 60000;
            long seconds = timeInMillis % 60000 / 1000;
            if (hours != 0) {
                text = hours + ":" + minutes + ":" + seconds;
            } else if (minutes != 0) {
                text = minutes + ":" + seconds;
            } else {
                text = seconds + "";
            }

            Intent in = new Intent("RefreshTimerText");
            in.putExtra("timerText", text);
            context.sendBroadcast(in);
        }
    }

}
