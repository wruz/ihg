package com.wruzjan.ihg.utils.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.wruzjan.ihg.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ProgressLayout extends FrameLayout {

    public ProgressLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(getResources().getColor(R.color.translucent_black));
        super.addView(createProgressBar(context), new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
        ));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    private ProgressBar createProgressBar(@NonNull Context context) {
        return new ProgressBar(context);
    }
}
