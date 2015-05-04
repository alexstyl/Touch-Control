package com.alexstyl.touchcontrol.ui.widget;

import android.content.Context;
import android.gesture.GestureOverlayView;
import android.graphics.drawable.TransitionDrawable;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alexstyl.touchcontrol.R;

/**
 * <p>Created by alexstyl on 07/03/15.</p>
 */
public class GestureOverlayingView extends FrameLayout {

    private static final String TAG = "Gestureclick";
    private RelativeLayout mBackground;
    private FrameLayout mLayout;
    private GestureOverlayView mOverlay;
    private TransitionDrawable mBackgroundDrawable;


    public GestureOverlayingView(Context context) {
        super(context);
        init();
    }

    private TextView mTextView;

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.overlay_standard, this);
        mTextView = (TextView) findViewById(android.R.id.text1);
    }


    public void setCount(int count) {
        this.mTextView.setText(String.valueOf(count));
    }

    public void resetCount() {
        this.mTextView.setText(String.valueOf("o"));

    }
}
