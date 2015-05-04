package com.alexstyl.touchcontrol.ui.loader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;

import com.alexstyl.commons.logging.DeLog;
import com.alexstyl.commons.ui.loader.SimpleAsyncTaskLoader;
import com.alexstyl.touchcontrol.R;
import com.alexstyl.touchcontrol.entity.NamedGesture;
import com.alexstyl.touchcontrol.manager.GestureManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Loader that loads all the saved TouchGestures
 * <p>Created by alexstyl on 08/03/15.</p>
 */
public class TouchGesturesLoader extends SimpleAsyncTaskLoader<List<NamedGesture>> {

    private static final String TAG = TouchGesturesLoader.class.getSimpleName();
    private TouchGesturesChangedListener mListener;

    private final int mHeight;
    private final int mWidth;
    private int mColor;
    private int mInset;

    public TouchGesturesLoader(Context context) {
        super(context);
        this.mLibrary = GestureLibraries.fromFile(GestureManager.TOUCH_GESTURES_FILE);
        this.mListener = new TouchGesturesChangedListener();

        this.mWidth = context.getResources().getDimensionPixelSize(R.dimen.gesture_icon_width);
        this.mHeight = context.getResources().getDimensionPixelSize(R.dimen.gesture_icon_height);
        this.mColor = context.getResources().getColor(R.color.accent);
        this.mInset = context.getResources().getDimensionPixelOffset(R.dimen.gesture_thumbnail_inset);
    }

    @Override
    protected void onAbandon() {
        super.onAbandon();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(this.mListener);

    }

    private GestureLibrary mLibrary;


    @Override
    public List<NamedGesture> loadInBackground() {
        DeLog.i(TAG, "onLoadInBackground called");
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            DeLog.e(TAG, "Media not mounted");
            return null;
        }

        List<NamedGesture> mGestures = null;
        if (mLibrary.load()) {
            mGestures = new ArrayList<>();
            for (String name : mLibrary.getGestureEntries()) {
                for (Gesture gesture : mLibrary.getGestures(name)) {
                    final NamedGesture namedGesture = new NamedGesture(gesture, name);
                    // TODO For performance and memory efficiency, the thumbnails need to be loaded off the Loader
                    // it was done this way for simplicity reason
                    Bitmap thumbnail = gesture.toBitmap(mWidth, mHeight, mInset, mColor);
                    namedGesture.setThumbnail(thumbnail);
                    mGestures.add(namedGesture);
                }
            }
        }
        Collections.sort(mGestures);
        return mGestures;
    }

    private class TouchGesturesChangedListener extends BroadcastReceiver {

        TouchGesturesChangedListener() {
            IntentFilter intentFilter = new IntentFilter(GestureManager.ACTION_NEW_GESTURE);
            LocalBroadcastManager.getInstance(getContext()).registerReceiver(this, intentFilter);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            DeLog.d(TAG, "onReceive");
            onContentChanged();
        }
    }

    @Override
    protected void releaseResources(List<NamedGesture> oldData) {
        for (NamedGesture gesture : oldData) {
            gesture.clearThumbnail();
        }
    }
}
