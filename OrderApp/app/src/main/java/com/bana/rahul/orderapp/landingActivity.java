package com.bana.rahul.orderapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

public class landingActivity extends ActionBarActivity implements View.OnClickListener {
    Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        btnLogin = (Button)findViewById(R.id.btn_gotologin);
        btnLogin.setOnClickListener(this);
    }
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_gotologin:
                Intent intent = new Intent(landingActivity.this, loginActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
