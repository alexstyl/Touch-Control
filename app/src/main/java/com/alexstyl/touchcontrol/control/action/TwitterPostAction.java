package com.alexstyl.touchcontrol.control.action;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.alexstyl.touchcontrol.R;

/**
 * An Action that, when fired, opens up the twitter app or browser in order to let the user tweet
 * <p>Created by alexstyl on 04/03/15.</p>
 */
public class TwitterPostAction extends AbstractLaunchAppAction {

    private static final String URL_TWEET = "https://twitter.com/intent/tweet";
    public static final String TWITTER = "Twitter";

    public TwitterPostAction() {
    }


    @Override
    public String getActionString(Context context) {
        return context.getString(R.string.checks_in);
    }

    @Override
    protected String getDataString(Context context) {
        return TWITTER;
    }

    @Override
    protected Intent getLaunchingIntent(Context context) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(URL_TWEET));
        return i;
    }
}
