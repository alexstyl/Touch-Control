package com.alexstyl.touchcontrol.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.alexstyl.touchcontrol.R;
import com.alexstyl.touchcontrol.service.OverlayService;
import com.alexstyl.touchcontrol.ui.BaseFragment;

/**
 * Fragment that contains the switch that  enables/disables the {@link com.alexstyl.touchcontrol.service.OverlayService}
 * <p>Created by alexstyl on 08/03/15.</p>
 */
public class MasterSwitchFragment extends BaseFragment {
    private SwitchCompat mServiceSwitch;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_master_control, container, false);
        mServiceSwitch = (SwitchCompat) view.findViewById(R.id.master_switch);
        mServiceSwitch.setOnCheckedChangeListener(mCheckedListener);
        return view;
    }

    CompoundButton.OnCheckedChangeListener mCheckedListener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            OverlayService.setEnabled(getActivity(), isChecked);
            if (isChecked) {
                Intent intent = new Intent(getActivity().getApplicationContext(), OverlayService.class);
                getActivity().startService(intent);
            } else {
                Intent intent = new Intent(getActivity().getApplicationContext(), OverlayService.class);
                getActivity().stopService(intent);
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        mServiceSwitch.setChecked(OverlayService.isEnabled(getActivity()));

    }
}
