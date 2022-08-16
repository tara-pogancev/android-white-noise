package com.tarapogancev.denoise;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tarapogancev.denoise.service.MediaPlayerService;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    CardView settingsButton, whiteNoiseButton, pinkNoiseButton, brownNoiseButton, nextButton, previousButton, playPauseButton;
    TextView activeSoundText, greetingText;
    ImageView playPauseImage;
    RelativeLayout playControls;
    MediaPlayerService mediaPlayerService = MediaPlayerService.getInstance();
    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        refreshTheme();
        setDefaultSound();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.getBoolean("firstRun", false)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("firstRun", false).apply();
            Intent intent = new Intent(this, Onboarding.class);
            finish();
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
        playPauseImage = findViewById(R.id.img_playPause);
        playControls = findViewById(R.id.layout_playControls);

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
                redirectWhiteNoise();
            }
        });

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
                    mediaPlayerService.play(MainActivity.this);
                    playPauseImage.setImageResource(R.drawable.pause_button);
                    startService();
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayerService.next(MainActivity.this);
                refreshCurrentSoundText();
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayerService.previous(MainActivity.this);
                refreshCurrentSoundText();
            }
        });

        playControls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayerService.getCurrentSoundName().equals("White Noise")) {
                    redirectWhiteNoise();
                } else if (mediaPlayerService.getCurrentSoundName().equals("Pink Noise"))  {
                    redirectPinkNoise();
                } else {
                    redirectBrownNoise();
                }
            }
        });

        setupGreetingsMessage();
        refreshPlayPauseButton();
        refreshCurrentSoundText();
        registerReceiver();
    }

    private void setDefaultSound() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String defaultSound = preferences.getString("defaultSound", "White Noise");
        if (defaultSound.equals("White Noise")) {
            mediaPlayerService.setSong(0);
        } else if (defaultSound.equals("Pink Noise")) {
            mediaPlayerService.setSong(1);
        } else {
            mediaPlayerService.setSong(2);
        }
    }

    private void redirectWhiteNoise() {
        if (!MediaPlayerService.getInstance().getCurrentSoundName().equals("White Noise")) {
            mediaPlayerService.close();
            mediaPlayerService.setSong(0);
            mediaPlayerService.play(this);
        }
        Intent intent = new Intent(MainActivity.this, WhiteNoisePlayer.class);
        startActivity(intent);
    }

    private void redirectPinkNoise() {
        if (!MediaPlayerService.getInstance().getCurrentSoundName().equals("Pink Noise")) {
            mediaPlayerService.close();
            mediaPlayerService.setSong(1);
            mediaPlayerService.play(this);
        }
        Intent intent = new Intent(MainActivity.this, PinkNoisePlayer.class);
        startActivity(intent);
    }

    private void redirectBrownNoise() {
        if (!MediaPlayerService.getInstance().getCurrentSoundName().equals("Brown Noise")) {
            mediaPlayerService.close();
            mediaPlayerService.setSong(2);
            mediaPlayerService.play(this);
        }
        Intent intent = new Intent(MainActivity.this, BrownNoisePlayer.class);
        startActivity(intent);
    }

    private void refreshCurrentSoundText() {
        activeSoundText.setText(MediaPlayerService.getInstance().getCurrentSoundName());
    }

    private void refreshPlayPauseButton() {
        if (mediaPlayerService.isPlaying()) {
            playPauseImage.setImageResource(R.drawable.pause_button);
        } else {
            playPauseImage.setImageResource(R.drawable.play_button);
        }
    }

    private void refreshTheme() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean darkTheme = preferences.getBoolean("darkMode", false);
        if (darkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public void setupGreetingsMessage() {
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

        if (d <= 400 || d > 2001) {
            greeting = ("Good Night") + name;
        } else if (d <= 1100) {
            greeting = ("Good Morning") + name;
        } else if (d <= 1600) {
            greeting = ("Good Day") + name;
        } else if (d <= 2000) {
            greeting = ("Good Afternoon") + name;
        } else {
            greeting = ("Good Evening") + name;
        }

        greetingText.setText(greeting);
        greetingText.setSelected(true);

    }

    public void startService() {
        Intent serviceIntent = new Intent(this, MediaPlayerService.class);
        serviceIntent.putExtra("soundName", activeSoundText.getText());
        serviceIntent.putExtra("playingState", true);
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    private void registerReceiver() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                refreshPlayPauseButton();
            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter("RefreshPlayPause"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
    }

    @Override
    protected void onResume() {
        refreshPlayPauseButton();
        refreshCurrentSoundText();
        setupGreetingsMessage();
        refreshTheme();
        super.onResume();
    }
}