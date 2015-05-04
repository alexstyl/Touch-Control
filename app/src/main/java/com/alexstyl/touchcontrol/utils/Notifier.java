package com.alexstyl.touchcontrol.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.NotificationCompat;

import com.alexstyl.touchcontrol.R;
import com.alexstyl.touchcontrol.receiver.DeviceEventReceiver;
import com.alexstyl.touchcontrol.ui.activity.MainActivity;

/**
 * <p>Created by alexstyl on 03/03/15.</p>
 */
final public class Notifier {


    private static final int ID_FLASH = 23;
    private static final int ID_LIGHT = 24;

    private Notifier() {
        //
    }


    /**
     * Creates the notification that indicates that the service is running
     *
     * @param context
     * @return
     */
    public static Notification createServiceNotification(Context context) {

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        return new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_stat_content_gesture)
                .setColor(context.getResources().getColor(R.color.primary))
                .setOngoing(true)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(context.getString(R.string.service_is_running))
                .setContentIntent(pendingIntent)
                .build();

    }


    public static void forFlash(Context context) {
        Intent intent = new Intent(DeviceEventReceiver.ACTION_TORCH_OFF);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Resources res = context.getResources();
        Notification notification = new NotificationCompat.Builder(context)
                .setColor(res.getColor(R.color.yellow_torch))
                .setSmallIcon(R.drawable.ic_stat_image_flare)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_STATUS)
                .setContentTitle(res.getString(R.string.ticker_flash))
                .setContentText(res.getString(R.string.touch_to_turn_off))
                .setTicker(res.getText(R.string.ticker_flash))
                .setContentIntent(pIntent)
                .build();
        NotificationManager mng = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mng.notify(ID_FLASH, notification);
    }

    public static void cancelForFlash(Context context) {
        NotificationManager mng = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mng.cancel(ID_FLASH);
    }

    /**
     * Creates a notification that suggests the user to turn the light on because it's dark
     *
     * @param context The context to use
     */
    public static void forFlashSuggestion(Context context) {
        Intent intent = new Intent(DeviceEventReceiver.ACTION_TORCH_ON);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Resources res = context.getResources();
        Notification notification = new NotificationCompat.Builder(context)
                .setColor(res.getColor(R.color.yellow_torch))
                .setSmallIcon(R.drawable.ic_stat_image_brightness_4)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_STATUS)
                .setContentTitle(res.getString(R.string.its_dark_in_here))
                .setContentText(res.getString(R.string.touch_to_turn_on))
                .setContentIntent(pIntent)
                .build();
        NotificationManager mng = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mng.notify(ID_LIGHT, notification);
    }

    public static void cancelFlashSuggestion(Context context) {
        NotificationManager mng = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mng.cancel(ID_LIGHT);

    }
}
