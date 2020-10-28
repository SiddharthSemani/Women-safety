package com.example.womensafety.test;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.womensafety.Adapter.Adapter2;
import com.example.womensafety.Adapter.Contact;
import com.example.womensafety.Data.DbHelper;
import com.example.womensafety.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class SecondPage extends AppCompatActivity {
    private Adapter2 adapter2;
    private RecyclerView recyclerView;
    DbHelper db = new DbHelper(SecondPage.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_page);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<Contact> savedList = adapter2.check();

                for (int i = 0; i < savedList.size(); i++) {
                    db.addContact(savedList.get(i));
                }
            }
        });


        //recyclerView Initialization
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter2 = new Adapter2(new ArrayList<Contact>(),1);
        recyclerView.setAdapter(adapter2);

        //permission for Contacts
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS)!=PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SecondPage.this,new String[]{Manifest.permission.READ_CONTACTS},1);
        } else {
            adapter2.updateData(getContact());
        }

    }
    public ArrayList<Contact> getContact() {
        ArrayList<Contact> contacts = new ArrayList<>();
        Cursor cursor = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, null, null);
        }
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            contacts.add(new Contact(name, number, false));
        }

        return contacts;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                adapter2.updateData(getContact());
            }
        }
    }



}