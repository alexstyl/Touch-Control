package com.alexstyl.touchcontrol.control.action;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

/**
 * <p>Created by alexstyl on 03/03/15.</p>
 */
public class PhoneLaunchAction extends AbstractLaunchAppAction {

    private String mNumber;

    public PhoneLaunchAction(String number) {
        this.mNumber = number;
    }


    @Override
    protected Intent getLaunchingIntent(Context context) {

        Intent intent = new Intent(Intent.ACTION_DIAL);
        if (!TextUtils.isEmpty(mNumber)) {
            String uri = "tel:" + mNumber.trim();
            intent.setData(Uri.parse(uri));
        }
        return intent;
    }


    @Override
    protected String getDataString(Context context) {
        return null;
    }
}
