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

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout layout_step1, objlayout_names_first, objlayout_names_last, objlayout_pwd,  objlayout_mobile;
    private Button btnLogin, btnSignup,  register_btn_tac, register_btn_pp;
    private EditText ed_firstname, ed_lastname, ed_password, ed_mobile, ed_srcode;
    private ImageView imgPassword, imgMobileno;
    private Boolean ed_password_flag = false;
    private String fname, lname, pwd, mobileno, code;
    private NetConnection netConnection;

    private ProgressDialog pDialog = null;
    private JSONParser jsonParser = new JSONParser();
    private JSONObject json, jsonOTP;
    private UserPref userPref;
    private User objuser;
    private SignupActivity fthis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        fthis = this;
        objlayout_names_first = (LinearLayout)findViewById(R.id.layout_names_first);
        objlayout_names_last = (LinearLayout)findViewById(R.id.layout_names_last);
        objlayout_pwd = (LinearLayout)findViewById(R.id.layout_password);
        objlayout_mobile = (LinearLayout)findViewById(R.id.layout_mobileno);
        layout_step1 = (LinearLayout)findViewById(R.id.layout_step1);

        imgPassword = (ImageView)findViewById(R.id.img_password);
        imgPassword.setOnClickListener(this);
        imgMobileno = (ImageView)findViewById(R.id.img_mobileno);

        ed_firstname = (EditText)findViewById(R.id.ed_firstname);
        ed_lastname = (EditText)findViewById(R.id.ed_lastname);
        ed_password = (EditText)findViewById(R.id.ed_password);
        ed_mobile = (EditText)findViewById(R.id.ed_mobileno);
        ed_srcode = (EditText)findViewById(R.id.ed_srcode);

        btnLogin = (Button)findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
        btnSignup = (Button)findViewById(R.id.btn_signup);
        btnSignup.setOnClickListener(this);

        register_btn_tac = (Button)findViewById(R.id.register_btn_tac);
        register_btn_tac.setOnClickListener(this);
        register_btn_pp = (Button)findViewById(R.id.register_btn_pp);
        register_btn_pp.setOnClickListener(this);


        ed_mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int arg1, int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                if (ed_mobile.getText().toString().trim().length() == 10){
                    objlayout_mobile.setBackgroundResource(R.drawable.bottom_brdr_style_default);
                    imgMobileno.setImageResource(R.drawable.ic_correct);
                    imgMobileno.setVisibility(View.VISIBLE);
                }else{
                    objlayout_mobile.setBackgroundResource(R.drawable.bottom_brdr_style_error);
                    imgMobileno.setImageResource(R.drawable.ic_error);
                    imgMobileno.setVisibility(View.VISIBLE);
                }
            }
        });


        userPref = new UserPref(this);
    }

    @Override
    public void onClick(View v) {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        switch(v.getId()){
            case R.id.register_btn_pp:
                Intent cnt3  = new Intent(SignupActivity.this, ContentActivity.class);
                cnt3.putExtra("contentpage", 3);
                startActivity(cnt3);
            break;
            case R.id.register_btn_tac:
                Intent cnt4  = new Intent(SignupActivity.this, ContentActivity.class);
                cnt4.putExtra("contentpage", 4);
                startActivity(cnt4);
            break;
            case R.id.btn_login:
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                finish();
                break;
            case R.id.btn_signup:
                checSignup();
                break;
            case R.id.img_password:
                ed_password_flag = !ed_password_flag;
                if(ed_password_flag){
                    ed_password.setInputType(InputType.TYPE_NULL);
                    ed_password.setRawInputType(InputType.TYPE_CLASS_TEXT);
                    ed_password.setSelection(ed_password.getText().length());
                    //ed_cpassword.setSelection(ed_password.getText().length());
                    imgPassword.setImageResource(R.drawable.ic_view_password_enable);
                }else{
                    ed_password.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD );
                    ed_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ed_password.setSelection(ed_password.getText().length());
                    imgPassword.setImageResource(R.drawable.ic_view_password_disable);
                }
                break;
        }
    }

    public void checSignup() {

        InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        objlayout_names_first.setBackgroundResource(R.drawable.bottom_brdr_style_default);
        objlayout_names_last.setBackgroundResource(R.drawable.bottom_brdr_style_default);
        objlayout_pwd.setBackgroundResource(R.drawable.bottom_brdr_style_default);
        objlayout_mobile.setBackgroundResource(R.drawable.bottom_brdr_style_default);

        imgMobileno.setVisibility(View.INVISIBLE);

        if (ed_firstname.getText().toString().trim().length() < 1) {
            ed_firstname.requestFocus();
            Toast.makeText(getApplicationContext(), ErrorMessages.getFirstNameMissing(fthis), Toast.LENGTH_LONG).show();
            objlayout_names_first.setBackgroundResource(R.drawable.bottom_brdr_style_error);
        }else if (ed_password.getText().toString().trim().length() < 1) {
            ed_password.requestFocus();
            Toast.makeText(getApplicationContext(), ErrorMessages.getPasswordMissing(fthis), Toast.LENGTH_LONG).show();
            objlayout_pwd.setBackgroundResource(R.drawable.bottom_brdr_style_error);
        }  else if(ed_mobile.getText().toString().trim().length() < 10){
            ed_mobile.requestFocus();
            Toast.makeText(getApplicationContext(), ErrorMessages.getMobileMissing(fthis), Toast.LENGTH_LONG).show();
            objlayout_mobile.setBackgroundResource(R.drawable.bottom_brdr_style_error);
        }else if(ed_mobile.getText().toString().trim().length() > 0 && ed_mobile.getText().toString().trim().length() < 10){
            ed_mobile.requestFocus();
            Toast.makeText(getApplicationContext(), ErrorMessages.getMobileValidation(fthis), Toast.LENGTH_LONG).show();
            objlayout_mobile.setBackgroundResource(R.drawable.bottom_brdr_style_error);
        }else{
            netConnection = new NetConnection();
            Map<String, String> networkDetails = netConnection.getConnectionDetails(SignupActivity.this);
            if(!networkDetails.isEmpty()) {
                fname = ed_firstname.getText().toString().trim();
                lname = ed_lastname.getText().toString().trim();
                mobileno = ed_mobile.getText().toString().trim();
                pwd = ed_password.getText().toString().trim();
                code = ed_srcode.getText().toString().trim();
                new ValidatingUser().execute();
            }else{
                Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(fthis), Toast.LENGTH_LONG).show();
            }
        }
    }

    class ValidatingUser extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = CProgress.ctor(SignupActivity.this);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... args) {
            JSONObject objData = new JSONObject();
            try {
                objData.put("userID", "0");
                objData.put("userMobNo", mobileno);
                objData.put("userlastname", lname);
                objData.put("userName", fname);
                objData.put("userPassword", pwd);
                objData.put("userEmail", "");
                objData.put("srcode", code);
                objData.put("os", "");
                objData.put("deviceId", "");
                json = jsonParser.makeHttpRequestJSON(URLManager.getValidateUserURL(), "POST", objData);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String responseData) {
            try {
                if (json != null) {
                    if(json.getString("Message").toString().trim().equals("error")){
                        pDialog.dismiss();
                        Toast.makeText(getApplicationContext(), json.getString("MessageText").toString(),Toast.LENGTH_LONG ).show();
                    }else if(json.getString("Message").toString().trim().equals("success")){
                        new GeneratingOtp().execute();
                    }else{
                        pDialog.dismiss();
                    }
                }else{
                    pDialog.dismiss();
                }
            }catch(Exception ex){
                pDialog.dismiss();
            }
        }
    }

    class GeneratingOtp extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... args) {
            JSONObject objData = new JSONObject();
            try {
                objData.put("mobileno", mobileno);
                jsonOTP = jsonParser.makeHttpRequestJSON(URLManager.getGenereateOTPURL(), "POST", objData);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String responseData) {
            try {
                if (jsonOTP != null) {
                    if(jsonOTP.getString("Message").toString().trim().equals("error")){
                        pDialog.dismiss();
                        Toast.makeText(getApplicationContext(), jsonOTP.getString("Message").toString(), Toast.LENGTH_LONG).show();
                    }else if(jsonOTP.getString("Message").toString().trim().equals("success")){
                        Intent objintent = new Intent(SignupActivity.this, PopupSignupOTPActivity.class);
                        objintent.putExtra("fname", fname);
                        objintent.putExtra("lname", lname);
                        objintent.putExtra("mobileno", mobileno);
                        objintent.putExtra("otp", jsonOTP.getJSONObject("ResponseObject").getString("otp"));
                        objintent.putExtra("otpkey", jsonOTP.getJSONObject("ResponseObject").getString("otpkey"));
                        pDialog.dismiss();
                        startActivityForResult(objintent, 200);
                    }else {
                        pDialog.dismiss();
                    }
                }
            }catch(Exception ex){
                pDialog.dismiss();
                ex.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)   {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 200){
            try {
                if (data.getIntExtra("status", 0) == 1) {
                    new AttemptRegister().execute();
                } else {

                }
            }catch(Exception ex){
                ex.printStackTrace();
            }finally {

            }
        }
    }

    class AttemptRegister extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = CProgress.ctor(SignupActivity.this);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            JSONObject objData = new JSONObject();
            try {
                objData.put("userID", "0");
                objData.put("userMobNo", mobileno);
                objData.put("userlastname", lname);
                objData.put("userName", fname);
                objData.put("userPassword", pwd);
                objData.put("userEmail", "");
                objData.put("appsource", "customerapp");
                objData.put("srcode", code);
                objData.put("deviceId", userPref.getUser(fthis).getDeviceID());
                objData.put("os", "android");

                json = jsonParser.makeHttpRequestJSON(URLManager.getUserSignupUrl(), "POST", objData);

            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String responseData) {
            try {
                if (json != null) {
                    if(json.getString("Message").toString().trim().equals("error")){
                        pDialog.dismiss();
                        Toast.makeText(getApplicationContext(), json.getString("MessageText").toString(),Toast.LENGTH_LONG ).show();
                    }else if(json.getString("Message").toString().trim().equals("success")){
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.signup_register_success), Toast.LENGTH_LONG ).show();
                        objuser = new User();
                        JSONObject objj = json.getJSONObject("ResponseObject");
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


                        pDialog.dismiss();
                        Intent intent = new Intent(SignupActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    }else{
                        pDialog.dismiss();
                    }
                }else{
                    pDialog.dismiss();
                }
            }catch(Exception ex){
                pDialog.dismiss();
                ex.printStackTrace();
            }
        }
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
    public void onDestroy() {
        super.onDestroy();
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
}
