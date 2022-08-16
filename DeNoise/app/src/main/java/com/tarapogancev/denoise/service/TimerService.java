package com.tarapogancev.denoise.service;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;

import androidx.annotation.NonNull;

public class TimerService {

    private long startTimeInMillis = 10000;
    private static TimerService instance;
    private CountDownTimer countDownTimer;
    private Boolean timerRunning = false;
    private long timeLeftInMillis = startTimeInMillis;

    public static TimerService getInstance() {
        if (instance == null) {
            instance = new TimerService();
        }
        return instance;
    }

    public Boolean isTimerRunning() {
        return timerRunning;
    }

    public void startTimer(Context context, long millis) {
        timerRunning = true;
        timeLeftInMillis = millis;
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
        countDownTimer.cancel();
        timerRunning = false;
    }

    public long getMillisRemaining() {
        return timeLeftInMillis;
    }

    private void setupTimerText(Context context) {
        if (!TimerService.getInstance().isTimerRunning()) {
        } else {
            String text = getStringTimeRemaining();
            Intent in = new Intent("RefreshTimerText");
            in.putExtra("timerText", text);
            context.sendBroadcast(in);
        }
    }

    @NonNull
    public String getStringTimeRemaining() {
        long timeInMillis = TimerService.getInstance().getMillisRemaining();
        String text = "";

        long hours = timeInMillis / 3600000;
        long minutes = timeInMillis % 3600000 / 60000;
        long seconds = timeInMillis % 60000 / 1000;
        if (hours != 0) {
            if (minutes > 10) {
                text = hours + ":" + minutes + ":" + seconds;
            } else {
                text = hours + ":0" + minutes + ":" + seconds;
            }
        } else if (minutes != 0) {
            if (seconds > 10) {
                text = minutes + ":" + seconds;
            } else {
                text = minutes + ":0" + seconds;
            }
        } else {
            text = seconds + "";
        }
        return text;
    }

}
