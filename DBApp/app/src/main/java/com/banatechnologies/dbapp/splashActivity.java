package com.banatechnologies.dbapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;
import java.util.Locale;

public class splashActivity extends AppCompatActivity{

    EditText edSearch;
    ListView objcontactlist;
    listviewAdapter objAdapter;
    DatabaseHandler db;
    List<Contact> contacts = null;
    int selIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        edSearch = (EditText)findViewById(R.id.edSearch);
        db = new DatabaseHandler(this);
        objcontactlist = (ListView)findViewById(R.id.contactlist);
        contacts = db.getAllContacts();
        objAdapter = new listviewAdapter(this, contacts, R.layout.custom_list);
        objcontactlist.setAdapter(objAdapter);
        registerForContextMenu(objcontactlist);
        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = edSearch.getText().toString().toLowerCase(Locale.getDefault());
                objAdapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_add){
            startActivity(new Intent(splashActivity.this, addContact.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select Options");
        menu.add(0, v.getId(), 1, "Edit");
        menu.add(0, v.getId(), 2, "Delete");
        menu.add(0, v.getId(), 3, "Cancel");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        selIndex = info.position;
        if(item.getTitle() == "Edit"){
            int cid = db.getContact(contacts.get(selIndex).getID()).getID();
            Intent intent = new Intent(splashActivity.this, editActivity.class);
            intent.putExtra("cid", cid);
            startActivity(intent);
        }else if(item.getTitle() == "Delete"){
            deleteContact();
        }else if(item.getTitle() == "Cancel"){
            return false;
        }
        return false;
    }
    public void deleteContact(){
            android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Are you sure you want to delete it ??");
            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    db.deleteContact(db.getContact(contacts.get(selIndex).getID()));
                    contacts.clear();
                    contacts = db.getAllContacts();
                    objAdapter = new listviewAdapter(getBaseContext(), contacts, R.layout.custom_list);
                    objcontactlist.setAdapter(objAdapter);
                }
            });
            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        contacts.clear();
        contacts = db.getAllContacts();
        objAdapter = new listviewAdapter(this, contacts, R.layout.custom_list);
        objcontactlist.setAdapter(objAdapter);
    }

}
