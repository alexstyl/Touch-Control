package com.alexstyl.touchcontrol.service;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;

import com.alexstyl.commons.logging.DeLog;
import com.alexstyl.touchcontrol.BuildConfig;
import com.alexstyl.touchcontrol.R;
import com.alexstyl.touchcontrol.TouchControl;
import com.alexstyl.touchcontrol.manager.CameraManager;
import com.alexstyl.touchcontrol.manager.GestureManager;
import com.alexstyl.touchcontrol.ui.activity.SimpleOverlayActivity;
import com.alexstyl.touchcontrol.ui.activity.VoiceCommandActivity;
import com.alexstyl.touchcontrol.ui.widget.CircleView;
import com.alexstyl.touchcontrol.ui.widget.listener.AbstractFlipListener;
import com.alexstyl.touchcontrol.ui.widget.listener.DarknessDetectorSensor;
import com.alexstyl.touchcontrol.ui.widget.listener.DragMeAroundTouchListener;
import com.alexstyl.touchcontrol.ui.widget.listener.MultiSensorEventListener;
import com.alexstyl.touchcontrol.ui.widget.listener.MultiTapListener;
import com.alexstyl.touchcontrol.ui.widget.listener.ShakeListener;
import com.alexstyl.touchcontrol.utils.Notifier;
import com.alexstyl.touchcontrol.utils.Utils;

/**
 * <p>Created by alexstyl on 03/03/15.</p>
 */
public class OverlayService extends AbstractOverlayService {

    private static final boolean DEBUG = BuildConfig.DEBUG;

    private static final String TAG = "Overlay";

    private static final int ONGOING_NOTIFICATION_ID = 1;
    private static final int TRANSITION_TIME = 700;
    public static final int REQ_VOICE_RECOGNITION = 800;


    /**
     * Mode Knock Lock
     * </br>In this mode, the device is locked from any gestures (physical or touch). Any gesture perform is not going to
     * have any effect on the activity and will be forwarded to the underlaying app.
     * <p>{@linkplain #MODE_GESTURE}</p>
     */
    private static final int MODE_KNOCK_LOCK = 0;
    /**
     *
     */
    private static final int MODE_GESTURE = 1;
    public static final String EXTRA_MOVE_FOREGROUND = TouchControl.PACKAGE + ".EXTRA_MOVE_FOREGROUND";
    public static final int MOVE_FOREGROUND = 0;
    public static final int MOVE_BACKGROUND = 1;

    /**
     * Extra value that notifies the service whether to show or to hide the overlay window of the service
     * <p>[Value: boolean]</p>
     */
    public static final String EXTRA_SHOW_OVERLAY = TouchControl.PACKAGE + ".EXTRA_SHOW_OVERLAY";
    /**
     * Extra value that tells the service to enable or disable the sensors.
     * When the sensors are disabled, no Physical gesture can be triggered
     * <p>[Value: int]</p>
     */
    private static final String EXTRA_SET_SENSOR_STATE = TouchControl.PACKAGE + ".EXTRA_SET_SENSOR_STATE";

    private static final int SENSOR_STATE_OFF = -1;
    private static final int SENSOR_STATE_ON = 1;

    /**
     * Action called when some physical action has been enabled/disabled by the user
     */
    private static final String ACTION_PHYSICAL_SENSOR_CHANGED = TouchControl.PACKAGE + ".EXTRA_PHYSICAL_SENSOR_CHANGED";

    public static boolean isRunning = false;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mLight;


    protected void moveToForeground() {
        Notification notification = Notifier.createServiceNotification(this);
        startForeground(ONGOING_NOTIFICATION_ID, notification);
    }

    private void moveToBackground() {
        this.stopForeground(true);
    }


    private DarknessDetectorSensor mLightListener;
    /**
     * SensorListener that keeps hold of all of our acceleroter listeners
     */
    private MultiSensorEventListener mAccMultiSensor = new MultiSensorEventListener();
    private ScreenReceiver mScreenReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        mFlipGestureEnabled = prefs.getBoolean(getString(R.string.key_flip_gesture), true);
        mShakeGestureEnabled = prefs.getBoolean(getString(R.string.key_shake_gesture), true);
        mLongPressEnabled = prefs.getBoolean(getString(R.string.key_long_press_action), true);
        mLightEnabled = prefs.getBoolean(getString(R.string.key_light_action), true);

        mScreenReceiver = new ScreenReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(mScreenReceiver, filter);
        registerSensors();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DeLog.d(TAG, "Starting Service");
        isRunning = true;

        if (TouchControl.getInstance().isAppVisible()) {
            DeLog.v(TAG, "App is visible. moving to background");
            //if the app is visible, move the service to the background
            moveToBackground();
        } else {
            DeLog.v(TAG, "App is visible. moving to foreground");
            moveToForeground();
        }


        if (intent != null) {
            checkforExtras(intent);
        }

        return super.onStartCommand(intent, flags, startId);
    }


    private void checkforExtras(Intent intent) {

        String action = intent.getAction();
        if (ACTION_PHYSICAL_SENSOR_CHANGED.equals(action)) {
            DeLog.d(TAG, "Some sensor changed!");
            registerSensors();

            if (mLongPressEnabled) {
                DeLog.d(TAG, "Enabling Long Press");
                getOverlayView().setOnLongClickListener(mLongClickListener);
            } else {
                DeLog.d(TAG, "Disabling Long Press");
                getOverlayView().setOnLongClickListener(null);
            }
        }

        Bundle args = intent.getExtras();
        if (args != null) {
            if (args.containsKey(EXTRA_SHOW_OVERLAY)) {
                boolean display = !TouchControl.getInstance().isAppVisible();
                setOverlayVisibility(display);
            }

            if (args.containsKey(EXTRA_SET_SENSOR_STATE)) {
                if (args.getInt(EXTRA_SET_SENSOR_STATE) == SENSOR_STATE_ON) {
                    registerSensors();
                } else if (args.getInt(EXTRA_SET_SENSOR_STATE) == SENSOR_STATE_OFF) {
                    unregisterAllSensors();

                }
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mScreenReceiver);
        unregisterAllSensors();
        isRunning = false;
    }

    private View.OnLongClickListener mLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            DeLog.d(TAG, "Starting Voice Command");
            Intent i = new Intent(OverlayService.this, VoiceCommandActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(i);
            setOverlayVisibility(false);
            return true;
        }
    };

    @Override
    protected View onCreateOverlayingView() {

        //TODO
        final CircleView view = new CircleView(this);

        view.setOnTouchListener(
                new DragMeAroundTouchListener(this) {
                    // remove the long click listener while the view is moving around,
                    // in order to prevent accidental long presses

                    @Override
                    protected void onStartMoving() {
//                        DeLog.i(TAG, "User is moving the overlay");
                        if (mLongPressEnabled) {
                            view.setOnLongClickListener(null);
                        }
                    }

                    @Override
                    protected void onStopMoving() {
//                        DeLog.i(TAG, "The overlay has stopped moving");
                        if (mLongPressEnabled) {
                            view.setOnLongClickListener(mLongClickListener);
                        }
                    }
                }
        );
        if (mLongPressEnabled) {
            view.setOnLongClickListener(mLongClickListener);
        }
        view.setOnClickListener(new MultiTapListener(3) {
            @Override
            protected void onMultitapPerformed() {
                // the overlay has been triple clicked. star the SimpleOverlayActivity and hide the overlay

                Intent i = new Intent(OverlayService.this, SimpleOverlayActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
                setOverlayVisibility(false);
            }

        });


        // when the view is created for the first time, hide it if the app is on the foreground
        setOverlayVisibility(!TouchControl.getInstance().isAppVisible());
        return view;
    }


    public static void triggerService(Context context, boolean isChecked) {
        if (isChecked) {
            Intent stopService = new Intent(context, OverlayService.class);
            context.stopService(stopService);
        } else {
            Intent stopService = new Intent(context, OverlayService.class);
            context.startService(stopService);
        }
    }

    public static void moveService(Context context, int state) {
        Intent i = new Intent(context, OverlayService.class);
        i.putExtra(EXTRA_MOVE_FOREGROUND, state);
        context.startService(i);
        DeLog.d(TAG, "Moving Service to " + getStateName(state));
    }

    /**
     * Returns the name of the given state.
     */
    private static String getStateName(int state) {
        if (state == MOVE_BACKGROUND) {
            return "background";
        } else if (state == MOVE_FOREGROUND) {
            return "foreground";
        } else
            return "unknown state(" + state + ")";
    }

    /**
     * Notifies the service to show the overlay window
     *
     * @param context The context to use
     */
    public static void showOverlay(Context context) {
        internalToggleOverlay(context, true);
    }

    /**
     * Notifies the service to hide the overlay window
     *
     * @param context The context to use
     */
    public static void hideOverlay(Context context) {
        internalToggleOverlay(context, false);
    }

    private static void internalToggleOverlay(Context context, boolean b) {
        Intent i = new Intent(context, OverlayService.class);
        i.putExtra(OverlayService.EXTRA_SHOW_OVERLAY, b);
        context.startService(i);
    }


    /**
     * Registers the Accelerometer sensor if it is available in the device
     */
    private void registerSensors() {
        if (mAccelerometer != null) {
            if (DEBUG) {
                Log.d(TAG, "sensor registered");
            }
            if (isAnyAccSensorEnabled()) {
                mSensorManager.unregisterListener(mAccMultiSensor);
                mAccMultiSensor.removeAllListeners();
                if (mFlipGestureEnabled) {
                    mAccMultiSensor.addSensorListener(mFlipListener);
                    DeLog.v(TAG, "Registering Flip Sensor");
                }
                if (mShakeGestureEnabled) {
                    mAccMultiSensor.addSensorListener(mShakeListener);
                    DeLog.v(TAG, "Registering Shake Sensor");
                }
                mSensorManager.registerListener(mAccMultiSensor, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
            } else {
                DeLog.w(TAG, "Tried to register Sensors but no sensor was needed - unregisterAllSensors()");
                // no sensor needs to be registered.
                unregisterAllSensors();
            }

        }
        if (mLight != null) {
            if (mLightListener == null) {
                mLightListener = new DarknessDetectorSensor() {

                    @Override
                    protected void onDarknessDetected() {
                        if (!CameraManager.getInstance(OverlayService.this).isFlashOn()) {
                            // only notify the user if the flash is off
                            Notifier.forFlashSuggestion(OverlayService.this);
                        }
                    }

                    @Override
                    protected void onDarknessGone() {
                        Notifier.cancelFlashSuggestion(OverlayService.this);

                    }
                };
            }
            if (mLightEnabled) {
                mSensorManager.registerListener(mLightListener, mLight, SensorManager.SENSOR_DELAY_NORMAL);
            } else {
                unregisterLightSensors();
            }

        }
    }

    private void unregisterLightSensors() {
        if (mLight != null) {
            if (DEBUG) {
                Log.v(TAG, "Light unregistered");
            }
            mSensorManager.unregisterListener(mLightListener);
        }
    }

    private void unregisterAcceSensors() {
        if (mAccelerometer != null) {
            if (DEBUG) {
                Log.v(TAG, "Accelerometer unregistered");
            }
            mSensorManager.unregisterListener(mAccMultiSensor);
        }
    }

    /**
     * Unregisters the Accelerometer sensor
     */
    private void unregisterAllSensors() {
        unregisterAcceSensors();
        unregisterLightSensors();
    }


    private AbstractFlipListener mFlipListener = new AbstractFlipListener() {

        @Override
        protected void onFlipDown() {
            GestureManager.getInstance(OverlayService.this).onCommandRecognised(OverlayService.this);
            Utils.muteNotifications(OverlayService.this);
        }

        @Override
        protected void onFlipUp() {
            GestureManager.getInstance(OverlayService.this).onCommandRecognised(OverlayService.this);
            Utils.unmuteNotifications(OverlayService.this);
        }
    };

    private ShakeListener mShakeListener = new ShakeListener(2 * DateUtils.SECOND_IN_MILLIS) {

        @Override
        protected void onDeviceShaken(float speed) {
            GestureManager.getInstance(OverlayService.this).onCommandRecognised(OverlayService.this);
            CameraManager.getInstance(OverlayService.this).toggleFlash();
        }
    };


    public static void registerSensors(Context context) {
        Intent i = new Intent(context.getApplicationContext(), OverlayService.class);
        i.putExtra(EXTRA_SET_SENSOR_STATE, SENSOR_STATE_ON);
        context.startService(i);
    }

    public static void unregisterSensors(Context context) {
        if (isRunning) {
            Intent i = new Intent(context.getApplicationContext(), OverlayService.class);
            i.putExtra(EXTRA_SET_SENSOR_STATE, SENSOR_STATE_OFF);
            context.startService(i);
        }
    }

    private static boolean mFlipGestureEnabled = true;
    private static boolean mShakeGestureEnabled = true;
    private static boolean mLightEnabled = true;

    private static boolean mLongPressEnabled = true;

    /**
     * Returns if any of the sensors are being enabled by the user
     */
    private static boolean isAnyAccSensorEnabled() {
        return mFlipGestureEnabled || mShakeGestureEnabled;
    }

    public static boolean getIsFlipGestureEnabled() {
        return mFlipGestureEnabled;
    }


    public static boolean getIsLightGestureEnabled() {
        return mLightEnabled;
    }

    public static boolean getIsShakeGestureEnabled() {
        return mShakeGestureEnabled;
    }

    public static void setFlipGestureEnabled(Context context, boolean isChecked) {
        mFlipGestureEnabled = isChecked;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putBoolean(context.getString(R.string.key_flip_gesture), isChecked).apply();

        if (isRunning) {
            // tell the service to toggle the sensor, only if it is running
            Intent i = new Intent(context.getApplicationContext(), OverlayService.class);
            i.setAction(ACTION_PHYSICAL_SENSOR_CHANGED);
            context.startService(i);
        }
    }

    public static void setShakeGestureEnabled(Context context, boolean isChecked) {
        mShakeGestureEnabled = isChecked;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putBoolean(context.getString(R.string.key_shake_gesture), isChecked).apply();

        if (isRunning) {
            // tell the service to toggle the sensor, only if it is running
            Intent i = new Intent(context.getApplicationContext(), OverlayService.class);
            i.setAction(ACTION_PHYSICAL_SENSOR_CHANGED);
            context.startService(i);
        }
    }


    /**
     * Returns whether the user has enabled the long-press-to-voice-command option
     */
    public static boolean getIsVoiceCommandEnabled() {
        return mLongPressEnabled;
    }

    public static void setLongPresstoVoiceCommandEnabled(Context context, boolean isChecked) {
        mLongPressEnabled = isChecked;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putBoolean(context.getString(R.string.key_long_press_action), isChecked).apply();

        if (isRunning) {
            // tell the service to toggle the sensor, only if it is running
            Intent i = new Intent(context.getApplicationContext(), OverlayService.class);
            i.setAction(ACTION_PHYSICAL_SENSOR_CHANGED);
            context.startService(i);
        }
    }


    public static void setLightGestureEnabled(Context context, boolean isChecked) {
        mLightEnabled = isChecked;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putBoolean(context.getString(R.string.key_light_action), isChecked).apply();

        if (isRunning) {
            // tell the service to toggle the sensor, only if it is running
            Intent i = new Intent(context.getApplicationContext(), OverlayService.class);
            i.setAction(ACTION_PHYSICAL_SENSOR_CHANGED);
            context.startService(i);
        }
    }

    public static boolean isEnabled(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(context.getString(R.string.key_service_enabled), true);
    }

    public static void setEnabled(Context context, boolean value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putBoolean(context.getString(R.string.key_service_enabled), value).apply();
    }


    private class ScreenReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (Intent.ACTION_SCREEN_ON.equals(action)) {
                onScreenOn(context);
            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                onScreenOff(context);
            }
        }
    }

    /**
     * Called whenever the screen is turned of
     *
     * @param context
     */
    private void onScreenOff(Context context) {
        DeLog.d(TAG, "onScreenOff");
        OverlayService.unregisterSensors(context);
    }

    /**
     * Called whenever the screen is turned on.
     *
     * @param context The context to use
     */
    private void onScreenOn(Context context) {
        DeLog.d(TAG, "onScreenOn");
        OverlayService.registerSensors(context);
    }
}
