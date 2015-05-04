package com.alexstyl.touchcontrol.ui.widget.listener;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

/**
 * A light sensor that detects when it's complete darkness
 * <p>Created by alexstyl on 24/03/15.</p>
 */
public class DarknessDetectorSensor implements SensorEventListener {

    private static final String TAG = "Dark";

    public DarknessDetectorSensor() {
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private float mPreviousLux;
    private long mPreviousOccurance;

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        float mLux;

        if (sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT) {

            mLux = sensorEvent.values[0];
            if (mLux == 0 && mPreviousLux != 0) {
                onDarknessDetected();
            } else {
                onDarknessGone();
            }
            this.mPreviousLux = mLux;


        }
    }

    /**
     * Called when the sensor realises it' dark.
     */
    protected void onDarknessDetected() {
    }

    /**
     * Called when the sensor realised it's light again
     */
    protected void onDarknessGone() {
    }

}
