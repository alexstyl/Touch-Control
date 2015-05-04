package com.alexstyl.touchcontrol.ui.widget.listener;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.alexstyl.touchcontrol.utils.Utils;

/**
 * Class that implements the {@linkplain android.view.View.OnTouchListener} interface that allows a view to be moved around
 * <p>Created by alexstyl on 09/03/15.</p>
 */
public class DragMeAroundTouchListener implements View.OnTouchListener {

    private static final int SIGNIFICATION_THRESHOLD = 5;
    private WindowManager mManager;
    private Configuration mConfig;
    Point mPoint = new Point();

    public DragMeAroundTouchListener(Context context) {
        this.mManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        this.mConfig = context.getResources().getConfiguration();

        calculateEdges(context);
    }

    private void calculateEdges(Context context) {
        this.mManager.getDefaultDisplay().getSize(mPoint);

        LEFT_EDGE = -(mPoint.x / 2);
        RIGHT_EDGE = -LEFT_EDGE;
        TOP_EDGE = Utils.getStatusBarHeight(context.getResources());
        BOTTOM_EDGE = mPoint.y - Utils.getNavigationBarHeight(context.getResources());
    }

    private int _xDelta;
    private int _yDelta;

    private int LEFT_EDGE;
    private int RIGHT_EDGE;
    private int TOP_EDGE;
    private int BOTTOM_EDGE;


    boolean onStartMoving;
    private int old_x;

    private int old_y;
    private int mWidth = -1;

    private int mHeight = -1;

    @Override
    final public boolean onTouch(View view, MotionEvent event) {
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();

        WindowManager.LayoutParams lParams = (WindowManager.LayoutParams) view.getLayoutParams();
        if (orientationChanged()) {
            calculateEdges(view.getContext());
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                _xDelta = X - lParams.x;
                _yDelta = Y - lParams.y;
                break;
            case MotionEvent.ACTION_UP:
                onStartMoving = false;
                onStopMoving();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                if (mWidth == -1) {
                    mWidth = view.getMeasuredWidth() / 2;
                    mHeight = view.getMeasuredHeight() / 2;
                }
                lParams.x = X - _xDelta;
                //clip out of bounds
                if (lParams.x + mWidth > RIGHT_EDGE) {
                    lParams.x = RIGHT_EDGE - mWidth;
                } else if (LEFT_EDGE > lParams.x - mWidth) {
                    lParams.x = LEFT_EDGE + mWidth;
                }

                lParams.y = Y - _yDelta;
                if (lParams.y + mHeight < TOP_EDGE) {
                    lParams.y = 0;
                } else if (lParams.y + mHeight > BOTTOM_EDGE) {
                    lParams.y = BOTTOM_EDGE - mHeight;
                }

                view.setLayoutParams(lParams);
                if (!onStartMoving && significantMovement(lParams.x, lParams.y)) {
                    onStartMoving = true;
                    onStartMoving();
                }
                old_x = lParams.x;
                old_y = lParams.y;

                break;
        }
        mManager.updateViewLayout(view, lParams);

        return false;
    }

    int mPreviousOrientation = -1;

    private boolean orientationChanged() {
        if (mPreviousOrientation != mConfig.orientation) {
            mPreviousOrientation = mConfig.orientation;

            return true;

        }
        return false;
    }

    private boolean significantMovement(int x, int y) {
        return Math.abs(x - old_x) > SIGNIFICATION_THRESHOLD ||
                Math.abs(y - old_y) > SIGNIFICATION_THRESHOLD;
    }


    /**
     * Called when the View has stopped moving.
     */
    protected void onStopMoving() {
    }

    /**
     * Called when the View has just started moving.
     */
    protected void onStartMoving() {
    }


}
