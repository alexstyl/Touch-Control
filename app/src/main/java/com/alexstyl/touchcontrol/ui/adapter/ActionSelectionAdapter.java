package com.alexstyl.touchcontrol.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alexstyl.touchcontrol.R;
import com.alexstyl.touchcontrol.entity.SelectableAction;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Created by alexstyl on 14/03/15.</p>
 */
public class ActionSelectionAdapter extends AbstractSimpleAdapter<SelectableAction> {


    public ActionSelectionAdapter(Context context) {
        super(context);
        initAvailableActions();
    }

    private void initAvailableActions() {
        List<SelectableAction> list = new ArrayList<>();
        list.add(new SelectableAction(SelectableAction.ID_CALL, R.string.action_call));
        list.add(new SelectableAction(SelectableAction.ID_CALL_CONTACT, R.string.action_call_contact));

        list.add(new SelectableAction(SelectableAction.ID_CHECK_IN, R.string.action_check_in));
        list.add(new SelectableAction(SelectableAction.ID_NAVIGATE, R.string.action_navigate));
        list.add(new SelectableAction(SelectableAction.ID_LAUNCH_APP, R.string.action_open));

        list.add(new SelectableAction(SelectableAction.ID_VOLUME_UP, R.string.action_volume_up));
        list.add(new SelectableAction(SelectableAction.ID_VOLUME_DOWN, R.string.action_volume_down));


        setData(list);
    }

    @Override
    protected View onNewView(LayoutInflater inflater, ViewGroup parent, int position) {
        View view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        return view;
    }


    @Override
    protected void onBindView(Context mContext, View convertView, int position) {

        SelectableAction action = getItem(position);
        String title = action.getTitle(mContext);
        ((TextView) convertView.findViewById(android.R.id.text1)).setText(title);


    }
}
