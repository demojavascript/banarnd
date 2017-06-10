package com.accelity.wow21.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.accelity.wow21.R;
import com.accelity.wow21.components.CProgress;
import com.accelity.wow21.library.JSONParser;
import com.accelity.wow21.models.User;
import com.accelity.wow21.models.UserPref;
import com.accelity.wow21.utilities.ErrorMessages;
import com.accelity.wow21.utilities.NetConnection;
import com.accelity.wow21.utilities.URLManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnLogin, btnSignup, btnForgetPwd;
    private EditText edMobileno, edPwd;
    private ImageView img_password;
    private LinearLayout lout_login_emailmobile, lout_login_pwd;
    private NetConnection netConnection;
    private String uemail, upassword;
    private ProgressDialog pDialog = null;
    private JSONParser jsonParser = new JSONParser();
    private JSONObject json;
    private User objuser;
    private UserPref userPref;
    private Boolean ed_password_flag = false;
    private LoginActivity fthis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fthis = this;
        img_password = (ImageView)findViewById(R.id.img_password);
        img_password.setOnClickListener(this);
        lout_login_pwd = (LinearLayout)findViewById(R.id.lout_login_pwd);
        lout_login_emailmobile = (LinearLayout)findViewById(R.id.lout_login_emailmobile);

        edMobileno = (EditText)findViewById(R.id.ed_mobileno);
        edMobileno.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

            }
            @Override
            public void beforeTextChanged(CharSequence s, int arg1, int arg2, int arg3) {

            }
            @Override
            public void afterTextChanged(Editable arg0) {
                if(edMobileno.getText().toString().trim().length() < 1){
                    lout_login_emailmobile.setBackgroundResource(R.drawable.bottom_brdr_style_error);
                }else{
                    lout_login_emailmobile.setBackgroundResource(R.drawable.bottom_brdr_style_default);
                }
            }
        });
        edPwd = (EditText)findViewById(R.id.ed_password);
        edPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

            }
            @Override
            public void beforeTextChanged(CharSequence s, int arg1, int arg2, int arg3) {

            }
            @Override
            public void afterTextChanged(Editable arg0) {
                if(edPwd.getText().toString().trim().length() < 1){
                    lout_login_pwd.setBackgroundResource(R.drawable.bottom_brdr_style_error);
                }else{
                    lout_login_pwd.setBackgroundResource(R.drawable.bottom_brdr_style_default);
                }
            }
        });
        btnLogin = (Button)findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
        btnSignup = (Button)findViewById(R.id.btn_signup);
        btnSignup.setOnClickListener(this);
        btnForgetPwd = (Button)findViewById(R.id.btn_forgetpwd);
        btnForgetPwd.setOnClickListener(this);

        userPref = new UserPref(this);
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
    public void onClick(View v) {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        switch(v.getId()){
            case R.id.img_password:
                ed_password_flag = !ed_password_flag;
                if(ed_password_flag){
                    edPwd.setInputType(InputType.TYPE_NULL);
                    edPwd.setRawInputType(InputType.TYPE_CLASS_TEXT);
                    edPwd.setSelection(edPwd.getText().length());
                    img_password.setImageResource(R.drawable.ic_view_password_enable);
                }else{
                    edPwd.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD );
                    edPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    edPwd.setSelection(edPwd.getText().length());
                    img_password.setImageResource(R.drawable.ic_view_password_disable);
                }
                break;
            case R.id.btn_login:
                if(edMobileno.getText().toString().trim().length()<1){
                    edMobileno.requestFocus();
                    lout_login_emailmobile.setBackgroundResource(R.drawable.bottom_brdr_style_error);
                    Toast.makeText(getApplicationContext(), ErrorMessages.getMobileEmailMissing(fthis), Toast.LENGTH_LONG).show();
                }else if(edPwd.getText().toString().trim().length()<1){
                    edPwd.requestFocus();
                    lout_login_pwd.setBackgroundResource(R.drawable.bottom_brdr_style_error);
                    Toast.makeText(getApplicationContext(), ErrorMessages.getPasswordMissing(fthis), Toast.LENGTH_LONG).show();
                }else{
                    netConnection = new NetConnection();
                    Map<String, String> networkDetails = netConnection.getConnectionDetails(LoginActivity.this);
                    if(!networkDetails.isEmpty()) {
                        uemail = edMobileno.getText().toString();
                        upassword = edPwd.getText().toString();
                        new AttemptLogin().execute();
                    }else{
                        Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(fthis), Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.btn_signup:
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                finish();
                break;
            case R.id.btn_forgetpwd:
                startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
                break;
        }
    }

    class AttemptLogin extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = CProgress.ctor(LoginActivity.this);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... args) {
            JSONObject objData = new JSONObject();
            try{
                objData.put("userMobNo", uemail);
                objData.put("userEmail", "");
                objData.put("appsource", "customerapp");
                objData.put("userPassword", upassword);
                objData.put("deviceId", userPref.getUser(fthis).getDeviceID());
                json = jsonParser.makeHttpRequestJSON(URLManager.getUserLoginUrl(), "POST", objData);
                Log.d("jdon", objData+"");
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String responseData) {
            try {
                if (json != null) {
                    if (json.getString("Message").toString().trim().equals("error")) {
                        pDialog.dismiss();
                        Toast.makeText(getApplicationContext(), json.getString("MessageText").toString(), Toast.LENGTH_LONG).show();
                    } else if (json.getString("Message").toString().trim().equals("success")) {
                        JSONObject objj = json.getJSONArray("ResponseObject").getJSONObject(0);
                        objuser = new User();
                        objuser.setID(json.getString("ID").toString());
                        objuser.setPassword(objj.getString("userPassword"));
                        objuser.setMobile(objj.getString("userMobNo"));
                        objuser.setFName(objj.getString("userName"));
                        objuser.setLName(objj.getString("userlastname"));
                        objuser.setEmail(objj.getString("userEmail"));
                        objuser.setCountry("");
                        objuser.setState("");
                        objuser.setCity("");
                        objuser.setAddress("");
                        objuser.setLat(objj.getString("latitude"));
                        objuser.setLong(objj.getString("longitude"));
                        objuser.setImg(objj.getString("imagename"));
                        objuser.setLoggedIn(true);
                        objuser.setDob(objj.getString("dob"));
                        objuser.setGender(objj.getString("gender"));
                        objuser.setMStatus(objj.getString("maritialstatus"));
                        objuser.setDeviceID(objj.getString("deviceid"));
                        objuser.setOS(objj.getString("os"));
                        userPref.setUser(objuser);
                        userPref.setLat(objj.getString("latitude"));
                        userPref.setLong(objj.getString("longitude"));

                        pDialog.dismiss();
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    } else {
                        pDialog.dismiss();
                    }
                } else {
                    pDialog.dismiss();
                }
            }catch(Exception ex){
                pDialog.dismiss();
                ex.printStackTrace();
            }
        }
    }
}
