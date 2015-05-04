package com.alexstyl.touchcontrol.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexstyl.touchcontrol.R;
import com.alexstyl.touchcontrol.control.action.AbstractAction;
import com.alexstyl.touchcontrol.entity.NamedGesture;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Created by alexstyl on 08/03/15.</p>
 */
public class GestureAdapter extends RecyclerView.Adapter<GestureAdapter.ReminderViewHolder> {

    private List<NamedGesture> mGestures;
    private final Context mContext;
    private LayoutInflater mInflater;

    public GestureAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mGestures = new ArrayList<>();
    }

    public interface OnGestureClickedListener {

        /**
         * Called whenver a Gesture has been clicked
         *
         * @param view    The view that was clicked
         * @param gesture The gesture that was represented by the view clicked
         */
        public void onGestureClicked(View view, NamedGesture gesture);
    }

    private OnGestureClickedListener mListener;


    public void setOnGestureClickedListener(OnGestureClickedListener l) {
        this.mListener = l;

    }

    public class ReminderViewHolder extends RecyclerView.ViewHolder {

        private TextView mName;
        private TextView mLabel;
        private ImageView mIcon;

        public ReminderViewHolder(View itemView) {
            super(itemView);
            this.mName = (TextView) itemView.findViewById(android.R.id.text1);
            this.mLabel = (TextView) itemView.findViewById(android.R.id.text2);
            this.mIcon = (ImageView) itemView.findViewById(android.R.id.icon);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onGestureClicked(v, getGesture(getPosition()));
                    }
                }
            });
        }
    }

    @Override
    public ReminderViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View view = mInflater.inflate(R.layout.row_saved_gesture, parent, false);
        return new ReminderViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ReminderViewHolder reminderViewHolder, int i) {
        NamedGesture gesture = getGesture(i);


        reminderViewHolder.mName.setText(gesture.toString());
        AbstractAction action = gesture.getLinkedAction(mContext);
        if (action != null) {
            reminderViewHolder.mLabel.setText(action.getLabelString(mContext));
        } else {
            reminderViewHolder.mLabel.setText(null);
        }

        reminderViewHolder.mIcon.setImageBitmap(gesture.getThumbnail());

    }


    public NamedGesture getGesture(int pos) {
        return mGestures.get(pos);
    }

    @Override
    public int getItemCount() {
        return mGestures.size();
    }


    public void setGestures(List<NamedGesture> gestures) {
        this.mGestures.clear();
        if (gestures != null) {
            this.mGestures.addAll(gestures);
        }
        notifyDataSetChanged();
    }
}
