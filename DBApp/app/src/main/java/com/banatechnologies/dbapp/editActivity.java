package com.banatechnologies.dbapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class editActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edName, edContactno;
    Button btnEdit;
    int cid;
    Contact objcontact;
    DatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        btnEdit = (Button)findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(this);
        edName = (EditText)findViewById(R.id.ed_ename);
        edContactno = (EditText)findViewById(R.id.ed_econtactno);
        db = new DatabaseHandler(this);
        objcontact = new Contact();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(!bundle.isEmpty()) {
            cid = bundle.getInt("cid");
            objcontact.setID(cid);
            objcontact.setName(db.getContact(cid).getName());
            objcontact.setPhoneNumber(db.getContact(cid).getPhoneNumber());
            edName.setText(objcontact.getName());
            edContactno.setText(objcontact.getPhoneNumber());
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnEdit){
            if( edName.getText().toString() == "" || edContactno.getText().toString() == "" ){
                Toast.makeText(getApplicationContext(), "Error:- Name or Contact no is incorrect.", Toast.LENGTH_LONG).show();
            }else {
                objcontact.setName(edName.getText().toString());
                objcontact.setPhoneNumber(edContactno.getText().toString());
                int result = db.updateContact(objcontact);
                finish();
            }
        }
    }
}
