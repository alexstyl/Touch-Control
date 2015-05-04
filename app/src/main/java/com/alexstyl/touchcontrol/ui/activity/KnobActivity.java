package com.alexstyl.touchcontrol.ui.activity;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.alexstyl.touchcontrol.control.action.VolumeDownAction;
import com.alexstyl.touchcontrol.control.action.VolumeUpAction;
import com.alexstyl.touchcontrol.ui.BaseActivity;
import com.alexstyl.touchcontrol.ui.widget.listener.KnobRotateListener;

/**
 * <p>Created by alexstyl on 22/03/15.</p>
 */
public class KnobActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RelativeLayout view = new RelativeLayout(this);
        view.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setContentView(view);

        final VolumeDownAction downVol = new VolumeDownAction();
        VolumeUpAction upVol = new VolumeUpAction();

        view.setOnTouchListener(new KnobRotateListener() {

            @Override
            protected void onClockwiseRotate() {
                downVol.run(KnobActivity.this);
            }
        });
    }
}
