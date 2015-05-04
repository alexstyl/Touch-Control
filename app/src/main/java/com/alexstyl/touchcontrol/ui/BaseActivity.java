package com.alexstyl.touchcontrol.ui;

import android.os.Handler;
import android.support.v7.app.ActionBarActivity;

import com.alexstyl.touchcontrol.TouchControl;
import com.alexstyl.touchcontrol.service.OverlayService;

/**
 * Base class that extends {@linkplain android.support.v7.app.ActionBarActivity} that adds additional functionality to the activity.
 * </br> Currently the Activity reports to the {@linkplain com.alexstyl.touchcontrol.TouchControl} if the activity is on the foreground or background, and
 * requests the {@linkplain com.alexstyl.touchcontrol.service.OverlayService} to move to foreground/background
 * <p>Created by alexstyl on 03/03/15.</p>
 */
public class BaseActivity extends ActionBarActivity {

    private static final long DELAY_THRESHOLD = 350l;
    private boolean isActivityVisible = false;
    private static Handler mHandler = new Handler();


    private Runnable mRunnable = new Runnable() {

        @Override
        public void run() {


            if (OverlayService.isRunning) {

                OverlayService.moveService(TouchControl.getInstance(), OverlayService.MOVE_FOREGROUND);
                if (TouchControl.getInstance().isAppVisible()) {
                    OverlayService.hideOverlay(TouchControl.getInstance());
                } else {
                    OverlayService.showOverlay(TouchControl.getInstance());
                }
            }

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        this.isActivityVisible = true;
        if (reportsVisibility()) {
            TouchControl.getInstance().setVisible(isActivityVisible);

            mHandler.removeCallbacksAndMessages(null);
            mHandler.postDelayed(mRunnable, DELAY_THRESHOLD);

        }
    }

    @Override
    protected void onPause() {
        super.onStop();
        this.isActivityVisible = false;
        if (reportsVisibility()) {
            TouchControl.getInstance().setVisible(isActivityVisible);

            mHandler.removeCallbacksAndMessages(null);
            mHandler.postDelayed(mRunnable, DELAY_THRESHOLD);
        }

    }

    /**
     * Returns whether this activity should count in the overall "Application is moving to the foreground"
     * event
     */
    protected boolean reportsVisibility() {
        return true;
    }


}
