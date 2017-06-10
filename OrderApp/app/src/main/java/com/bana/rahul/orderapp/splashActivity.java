package com.bana.rahul.orderapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;

public class splashActivity extends ActionBarActivity {

    private static int SPLASH_TIME_OUT = 3000;
    SharedPreferences sharedpreferences, userpreferences;
    public static final String MyPREFERENCES = "settingPrefs";
    public static final String userPREFERENCES = "userPrefs";
    public static final String settingKey = "settingKey";
    public static final String loggedIn = "loginKey";
    public String isSetStore = "";
    public Boolean isLogin = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        userpreferences = getSharedPreferences(userPREFERENCES, Context.MODE_PRIVATE);
        isSetStore = sharedpreferences.getString(settingKey, "");
        isLogin = userpreferences.getBoolean(loggedIn, false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent objintent;
                if(isSetStore != ""){
                    if(isLogin){
                        objintent = new Intent(splashActivity.this, MainActivity.class);
                    }else {
                        objintent = new Intent(splashActivity.this, landingActivity.class);
                    }
                }else {
                    objintent = new Intent(splashActivity.this, settingActivity.class);
                }
                startActivity(objintent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
