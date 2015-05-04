package com.alexstyl.touchcontrol.ui.widget.listener;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.text.format.DateUtils;

/**
 * Class that implements the SensorEventListener interface. When the user shakes the device, the class's {@linkplain #onDeviceShaken(float)}
 * <p>Created by alexstyl on 03/03/15.</p>
 */
abstract public class ShakeListener implements SensorEventListener {
    private float mShakeSpeed = 5000;
    private long mTimeBetweenIntervals = 2 * DateUtils.SECOND_IN_MILLIS;

    // For shake motion detection.
    private long lastUpdate = -1;
    private float x, y, z;
    private float last_x, last_y, last_z;


    private long previousTrigger;

    public ShakeListener(long timeThreshold) {
        this.mTimeBetweenIntervals = timeThreshold;
    }

    /**
     * Sets the amount of speed the device needs to be shaken, in order for {@linkplain #onDeviceShaken(float)} to be triggered
     *
     * @param speed
     */
    public void setShakeSpeed(float speed) {
        this.mShakeSpeed = speed;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long curTime = System.currentTimeMillis();

            if (curTime - previousTrigger < mTimeBetweenIntervals) {
                return;
            }


            // only allow one update every 100ms.
            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float[] values = new float[3];
                System.arraycopy(event.values, 0, values, 0, 3);
                x = values[SensorManager.DATA_X];
                y = values[SensorManager.DATA_Y];
                z = values[SensorManager.DATA_Z];


                float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;
                if (speed > mShakeSpeed) {
                    previousTrigger = curTime;
                    onDeviceShaken(speed);
                }
                last_x = x;
                last_y = y;
                last_z = z;
            }
        }


    }

    protected abstract void onDeviceShaken(float speed);

}
