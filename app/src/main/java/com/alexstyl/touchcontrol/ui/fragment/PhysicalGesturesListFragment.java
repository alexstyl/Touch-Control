package com.alexstyl.touchcontrol.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.alexstyl.commons.logging.DeLog;
import com.alexstyl.touchcontrol.R;
import com.alexstyl.touchcontrol.manager.CameraManager;
import com.alexstyl.touchcontrol.receiver.BatteryEventReceiver;
import com.alexstyl.touchcontrol.service.OverlayService;
import com.alexstyl.touchcontrol.ui.BaseFragment;
import com.alexstyl.touchcontrol.ui.adapter.PhysicalGestureAdapter;
import com.alexstyl.touchcontrol.ui.widget.CheckListRow;

/**
 * <p>Created by alexstyl on 16/03/15.</p>
 */
public class PhysicalGesturesListFragment extends BaseFragment {
    private static final String TAG = "PhysicalGesturesList";
    private BroadcastReceiver mReceiver;
    private IntentFilter mFilter;

    private ListView mList;
    private PhysicalGestureAdapter mAdapter;

    private CheckListRow flipCheck;
    private CheckListRow rotate;
    private CheckListRow shake;
    private CheckListRow light;
    private CheckListRow voiceCommand;

    private TextView mDisabledSensorsWarning;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Bundle e = intent.getExtras();
                int state = e.getInt(BatteryEventReceiver.EXTRA_BATTERY_STATE);
                if (state == BatteryEventReceiver.STATE_CONNECTED || state == BatteryEventReceiver.STATE_OK) {
                    onBatteryOK();
                } else {
                    onLowBattery();
                }

            }
        };
        mFilter = new IntentFilter(BatteryEventReceiver.ACTION_BATTERY_CHANGE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_physical_gestures_list, container, false);
        this.mList = (ListView) view.findViewById(android.R.id.list);
        this.mDisabledSensorsWarning = (TextView) view.findViewById(R.id.disabled_sensors);

//        flipCheck = (CheckListRow) view.findViewById(R.id.row_flip_check);
//
//        // flipCheck = new CheckListRow(getActivity());
//        // flipCheck.setTitle(R.string.flip_title);
//        // flipCheck.setSummary(R.string.flip_summary);
//
//        voiceCommand = new CheckListRow(getActivity());
//        voiceCommand.setTitle(R.string.long_press_for_voice);
//        voiceCommand.setSummary(R.string.long_press_for_voice_summary);
//
//
//        shake = new CheckListRow(getActivity());
//        shake.setTitle(R.string.shake_for_torch_title);
//        shake.setSummary(R.string.shake_for_torch_summary);
//
//        rotate = new CheckListRow(getActivity());
//        rotate.setTitle(R.string.rotate_for_camera);
//        rotate.setSummary(R.string.rotate_for_camera_summary);
//
//
//        light = new CheckListRow(getActivity());
//        light.setTitle(R.string.dark_for_torch);
//        light.setSummary(R.string.dark_for_torch_summary);
//
//
//        this.mLinear.addView(voiceCommand);
//        this.mLinear.addView(flipCheck);
//        this.mLinear.addView(shake);
//        this.mLinear.addView(light);
//
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
//        boolean isFlipped = prefs.getBoolean(getString(R.string.key_flip_gesture), true);
//        flipCheck.setChecked(isFlipped);
//        rotate.setChecked(prefs.getBoolean(getString(R.string.key_rotate_gesture), true));
//        shake.setChecked(prefs.getBoolean(getString(R.string.key_shake_gesture), true));
//        voiceCommand.setChecked(prefs.getBoolean(getString(R.string.key_long_press_action), true));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter = new PhysicalGestureAdapter(getActivity());
        mAdapter.setListener(mListener);
        mList.setAdapter(mAdapter);

    }

    private PhysicalGestureAdapter.OnCheckToggleListener mListener = new PhysicalGestureAdapter.OnCheckToggleListener() {
        @Override
        public void onCheckboxToggle(int id, boolean isChecked) {
            if (id == PhysicalGestureAdapter.ID_VOICE_COMMAND) {
                OverlayService.setLongPresstoVoiceCommandEnabled(getActivity(), isChecked);
            } else
            if (id == PhysicalGestureAdapter.ID_LIGHT) {
                OverlayService.setLightGestureEnabled(getActivity(), isChecked);
            } else
            if (id == PhysicalGestureAdapter.ID_SHAKE) {
                OverlayService.setShakeGestureEnabled(getActivity(), isChecked);
                if (!isChecked) {
                    // turn off flash if the user disables the flashling
                    CameraManager.getInstance(getActivity()).turnOffFLash();
                }
            } else

            if (id == PhysicalGestureAdapter.ID_FLIP) {
                if (!isChecked) {
                    CameraManager.getInstance(getActivity()).turnOffFLash();
                }
                OverlayService.setFlipGestureEnabled(getActivity(), isChecked);

            } else
                DeLog.d(TAG, "Unknown id " + id);

        }
    };


    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReceiver, mFilter);
        boolean lowBattery = isLowBattery();
        if (lowBattery) {
            onLowBattery();
        } else {
            onBatteryOK();
        }


    }

    private void onLowBattery() {
        this.mDisabledSensorsWarning.setVisibility(View.VISIBLE);
        this.mAdapter.onLowBattery();

    }

    private void onBatteryOK() {
        this.mDisabledSensorsWarning.setVisibility(View.GONE);
        this.mAdapter.onBatteryOK();
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mReceiver);
    }

    private boolean isLowBattery() {
        return getBatteryLevel() < 0.15f;
    }


    /**
     * Returns the battery level of the device
     *
     * @return
     */
    public float getBatteryLevel() {
        Intent batteryIntent = getActivity().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        return ((float) level / (float) scale);
    }

}
