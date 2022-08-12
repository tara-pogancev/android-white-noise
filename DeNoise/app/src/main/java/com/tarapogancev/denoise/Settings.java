package com.tarapogancev.denoise;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (findViewById(R.id.layout_settings) != null) {
            if (savedInstanceState != null) {
                return;
            }

            getFragmentManager().beginTransaction().add(R.id.layout_settings, new SettingsFragment()).commit();
        }
    }
}