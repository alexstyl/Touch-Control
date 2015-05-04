package com.alexstyl.touchcontrol.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.text.format.DateUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexstyl.commons.logging.DeLog;
import com.alexstyl.touchcontrol.R;
import com.alexstyl.touchcontrol.TouchControl;
import com.alexstyl.touchcontrol.control.action.CallAction;
import com.alexstyl.touchcontrol.entity.Contact;
import com.alexstyl.touchcontrol.entity.Phone;
import com.alexstyl.touchcontrol.utils.ContactUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Created by alexstyl on 17/03/15.</p>
 */
public class PhoneticCallingActivity extends OverlayActivity {

    public static final String EXTRA_NAME = TouchControl.PACKAGE + ".EXTRA_NAME";
    private static final String TAG = "PhoneticCall";
    private String mWhoToCall;

    private TextView mCallingX;
    private TextView subtitle;
    private ImageView mAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overlay_call);
        mCallingX = (TextView) findViewById(R.id.calling_x);
        subtitle = (TextView) findViewById(R.id.subtitle);
        mAvatar = (ImageView) findViewById(R.id.avatar);
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            finish();
        } else {
            mWhoToCall = extras.getString(EXTRA_NAME);
            mCallingX.setText(getString(R.string.calling_x, mWhoToCall));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        subtitle.setVisibility(View.INVISIBLE);
        mAvatar.setVisibility(View.INVISIBLE);
        List<Contact> foundContact = findContact(this, mWhoToCall);
        if (foundContact == null || foundContact.isEmpty()) {
            onNoMatches();
        } else {
            // TODO support multiple contacts
            Contact contact = foundContact.get(0);
            onCallContact(contact);
        }

        // many
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                DeLog.d(TAG, "Finishing activity()");
                finish();
            }
        }, 3 * DateUtils.SECOND_IN_MILLIS);
    }

    /**
     * Calls when a contact found that matches the phonetic query.
     * This method will display the contact and the number calling accordingly
     *
     * @param contact The contact to display
     */
    private void onCallContact(Contact contact) {
        // TODO support multi numbers

        mCallingX.setText(getString(R.string.calling_x, contact.getDisplayName()));
        subtitle.setVisibility(View.VISIBLE);

        Uri contactUri = contact.getContactURI();

        int size = getResources().getDimensionPixelSize(R.dimen.call_avatar_width);
        mAvatar.startAnimation(new AlphaAnimation(0, 1));
        mAvatar.setVisibility(View.VISIBLE);

        Picasso.with(this).load(contactUri)
                .centerCrop()
                .placeholder(R.drawable.ic_contact_picture)
                .resize(size, size)
                .into(mAvatar);
        List<Phone> phones = ContactUtils.getAllPhones(getContentResolver(), contact.getID());
        if (phones != null && !phones.isEmpty()) {
            String number = phones.get(0).getNumber();
            subtitle.setText(number);
            new CallAction(number).run(this);
        } else {
            subtitle.setText(R.string.no_phone_number);
        }

    }

    private void onNoMatches() {
        subtitle.setText(R.string.no_matches_found);
    }

    public static List<Contact> findContact(Context context, String name) {

        DeLog.d(TAG, "Searching for [" + name + "]");
        Cursor cursor = null;
        List<Contact> contacts = new ArrayList<>();
        try {
            String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = ?";
            String[] selectArgs = {"1"};
            Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI,
                    Uri.encode(name));
            cursor = context.getContentResolver().query(uri,
                    null, selection, selectArgs, ContactsContract.Contacts.DISPLAY_NAME + " ASC");
            int index_ID = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            int index_DISPLAY = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            int index_lookup = cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY);

            while (cursor.moveToNext()) {
                long _ID = cursor.getLong(index_ID);
                String display = cursor.getString(index_DISPLAY);
                Contact contact = new Contact(_ID, display, cursor.getString(index_lookup));
                contacts.add(contact);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return contacts;
    }

    public static void startActivity(Context context, String phonetic) {
        Intent i = new Intent(context, PhoneticCallingActivity.class);
        i.putExtra(EXTRA_NAME, phonetic);
        context.startActivity(i);
    }
}
