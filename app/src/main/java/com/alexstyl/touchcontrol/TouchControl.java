package com.alexstyl.touchcontrol;

import android.app.Application;

import com.alexstyl.commons.logging.DeLog;
import com.alexstyl.touchcontrol.manager.GestureManager;
import com.alexstyl.touchcontrol.service.OverlayService;

/**
 * <p>Created by alexstyl on 09/03/15.</p>
 */
public class TouchControl extends Application {

    private static final String APP_NAME = "Touch Control";
    public static final String PACKAGE = "com.alexstyl.touchcontrol";


    private static TouchControl mControl; // it's okay to keep an instance of the Application object. As long as the app is running
    // The garbage collector isn't going to try to collect it

    @Override
    public void onCreate() {
        super.onCreate();
        DeLog.setLogging(BuildConfig.DEBUG);
        mControl = this;
        if (BuildConfig.DEBUG) {
            OverlayService.triggerService(this, true);
        }
        GestureManager.getInstance(this).init();
    }

    private boolean mIsAppVisible;

    public boolean isAppVisible() {
        return mIsAppVisible;
    }

    public void setVisible(boolean isVisible) {
        mIsAppVisible = isVisible;
    }

    /**
     * Returns the instance of the Application Object.
     * </br><b>Do NOT keep an Instance of this object unless you know how to use it.</b>
     *
     * @return
     */
    public static TouchControl getInstance() {
        return mControl;
    }

}
