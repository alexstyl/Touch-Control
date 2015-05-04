package com.alexstyl.touchcontrol.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureOverlayView;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.alexstyl.commons.logging.DeLog;
import com.alexstyl.touchcontrol.R;
import com.alexstyl.touchcontrol.TouchControl;
import com.alexstyl.touchcontrol.control.action.AbstractAction;
import com.alexstyl.touchcontrol.control.action.CallAction;
import com.alexstyl.touchcontrol.control.action.CallContactAction;
import com.alexstyl.touchcontrol.control.action.FoursquareCheckinAction;
import com.alexstyl.touchcontrol.control.action.LaunchSpecificAppAction;
import com.alexstyl.touchcontrol.control.action.NavigateToPointAction;
import com.alexstyl.touchcontrol.control.action.VolumeDownAction;
import com.alexstyl.touchcontrol.control.action.VolumeUpAction;
import com.alexstyl.touchcontrol.entity.AppInfo;
import com.alexstyl.touchcontrol.entity.Contact;
import com.alexstyl.touchcontrol.entity.SelectableAction;
import com.alexstyl.touchcontrol.manager.GestureManager;
import com.alexstyl.touchcontrol.manager.SpeechRecognition;
import com.alexstyl.touchcontrol.ui.BaseFragment;
import com.alexstyl.touchcontrol.ui.activity.SelectAppActivity;
import com.alexstyl.touchcontrol.ui.adapter.ActionSelectionAdapter;
import com.alexstyl.touchcontrol.ui.widget.AdditionalActionView;

/**
 * <p>Created by alexstyl on 12/03/15.</p>
 */
public class CreateGestureFragment extends BaseFragment {

    private static final String TAG = "CreateGesture";
    private static final float LENGTH_THRESHOLD = 120.0f;
    private static final int REQ_CODE_SELECT_APP = 45;
    private static final int REQ_CODE_CONTACT = 46;
    private static final String KEY_SELECTED_APP = TouchControl.PACKAGE + ".selected_app";
    private static final String KEY_SELECTED_ACTION = TouchControl.PACKAGE + ".selected_act_id";
    private static final String KEY_COMPOSED_TEXT = TouchControl.PACKAGE + ".composed_text";


    private GestureOverlayView mOverlay;
    private Spinner mAction;
    private EditText mName;

    private AdditionalActionView mAdditionalAction;

    private Gesture mGesture;

    private ActionSelectionAdapter adapter;
    private AppInfo selectedApp;
    private Contact selectedContact;

    private int selectedActionID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private MenuItem mSaveBtn;


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_create_gesture, menu);
        mSaveBtn = menu.findItem(R.id.action_save);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save: {
                onSaveGesture();
                return true;
            }
            default: {
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_gesture, container, false);

        mAction = (Spinner) view.findViewById(R.id.gesture_action);
        mOverlay = (GestureOverlayView) view.findViewById(R.id.gesture_to_record);
        mName = (EditText) view.findViewById(R.id.gesture_name);
        mAdditionalAction = (AdditionalActionView) view.findViewById(R.id.additional_action);
        mOverlay.addOnGestureListener(new GesturesProcessor());


        adapter = new ActionSelectionAdapter(getActivity());
        mAction.setAdapter(adapter);
        mAction.setOnItemSelectedListener(mOnActionSelectedListener);

        if (savedInstanceState != null) {
            this.selectedActionID = savedInstanceState.getInt(KEY_SELECTED_ACTION);
            AppInfo app = (AppInfo) savedInstanceState.getSerializable(KEY_SELECTED_APP);
            String text = savedInstanceState.getString(KEY_COMPOSED_TEXT);
            setSelectedAction(selectedActionID, false);
            if (app != null) {
                setSelectedApp(app);
            }
            mAdditionalAction.setText(text);
        } else {
            setSelectedAction(0, true);
        }
        return view;
    }

    private class GesturesProcessor implements GestureOverlayView.OnGestureListener {

        public void onGestureStarted(GestureOverlayView overlay, MotionEvent event) {
            mSaveBtn.setEnabled(false);
            mGesture = null;
        }

        public void onGesture(GestureOverlayView overlay, MotionEvent event) {
        }

        public void onGestureEnded(GestureOverlayView overlay, MotionEvent event) {
            mGesture = overlay.getGesture();
            if (mGesture.getLength() < LENGTH_THRESHOLD) {
                overlay.clear(false);
            }
            mSaveBtn.setEnabled(true);
        }

        public void onGestureCancelled(GestureOverlayView overlay, MotionEvent event) {
        }


    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_SELECTED_APP, selectedApp);
        outState.putSerializable(KEY_SELECTED_ACTION, selectedActionID);
        outState.putString(KEY_COMPOSED_TEXT, mAdditionalAction.getComposingText());
    }


    boolean mBlockRogued = false;
    private AdapterView.OnItemSelectedListener mOnActionSelectedListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //an item was selected. maybe it has additional actions

            if (!mBlockRogued) {
                DeLog.w(TAG, "Prevented rogue event");
                mBlockRogued = true;
                return;
            }

            SelectableAction action = adapter.getItem(position);
            setSelectedAction(action.getActionID(), true);
            switch (action.getActionID()) {
                case SelectableAction.ID_LAUNCH_APP:
                    startAppSelection();
                    break;
                case SelectableAction.ID_CALL_CONTACT:
                    startContactSelection();
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };


    private void setSelectedAction(int actionID, boolean clearText) {
        selectedApp = null;
        selectedContact = null;

        int mode = AdditionalActionView.MODE_READ_ONLY;

        selectedActionID = actionID;
        mAdditionalAction.hideIcon();
        if (clearText) {
            mAdditionalAction.clearComposingText();
        }

        switch (actionID) {
            case SelectableAction.ID_CALL:
                //TODO ask to call contact or number
                mode = AdditionalActionView.MODE_EDIT;
                mAdditionalAction.setEditHint(R.string.hint_enter_number);
                break;
            case SelectableAction.ID_CHECK_IN:
                mAdditionalAction.setText(FoursquareCheckinAction.FOURSQUARE);
                break;
            case SelectableAction.ID_LAUNCH_APP:
                // startAppSelection();
                break;
            case SelectableAction.ID_NAVIGATE:
                mode = AdditionalActionView.MODE_EDIT;
                mAdditionalAction.setEditHint(R.string.hint_enter_address);
                break;
            case SelectableAction.ID_VOLUME_UP:
            case SelectableAction.ID_VOLUME_DOWN:
                mode = AdditionalActionView.MODE_HIDE;
                break;
            default:
                break;
        }
        mAdditionalAction.setMode(mode);

        if (mode == AdditionalActionView.MODE_EDIT) {
            mAdditionalAction.focusComposingText();
        }
    }

    private void startContactSelection() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, REQ_CODE_CONTACT);
    }


    private void startAppSelection() {
        Intent i = new Intent(getActivity(), SelectAppActivity.class);
        startActivityForResult(i, REQ_CODE_SELECT_APP);
    }

    /**
     * Called when the save button was saved and it's time to store the new Gesture.
     */
    private void onSaveGesture() {
        String name;
        if (TextUtils.isEmpty(name = mName.getText().toString())) {
            // the user needs to pass a valid name
            mName.setError(getString(R.string.error_missing_name));
            return;
        }

        if (mOverlay.getGesture() == null) {
            // TODO flashing animation on missing gesture
            Toast.makeText(getActivity(), R.string.error_missing_gesture, Toast.LENGTH_SHORT).show();
            return;
        }


        AbstractAction action = null;
        String data = mAdditionalAction.getComposingText();
        if (selectedActionID == SelectableAction.ID_CALL) {
            if (TextUtils.isEmpty(data)) {
                mAdditionalAction.showError(R.string.error_missing_number);
                return;
            }
            action = new CallAction(data);
        } else if (selectedActionID == SelectableAction.ID_NAVIGATE) {
            if (TextUtils.isEmpty(data)) {
                mAdditionalAction.showError(R.string.error_missing_address);
                return;
            }
            action = new NavigateToPointAction(data);
        } else if (selectedActionID == SelectableAction.ID_CHECK_IN) {
            action = new FoursquareCheckinAction();
        } else if (selectedActionID == SelectableAction.ID_LAUNCH_APP) {
            if (selectedApp == null) {
                startAppSelection();
                return;
            }
            action = new LaunchSpecificAppAction(selectedApp);
        } else if (selectedActionID == SelectableAction.ID_CALL_CONTACT) {
            if (selectedContact == null) {
                // the user hasn't selected a contact yet.
                startContactSelection();
                return;
            }
            action = new CallContactAction(selectedContact.getID(), selectedContact.getLookupUri(), selectedContact.getDisplayName());
        } else if (selectedActionID == SelectableAction.ID_VOLUME_UP) {
            action = new VolumeUpAction();
        }else if (selectedActionID== SelectableAction.ID_VOLUME_DOWN){
            action = new VolumeDownAction();
        }

        GestureManager.getInstance(getActivity()).addGesture(
                name, mOverlay.getGesture(), action
        );
        Toast.makeText(getActivity(), R.string.gesture_saved, Toast.LENGTH_SHORT).show();

        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQ_CODE_SELECT_APP) {
                String appPackage = data.getExtras().getString(SelectAppActivity.EXTRA_APP_PACKAGE);
                AppInfo app = SpeechRecognition.getInstalledPackage(getActivity(), appPackage);
                setSelectedApp(app);
            } else if (requestCode == REQ_CODE_CONTACT) {
                Uri contactData = data.getData();
                Contact contact = Contact.from(getActivity(), contactData);
                setSelectedContact(contact);
            }
        } else {
            mAction.setSelection(0);
        }
    }

    private void setSelectedContact(Contact contact) {
        selectedContact = contact;
        mAdditionalAction.loadContactAvatar(getActivity(), contact.getContactURI());
        mAdditionalAction.setText(contact.getDisplayName());
        mAdditionalAction.showIcon();
        mAdditionalAction.setMode(AdditionalActionView.MODE_READ_ONLY);
    }

    private void setSelectedApp(AppInfo app) {
        selectedApp = app;
        mAdditionalAction.setIcon(app.getIconDrawable());
        mAdditionalAction.setText(app.getAppName());
        mAdditionalAction.showIcon();
        mAdditionalAction.setMode(AdditionalActionView.MODE_READ_ONLY);
    }
}
