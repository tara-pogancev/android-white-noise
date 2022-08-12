package com.tarapogancev.denoise;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import java.util.Properties;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

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
        } else {
            Intent intent = new Intent(this, Settings.class);
            startActivity(intent);
        }
    }
}