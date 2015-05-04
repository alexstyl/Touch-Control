package com.alexstyl.touchcontrol.entity;

import android.graphics.drawable.Drawable;

import com.alexstyl.touchcontrol.utils.Utils;

import java.io.Serializable;
import java.util.Comparator;

/**
 * <p>Created by alexstyl on 17/03/15.</p>
 */
public class AppInfo implements Serializable {

    private String appName;
    private String packageName;
    private String versionName;
    private int versionCode;
    private Drawable iconDrawable;

    @Override
    public boolean equals(Object o) {
        AppInfo other = Utils.as(AppInfo.class, o);
        if (other == null)
            return false;

        return packageName.equals(other.packageName)
                && appName.equals(other.appName);
    }

    @Override
    public int hashCode() {
        return appName.hashCode() + packageName.hashCode();
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppName() {
        return appName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setIconDrawable(Drawable iconDrawable) {
        this.iconDrawable = iconDrawable;
    }

    public Drawable getIconDrawable() {
        return iconDrawable;
    }


    static public class AppNameComparator implements Comparator<AppInfo> {

        @Override
        public int compare(AppInfo lhs, AppInfo rhs) {
            return lhs.getAppName().compareTo(rhs.getAppName());
        }

    }
}
