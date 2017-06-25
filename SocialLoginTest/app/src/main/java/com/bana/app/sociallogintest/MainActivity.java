package com.bana.app.sociallogintest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener {

    private SharedPrefManager sharedPrefManager;
    private Button btnlogout;

    private GoogleApiClient mGoogleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPrefManager = new SharedPrefManager(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        System.out.println("Name    - "+sharedPrefManager.getUname());
        System.out.println("Token   - "+sharedPrefManager.getAccessToken());
        System.out.println("userid  - "+sharedPrefManager.getUserId());
        System.out.println("emailid - "+sharedPrefManager.getEmail());
        System.out.println("picurl  - "+sharedPrefManager.getUpicurl());
        System.out.println("Login   - "+sharedPrefManager.getIsLogin());

        btnlogout = (Button)findViewById(R.id.btnlogout);
        btnlogout.setOnClickListener(this);
        AccessToken token = AccessToken.getCurrentAccessToken();


    }
    private void googleSignOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        sharedPrefManager.logout();
                        LoginManager.getInstance().logOut();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    }
                });
    }
    private void fbSignOut() {
        sharedPrefManager.logout();
        LoginManager.getInstance().logOut();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }
    private void twSignOut() {
        sharedPrefManager.logout();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }
    @Override
    public void onClick(View view) {
        if(sharedPrefManager.getLoginType().equals("google")){
            googleSignOut();
        }else if(sharedPrefManager.getLoginType().equals("facebook")){
            fbSignOut();
        }else if(sharedPrefManager.getLoginType().equals("twitter")){
            twSignOut();
        }else{

        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
