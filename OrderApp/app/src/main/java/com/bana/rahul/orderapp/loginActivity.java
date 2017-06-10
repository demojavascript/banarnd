package com.bana.rahul.orderapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class loginActivity extends ActionBarActivity implements View.OnClickListener {

    EditText eduseremail, eduserpwd;
    Button btnloginMe;
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private static final String LOGIN_URL = "http://deal26.in/api/index.php/manager/api/login";
    private static final String TAG_STATUS = "status";
    private static final String TAG_MESSAGE = "message";
    NetConnection netConnection;
    String userEmail, userPwd;
    public static final String MyPREFERENCES = "userPrefs";
    public static final String Name = "nameKey";
    public static final String Phone = "phoneKey";
    public static final String Email = "emailKey";
    public static final String Id = "idKey";
    public static final String loggedIn = "loginKey";
    String deviceID;
    public int status_success = 0;
    private static final String TAG_User_Name = "userName";
    private static final String TAG_User_ID = "userID";
    private static final String TAG_User_M = "userMobNo";
    private static final String TAG_User_Email = "userEmail";
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        eduseremail = (EditText)findViewById(R.id.ed_useremail);
        eduserpwd = (EditText)findViewById(R.id.ed_userpwd);
        btnloginMe = (Button) findViewById(R.id.btn_loginme);
        btnloginMe.setOnClickListener(this);
        deviceID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_loginme:
                netConnection = new NetConnection();
                Map<String, String> networkDetails = netConnection.getConnectionDetails(loginActivity.this);
                if(!networkDetails.isEmpty()) {
                    userEmail = eduseremail.getText().toString();
                    userPwd = eduserpwd.getText().toString();
                    new AttemptLogin().execute();
                }else{
                    Toast.makeText(getApplicationContext(), "Not Connected to Internet", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }
    class AttemptLogin extends AsyncTask<String, String, String> {
        boolean failure = false;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(loginActivity.this);
            pDialog.setMessage("Attempting login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            int success;
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("email", userEmail));
                params.add(new BasicNameValuePair("pwd", userPwd));
                params.add(new BasicNameValuePair("deviceId", deviceID));
                JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST", params);
                success = json.getInt(TAG_STATUS);
                if (success == 1) {
                    status_success = 1;
                    Log.d("Login Successful!", json.toString());
                    return json.getString(TAG_MESSAGE);
                }else{
                    status_success = 0;
                    Log.d("Login Failure!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            if (file_url != null){
                if(status_success == 1) {
                    JSONObject jreader;
                    String _name = null;
                    try {
                        jreader = new JSONObject(file_url);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Id, jreader.getString(TAG_User_ID));
                        editor.putString(Name, jreader.getString(TAG_User_Name));
                        editor.putString(Email, jreader.getString(TAG_User_Email));
                        editor.putString(Phone, jreader.getString(TAG_User_M));
                        editor.putBoolean(loggedIn, true);
                        editor.commit();
                        Toast.makeText(loginActivity.this, "logged In", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(loginActivity.this, MainActivity.class));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(loginActivity.this, file_url, Toast.LENGTH_LONG).show();

                }
            }

        }

    }
}
