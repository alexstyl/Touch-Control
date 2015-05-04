package com.alexstyl.touchcontrol.control.action;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.alexstyl.touchcontrol.R;

/**
 * <p>Created by alexstyl on 04/03/15.</p>
 */
public class NavigateToPointAction extends AbstractLaunchAppAction {

    private String mLocation;

    public NavigateToPointAction(String location) {
        super();
        if (TextUtils.isEmpty(location)) {
            throw new IllegalArgumentException("NavigateToPointAction must have a destination location");
        }
        this.mLocation = location.replace(' ', '+');
    }

    @Override
    protected Intent getLaunchingIntent(Context context) {
        Uri gmmUri = Uri.parse("geo:0,0?q=" + mLocation);
        Intent mapSearchIntent = new Intent(
                android.content.Intent.ACTION_VIEW,
                gmmUri);
//        mapSearchIntent.setPackage("com.google.android.apps.maps");

        return mapSearchIntent;
    }


    @Override
    protected String getDataString(Context context) {
        return context.getString(R.string.navigation);
    }
}
