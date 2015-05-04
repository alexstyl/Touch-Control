package com.alexstyl.touchcontrol.entity;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.alexstyl.commons.logging.DeLog;

/**
 * <p>Created by alexstyl on 17/03/15.</p>
 */
public class Contact {

    private String mDisplayName;
    private long contactID;
    private String mLookup;

    public Contact(long id, String display, String lookupKey) {
        this.contactID = id;
        this.mLookup =lookupKey;
        this.mDisplayName = display;
    }

    public long getID() {
        return contactID;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public Uri getContactURI() {
        return ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactID);
    }

    public static Contact from(Context context, Uri contactData) {

        Cursor c = null;
        Contact contact = null;
        try {
            c = context.getContentResolver().query(contactData, null, null, null, null);
            if (c.moveToFirst()) {
                long contactID = c.getLong(c.getColumnIndex(ContactsContract.Contacts._ID));
                String lookupKey = c.getString(c.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                String display = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

//            AbstractAction camera = new CallContactAction(contactID, lookupKey);
//            camera.run(getActivity());
                contact = new Contact(contactID, display, lookupKey);
            }
        } catch (Exception e) {
            DeLog.log(e);
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
            }
        }
        return contact;

    }

    public String getLookupUri() {
        return mLookup;
    }
}
