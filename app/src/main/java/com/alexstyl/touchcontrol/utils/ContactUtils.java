package com.alexstyl.touchcontrol.utils;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.alexstyl.touchcontrol.BuildConfig;
import com.alexstyl.touchcontrol.entity.Email;
import com.alexstyl.touchcontrol.entity.Phone;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Created by alexstyl on 04/03/15.</p>
 */
public class ContactUtils {

    private ContactUtils() {
    }

    private static class DataTypeQuery {
        // Stuff like projection and selection are going to be the same, since
        // Phone and Email contain the same columns
        static String[] PROJECTION = {

                ContactsContract.Data.DATA1, // NUMBER
                ContactsContract.Data.DATA2, // TYPE
                ContactsContract.Data.DATA3, // LABEL
        };
        static String SELECTION = ContactsContract.Data.CONTACT_ID + " = ?";

        static String SORTORDER = ContactsContract.Data.IS_PRIMARY + " DESC";
    }

    public static List<Phone> getAllPhones(ContentResolver resolver, long contactID) {

        Cursor phoneCursor = null;
        String[] selectionArgs = {
                String.valueOf(contactID)
        };

        Uri contentUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        phoneCursor = resolver.query(contentUri, DataTypeQuery.PROJECTION,
                DataTypeQuery.SELECTION,
                selectionArgs, DataTypeQuery.SORTORDER);

        if (phoneCursor == null) {
            return null;
        }
        List<Phone> phones = new ArrayList<Phone>();

        try {
            int colData1 = phoneCursor.getColumnIndex(ContactsContract.Data.DATA1);
            int colData2 = phoneCursor.getColumnIndex(ContactsContract.Data.DATA2);
            int colData3 = phoneCursor.getColumnIndex(ContactsContract.Data.DATA3);

            while (phoneCursor.moveToNext()) {
                String number = phoneCursor.getString(colData1);
                Integer type = phoneCursor.getInt(colData2);
                String label = phoneCursor.getString(colData3);

                Phone data = new Phone(number, type, label);
                if (!phones.contains(data)) {
                    phones.add(data);
                }
            }

        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
        } finally {
            if (!phoneCursor.isClosed())
                phoneCursor.close();
        }

        return phones;
    }

    public static List<Email> getAllEMails(ContentResolver resolver, long contactID) {
        Cursor cur = null;
        String[] selectionArgs = {
                String.valueOf(contactID)
        };

        Uri contentUri = ContactsContract.CommonDataKinds.Email.CONTENT_URI;

        cur = resolver.query(contentUri, DataTypeQuery.PROJECTION,
                DataTypeQuery.SELECTION,
                selectionArgs, DataTypeQuery.SORTORDER);

        if (cur == null)
            return null;
        List<Email> mails = new ArrayList<Email>();

        try {
            int colData1 = cur.getColumnIndex(ContactsContract.Data.DATA1);
            int colData2 = cur.getColumnIndex(ContactsContract.Data.DATA2);
            int colData3 = cur.getColumnIndex(ContactsContract.Data.DATA3);

            while (cur.moveToNext()) {
                String number = cur.getString(colData1);
                Integer type = cur.getInt(colData2);
                String label = cur.getString(colData3);

                Email data = new Email(number, type, label);
                if (!mails.contains(data)) {
                    mails.add(data);
                }
            }

        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
        } finally {
            if (cur != null && !cur.isClosed())
                cur.close();
        }

        return mails;
    }
}
