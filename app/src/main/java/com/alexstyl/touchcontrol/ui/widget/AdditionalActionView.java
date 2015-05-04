package com.alexstyl.touchcontrol.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexstyl.touchcontrol.R;
import com.squareup.picasso.Picasso;

/**
 * <p>Created by alexstyl on 14/03/15.</p>
 */
public class AdditionalActionView extends FrameLayout {


    public static final int MODE_HIDE = -1;
    public static final int MODE_EDIT = 0;
    public static final int MODE_READ_ONLY = 1;


    public AdditionalActionView(Context context) {
        super(context);
        init();
    }

    public AdditionalActionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AdditionalActionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AdditionalActionView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private EditText mEditLayout;


    private ViewGroup mReadLayout;
    private TextView mHeader;
    private TextView mSubtitle;
    private ImageView mIcon;


    private int mMode = MODE_EDIT;

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.row_actiondata_view, this);
        mReadLayout = (ViewGroup) findViewById(R.id.read_layout);
        mHeader = (TextView) findViewById(android.R.id.text1);
        mSubtitle = (TextView) findViewById(android.R.id.text2);
        mIcon = (ImageView) findViewById(android.R.id.icon);

        mEditLayout = (EditText) findViewById(R.id.edit_layout);
        mEditLayout.addTextChangedListener(new PhoneNumberFormattingTextWatcher());


        mReadLayout.setVisibility(View.GONE);
        mEditLayout.setVisibility(View.GONE);

    }


    public void setMode(int modeEdit) {
        this.mMode = modeEdit;
        invalidate();
    }

    @Override
    public void invalidate() {
        if (mMode == MODE_EDIT) {
            mEditLayout.setVisibility(View.VISIBLE);
            mReadLayout.setVisibility(View.GONE);
        } else if (mMode == MODE_READ_ONLY) {
            mEditLayout.setVisibility(View.GONE);
            mReadLayout.setVisibility(View.VISIBLE);
        } else if (mMode == MODE_HIDE) {
            mReadLayout.setVisibility(View.GONE);
            mEditLayout.setVisibility(View.GONE);
        }
        super.invalidate();
    }

    public void clearComposingText() {
        this.mEditLayout.setText(null);
    }

    public void focusComposingText() {
        this.mEditLayout.requestFocus();
    }

    public String getComposingText() {
        return this.mEditLayout.getText().toString();
    }

    public void setEditHint(int resString) {
        this.mEditLayout.setHint(resString);
    }

    public void setText(String text) {
        this.mHeader.setText(text);
    }

    public void hideIcon() {
        this.mIcon.setVisibility(View.INVISIBLE);
    }

    public void showIcon() {
        this.mIcon.setVisibility(View.VISIBLE);
    }

    public void showError(int resString) {
        this.mEditLayout.setError(getContext().getString(resString));
    }

    public void setIcon(Drawable iconDrawable) {
        mIcon.setImageDrawable(iconDrawable);
    }

    public void loadContactAvatar(Context context, Uri contactURI) {
        Picasso.with(context).load(contactURI)
                .centerCrop()
                .placeholder(R.drawable.ic_contact_picture)
                .resize(mIcon.getWidth(), mIcon.getHeight())
                .into(mIcon);

    }
}
