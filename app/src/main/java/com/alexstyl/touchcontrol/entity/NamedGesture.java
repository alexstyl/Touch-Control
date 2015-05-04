package com.alexstyl.touchcontrol.entity;

import android.content.Context;
import android.gesture.Gesture;
import android.graphics.Bitmap;

import com.alexstyl.touchcontrol.control.action.AbstractAction;
import com.alexstyl.touchcontrol.manager.GestureManager;

/**
 * <p>Created by alexstyl on 08/03/15.</p>
 */
public class NamedGesture implements Comparable<NamedGesture> {

    private Gesture mGesture;
    private String mName;
    private Bitmap mThumbnail;

    public NamedGesture(Gesture gesture, String name) {
        this.mGesture = gesture;
        this.mName = name;
    }

    @Override
    public String toString() {
        return mName;
    }

    public AbstractAction getLinkedAction(Context context) {
        return GestureManager.getInstance(context).getActionOfGesture(mName);
    }

    public Gesture getGesture() {
        return mGesture;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.mThumbnail = thumbnail;
    }

    public void clearThumbnail() {
        if (mThumbnail != null)
            mThumbnail.recycle();
    }

    public Bitmap getThumbnail() {
        return mThumbnail;
    }

    @Override
    public int compareTo(NamedGesture another) {
        return mName.compareTo(another.mName);
    }
}
