package com.example.womensafety.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.womensafety.Adapter.Contact;
import com.example.womensafety.Data.Contract.PhoneBook;

import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = DbHelper.class.getSimpleName();

    /** Name of the database file */
    private static final String DATABASE_NAME = "contact.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_CONTACTS_TABLE = "CREATE TABLE " + PhoneBook.TABLE_NAME+ " ("
                + PhoneBook._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PhoneBook.COLUMN_CONTACT_NAME + " TEXT NOT NULL, "
                + PhoneBook.COLUMN_PHONE_NUMBER + " TEXT" +");";
            db.execSQL(SQL_CREATE_CONTACTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void addContact(Contact contact){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PhoneBook.COLUMN_CONTACT_NAME,contact.getName());
        values.put(PhoneBook.COLUMN_PHONE_NUMBER,contact.getNumber());
        db.insert(PhoneBook.TABLE_NAME,null,values);
        Log.d("dbsid","Successfully saved in Database");
        db.close();
    }
    public ArrayList<Contact> getAllContacts(){
        ArrayList<Contact> contactList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Generate the query to read from the database
        String select = "SELECT * FROM " + PhoneBook.TABLE_NAME;
        Cursor cursor = db.rawQuery(select, null);

        //Loop through now
        if(cursor.moveToFirst()){
            do{
                String name = cursor.getString(1);
                String number = cursor.getString(2);
                Contact contact = new Contact(name,number, true);
                contactList.add(contact);
            }while(cursor.moveToNext());
        }
        return contactList;
    }
}
