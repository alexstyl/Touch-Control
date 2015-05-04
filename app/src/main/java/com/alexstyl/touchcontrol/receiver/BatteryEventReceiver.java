package com.alexstyl.touchcontrol.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.support.v4.content.LocalBroadcastManager;

import com.alexstyl.touchcontrol.TouchControl;
import com.alexstyl.touchcontrol.service.OverlayService;

/**
 * Receiver that listens to events of the battery (such as battery is charged or is low
 * </br>As soon as the battery is low, it tells the OverlayService to turn off the sensors.
 * <p>Created by alexstyl on 17/03/15.</p>
 */
public class BatteryEventReceiver extends BroadcastReceiver {
    private static final float LOW_BATTERY_LEVEL = 0.15f;
    public static final int STATE_CONNECTED = 0;
    public static final int STATE_OK = 1;
    public static final int STATE_LOW = 2;
    public static final String ACTION_BATTERY_CHANGE = TouchControl.PACKAGE + ".ACTION_BATTERY_CHANGE";
    public static final String EXTRA_BATTERY_STATE = TouchControl.PACKAGE + ".state";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_BATTERY_LOW.equals(action)) {
            // disable physical gestures
            onLowBattery(context);
        } else if (Intent.ACTION_BATTERY_OKAY.equals(action)) {
            // battery is ok. we can start the gestures again
            onBatteryNormal(context);
        } else if (Intent.ACTION_POWER_CONNECTED.equals(action)) {
            // connected. turn on sensors
            onConnected(context);
        } else if (Intent.ACTION_POWER_DISCONNECTED.equals(action)) {
            // check if the battery is ok
            IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = context.registerReceiver(null, ifilter);

            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

            float batteryPct = level / (float) scale;
            if (batteryPct > LOW_BATTERY_LEVEL) {
                // we got enough battery to operate
                onBatteryNormal(context);
            } else {
                onLowBattery(context);
            }

        }

    }

    /**
     * Called when the device has started
     *
     * @param context The context to use
     */
    private void onConnected(Context context) {
        if (OverlayService.isEnabled(context) && !OverlayService.isRunning) {
            Intent i = new Intent(context.getApplicationContext(), OverlayService.class);
            context.startService(i);
        }
        sendBroadcast(context, STATE_CONNECTED);

    }

    private void onBatteryNormal(Context context) {
        if (OverlayService.isEnabled(context) && !OverlayService.isRunning) {
            Intent i = new Intent(context.getApplicationContext(), OverlayService.class);
            context.startService(i);

            sendBroadcast(context, STATE_OK);
        }
    }

    /**
     * Called when the battery is low (less than {@value #LOW_BATTERY_LEVEL})
     *
     * @param context
     */
    private void onLowBattery(Context context) {
        if (OverlayService.isRunning) {
            Intent i = new Intent(context.getApplicationContext(), OverlayService.class);
            context.startService(i);
        }
        sendBroadcast(context, STATE_LOW);
    }

    private void sendBroadcast(Context context, int state) {
        Intent i = new Intent(ACTION_BATTERY_CHANGE);
        i.putExtra(EXTRA_BATTERY_STATE, state);
        LocalBroadcastManager.getInstance(context).sendBroadcast(i);
    }
}
