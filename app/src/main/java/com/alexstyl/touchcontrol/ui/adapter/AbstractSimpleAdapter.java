package com.alexstyl.touchcontrol.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Created by alexstyl on 14/03/15.</p>
 */
abstract public class AbstractSimpleAdapter<T> extends BaseAdapter {

    private final List<T> mData = new ArrayList<T>();

    private final Context mContext;
    private final LayoutInflater mInflater;

    public AbstractSimpleAdapter(Context context) {
        this(context, null);
    }


    public AbstractSimpleAdapter(Context context, List<T> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context.getApplicationContext();
        if (data != null) {
            this.mData.addAll(data);
        }
    }


    /**
     * Sets the given {@literal T}
     *
     * @param data
     */
    public void setData(List<T> data) {
        this.mData.clear();
        if (data != null) {
            this.mData.addAll(data);
        }
        notifyDataSetChanged();
    }


    public void addData(T data) {
        this.mData.add(data);
    }


    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    final public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = onNewView(mInflater, parent, position);
        }

        onBindView(mContext, convertView, position);
        return convertView;
    }


    protected Context getContext() {
        return mContext;
    }

    protected abstract void onBindView(Context mContext, View convertView, int position);

    protected abstract View onNewView(LayoutInflater inflater, ViewGroup parent, int position);


}
