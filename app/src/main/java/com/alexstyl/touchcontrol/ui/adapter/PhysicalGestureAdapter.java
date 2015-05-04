package com.alexstyl.touchcontrol.ui.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.speech.SpeechRecognizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.alexstyl.touchcontrol.R;
import com.alexstyl.touchcontrol.entity.PhysicalAction;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Created by alexstyl on 16/03/15.</p>
 */
public class PhysicalGestureAdapter extends AbstractSimpleAdapter<PhysicalAction> {


    public PhysicalGestureAdapter(Context context) {
        super(context);
        initData();
    }

    public static final int ID_FLIP = 0;
    public static final int ID_VOICE_COMMAND = 1;
    public static final int ID_SHAKE = 2;
    public static final int ID_LIGHT = 3;

    private void initData() {
        List<PhysicalAction> list = new ArrayList<>();

        Resources res = getContext().getResources();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean voice = prefs.getBoolean(res.getString(R.string.key_long_press_action), true);
        boolean flip = prefs.getBoolean(res.getString(R.string.key_flip_gesture), true);
        boolean shake = prefs.getBoolean(res.getString(R.string.key_shake_gesture), true);
        boolean light = prefs.getBoolean(res.getString(R.string.key_light_action), true);

        list.add(new PhysicalAction(ID_VOICE_COMMAND, R.string.long_press_for_voice, R.string.long_press_for_voice_summary, voice, false));

        list.add(new PhysicalAction(ID_FLIP, R.string.flip_title, R.string.flip_summary, flip, true));
        list.add(new PhysicalAction(ID_SHAKE, R.string.shake_for_torch_title, R.string.shake_for_torch_summary, shake, true));
        list.add(new PhysicalAction(ID_LIGHT, R.string.dark_for_torch, R.string.dark_for_torch_summary, light, true));

        setData(list);
    }

    private boolean isBatteryOK;

    public void onBatteryOK() {
        this.isBatteryOK = true;
        notifyDataSetChanged();
    }

    public void onLowBattery() {
        this.isBatteryOK = false;
        notifyDataSetChanged();
    }

    public interface OnCheckToggleListener {

        public void onCheckboxToggle(int pos, boolean check);
    }

    private OnCheckToggleListener mOnCheckToggleListener;

    public void setListener(OnCheckToggleListener l) {
        this.mOnCheckToggleListener = l;

    }

    private static class ViewHolder {
        TextView mHeader;
        TextView mSub;
        CheckBox mCheck;
    }


    @Override
    protected View onNewView(LayoutInflater inflater, ViewGroup parent, int position) {
        View view = inflater.inflate(R.layout.row_check, parent, false);
        ViewHolder vh = new ViewHolder();
        vh.mHeader = (TextView) view.findViewById(android.R.id.text1);
        vh.mSub = (TextView) view.findViewById(android.R.id.text2);
        vh.mCheck = (CheckBox) view.findViewById(R.id.checkbox);
        view.setTag(vh);
        return view;
    }


    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }


    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }


    @Override
    public boolean isEnabled(int position) {
        boolean isEnabled = false;
        PhysicalAction action = getItem(position);
        if (action.getId() == ID_VOICE_COMMAND) {
            isEnabled = SpeechRecognizer.isRecognitionAvailable(getContext());
        } else {
            isEnabled = (!action.isBatteryDependant() || isBatteryOK);
        }
        return isEnabled;
    }

    @Override
    protected void onBindView(Context mContext, View convertView, final int position) {
        final ViewHolder vh = (ViewHolder) convertView.getTag();
        final PhysicalAction action = getItem(position);

        convertView.setEnabled(isEnabled(position));
        vh.mHeader.setText(action.getTitleRes());
        vh.mSub.setText(action.getSummary());
        vh.mCheck.setChecked(action.isChecked());
        vh.mCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mOnCheckToggleListener != null) {
                    mOnCheckToggleListener.onCheckboxToggle(action.getId(), isChecked);
                }
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vh.mCheck.toggle();
            }
        });
    }
}
