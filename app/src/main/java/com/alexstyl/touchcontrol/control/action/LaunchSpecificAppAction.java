package com.alexstyl.touchcontrol.control.action;

import android.content.Context;
import android.content.Intent;

import com.alexstyl.touchcontrol.entity.AppInfo;

/**
 * <p>Created by alexstyl on 18/03/15.</p>
 */
public class LaunchSpecificAppAction extends AbstractLaunchAppAction {

    private String packageName;
    private String appName;

    public LaunchSpecificAppAction(AppInfo selectedApp) {
        this.packageName = selectedApp.getPackageName();
        this.appName = selectedApp.getAppName();

    }

    @Override
    protected Intent getLaunchingIntent(Context context) {
        return context.getPackageManager().getLaunchIntentForPackage(packageName);
    }

    @Override
    protected String getDataString(Context context) {
        return appName;
    }
}
