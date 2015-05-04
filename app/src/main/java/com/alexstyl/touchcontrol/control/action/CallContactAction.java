package com.alexstyl.touchcontrol.control.action;

import android.content.Context;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.ContactsContract;

import com.alexstyl.touchcontrol.R;

/**
 * <p>Created by alexstyl on 03/03/15.</p>
 */
public class CallContactAction extends AbstractAction {

    private long mContactID;
    private String mContactName;
    private String lookupUri;

    public CallContactAction(long contactID, String lookupKey, String contactName) {
        this.mContactID = contactID;
        this.lookupUri = ContactsContract.Contacts.getLookupUri(contactID, lookupKey).toString();
        this.mContactName = contactName;
    }

    @Override
    protected boolean onRunExecuted(Context context) {
        Rect rect = new Rect(0, 0, 5, 5);
        ContactsContract.QuickContact.showQuickContact(context, rect, Uri.parse(lookupUri),
                ContactsContract.QuickContact.MODE_LARGE, null);
        return true;
    }

    @Override
    protected String getDataString(Context context) {
        return mContactName;
    }

    @Override
    protected String getActionString(Context context) {
        return context.getString(R.string.calls);
    }


}
