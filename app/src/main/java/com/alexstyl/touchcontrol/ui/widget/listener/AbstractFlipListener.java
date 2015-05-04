package com.alexstyl.touchcontrol.ui.widget.listener;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

/**
 * SensorEventListener that detects when the device has been flipped.
 * <p>Created by alexstyl on 09/03/15.</p>
 *
 * @see <a href="http://stackoverflow.com/a/22952085/1315110">Stackoverflow.com answer</a>
 */
public abstract class AbstractFlipListener implements SensorEventListener {
    private float mGZ = 0;//gravity acceleration along the z axis
    private int mEventCountSinceGZChanged = 0;
    private static final int MAX_COUNT_GZ_CHANGE = 10;

    @Override
    public void onSensorChanged(SensorEvent event) {
        int type = event.sensor.getType();
        if (type == Sensor.TYPE_ACCELEROMETER) {
            float gz = event.values[2];
            if (mGZ == 0) {
                mGZ = gz;
            } else {
                if ((mGZ * gz) < 0) {
                    mEventCountSinceGZChanged++;
                    if (mEventCountSinceGZChanged == MAX_COUNT_GZ_CHANGE) {
                        mGZ = gz;
                        mEventCountSinceGZChanged = 0;
                        if (gz > 0) {
                            onFlipUp();
                        } else if (gz < 0) {
                            onFlipDown();
                        }
                    }
                } else {
                    if (mEventCountSinceGZChanged > 0) {
                        mGZ = gz;
                        mEventCountSinceGZChanged = 0;
                    }
                }
            }
        }
    }

    /**
     * Called when the device has been flipped down
     */
    abstract protected void onFlipDown();

    /**
     * Called when the device has been flipped up
     */
    abstract protected void onFlipUp();

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // do nothing
    }
}
