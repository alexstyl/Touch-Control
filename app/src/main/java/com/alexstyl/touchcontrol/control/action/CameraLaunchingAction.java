package com.alexstyl.touchcontrol.control.action;

import android.content.Context;
import android.content.Intent;

import com.alexstyl.touchcontrol.R;

/**
 * Action that opens the devices camera
 * <p>Created by alexstyl on 03/03/15.</p>
 */
public class CameraLaunchingAction extends AbstractLaunchAppAction {

    public CameraLaunchingAction(Context context) {
        super();
    }

    @Override
    protected Intent getLaunchingIntent(Context context) {
        Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        return i;
    }

    @Override
    protected String getDataString(Context context) {
        return context.getString(R.string.camera);
    }


}
