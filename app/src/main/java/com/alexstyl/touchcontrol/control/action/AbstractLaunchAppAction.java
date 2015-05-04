package com.alexstyl.touchcontrol.control.action;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.alexstyl.touchcontrol.R;

/**
 * Action that starts an app
 * <p>Created by alexstyl on 03/03/15.</p>
 */
abstract public class AbstractLaunchAppAction extends AbstractAction {


    public AbstractLaunchAppAction() {
        super();
    }


    @Override
    final protected boolean onRunExecuted(Context context) {

        Intent intent = getLaunchingIntent(context);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context,R.string.no_app_for_action,Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return false;
        }
        return true;

    }

    /**
     * Returns the intent of the app to launch
     *
     * @return
     * @param context
     */
    protected abstract Intent getLaunchingIntent(Context context);


    @Override
    public String getActionString(Context context) {
        return context.getString(R.string.launches_app);
    }

}
