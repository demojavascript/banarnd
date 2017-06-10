package com.banatechnologies.dbapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class addContact extends AppCompatActivity implements View.OnClickListener {

    EditText edName, edContactno;
    Button btnAdd;
    DatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        db = new DatabaseHandler(this);
        btnAdd = (Button)findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);
        edName = (EditText)findViewById(R.id.ed_name);
        edContactno = (EditText)findViewById(R.id.ed_contactno);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnAdd){
            if( edName.getText().toString() == "" || edContactno.getText().toString() == "" ){
                Toast.makeText(getApplicationContext(), "Error:- Name or Contact no is incorrect.", Toast.LENGTH_LONG).show();
            }else {
                db.addContact(new Contact(edName.getText().toString(), edContactno.getText().toString()));
                edName.setText("");
                edContactno.setText("");
                finish();
            }
        }
    }
}
