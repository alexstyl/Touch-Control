package com.alexstyl.touchcontrol.ui.widget.listener;

import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Created by alexstyl on 22/03/15.</p>
 */
public class MultiTouchListener implements View.OnTouchListener {
    private List<View.OnTouchListener> mListeners = new ArrayList<>();


    public MultiTouchListener(View.OnTouchListener... listeners) {
        for (View.OnTouchListener l : listeners) {
            addListener(l);
        }
    }

    public void addListener(View.OnTouchListener l) {
        this.mListeners.add(l);
    }

    public void removeListener(View.OnTouchListener l) {
        this.mListeners.remove(l);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        for (View.OnTouchListener l : mListeners) {
            if (l.onTouch(v, event)) {
                return true;
            }
        }
        return false;
    }
}
