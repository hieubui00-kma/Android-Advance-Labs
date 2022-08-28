package com.hieubui.session;

import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.WRITE_CONTACTS;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static androidx.core.content.ContextCompat.checkSelfPermission;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.provider.Telephony.Sms;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Phonebook {

    @NonNull
    public static List<Contact> getContacts(@NonNull Context context) {
        List<Contact> contacts = new ArrayList<>();

        if (!(checkSelfPermission(context, READ_CONTACTS) == PERMISSION_GRANTED)) {
            return contacts;
        }

        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(Contacts.CONTENT_URI, null, null, null, null);

        if (cursor == null) {
            return contacts;
        }

        if (cursor.getCount() <= 0) {
            cursor.close();
            return contacts;
        }

        Contact contact;

        while (cursor.moveToNext()) {
            contact = new Contact();

            contact.setID(cursor.getLong(cursor.getColumnIndexOrThrow(Contacts._ID)));
            contact.setName(cursor.getString(cursor.getColumnIndexOrThrow(Contacts.DISPLAY_NAME)));
            contact.setPhoneNumbers(getPhoneNumbers(context, cursor, contact.getID()));
            contacts.add(contact);
        }
        cursor.close();
        return contacts;
    }

    @NonNull
    private static List<String> getPhoneNumbers(@NonNull Context context, @NonNull Cursor cursorContact, long contactID) {
        List<String> phoneNumbers = new ArrayList<>();

        if (cursorContact.getInt(cursorContact.getColumnIndexOrThrow(Contacts.HAS_PHONE_NUMBER)) <= 0) {
            return phoneNumbers;
        }

        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(
            Phone.CONTENT_URI,
            null,
            Phone.CONTACT_ID + " = ?",
            new String[]{contactID + ""},
            null
        );

        while (cursor.moveToNext()) {
            phoneNumbers.add(cursor.getString(cursor.getColumnIndexOrThrow(Phone.NUMBER)));
        }
        cursor.close();
        return phoneNumbers;
    }

    public static void deleteContact(@NonNull Context context, long contactID) {
        if (!(checkSelfPermission(context, WRITE_CONTACTS) == PERMISSION_GRANTED)) {
            return;
        }

        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(
            Contacts.CONTENT_URI,
            null,
            Contacts._ID + "=" + contactID,
            null,
            null
        );

        if (cursor == null) {
            return;
        }

        if (cursor.moveToFirst()) {
            String lookupKey = cursor.getString(cursor.getColumnIndexOrThrow(Contacts.LOOKUP_KEY));
            Uri uri = Uri.withAppendedPath(Contacts.CONTENT_LOOKUP_URI, lookupKey);

            contentResolver.delete(uri, Contacts._ID + "=" + contactID, null);
        }
        cursor.close();
    }

    @NonNull
    public static List<SMSMessage> getSMSMessages(@NonNull Context context) {
        List<SMSMessage> messages = new ArrayList<>();

        if (!(checkSelfPermission(context, READ_SMS) == PERMISSION_GRANTED)) {
            return messages;
        }

        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = Uri.parse("content://sms/inbox");
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        SMSMessage message;

        while (cursor.moveToNext()) {
            message = new SMSMessage();

            message.setID(cursor.getInt(cursor.getColumnIndexOrThrow(Sms._ID)));
            message.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(Sms.ADDRESS)));
            message.setSubject(cursor.getString(cursor.getColumnIndexOrThrow(Sms.SUBJECT)));
            message.setBody(cursor.getString(cursor.getColumnIndexOrThrow(Sms.BODY)));
            message.setDateSent(new Date(cursor.getLong(cursor.getColumnIndexOrThrow(Sms.DATE_SENT))));
            messages.add(message);
        }
        cursor.close();
        return messages;
    }

    public static void deleteSMSMessage(@NonNull Context context, long messageId) {
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = Uri.parse("content://sms/inbox");
        String[] projection = new String[]{Sms._ID, Sms.THREAD_ID, Sms.ADDRESS, Sms.PERSON, Sms.DATE, Sms.BODY};
        Cursor cursor = contentResolver.query(uri, projection, null, null, null);

        while (cursor.moveToNext()) {
            contentResolver.delete(Uri.parse("content://sms/" + messageId), null, null);
        }
        cursor.close();
    }
}
