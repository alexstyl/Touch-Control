package com.alexstyl.touchcontrol.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.alexstyl.commons.logging.DeLog;
import com.alexstyl.touchcontrol.TouchControl;
import com.alexstyl.touchcontrol.manager.CameraManager;

/**
 * Receiver that listens to device events such as the Screen has been turned on and off
 * <p>Created by alexstyl on 12/03/15.</p>
 */
public class DeviceEventReceiver extends BroadcastReceiver {
    private static final String TAG = "DeviceEventReceiver";
    public static final String ACTION_TORCH_OFF = TouchControl.PACKAGE + ".torch_off";
    public static final String ACTION_TORCH_ON = TouchControl.PACKAGE + ".torch_on";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        DeLog.d(TAG, "onReceive: " + action);

        if (ACTION_TORCH_OFF.equals(action)) {
            CameraManager.getInstance(context).turnOffFLash();
        } else if (ACTION_TORCH_ON.equals(action)) {
            CameraManager.getInstance(context).turnOnFLash();
        }
    }


}
