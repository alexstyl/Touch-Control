package com.alexstyl.touchcontrol.ui.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alexstyl.touchcontrol.R;

/**
 * <p>Created by alexstyl on 17/03/15.</p>
 */
public class CheckListRow extends FrameLayout {

    private TextView mTitle;
    private TextView mSummary;
    private CheckBox mCheck;


    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        mTitle.setEnabled(enabled);
        mSummary.setEnabled(enabled);
        mCheck.setEnabled(enabled);
    }


    public CheckListRow(Context context) {
        super(context);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.row_check, this);
        this.mTitle = (TextView) view.findViewById(android.R.id.text1);
        this.mSummary = (TextView) view.findViewById(android.R.id.text2);
        this.mCheck = (CheckBox) view.findViewById(R.id.checkbox);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCheck.toggle();
            }
        });
    }

    public void setOnCheckChanagedListener(CompoundButton.OnCheckedChangeListener l) {
        this.mCheck.setOnCheckedChangeListener(l);
    }

    public void setTitle(int resString) {
        this.mTitle.setText(getResources().getString(resString));
    }

    public void setTitle(String title) {
        this.mTitle.setText(title);
    }

    public void setSummary(int resString) {
        this.mSummary.setText(getResources().getString(resString));
    }

    public void setSummary(String title) {
        this.mSummary.setText(title);
    }


    /**
     * Changes the checked state of the Checkbox
     */
    public void setChecked(boolean checked) {
        this.mCheck.setChecked(checked);
    }
}
