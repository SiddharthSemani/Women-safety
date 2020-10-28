package com.example.womensafety.Data;

import android.provider.BaseColumns;

public class Contract {

    private Contract(){}

    public static final class PhoneBook implements BaseColumns {


        public final static String TABLE_NAME = "contacts";


        public final static String _ID = BaseColumns._ID;


        public final static String COLUMN_CONTACT_NAME ="name";

        public final static  String COLUMN_PHONE_NUMBER="phoneNumber";

    }
}
