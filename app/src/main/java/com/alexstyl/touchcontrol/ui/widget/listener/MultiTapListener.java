package com.alexstyl.touchcontrol.ui.widget.listener;

import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * An OnClickListener that checks the number of concecutive taps the user has performed on this view.
 * <p>Created by alexstyl on 03/03/15.</p>
 */
public abstract class MultiTapListener implements View.OnClickListener {
    private static final String TAG = "MultiTapListener";
    private static final boolean DEBUG = false;
    private int mTapCounts = 0;
    private long mPreviousTouchMs = 0;

    final private int mRequestedTapCount;
    private long mTimeBetweenTapsMs = ViewConfiguration.getDoubleTapTimeout();

    private MultiTapListener() {
        // NO!
        mRequestedTapCount = 2;
    }

    /**
     * Creates a new MultiTapListener in order to listen to multiple consiquent taps of a view
     *
     * @param counts The number of times to trigger the listener's {@link #onMultitapPerformed()}
     */
    public MultiTapListener(int counts) {
        this.mRequestedTapCount = counts;
    }

    /**
     * Sets the interval time between the consecutive taps
     *
     * @param intervalInMs The time in miliseconds
     */
    public void setTimeBetweenTaps(long intervalInMs) {
        this.mTimeBetweenTapsMs = intervalInMs;
    }

    @Override
    final public void onClick(View v) {

        long newTouchMs = System.currentTimeMillis();
        if (newTouchMs - mPreviousTouchMs > mTimeBetweenTapsMs) {
            if (DEBUG) {
                Log.w(TAG, "Resetting touches");
            }
            mTapCounts = 0;
        }

        mTapCounts++;
        if (DEBUG) {
            Log.d(TAG, "Count = " + mTapCounts);
        }
        mPreviousTouchMs = newTouchMs;

        if (hasPerformedMultiTap()) {
            onMultitapPerformed();
            mTapCounts = 0;
            mPreviousTouchMs = 0;
        } else {
            publishUpdate(mTapCounts);
        }

    }


    /**
     * Called whenever a tap has been performed on the view.
     *
     * @param tap
     */
    protected void publishUpdate(int tap) {
        // the default implementation does nothing
    }


    /**
     * Called when the user has performed the number of requested consequent taps needed.
     */
    protected abstract void onMultitapPerformed();

    /**
     * Returns whether the user has performed the number of consequent taps needed
     */
    final private boolean hasPerformedMultiTap() {
        return mTapCounts >= mRequestedTapCount;
    }
}
