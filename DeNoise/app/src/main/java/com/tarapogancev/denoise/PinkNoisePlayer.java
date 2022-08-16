package com.tarapogancev.denoise;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.tarapogancev.denoise.service.MediaPlayerService;

import java.util.Objects;

public class PinkNoisePlayer extends AppCompatActivity {

    CardView backButton, timerButton, whiteNoiseButton, brownNoiseButton, nextButton, previousButton, playPauseButton;
    ImageView playPauseImage;
    MediaPlayerService mediaPlayerService = MediaPlayerService.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pink_noise_player);

        backButton = findViewById(R.id.button_back);
        timerButton = findViewById(R.id.button_timer);
        whiteNoiseButton = findViewById(R.id.card_white);
        brownNoiseButton = findViewById(R.id.card_brown);
        nextButton = findViewById(R.id.button_next);
        previousButton = findViewById(R.id.button_previous);
        playPauseButton = findViewById(R.id.button_playPause);
        playPauseImage = findViewById(R.id.img_playPause);

        if (!mediaPlayerService.isPlaying() && !Objects.equals(mediaPlayerService.getCurrentSoundName(), "Pink Noise")) {
            mediaPlayerService.close();
            mediaPlayerService.setSong(1);
            playPauseImage.setImageResource(R.drawable.play_button);
        } else if (!mediaPlayerService.isPlaying() && Objects.equals(mediaPlayerService.getCurrentSoundName(), "Pink Noise")) {
            playPauseImage.setImageResource(R.drawable.play_button);
        } else if (mediaPlayerService.isPlaying() && !Objects.equals(mediaPlayerService.getCurrentSoundName(), "Pink Noise")) {
            mediaPlayerService.close();
            mediaPlayerService.setSong(1);
            mediaPlayerService.play(this);
            playPauseImage.setImageResource(R.drawable.pause_button);
            startService();
        } else {
            playPauseImage.setImageResource(R.drawable.pause_button);
            startService();
        }

        whiteNoiseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectWhiteNoise();
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
                    mediaPlayerService.play(PinkNoisePlayer.this);
                    playPauseImage.setImageResource(R.drawable.pause_button);
                }
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectWhiteNoise();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectBrownNoise();
            }
        });

        timerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectMainActivity();
            }
        });
    }

    private void redirectWhiteNoise() {
        Intent intent = new Intent(PinkNoisePlayer.this, WhiteNoisePlayer.class);
        finish();
        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(PinkNoisePlayer.this,
                android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
        startActivity(intent, bundle);
    }

    private void redirectBrownNoise() {
        Intent intent = new Intent(PinkNoisePlayer.this, BrownNoisePlayer.class);
        finish();
        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(PinkNoisePlayer.this,
                android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
        startActivity(intent, bundle);
    }

    private void redirectMainActivity() {
        Intent intent = new Intent(PinkNoisePlayer.this, MainActivity.class);
        finish();
        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(PinkNoisePlayer.this,
                android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
        startActivity(intent, bundle);
    }

    public void startService() {
        Intent serviceIntent = new Intent(this, MediaPlayerService.class);
        serviceIntent.putExtra("soundName", "Pink Noise");
        serviceIntent.putExtra("playingState", true);
        ContextCompat.startForegroundService(this, serviceIntent);
    }
}