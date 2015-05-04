package com.alexstyl.touchcontrol.ui.activity;

import android.os.Bundle;

import com.alexstyl.touchcontrol.R;
import com.alexstyl.touchcontrol.ui.BaseActivity;

/**
 * Activity that creates an activity for the user
 * <p>Created by alexstyl on 12/03/15.</p>
 */
public class CreateAGestureActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_gesture);
        setTitle(R.string.create_gesture);
    }
}
