package com.alexstyl.touchcontrol.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.alexstyl.touchcontrol.R;
import com.alexstyl.touchcontrol.TouchControl;
import com.alexstyl.touchcontrol.manager.GestureManager;
import com.alexstyl.touchcontrol.ui.BaseDialog;

/**
 * DialogFramgne that prompts the user if they want to really delete some gesture and then removes it from the database
 * <p>Created by alexstyl on 17/03/15.</p>
 */
public class GestureDeletionDialogFragment extends BaseDialog {


    private static final String KEY_GESTURE_NAME = TouchControl.PACKAGE + ".GESTURE_NAME";

    public static GestureDeletionDialogFragment createInstance(String gestureName) {
        GestureDeletionDialogFragment dialog = new GestureDeletionDialogFragment();
        Bundle args = new Bundle(1);
        args.putString(KEY_GESTURE_NAME, gestureName);
        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final String gestureName = getArguments().getString(KEY_GESTURE_NAME);

        Dialog mDialog = new AlertDialog.Builder(getActivity())
                .setMessage(R.string.delete_gesture_summary)
                .setTitle(getString(R.string.delete_gesture, gestureName))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (GestureManager.getInstance(getActivity()).removeGesture(gestureName)) {
                            Toast.makeText(getActivity(), getString(R.string.gesture_removed, gestureName), Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .create();
        return mDialog;
    }


}
