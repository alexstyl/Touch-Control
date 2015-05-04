package com.alexstyl.touchcontrol.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alexstyl.touchcontrol.R;
import com.alexstyl.touchcontrol.TouchControl;
import com.alexstyl.touchcontrol.entity.AppInfo;
import com.alexstyl.touchcontrol.ui.BaseActivity;
import com.alexstyl.touchcontrol.ui.adapter.AppSelectionAdapter;

/**
 * <p>Created by alexstyl on 18/03/15.</p>
 */
public class SelectAppActivity extends BaseActivity {

    private ListView mAppList;
    public static final String ACTION_RESULT = TouchControl.PACKAGE + ".RESULT_ACTION";
    public static final String EXTRA_APP_PACKAGE = TouchControl.PACKAGE + ".APP_PACKAGE";
    private AppSelectionAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.select_app);
        setContentView(R.layout.dialog_selectapp);
        mAppList = (ListView) findViewById(R.id.list);
        mAppList.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AppInfo pInfo = mAdapter.getItem(position);
                Intent data = new Intent(ACTION_RESULT);
                data.putExtra(EXTRA_APP_PACKAGE, pInfo.getPackageName());

                setResult(RESULT_OK, data);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter = new AppSelectionAdapter(this);
        mAppList.setAdapter(mAdapter);
    }
}
