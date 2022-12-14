package com.tarapogancev.denoise;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.tarapogancev.denoise.player.BeachWavesPlayer;
import com.tarapogancev.denoise.player.BrownNoisePlayer;
import com.tarapogancev.denoise.player.ForestPlayer;
import com.tarapogancev.denoise.player.PinkNoisePlayer;
import com.tarapogancev.denoise.player.RainPlayer;
import com.tarapogancev.denoise.player.ShorePlayer;
import com.tarapogancev.denoise.player.WhiteNoisePlayer;
import com.tarapogancev.denoise.service.MediaPlayerService;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    CardView settingsButton, cardWhite, cardPink, cardBrown, cardRain, cardWaves, cardForest,
            cardShore, nextButton, previousButton, playPauseButton, cardEmail;
    TextView activeSoundText, greetingText;
    ImageView playPauseImage;
    RelativeLayout playControls;
    MediaPlayerService mediaPlayerService = MediaPlayerService.getInstance();
    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setDefaultLanguage();
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
        cardWhite = findViewById(R.id.card_white);
        cardBrown = findViewById(R.id.card_brown);
        cardPink = findViewById(R.id.card_pink);
        cardRain = findViewById(R.id.card_rain);
        cardWaves = findViewById(R.id.card_waves);
        cardForest = findViewById(R.id.card_forest);
        cardShore = findViewById(R.id.card_shore);
        nextButton = findViewById(R.id.button_next);
        previousButton = findViewById(R.id.button_previous);
        playPauseButton = findViewById(R.id.button_playPause);
        greetingText = findViewById(R.id.text_greeting);
        activeSoundText = findViewById(R.id.text_activeText);
        playPauseImage = findViewById(R.id.img_playPause);
        playControls = findViewById(R.id.layout_playControls);
        cardEmail = findViewById(R.id.card_email);

        cardEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "tarapogancev@gmail.com", null));
                startActivity(Intent.createChooser(intent, getString(R.string.choose_email_client)));
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Settings.class);
                finish();
                startActivity(intent);
            }
        });

        cardWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectWhiteNoise();
            }
        });

        cardBrown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectBrownNoise();
            }
        });

        cardPink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectPinkNoise();
            }
        });

        cardRain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectRain();
            }
        });

        cardWaves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectWaves();
            }
        });

        cardForest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectForest();
            }
        });

        cardShore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectShore();
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
                if (mediaPlayerService.getActiveSound() == 0) {
                    redirectWhiteNoise();
                } else if (mediaPlayerService.getActiveSound() == 1) {
                    redirectPinkNoise();
                } else if (mediaPlayerService.getActiveSound() == 2) {
                    redirectBrownNoise();
                }
            }
        });

        setupGreetingsMessage();
        refreshPlayPauseButton();
        refreshCurrentSoundText();
        registerReceiver();
    }

    private void setDefaultLanguage() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String language = preferences.getString("language", "en");
        Locale locale = new Locale(language);
        Resources resources = getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        android.content.res.Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, metrics);
    }

    private void setDefaultSound() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String defaultSound = preferences.getString("defaultSound", "White Noise");
        if (defaultSound.equals("White Noise")) {
            mediaPlayerService.setSong(0);
        } else if (defaultSound.equals("Pink Noise")) {
            mediaPlayerService.setSong(1);
        } else if (defaultSound.equals("Brown Noise")) {
            mediaPlayerService.setSong(2);
        } else if (defaultSound.equals("Calming Rain")) {
            mediaPlayerService.setSong(3);
        } else if (defaultSound.equals("Beach Waves")) {
            mediaPlayerService.setSong(4);
        } else if (defaultSound.equals("Relaxing Forest")) {
            mediaPlayerService.setSong(5);
        } else {
            mediaPlayerService.setSong(6);
        }
    }

    private void redirectWhiteNoise() {
        if (MediaPlayerService.getInstance().getActiveSound() != 0) {
            mediaPlayerService.close();
            mediaPlayerService.setSong(0);
            mediaPlayerService.play(this);
        }
        Intent intent = new Intent(MainActivity.this, WhiteNoisePlayer.class);
        startActivity(intent);
    }

    private void redirectBrownNoise() {
        if (MediaPlayerService.getInstance().getActiveSound() != 2) {
            mediaPlayerService.close();
            mediaPlayerService.setSong(2);
            mediaPlayerService.play(this);
        }
        Intent intent = new Intent(MainActivity.this, BrownNoisePlayer.class);
        startActivity(intent);
    }

    private void redirectPinkNoise() {
        if (MediaPlayerService.getInstance().getActiveSound() != 1) {
            mediaPlayerService.close();
            mediaPlayerService.setSong(1);
            mediaPlayerService.play(this);
        }
        Intent intent = new Intent(MainActivity.this, PinkNoisePlayer.class);
        startActivity(intent);
    }

    private void redirectRain() {
        if (MediaPlayerService.getInstance().getActiveSound() != 3) {
            mediaPlayerService.close();
            mediaPlayerService.setSong(3);
            mediaPlayerService.play(this);
        }
        Intent intent = new Intent(MainActivity.this, RainPlayer.class);
        startActivity(intent);
    }

    private void redirectWaves() {
        if (MediaPlayerService.getInstance().getActiveSound() != 4) {
            mediaPlayerService.close();
            mediaPlayerService.setSong(4);
            mediaPlayerService.play(this);
        }
        Intent intent = new Intent(MainActivity.this, BeachWavesPlayer.class);
        startActivity(intent);
    }

    private void redirectForest() {
        if (MediaPlayerService.getInstance().getActiveSound() != 5) {
            mediaPlayerService.close();
            mediaPlayerService.setSong(5);
            mediaPlayerService.play(this);
        }
        Intent intent = new Intent(MainActivity.this, ForestPlayer.class);
        startActivity(intent);
    }

    private void redirectShore() {
        if (MediaPlayerService.getInstance().getActiveSound() != 6) {
            mediaPlayerService.close();
            mediaPlayerService.setSong(6);
            mediaPlayerService.play(this);
        }
        Intent intent = new Intent(MainActivity.this, ShorePlayer.class);
        startActivity(intent);
    }

    private void refreshCurrentSoundText() {
        activeSoundText.setText(MediaPlayerService.getInstance().getCurrentSoundName(this));
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
        int d = 100 * currentTime.getHours() + currentTime.getMinutes();

        if (d <= 400 || d > 2001) {
            greeting = (getString(R.string.good_night)) + name;
        } else if (d <= 1100) {
            greeting = (getString(R.string.good_morning)) + name;
        } else if (d <= 1600) {
            greeting = (getString(R.string.good_day)) + name;
        } else if (d <= 2000) {
            greeting = (getString(R.string.good_afternoon)) + name;
        } else {
            greeting = (getString(R.string.good_evening)) + name;
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
        if (broadcastReceiver != null) {
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