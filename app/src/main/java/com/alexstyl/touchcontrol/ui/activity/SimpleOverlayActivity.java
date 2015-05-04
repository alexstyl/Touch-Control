package com.alexstyl.touchcontrol.ui.activity;

import android.content.Context;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.alexstyl.commons.logging.DeLog;
import com.alexstyl.touchcontrol.BuildConfig;
import com.alexstyl.touchcontrol.R;
import com.alexstyl.touchcontrol.control.action.AbstractAction;
import com.alexstyl.touchcontrol.manager.GestureManager;

import java.util.ArrayList;

/**
 * <p>Created by alexstyl on 09/03/15.</p>
 */
public class SimpleOverlayActivity extends OverlayActivity {

    private static final String TAG = "SimpleOverlayActivity";
    private static final boolean DEBUG = BuildConfig.DEBUG;
    private GestureOverlayView mOverlay;
    private GestureLibrary mLibrary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overlay_new);
        blurBackground();
        mOverlay = (GestureOverlayView) findViewById(R.id.gesture_overlay);

        if (mLibrary == null) {
            mLibrary = GestureLibraries.fromFile(GestureManager.TOUCH_GESTURES_FILE);
        }
        if (!mLibrary.load()) {
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mOverlay.addOnGesturePerformedListener(mGesturePerformedListener);
    }

    private void blurBackground() {
        Window mWindow = getWindow();
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        lp.dimAmount = 0.6f;
        mWindow.setAttributes(lp);
    }

    private GestureOverlayView.OnGesturePerformedListener mGesturePerformedListener = new GestureOverlayView.OnGesturePerformedListener() {

        public static final int ALLOWED_GESTURE_TRIES = 2;

        private int mTryCount = 1;
        private boolean mSuccess = false;

        @Override
        public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
            ArrayList<Prediction> predictions = mLibrary.recognize(gesture);
            if (predictions.isEmpty()) {
                DeLog.d(TAG, "No predictions");
                return;
            }
            Context context = SimpleOverlayActivity.this;

            for (Prediction prediction : predictions) {

                if (prediction.score > 2.0) {

                    AbstractAction action = GestureManager.getInstance(context).getActionOfGesture(prediction.name);
                    if (action != null) {
                        if (DEBUG) {
                            Log.d(TAG, "Deleted gesture: " + prediction.name + " :: " + prediction.score);
                        }
                        Toast.makeText(context, prediction.name, Toast.LENGTH_SHORT).show();
                        onGestureDetected(action);
                        mSuccess = true;
                        break;
                    }
                }

            }
            if (!mSuccess) {
                Toast.makeText(SimpleOverlayActivity.this, R.string.unrecognised_gesture, Toast.LENGTH_SHORT).show();
            }
            mTryCount++;
            if (mSuccess || mTryCount > ALLOWED_GESTURE_TRIES) {
                mTryCount = 1;
                mSuccess = false;
                onGestureFailed();
            }
        }


        private void onGestureDetected(AbstractAction action) {
            GestureManager.getInstance(SimpleOverlayActivity.this).onCommandRecognised(SimpleOverlayActivity.this);
            action.run(SimpleOverlayActivity.this);
        }

        /**
         * Called whenever the user has finished all of their tries to perform a gesture
         */
        private void onGestureFailed() {
            if (!BuildConfig.DEBUG) {
                GestureManager.getInstance(SimpleOverlayActivity.this).onCommandUnrecognised(SimpleOverlayActivity.this);
            }
            finish();

        }

    };
}