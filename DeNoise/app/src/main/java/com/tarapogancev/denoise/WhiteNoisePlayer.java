package com.tarapogancev.denoise;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tarapogancev.denoise.service.MediaPlayerService;
import com.tarapogancev.denoise.service.TimerService;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class WhiteNoisePlayer extends AppCompatActivity {

    CardView backButton, timerButton, pinkNoiseButton, brownNoiseButton, nextButton, previousButton, playPauseButton;
    ImageView playPauseImage;
    MediaPlayerService mediaPlayerService = MediaPlayerService.getInstance();
    TextView timerText;
    TimerService timerService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_white_noise_player);

        backButton = findViewById(R.id.button_back);
        timerButton = findViewById(R.id.button_timer);
        pinkNoiseButton = findViewById(R.id.card_pink);
        brownNoiseButton = findViewById(R.id.card_brown);
        nextButton = findViewById(R.id.button_next);
        previousButton = findViewById(R.id.button_previous);
        playPauseButton = findViewById(R.id.button_playPause);
        playPauseImage = findViewById(R.id.img_playPause);
        timerText = findViewById(R.id.text_timer);

        if (!mediaPlayerService.isPlaying() && !Objects.equals(mediaPlayerService.getCurrentSongName(), "White Noise")) {
            mediaPlayerService.close();
            mediaPlayerService.setSong(0);
            playPauseImage.setImageResource(R.drawable.play_button);
        } else if (!mediaPlayerService.isPlaying() && Objects.equals(mediaPlayerService.getCurrentSongName(), "White Noise")) {
            playPauseImage.setImageResource(R.drawable.play_button);
        } else if (mediaPlayerService.isPlaying() && !Objects.equals(mediaPlayerService.getCurrentSongName(), "White Noise")) {
            mediaPlayerService.close();
            mediaPlayerService.setSong(0);
            mediaPlayerService.play(this);
            playPauseImage.setImageResource(R.drawable.pause_button);
        } else {
            playPauseImage.setImageResource(R.drawable.pause_button);
        }

        pinkNoiseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectPinkNoise();
            }
        });

        brownNoiseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectBrownNoise();
            }
        });

        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayerService.isPlaying()) {
                    mediaPlayerService.pause();
                    playPauseImage.setImageResource(R.drawable.play_button);
                } else {
                    mediaPlayerService.play(WhiteNoisePlayer.this);
                    playPauseImage.setImageResource(R.drawable.pause_button);
                }
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectBrownNoise();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectPinkNoise();
            }
        });

        setupTimerText();
        timerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimerService.getInstance().startTimer();
                timerText.setVisibility(View.VISIBLE);
                startTrackingTimer();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectMainActivity();
            }
        });
    }

    private void startTrackingTimer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                setupTimerText();
                Toast.makeText(WhiteNoisePlayer.this, "12", Toast.LENGTH_SHORT).show();
            }
        }, 0, 1000);
    }

    private void setupTimerText() {
        if (!TimerService.getInstance().isTimerRunning()) {
            playPauseImage.setImageResource(R.drawable.play_button);
            timerText.setVisibility(View.INVISIBLE);
        } else {
            long timeInMillis = TimerService.getInstance().getMillisRemaining();
            String text = "";
            /*
            long hours = timeInMillis / 3600000;
            long minutes = timeInMillis % 60000 / 60000;
            long seconds = timeInMillis % 60000 / 1000;
            if (hours != 0) {
                text = hours + ":" + minutes + ":" + seconds;
            } else if (minutes != 0) {
                text = minutes + ":" + seconds;
            } else {
                text = seconds + "";
            }*/

            timerText.setText(timeInMillis + "");
        }
    }

    private void redirectPinkNoise() {
        Intent intent = new Intent(WhiteNoisePlayer.this, PinkNoisePlayer.class);
        finish();
        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(WhiteNoisePlayer.this,
                android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
        startActivity(intent, bundle);
    }

    private void redirectBrownNoise() {
        Intent intent = new Intent(WhiteNoisePlayer.this, BrownNoisePlayer.class);
        finish();
        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(WhiteNoisePlayer.this,
                android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
        startActivity(intent, bundle);
    }

    private void redirectMainActivity() {
        Intent intent = new Intent(WhiteNoisePlayer.this, MainActivity.class);
        finish();
        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(WhiteNoisePlayer.this,
                android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
        startActivity(intent, bundle);
    }

}