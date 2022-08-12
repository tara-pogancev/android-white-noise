package com.tarapogancev.denoise;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class OnboardingPagerAdapter extends PagerAdapter {

    Context context;

    @Override
    public int getCount() {
        return 3;
    }

    int images[] = {
            R.drawable.sleep,
            R.drawable.focus,
            R.drawable.relax2
    };

    String titles[] = {
            "Relax",
            "Focus",
            "Enjoy"
    };

    String descriptions[] = {
            "Enjoy calming white noise sounds for focus, relaxation and sleep.",
            "Finish your work with razor-sharp focus while filtering all distractions.",
            "Get the relaxation you need and deserve, while finding your inner peace."
    };

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }

    public OnboardingPagerAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.onboarding_slider, container, false);

        ImageView image = view.findViewById(R.id.image_onboarding);
        TextView title = view.findViewById(R.id.text_title);
        TextView description = view.findViewById(R.id.text_description);

        image.setImageResource(images[position]);
        title.setText(titles[position]);
        description.setText(descriptions[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}
