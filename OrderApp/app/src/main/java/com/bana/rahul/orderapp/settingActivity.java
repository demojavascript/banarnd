package com.bana.rahul.orderapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class settingActivity extends ActionBarActivity implements View.OnClickListener {

    EditText edStireId;
    Button btnSetStore;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "settingPrefs";
    public static final String settingKey = "settingKey";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        edStireId = (EditText)findViewById(R.id.ed_storeid);
        btnSetStore = (Button)findViewById(R.id.btn_setstore);
        btnSetStore.setOnClickListener(this);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_setstore:
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(settingKey, edStireId.getText().toString());
                editor.commit();
                Intent intent = new Intent(settingActivity.this, landingActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
