package com.alexstyl.touchcontrol.entity;


import android.content.Context;
import android.content.Intent;

import com.alexstyl.touchcontrol.R;
import com.alexstyl.touchcontrol.utils.Utils;


public class Email extends DataType {

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;

        DataType other = Utils.as(DataType.class, o);

        if (other == null)
            return false;

        if (this.dataType != other.dataType)
            return false;

        return data.equals(other.data);
    }

    public Email(String address, Integer type, String label) {
        super(address, type, label, TYPE_EMAIL);
    }

    public String getEmail() {
        return data;
    }

    public void sendMail(Context context) {
        final Intent emailIntent = Utils.getEmailIntent(data, null, null);
        context.startActivity(
                Intent.createChooser(emailIntent,
                        context.getString(R.string.send_mail_via)));

    }

}

