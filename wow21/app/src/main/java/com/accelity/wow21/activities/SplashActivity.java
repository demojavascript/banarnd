package com.accelity.wow21.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.accelity.wow21.R;
import com.accelity.wow21.models.User;
import com.accelity.wow21.models.UserPref;

public class SplashActivity extends Activity {

    private static int SPLASH_TIME_OUT = 3000;
    private User user;
    private UserPref userPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        userPref = new UserPref(this);
        user = userPref.getUser(this);

        startGo();
    }



    public void startGo(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent objintent;
                if(user.getLoggedIn()){
                    objintent = new Intent(SplashActivity.this, HomeActivity.class);
                }else{
                    objintent = new Intent(SplashActivity.this, SignupActivity.class);
                }
                startActivity(objintent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
