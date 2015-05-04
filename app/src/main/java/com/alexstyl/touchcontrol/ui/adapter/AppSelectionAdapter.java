package com.alexstyl.touchcontrol.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexstyl.touchcontrol.R;
import com.alexstyl.touchcontrol.entity.AppInfo;
import com.alexstyl.touchcontrol.manager.SpeechRecognition;

import java.util.Collections;
import java.util.List;

/**
 * <p>Created by alexstyl on 18/03/15.</p>
 */
public class AppSelectionAdapter extends AbstractSimpleAdapter<AppInfo> {
    public AppSelectionAdapter(Context context) {
        super(context);
        List<AppInfo> data = SpeechRecognition.getAllInstalledApps(context, true);
        Collections.sort(data, new AppInfo.AppNameComparator());
        setData(data);
    }

    private static class ViewHolder {

        private TextView appName;
        private ImageView icon;

    }


    @Override
    protected View onNewView(LayoutInflater inflater, ViewGroup parent, int position) {
        View view = inflater.inflate(R.layout.row_application, parent, false);
        ViewHolder vh = new ViewHolder();
        vh.appName = (TextView) view.findViewById(android.R.id.text1);
        vh.icon = (ImageView) view.findViewById(android.R.id.icon);
        view.setTag(vh);

        return view;
    }

    @Override
    protected void onBindView(Context mContext, View convertView, int position) {
        AppInfo app = getItem(position);
        ViewHolder vh = (ViewHolder) convertView.getTag();
        vh.icon.setImageDrawable(app.getIconDrawable());
        vh.appName.setText(app.getAppName());
    }
}
