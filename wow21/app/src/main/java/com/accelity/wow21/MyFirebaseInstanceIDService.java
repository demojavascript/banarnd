package com.accelity.wow21;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Rahul on 01-07-2016.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {
        SharedPreferences sharedpreferences = this.getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        SharedPreferences.Editor editor = sharedpreferences.edit();
       // Log.d(TAG, "Refreshed token: " + refreshedToken);
        editor.putString("deviceidKey", refreshedToken);
        editor.commit();
    }
}