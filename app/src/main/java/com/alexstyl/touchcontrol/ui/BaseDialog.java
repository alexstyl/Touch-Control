package com.alexstyl.touchcontrol.ui;

import android.app.Activity;
import android.support.v4.app.DialogFragment;

/**
 * <p>Created by alexstyl on 17/03/15.</p>
 */
public class BaseDialog extends DialogFragment {


    private BaseActivity mActivity;

    public BaseActivity getBaseActivity() {
        return mActivity;
    }

    @Override
    public void onAttach(Activity activity) {
        try {
            this.mActivity = (BaseActivity) activity;
        } catch (ClassCastException ex) {
            throw new RuntimeException("BaseDialog needs to be attached to a BaseActivity");
        }
        super.onAttach(activity);
    }
}
