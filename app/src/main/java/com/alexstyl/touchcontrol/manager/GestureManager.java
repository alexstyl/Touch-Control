package com.alexstyl.touchcontrol.manager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.media.MediaPlayer;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;

import com.alexstyl.commons.logging.DeLog;
import com.alexstyl.touchcontrol.BuildConfig;
import com.alexstyl.touchcontrol.R;
import com.alexstyl.touchcontrol.TouchControl;
import com.alexstyl.touchcontrol.control.action.AbstractAction;
import com.alexstyl.touchcontrol.control.action.CallAction;
import com.alexstyl.touchcontrol.control.action.FoursquareCheckinAction;
import com.alexstyl.touchcontrol.control.action.NavigateToPointAction;
import com.alexstyl.touchcontrol.control.action.VolumeDownAction;
import com.alexstyl.touchcontrol.control.action.VolumeUpAction;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Set;

/**
 * <p>Created by alexstyl on 04/03/15.</p>
 */
public class GestureManager {


    /**
     * Intent action send by a broadcast that a gesture has been saved
     */
    public static final String ACTION_NEW_GESTURE = TouchControl.PACKAGE + ".ACTION_NEW_GESTURE";
    private Context mContext;
    private HashMap<String, AbstractAction> mMap;
    private static final String TAG = "GestureManager";
    private static GestureManager sInstance;

    private GestureManager(Context context) {
        this.mContext = context;
    }

    public static GestureManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new GestureManager(context);
        }
        return sInstance;
    }


    public void onCommandRecognised(Context context) {
        //TODO let the user select their own sound
        //TODO let the user mute the sound
        MediaPlayer.create(context, R.raw.gesture_performed).start();
    }


    public static final File TOUCH_GESTURES_PATH = Environment.getExternalStorageDirectory();
    public static final String TOUCH_GESTURES_FILE_NAME = "gestures";
    /**
     * The file to load and save the touch gestures
     */
    public static final File TOUCH_GESTURES_FILE = new File(TOUCH_GESTURES_PATH, TOUCH_GESTURES_FILE_NAME);


    public void init() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        boolean firstRun = prefs.getBoolean(
                mContext.getString(R.string.key_firstTimeRun), true

        );

        GestureLibrary fileLib = GestureLibraries.fromFile(TOUCH_GESTURES_FILE);
        if (firstRun) {

            GestureLibrary rawLib = GestureLibraries.fromRawResource(mContext, R.raw.default_gestures);
            if (!rawLib.load()) {
                if (BuildConfig.DEBUG) {
                    throw new RuntimeException("Unable to load default gestures");
                }
            }
            Set<String> nameEntries = rawLib.getGestureEntries();
            for (String name : nameEntries) {
                for (Gesture gesture : rawLib.getGestures(name)) {
                    fileLib.addGesture(name, gesture);
                }
            }

            fileLib.save();
            prefs.edit().putBoolean(mContext.getString(R.string.key_firstTimeRun), false).apply();

        } else {
            if (!fileLib.load()) {
                DeLog.e(TAG, "Unable to load gesture lib!");
                prefs.edit().putBoolean(mContext.getString(R.string.key_firstTimeRun), true).apply();
                init();
            }
        }
        try {
            this.mMap = loadMapFromFile();
        } catch (FileNotFoundException e) {
            DeLog.log(TAG, "File not found", e);
            setUpHashMap();

        }
        if (BuildConfig.DEBUG) {
            printLibrary(fileLib);
        }


        if (BuildConfig.DEBUG && this.mMap == null) {
            throw new RuntimeException("Map was empty!");
        }

    }

    private static final String CHECK = "Check";
    private static final String VOL_DOWN_FULL = "VolDownFull";
    private static final String VOL_UP_FULL = "VolDownUp";
    private static final String VOL_UP_HALF = "VolUpHalf";
    private static final String POINT = "Point";
    private static final String HEART = "Heart";

    private void setUpHashMap() {
        this.mMap = new HashMap<>();
        this.mMap.put(CHECK, new FoursquareCheckinAction());
        this.mMap.put(POINT, new NavigateToPointAction("University of Birmingham"));

        //
        this.mMap.put(HEART, new CallAction("555-4-3-2-1-7")); //TODO made default a pick-a-favourite contact dialog
        this.mMap.put(VOL_UP_FULL, new VolumeUpAction());
        this.mMap.put(VOL_UP_HALF, new VolumeUpAction());
        this.mMap.put(VOL_DOWN_FULL, new VolumeDownAction());

        saveHashMap();

    }

    private static final String FILENAME_MAP = "mapping.dat";

    private boolean saveHashMap() {
        synchronized (mMap) {
            FileOutputStream fos = null;
            try {
                fos = mContext.openFileOutput(FILENAME_MAP, Context.MODE_PRIVATE);
                ObjectOutputStream os = new ObjectOutputStream(fos);
                os.writeObject(mMap);
                os.close();
                fos.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    private HashMap<String, AbstractAction> loadMapFromFile() throws FileNotFoundException {
        FileInputStream fis = null;
        try {
            fis = mContext.openFileInput(FILENAME_MAP);
            ObjectInputStream is = new ObjectInputStream(fis);
            HashMap<String, AbstractAction> map = (HashMap<String, AbstractAction>) is.readObject();

            is.close();
            fis.close();
            return map;
        } catch (FileNotFoundException ex) {
            throw ex;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void printLibrary(GestureLibrary lib) {
        Set<String> nameEntries = lib.getGestureEntries();
        for (String name : nameEntries) {
            for (Gesture gesture : lib.getGestures(name)) {
                DeLog.v(TAG, name + " " + gesture);
            }
        }
    }


    /**
     * Returns the action that is connected to the give name
     *
     * @param name The name of the gesture to perform
     * @return
     */
    public AbstractAction getActionOfGesture(String name) {
        return mMap.get(name);

    }


    public void onCommandUnrecognised(Context context) {

    }

    /**
     * Stores the given gesture.
     *
     * @param name    The name of the gesture
     * @param gesture The gesture
     * @param action  The action to perform when the touch gesture is performed
     */
    public void addGesture(String name, Gesture gesture, AbstractAction action) {

        GestureLibrary fileLib = GestureLibraries.fromFile(TOUCH_GESTURES_FILE);
        if (fileLib.load()) {
            fileLib.addGesture(name, gesture);
            fileLib.save();
            this.mMap.put(name, action);
            saveHashMap();
            DeLog.d(TAG, "Gesture " + name + " saved");
            sendGestureChangedBroadcast();
        } else {
            DeLog.e(TAG, "Couldn't save gesture " + name);
        }

    }


    /**
     * Returns whether a gesture of the given name exists or not
     */
    public boolean gestureExists(String name) {
        return mMap.get(name) != null;
    }

    /**
     * Removes the gesture with the given name from the manager
     *
     * @param name The name of the entry to remove
     * @return Whether it manages to remove an entry or not
     */
    public boolean removeGesture(String name) {

        boolean res = false;
        GestureLibrary fileLib = GestureLibraries.fromFile(TOUCH_GESTURES_FILE);
        if (fileLib.load()) {
            fileLib.removeEntry(name);
            fileLib.save();
            res = null != this.mMap.remove(name);
            saveHashMap();
            DeLog.d(TAG, "Gesture " + name + " removed");
            sendGestureChangedBroadcast();
        } else {
            DeLog.e(TAG, "Couldn't save gesture " + name);
        }
        return res;

    }

    /**
     * Sends a local broadcast that a gesture was saved
     */
    private void sendGestureChangedBroadcast() {
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(mContext);
        Intent intent = new Intent(ACTION_NEW_GESTURE);
        manager.sendBroadcast(intent);
    }

//    public String[] getAllAvailableActions() {
//        return mContext.getResources().getStringArray(R.array.touch_actions);
//    }
}
