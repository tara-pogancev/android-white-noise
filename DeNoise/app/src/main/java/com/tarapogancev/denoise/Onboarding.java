package com.tarapogancev.denoise;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class Onboarding extends AppCompatActivity {

    ViewPager sliderViewPager;
    LinearLayout lineIndicatorLayout;
    Button skipButton;

    TextView[] lines;
    OnboardingPagerAdapter onboardingPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        skipButton = findViewById(R.id.button_skip);

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getItem(0) < 2) {
                    sliderViewPager.setCurrentItem(getItem(1), true);
                } else {
                    Intent intent = new Intent(Onboarding.this, MainActivity.class);
                    finish();
                    startActivity(intent);
                }
            }
        });

        sliderViewPager = findViewById(R.id.layout_slider);
        lineIndicatorLayout = findViewById(R.id.layout_onboardingIndicators);

        onboardingPagerAdapter = new OnboardingPagerAdapter(this);
        sliderViewPager.setAdapter(onboardingPagerAdapter);

        setupIndicator(0);
        sliderViewPager.addOnPageChangeListener(viewListener);
    }

    public void setupIndicator(int position) {
        lines = new TextView[3];
        lineIndicatorLayout.removeAllViews();

        for (int i = 0; i < 3; i++) {
            lines[i] = new TextView(this);
            lines[i].setText("_ ");
            lines[i].setTextSize(50);
            lines[i].setTextColor(getResources().getColor(R.color.blue));
            lineIndicatorLayout.addView(lines[i]);
        }

        lines[position].setTextColor(getResources().getColor(R.color.teal));
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            setupIndicator(position);
            if (position == 2) {
                skipButton.setText(R.string.start_using_denoise);
            } else {
                skipButton.setText(R.string.next);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private int getItem(int i) {
        return sliderViewPager.getCurrentItem() + i;
    }

}