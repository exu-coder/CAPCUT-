package com.capcut.pro.modules;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CapCutContacts {
    private Context context;

    public CapCutContacts(Context context) {
        this.context = context;
    }

    public List<HashMap<String, String>> getContacts() {
        List<HashMap<String, String>> contacts = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
            null, null, null, null);
        
        if (cursor != null && cursor.moveToFirst()) {
            do {
                HashMap<String, String> contact = new HashMap<>();
                contact.put("name", cursor.getString(cursor.getColumnIndexOrThrow(
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
                contact.put("number", cursor.getString(cursor.getColumnIndexOrThrow(
                    ContactsContract.CommonDataKinds.Phone.NUMBER)));
                contacts.add(contact);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return contacts;
    }
}