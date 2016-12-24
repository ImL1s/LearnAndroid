package download.test.com.a05_querycontacts.Utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import download.test.com.a05_querycontacts.Bean.Contact;

/**
 * Created by iml1s-macpro on 2016/12/20.
 */

public class ContactsUtil
{
    public static List<Contact> queryContacts(Context context)
    {
        Uri contactsUrl = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri dataUri = Uri.parse("content://com.android.contacts/data");
        ContentResolver resolver = context.getContentResolver();

        List<Contact> contactList = new ArrayList<>();

        Cursor contactsCursor = resolver.query(contactsUrl,new String[]{"contact_id"},null,null,null);

        if(contactsCursor != null && contactsCursor.getCount() > 0)
        {
            while (contactsCursor.moveToNext())
            {
                Contact contact = new Contact();
                contact.setId(contactsCursor.getString(0));

                if(contact.getId() == null) continue;

                Cursor cursor = resolver.query(dataUri,new String[]{"mimetype","data1"},"raw_contact_id = ?",new String[]{contact.getId()},null);

                while (cursor.moveToNext())
                {
                    if(cursor.getString(0).equals("vnd.android.cursor.item/name"))
                    {
                        contact.setName(cursor.getString(1));
                    }
                    else if(cursor.getString(0).equals("vnd.android.cursor.item/email_v2"))
                    {
                        contact.setEmail(cursor.getString(1));
                    }
                    else if(cursor.getString(0).equals("vnd.android.cursor.item/phone_v2"))
                    {
                        contact.setPhone(cursor.getString(1));
                    }
                }
                cursor.close();
                contactList.add(contact);
            }
        }

        contactsCursor.close();

        return contactList;

        // 直接查詢data表
//        Cursor cursor = resolver.query(dataUri,new String[]{"raw_contact_id","mimetype","data1"},null,null,null);
//
//        while (cursor.moveToNext())
//        {
//            String contactID = cursor.getString(cursor.getColumnIndex("raw_contact_id"));
//            String mimetype = cursor.getString(cursor.getColumnIndex("mimetype"));
//            String data1 = cursor.getString(cursor.getColumnIndex("data1"));
//
//            Log.d("debug",contactID + "/" + mimetype + "/" + data1 + "/" );
////            Log.d("debug",data1 + "/" + mimetype);
//        }

//        return null;

    }

    // 輸出Data表中的所有Columns
    public static List<Contact> queryDataColumns(Context context)
    {
        Cursor cursor = context.getContentResolver().query(Uri.parse("content://com.android.contacts/data"),null,null,null,null);

        Log.d("debug","***** ColumnNames start *****");

        for (int i = 0; i < cursor.getColumnNames().length; i++)
        {
            Log.d("debug",cursor.getColumnName(i));
        }

        Log.d("debug","***** ColumnNames end *****");

        return null;
    }

}


//"raw_contact_id","mimetype_id,mimetype",