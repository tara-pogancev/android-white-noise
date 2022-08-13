package com.tarapogancev.denoise;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class PinkNoisePlayer extends AppCompatActivity {

    CardView backButton, timerButton, whiteNoiseButton, brownNoiseButton, nextButton, previousButton, playPauseButton;

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

        whiteNoiseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PinkNoisePlayer.this, WhiteNoisePlayer.class);
                finish();
                Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(PinkNoisePlayer.this,
                        android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
                startActivity(intent, bundle);
            }
        });

        brownNoiseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PinkNoisePlayer.this, BrownNoisePlayer.class);
                finish();
                Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(PinkNoisePlayer.this,
                        android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
                startActivity(intent, bundle);
            }
        });

        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                Intent intent = new Intent(PinkNoisePlayer.this, MainActivity.class);
                Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(PinkNoisePlayer.this,
                        android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
                startActivity(intent, bundle);
            }
        });
    }
}