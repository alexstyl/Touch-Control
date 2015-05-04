package com.alexstyl.touchcontrol.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;

/**
 * <p>Created by alexstyl on 03/03/15.</p>
 */
public class CameraUtils {


    private CameraUtils() {
    }


    public static boolean isFlashAvailable(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }


    public static void open(Context context) {
        Camera cam = Camera.open();
        Camera.Parameters p = cam.getParameters();
        p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        cam.setParameters(p);
        cam.startPreview();

    }

    public static void close(Context context) {
        Camera cam = Camera.open();
        cam.stopPreview();
        cam.release();

    }

}
