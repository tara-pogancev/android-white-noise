package com.tarapogancev.denoise.player;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tarapogancev.denoise.MainActivity;
import com.tarapogancev.denoise.R;
import com.tarapogancev.denoise.service.MediaPlayerService;
import com.tarapogancev.denoise.service.TimerService;

public class BrownNoisePlayer extends AppCompatActivity {

    CardView backButton, timerButton, whiteNoiseButton, pinkNoiseButton, nextButton, previousButton, playPauseButton;
    ImageView playPauseImage;
    MediaPlayerService mediaPlayerService = MediaPlayerService.getInstance();
    TextView timerText;
    BroadcastReceiver broadcastReceiver, broadcastReceiverTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brown_noise_player);

        backButton = findViewById(R.id.button_back);
        timerButton = findViewById(R.id.button_timer);
        whiteNoiseButton = findViewById(R.id.card_white);
        pinkNoiseButton = findViewById(R.id.card_pink);
        nextButton = findViewById(R.id.button_next);
        previousButton = findViewById(R.id.button_previous);
        playPauseButton = findViewById(R.id.button_playPause);
        playPauseImage = findViewById(R.id.img_playPause);
        timerText = findViewById(R.id.text_timer);

        if (!mediaPlayerService.isPlaying() && !(MediaPlayerService.getInstance().getActiveSound() == 2)) {
            mediaPlayerService.close();
            mediaPlayerService.setSong(2);
            playPauseImage.setImageResource(R.drawable.play_button);
        } else if (!mediaPlayerService.isPlaying() && (MediaPlayerService.getInstance().getActiveSound() == 2)) {
            playPauseImage.setImageResource(R.drawable.play_button);
        } else if (mediaPlayerService.isPlaying() && !(MediaPlayerService.getInstance().getActiveSound() == 2)) {
            mediaPlayerService.close();
            mediaPlayerService.setSong(2);
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
                redirect(WhiteNoisePlayer.class);
            }
        });

        pinkNoiseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirect(PinkNoisePlayer.class);
            }
        });

        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayerService.isPlaying()) {
                    mediaPlayerService.pause();
                    playPauseImage.setImageResource(R.drawable.play_button);
                } else {
                    mediaPlayerService.play(BrownNoisePlayer.this);
                    playPauseImage.setImageResource(R.drawable.pause_button);
                    startService();
                }
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirect(PinkNoisePlayer.class);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirect(WhiteNoisePlayer.class);
            }
        });

        if (TimerService.getInstance().isTimerRunning()) {
            timerText.setText(TimerService.getInstance().getStringTimeRemaining());
            timerText.setVisibility(View.VISIBLE);
        } else {
            timerText.setVisibility(View.INVISIBLE);
        }

        timerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(BrownNoisePlayer.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.activity_timer_dialog);

                EditText hoursText = dialog.findViewById(R.id.editText_hours);
                EditText minutesText = dialog.findViewById(R.id.editText_minutes);
                Button cancelButton = dialog.findViewById(R.id.button_cancel);
                Button startStopTimer = dialog.findViewById(R.id.button_startStopTimer);

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });

                if (TimerService.getInstance().isTimerRunning()) {
                    startStopTimer.setText(getString(R.string.stop_timer));
                    hoursText.setEnabled(false);
                    minutesText.setEnabled(false);

                    startStopTimer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // STOP TIMER
                            timerText.setVisibility(View.INVISIBLE);
                            TimerService.getInstance().stopTimer();
                            dialog.cancel();
                        }
                    });
                } else {
                    startStopTimer.setText(getString(R.string.start_timer));
                    hoursText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            if (checkTimerButtonAvailability(minutesText, hoursText)) {
                                startStopTimer.setEnabled(true);
                            } else {
                                startStopTimer.setEnabled(false);
                            }
                        }
                    });
                    minutesText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            if (checkTimerButtonAvailability(minutesText, hoursText)) {
                                startStopTimer.setEnabled(true);
                            } else {
                                startStopTimer.setEnabled(false);
                            }
                        }

                    });

                    startStopTimer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // START TIMER
                            TimerService.getInstance().startTimer(BrownNoisePlayer.this, getSelectedTimeInMillis(minutesText, hoursText));
                            if (!MediaPlayerService.getInstance().isPlaying()) {
                                MediaPlayerService.getInstance().play(BrownNoisePlayer.this);
                            }
                            timerText.setVisibility(View.VISIBLE);
                            dialog.cancel();
                        }
                    });
                }

                dialog.show();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirect(MainActivity.class);
            }
        });

        registerReceiver();
    }

    private long getSelectedTimeInMillis(EditText minutesText, EditText hoursText) {
        String hoursString = hoursText.getText().toString();
        int hoursInt = 0;
        int minutesInt = 0;
        if (!hoursString.equals("")) {
            hoursInt = Integer.parseInt(hoursString) * 60000 * 60;
        }

        String minutesString = minutesText.getText().toString();
        if (!minutesString.equals("")) {
            minutesInt = Integer.parseInt(minutesString) * 60000;
        }

        return minutesInt + hoursInt;
    }

    private boolean checkTimerButtonAvailability(EditText minutes, EditText hours) {
        String hoursString = hours.getText().toString();
        int hoursInt = 0;
        int minutesInt = 0;
        if (!hoursString.equals("")) {
            hoursInt = Integer.parseInt(hoursString);
            if (hoursInt > 99 || hoursInt < 0) {
                return false;
            }
        }

        String minutesString = minutes.getText().toString();
        if (!minutesString.equals("")) {
            minutesInt = Integer.parseInt(minutesString);
            if (minutesInt > 59 || minutesInt < 0) {
                return false;
            }
        }

        if (minutesString.equals("") && hoursString.equals("")) {
            return false;
        }

        if (minutesInt + hoursInt == 0) {
            return false;
        }

        return true;
    }

    private void redirect(Class<?> activity) {
        Intent intent = new Intent(BrownNoisePlayer.this, activity);
        finish();
        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(BrownNoisePlayer.this,
                android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
        startActivity(intent, bundle);
    }

    private void registerReceiver() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                refreshPlayPauseButton();
            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter("RefreshPlayPause"));
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                timerText.setText(intent.getStringExtra("timerText"));
                if (timerText.getText().equals("00")) {
                    timerText.setVisibility(View.INVISIBLE);
                } else {
                    timerText.setVisibility(View.VISIBLE);
                }
            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter("RefreshTimerText"));
    }

    private void refreshPlayPauseButton() {
        if (mediaPlayerService.isPlaying()) {
            playPauseImage.setImageResource(R.drawable.pause_button);
        } else {
            playPauseImage.setImageResource(R.drawable.play_button);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
        if(broadcastReceiverTimer != null) {
            unregisterReceiver(broadcastReceiverTimer);
        }
    }

    public void startService() {
        Intent serviceIntent = new Intent(this, MediaPlayerService.class);
        serviceIntent.putExtra("soundName", "Brown Noise");
        serviceIntent.putExtra("playingState", true);
        ContextCompat.startForegroundService(this, serviceIntent);
    }
}