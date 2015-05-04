package com.alexstyl.touchcontrol.control.action;

import android.content.Context;

import java.io.Serializable;

/**
 * <p>Created by alexstyl on 03/03/15.</p>
 */
abstract public class AbstractAction implements Serializable {


    public AbstractAction() {
    }


    /**
     * Executes the action.
     */
    public boolean run(Context context) {
        boolean successed = onRunExecuted(context);

        return successed;

    }

    /**
     * Called when the run() method of the Action has been called
     *
     * @param context The context we are running on
     * @return
     */
    abstract protected boolean onRunExecuted(Context context);

    /**
     * Returns the label of this Action.
     *
     * @param context The contet to use
     * @return The label of this action
     */
    final public String getLabelString(Context context) {
        String verb = getActionString(context);
        String data = getDataString(context);
        return String.format(verb, data);
    }

    /**
     * Returns the data the action is performed on</br>
     * i.e. The number to call, or the point to display
     * @param context The context to use
     * @return
     */
    protected abstract String getDataString(Context context);

    /**
     * The verb of the action</br>
     * calls, or points, or checks-in
     * @param context
     * @return
     */
    protected abstract String getActionString(Context context);


}
