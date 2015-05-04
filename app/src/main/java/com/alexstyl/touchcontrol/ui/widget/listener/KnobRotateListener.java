package com.alexstyl.touchcontrol.ui.widget.listener;

import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;

import com.alexstyl.commons.logging.DeLog;

/**
 * <p>Created by alexstyl on 22/03/15.</p>
 */
public class KnobRotateListener implements View.OnTouchListener {

    private static final String TAG = "Knob";
    private Point mPrimaryPoint = new Point();
    private Point mSecondaryPoint = new Point();


    private Point mCenterPoint = new Point();

    public KnobRotateListener() {
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                mPrimaryPoint.x = (int) event.getX();
                mPrimaryPoint.y = (int) event.getY();
                DeLog.d(TAG, "Setting main " + mPrimaryPoint);
            }
            break;
            case MotionEvent.ACTION_POINTER_DOWN: {
                int pointerIndex = event.getActionIndex();
                if (pointerIndex == 1) {
                    DeLog.d(TAG, "Setting index 1");
                    int pointerID = event.getPointerId(pointerIndex);
                    mSecondaryPoint.x = (int) event.getX();
                    mSecondaryPoint.y = (int) event.getY();
                    DeLog.d(TAG, "Setting index 1: " + mSecondaryPoint);

                    //
                    mCenterPoint.x = (mPrimaryPoint.x + mSecondaryPoint.x) / 2;
                    mCenterPoint.y = (mPrimaryPoint.y + mSecondaryPoint.y) / 2;
                    DeLog.d(TAG, "Center:: " + mCenterPoint);


                }

            }
            break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP: {
                int pointerIndex = event.getActionIndex();
                int pointerID = event.getPointerId(pointerIndex);
            }
            break;

            case MotionEvent.ACTION_MOVE: {
                int pointCount = event.getPointerCount();
                if (pointCount != 2) {
                    //
                    for (int idx = 0; idx < pointCount; idx++) {
                        int ID = event.getPointerId(idx);

                    }
                }
            }
            break;

        }
        return false;
    }

    protected void onClockwiseRotate() {

    }

    private boolean isLeft(Point centerPoint,
                           Point previousPoint,
                           Point currentPoint) {
        return ((previousPoint.x - centerPoint.x) * (currentPoint.y - centerPoint.y) -
                (previousPoint.y - centerPoint.y) * (currentPoint.x - centerPoint.x)) > 0;
    }
}
