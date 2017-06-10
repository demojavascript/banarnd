package com.nitinguru.app.nitinguru.services;

/**
 * Created by Rahul on 20-05-2017.
 */

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.nitinguru.app.nitinguru.models.UserPref;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    private UserPref userPref;
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        userPref = new UserPref(this);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        userPref.setDevice_Key(refreshedToken);
        sendRegistrationToServer(refreshedToken);
    }
    private void sendRegistrationToServer(String token) {

    }
}