package com.alexstyl.touchcontrol.ui.animation;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

/**
 * <p>Created by alexstyl on 10/03/15.</p>
 */
abstract public class AlphaAnimationListner extends AlphaAnimation implements Animation.AnimationListener {


    public AlphaAnimationListner(float from, float to) {
        super(from, to);
        setAnimationListener(this);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
