package com.alexstyl.touchcontrol.control.action;

import android.content.Context;

import com.alexstyl.touchcontrol.R;

/**
 * <p>Created by alexstyl on 04/03/15.</p>
 */
public class FoursquareCheckinAction extends WebSiteLaunchAction {
    public static final String FOURSQUARE = "Foursquare";

    public FoursquareCheckinAction() {
        super("https://foursquare.com/here");
    }


    @Override
    public String getActionString(Context context) {
        return context.getString(R.string.checks_in,FOURSQUARE);
    }
}
