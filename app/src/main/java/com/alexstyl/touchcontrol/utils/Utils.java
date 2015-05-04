package com.alexstyl.touchcontrol.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import com.alexstyl.commons.logging.DeLog;

/**
 * <p>Created by alexstyl on 04/03/15.</p>
 */
public class Utils {

    private static final String TAG = "Utils";

    /**
     * Emulate the as operator of C#. If the object can be cast to type it will
     * be casted. If not this returns null.
     */
    public static <T> T as(Class<T> type, Object o) {
        if (type.isInstance(o)) {
            return type.cast(o);
        }
        return null;
    }

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static Intent getEmailIntent(String to, String subject, String text) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", to, null));
        if (!TextUtils.isEmpty(subject)) {
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        }
        if (!TextUtils.isEmpty(text)) {
            emailIntent.putExtra(Intent.EXTRA_TEXT, text);
        }
        return emailIntent;
    }

    /**
     * Mutes the notification sounds of the device
     *
     * @param context The context to use
     */
    public static void muteNotifications(Context context) {
        AudioManager manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        manager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
        DeLog.d(TAG, "Muting notifications");
    }

    /**
     * Unmutes the nofication sounds of the device
     *
     * @param context The context to use
     */
    public static void unmuteNotifications(Context context) {
        AudioManager manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        manager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
        DeLog.d(TAG, "Unmuting notifications");
    }


    /**
     * Returns the height of the status bar
     * @param res
     * @return
     */
    public static int getStatusBarHeight(Resources res) {
        int result = 0;
        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * Returns the height of the system's navigation bar
     * @param resources
     * @return
     */
    public static int getNavigationBarHeight(Resources resources) {
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

}
