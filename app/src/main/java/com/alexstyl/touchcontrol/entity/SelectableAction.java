package com.alexstyl.touchcontrol.entity;

import android.content.Context;

/**
 * <p>Created by alexstyl on 17/03/15.</p>
 */
public class SelectableAction {

    public static final int ID_CALL = 0;
    public static final int ID_CALL_CONTACT = 1;
    public static final int ID_LAUNCH_APP = 2;
    public static final int ID_NAVIGATE = 3;
    public static final int ID_CHECK_IN = 4;
    public static final int ID_VOLUME_UP = 5;
    public static final int ID_VOLUME_DOWN = 6;


    private int mID;
    private int mTitle;

    public SelectableAction(int actionID, int resString) {
        this.mID = actionID;
        this.mTitle = resString;
    }


    public int getActionID() {
        return mID;
    }

    public String getTitle(Context context) {
        return context.getString(mTitle);
    }


}
