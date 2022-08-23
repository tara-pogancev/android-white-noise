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

    Context mContext;

    public OnboardingPagerAdapter(Context context) {
        this.mContext = context;
    }

    int[] images = {
            R.drawable.sleep,
            R.drawable.focus,
            R.drawable.relax2
    };

    int[] titles = {
            R.string.relax,
            R.string.focus,
            R.string.enjoy
    };

    int[] descriptions = {
            R.string.onboarding_slide_1,
            R.string.onboarding_slide_2,
            R.string.onboarding_slide_3
    };

    @Override
    public int getCount() {
        return titles.length;
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
