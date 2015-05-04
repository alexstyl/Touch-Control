package com.alexstyl.touchcontrol.control.action;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.alexstyl.touchcontrol.R;

/**
 * <p>Created by alexstyl on 03/03/15.</p>
 */
public class CallAction extends AbstractLaunchAppAction {
    private String mNumber;

    public CallAction(String number) {
        super();
        this.mNumber = number;
        if (TextUtils.isEmpty(mNumber)) {
            throw new IllegalArgumentException("CallAction must provide a number");
        }
    }

    @Override
    protected Intent getLaunchingIntent(Context context) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + mNumber));
        return intent;
    }


    @Override
    public String getActionString(Context context) {
        return context.getString(R.string.calls);
    }

    @Override
    protected String getDataString(Context context) {
        return mNumber;
    }


}
