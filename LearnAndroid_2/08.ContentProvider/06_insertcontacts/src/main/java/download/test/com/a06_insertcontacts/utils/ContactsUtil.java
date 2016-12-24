package download.test.com.a06_insertcontacts.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import download.test.com.a06_insertcontacts.model.Contact;

/**
 * Created by iml1s-macpro on 2016/12/21.
 */

public class ContactsUtil
{
    // 插入聯絡人
    public static void insertContact(Contact contact, Context context)
    {
        ContentResolver resolver = context.getContentResolver();

        Uri contactsUri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri dataUri = Uri.parse("content://com.android.contacts/data");

        // 查詢當前raw_contact_id 要插入的raw_contact_id
        Cursor cursor = resolver.query(contactsUri,null,null,null,null);
        int rowsCount = cursor.getCount();
        cursor.close();
        int index = rowsCount + 1;

        ContentValues values = new ContentValues();
        values.put("contact_id",index);

        resolver.insert(contactsUri,values);

        values.clear();

        values.put("raw_contact_id",index);
        values.put("mimetype","vnd.android.cursor.item/name");
        values.put("data1",contact.getName());

        resolver.insert(dataUri,values);

        values.clear();

        values.put("raw_contact_id",index);
        values.put("mimetype","vnd.android.cursor.item/phone_v2");
        values.put("data1",contact.getPhone());

        resolver.insert(dataUri,values);

        values.clear();

        values.put("raw_contact_id",index);
        values.put("mimetype","vnd.android.cursor.item/email_v2");
        values.put("data1",contact.getEmail());

        resolver.insert(dataUri,values);

    }
}
