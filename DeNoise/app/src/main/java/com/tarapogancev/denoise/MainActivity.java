package com.tarapogancev.denoise;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    CardView settingsButton, whiteNoiseButton, pinkNoiseButton, brownNoiseButton, nextButton, previousButton, playPauseButton;
    TextView greetingText, activeSoundText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.getBoolean("firstRun", false)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("firstRun", false).apply();
            Intent intent = new Intent(this, Onboarding.class);
            startActivity(intent);
        }

        settingsButton = findViewById(R.id.button_settings);
        whiteNoiseButton = findViewById(R.id.card_white);
        pinkNoiseButton = findViewById(R.id.card_pink);
        brownNoiseButton = findViewById(R.id.card_brown);
        nextButton = findViewById(R.id.button_next);
        previousButton = findViewById(R.id.button_previous);
        playPauseButton = findViewById(R.id.button_playPause);
        greetingText = findViewById(R.id.text_greeting);
        activeSoundText = findViewById(R.id.text_activeText);

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Settings.class);
                startActivity(intent);
            }
        });

        whiteNoiseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, WhiteNoisePlayer.class);
                startActivity(intent);
            }
        });

        pinkNoiseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PinkNoisePlayer.class);
                startActivity(intent);
            }
        });

        brownNoiseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BrownNoisePlayer.class);
                startActivity(intent);
            }
        });

        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        setupGreetinsMessage();
    }

    private void setupGreetinsMessage() {
        Date currentTime = Calendar.getInstance().getTime();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String name = preferences.getString("userName", "");
        if (name.isEmpty()) {
            name = "!";
        } else {
            name = ", " + name + "!";
        }

        String greeting;
        int d = 100*currentTime.getHours() + currentTime.getMinutes();

        if (d <= 400 || d > 1830) {
            greeting = ("Good Night") + name;
        } else if (d <= 1100) {
            greeting = ("Good Morning") + name;
        } else if (d <= 1500) {
            greeting = ("Good Afternoon") + name;
        } else {
            greeting = ("Good Evening") + name;
        }

        greetingText.setText(greeting);

    }
}