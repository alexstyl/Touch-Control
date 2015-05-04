package com.alexstyl.touchcontrol.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;

import com.alexstyl.commons.logging.DeLog;
import com.alexstyl.touchcontrol.R;
import com.alexstyl.touchcontrol.ui.animation.AlphaAnimationListner;
import com.alexstyl.touchcontrol.utils.Notifier;

/**
 * Service that runs in foreground and creates a window that overlays other applications
 * <p>Created by alexstyl on 07/03/15.</p>
 */
abstract public class AbstractOverlayService extends Service {


    /**
     * Called when the overlaying view the service needs to be created
     */
    protected abstract View onCreateOverlayingView();


    private View mView;
    private int mOverlayVisibility = View.VISIBLE;
    private WindowManager windowManager;
    public static final String TAG = "AbstractOverlayService";
    private Animation mEntranceAnimation = new AlphaAnimationListner(0, 1) {

        @Override
        public void onAnimationStart(Animation animation) {
            DeLog.d(TAG, "animation started");
            mView.setVisibility(View.VISIBLE);
        }

    };
    private Animation mExitAnimation = new AlphaAnimationListner(1, 0) {
        @Override
        public void onAnimationEnd(Animation animation) {
            DeLog.d(TAG, "animation ended");
            mView.setVisibility(View.GONE);
        }
    };

    /**
     * Sets the visibility of the service's overlay view
     */
    protected void setOverlayVisibility(boolean show) {
        int visibility = (show ? View.VISIBLE : View.GONE);
        if (mView != null) {
//            if (show) {
//                DeLog.d(TAG,"Showing View");
//                mView.startAnimation(mEntranceAnimation);
//            } else {
//                DeLog.d(TAG,"Hiding View");
//                mView.startAnimation(mExitAnimation);
//            }
            mView.setVisibility(visibility);
        }

        mOverlayVisibility = visibility;

    }


    @Override
    public void onCreate() {
        super.onCreate();

        mView = onCreateOverlayingView();
        if (mView == null) {
            Log.e(getClass().getSimpleName(), "-- Overlaying view was empty--");
            stopSelf();
            return;
        }
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);


        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);

        params.windowAnimations = R.style.Overlay;

        params.gravity = getWindowGravity();
        mView.setVisibility(mOverlayVisibility);
        windowManager.addView(mView, params);
        moveToForeground();

    }

    private int NOTIFICATION_FOREGROUND_ID = 1032;

    protected void moveToForeground() {
        startForeground(NOTIFICATION_FOREGROUND_ID, Notifier.createServiceNotification(this));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mView != null) windowManager.removeView(mView);
    }

    protected int getWindowGravity() {
        return Gravity.CLIP_HORIZONTAL | Gravity.TOP;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Returns the Overlay view of the service
     */
    protected View getOverlayView() {
        return mView;
    }

}
