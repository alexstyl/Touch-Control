package com.alexstyl.touchcontrol.control.action;

import android.content.Context;
import android.media.AudioManager;

import com.alexstyl.touchcontrol.R;

/**
 * Action that turns the volume up
 * <p>Created by alexstyl on 03/03/15.</p>
 */
public class VolumeUpAction extends AbstractAction {


    public VolumeUpAction() {
        super();
    }

    @Override
    protected boolean onRunExecuted(Context context) {
        AudioManager mManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mManager.setStreamVolume(getStream(), mManager.getStreamVolume(getStream()) + 1, AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
        return true;
    }

    @Override
    protected String getDataString(Context context) {
        return null;
    }

    @Override
    public String getActionString(Context context) {
        return context.getString(R.string.increases_volume);

    }

    private int getStream() {
        return AudioManager.STREAM_RING;
    }
}
