package com.hieubui.session;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

public class ZoomOutPageTransformer implements ViewPager2.PageTransformer {
    private final float MIN_SCALE = 0.95f;

    private final float MIN_ALPHA = 0.6f;

    private final float a = (1 - MIN_SCALE) * 4;

    @Override
    public void transformPage(@NonNull View page, float position) {
        if (position < -1) {    // [-Infinity,-1)
            page.setAlpha(0f);
            return;
        }

        if (position <= 1) {    // [-1,1]
            // Modify the default slide transition to shrink the page as well
            final float scaleFactor = a * Math.abs(position) * Math.abs(position) - a * Math.abs(position) + 1;
            final float ratio = (1 - scaleFactor) / 2;
            final float marginVertical = page.getHeight() * ratio;
            final float marginHorizontal = page.getWidth() * ratio;
            final float alpha = (MIN_ALPHA + (((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA)));

            if (position < 0) {
                page.setTranslationX(marginHorizontal - marginVertical / 2);
            } else {
                page.setTranslationX(marginHorizontal + marginVertical / 2);
            }

            // Scale the page down (between MIN_SCALE and 1)
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);

            // Fade the page relative to its size.
            page.setAlpha(alpha);
            return;
        }

        page.setAlpha(0f);  // (1,+Infinity]
    }
}
