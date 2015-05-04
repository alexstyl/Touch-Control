package com.alexstyl.touchcontrol.manager;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;

import com.alexstyl.touchcontrol.utils.Notifier;

/**
 * Manager that handles the turning on and off the back facing camera's flash
 * <p>Created by alexstyl on 03/03/15.</p>
 */
public class CameraManager {

    private boolean mHasFlash;
    private Camera cam;
    private Context mContext;

    private CameraManager(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            this.mHasFlash = true;
            this.mContext = context.getApplicationContext();
        }
    }

    private static CameraManager sInstance;

    public static CameraManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new CameraManager(context);
        }
        return sInstance;
    }

    private boolean isFlashOn;

    public void toggleFlash() {
        if (isFlashOn = !isFlashOn) {
            internalTurnOnFlash();
        } else {
            internalTurnOffFlash();
        }
    }
    /**
     * Turns the flashlight off if it is turned on
     */
    public void turnOffFLash() {
        if (isFlashOn) {
            internalTurnOffFlash();
        }
    }

    /**
     * Turns the flashlight off if it is switched off
     */
    public void turnOnFLash() {
        if (!isFlashOn) {
            internalTurnOnFlash();
        }
    }


    private void internalTurnOffFlash() {
        try {
            if (mHasFlash) {
                cam.stopPreview();
                cam.release();
                cam = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Notifier.cancelForFlash(mContext);
        }
        isFlashOn = false;
    }

    private void internalTurnOnFlash() {
        try {
            if (mHasFlash) {
                cam = Camera.open();
                Camera.Parameters p = cam.getParameters();
                p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                cam.setParameters(p);
                cam.startPreview();
                Notifier.forFlash(mContext);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        isFlashOn = true;
    }

    public boolean isFlashOn() {
        return isFlashOn;
    }


}
