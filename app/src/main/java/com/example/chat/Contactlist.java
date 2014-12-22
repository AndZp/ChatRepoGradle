package com.example.chat;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

/**
 * Created by User on 16.12.2014.
 */
public class Contactlist extends Activity {
    ListView lv;
    ContactAdapter contactAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contactlist);
        lv = (ListView)findViewById(R.id.listView2);
        contactAdapter = new ContactAdapter(this);
        lv.setAdapter(contactAdapter);

    }
}
