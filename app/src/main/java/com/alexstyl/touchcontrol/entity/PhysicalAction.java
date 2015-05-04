package com.alexstyl.touchcontrol.entity;

/**
 * <p>Created by alexstyl on 25/03/15.</p>
 */
public class PhysicalAction {

    private int id;
    private int mTitle;
    private int mSummary;
    private boolean isBatteryDependant;
    private boolean mChecked;

    public PhysicalAction(int id, int resTitle, int resSummary, boolean enabled, boolean batteryDependant) {
        this.id = id;
        this.mTitle = resTitle;
        this.mSummary = resSummary;
        this.mChecked = enabled;
        this.isBatteryDependant = batteryDependant;
    }


    public int getTitleRes() {
        return mTitle;
    }

    public int getId() {
        return id;
    }

    public int getSummary() {
        return mSummary;
    }

    public boolean isBatteryDependant() {
        return isBatteryDependant;
    }

    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean isChecked) {
        this.mChecked = isChecked;
    }
}
