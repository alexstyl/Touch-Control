package com.alexstyl.touchcontrol.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alexstyl.touchcontrol.R;
import com.alexstyl.touchcontrol.entity.NamedGesture;
import com.alexstyl.touchcontrol.ui.BaseFragment;
import com.alexstyl.touchcontrol.ui.adapter.GestureAdapter;
import com.alexstyl.touchcontrol.ui.dialog.GestureDeletionDialogFragment;
import com.alexstyl.touchcontrol.ui.loader.TouchGesturesLoader;
import com.alexstyl.touchcontrol.ui.widget.DividerItemDecoration;

import java.util.List;

/**
 * <p>Created by alexstyl on 08/03/15.</p>
 */
public class TouchGesturesListFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<List<NamedGesture>> {

    private static final int LOADER_ID_GESTURES = 19;
    public static final String FM_TAG_DELETE = "alexstyl:delete";

    private RecyclerView mRecyclerView;
    private TextView mEmptyView;
    private GestureAdapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved_gestures, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mEmptyView = (TextView) view.findViewById(android.R.id.empty);
        mEmptyView.setVisibility(View.GONE);


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter = new GestureAdapter(getActivity());
        mAdapter.setOnGestureClickedListener(new GestureAdapter.OnGestureClickedListener() {

            @Override
            public void onGestureClicked(View view, NamedGesture gesture) {
                // a gesture has been clicked
                GestureDeletionDialogFragment drag =
                        GestureDeletionDialogFragment.createInstance(gesture.toString());
                drag.show(getFragmentManager(), FM_TAG_DELETE);
            }
        });
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(LOADER_ID_GESTURES, null, this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(LOADER_ID_GESTURES, null, this);
    }

    @Override
    public Loader<List<NamedGesture>> onCreateLoader(int id, Bundle args) {
        if (id == LOADER_ID_GESTURES) {
            return new TouchGesturesLoader(getActivity());
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<List<NamedGesture>> loader, List<NamedGesture> data) {
        mAdapter.setGestures(data);
        if (data == null || data.isEmpty()) {
            mEmptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NamedGesture>> loader) {
        mAdapter.setGestures(null);
        mEmptyView.setVisibility(View.VISIBLE);


    }
}
