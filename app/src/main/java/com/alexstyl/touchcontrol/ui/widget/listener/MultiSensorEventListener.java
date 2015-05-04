package com.alexstyl.touchcontrol.ui.widget.listener;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@linkplain android.hardware.SensorEventListener} that can hold multiple SensorEventListeners and forwards the {@linkplain #onSensorChanged(android.hardware.SensorEvent)}
 * and {@linkplain #onAccuracyChanged(android.hardware.Sensor, int)} callbacks to all of them
 * <p>Created by alexstyl on 09/03/15.</p>
 */
public class MultiSensorEventListener implements SensorEventListener {


    private List<SensorEventListener> mListeners = new ArrayList<>();

    public MultiSensorEventListener() {
    }

    public MultiSensorEventListener(List<SensorEventListener> listeners) {
        this.mListeners.addAll(listeners);
    }


    public void addSensorListener(SensorEventListener l) {
        this.mListeners.add(l);
    }

    public void removeSensorListener(SensorEventListener l) {
        this.mListeners.remove(l);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        for (SensorEventListener l : mListeners) {
            l.onSensorChanged(event);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        for (SensorEventListener l : mListeners) {
            l.onAccuracyChanged(sensor, accuracy);
        }
    }

    /**
     * Removes all listeners from the Sensor
     */
    public void removeAllListeners() {
        this.mListeners.clear();
    }
}
