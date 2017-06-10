package com.nitinguru.app.nitinguru.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nitinguru.app.nitinguru.R;
import com.nitinguru.app.nitinguru.libs.JSONParser;
import com.nitinguru.app.nitinguru.models.UserPref;
import com.nitinguru.app.nitinguru.utils.ErrorMessages;
import com.nitinguru.app.nitinguru.utils.Helper;
import com.nitinguru.app.nitinguru.utils.NetConnection;
import com.nitinguru.app.nitinguru.utils.URLManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnLogin;
    private UserPref userPref;
    private EditText edMobno, edEmail;
    private JSONObject json;
    private ProgressDialog pDialog = null;
    private JSONParser jsonParser = new JSONParser();
    private String userMob, userEmail, userDKey;
    private NetConnection netConnection;
    private LoginActivity fthis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fthis = this;
        userPref = new UserPref(this);
        userDKey = userPref.getDevice_Key();
        Log.d("mykey", userPref.getDevice_Key());

        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        edMobno = (EditText)findViewById(R.id.edMobno);
        edEmail = (EditText)findViewById(R.id.edEmail);
    }

    @Override
    public void onResume() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onResume();
    }

    @Override
    public void onStart() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onStart();
    }
    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        if(pDialog != null){
            pDialog.dismiss();
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnLogin:
                if(edMobno.getText().length() < 10){
                    Toast.makeText(getApplicationContext(), "Please Enter Correct Mobile Number.", Toast.LENGTH_LONG).show();
                }else  if(!Helper.validateEmailAddress(edEmail.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Please Enter Correct Email Address.", Toast.LENGTH_LONG).show();
                }else{
                    userEmail = edEmail.getText().toString();
                    userMob = edMobno.getText().toString();
                    netConnection = new NetConnection();
                    Map<String, String> networkDetails3 = netConnection.getConnectionDetails(LoginActivity.this);
                    if(!networkDetails3.isEmpty()) {
                        new AttemptRegister().execute();
                    }else{
                        Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(this), Toast.LENGTH_LONG).show();
                    }
                }
                break;

        }
    }

    class AttemptRegister extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(fthis);
            pDialog.setMessage("Its loading....");
            pDialog.setTitle("ProgressDialog bar example");
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            JSONObject objData = new JSONObject();
            try {
                objData.put("mobileno", userMob);
                objData.put("email", userEmail);
                objData.put("fcmkey", userDKey);
                objData.put("useros", "android");


                json = jsonParser.makeHttpRequestJSON(URLManager.getAddUserUrl(), "POST", objData);
                Log.d("jsonresp", json.toString());

            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String responseData) {
            pDialog.dismiss();
            try{
                if(json.getInt("status") == 1){
                    userPref.setUser_mob(userMob);
                    userPref.setUser_email(userEmail);
                    Intent objintent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(objintent);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), json.getString("msg"), Toast.LENGTH_LONG).show();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
