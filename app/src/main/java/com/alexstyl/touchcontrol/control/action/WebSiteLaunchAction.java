package com.alexstyl.touchcontrol.control.action;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.alexstyl.touchcontrol.R;

/**
 * <p>Created by alexstyl on 04/03/15.</p>
 */
public class WebSiteLaunchAction extends AbstractLaunchAppAction {
    private String mURL;

    public WebSiteLaunchAction(String url) {
        if (TextUtils.isEmpty(url)) {
            throw new IllegalArgumentException("URL cannot be empty");
        }
        this.mURL = url;
    }


    @Override
    protected Intent getLaunchingIntent(Context context) {
        Intent a = new Intent(Intent.ACTION_VIEW);
        a.setData(Uri.parse(mURL));
        return a;
    }

    @Override
    protected String getDataString(Context context) {
        //TODO display title of URL instead of the url itself
        return mURL;
    }

    @Override
    public String getActionString(Context context) {
        return context.getString(R.string.launches_website);
    }


}
