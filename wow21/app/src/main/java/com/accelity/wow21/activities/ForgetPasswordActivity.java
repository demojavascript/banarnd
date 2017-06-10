package com.accelity.wow21.activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.accelity.wow21.R;
import com.accelity.wow21.components.CProgress;
import com.accelity.wow21.library.JSONParser;
import com.accelity.wow21.models.User;
import com.accelity.wow21.models.UserPref;
import com.accelity.wow21.utilities.CustMessages;
import com.accelity.wow21.utilities.ErrorMessages;
import com.accelity.wow21.utilities.NetConnection;
import com.accelity.wow21.utilities.URLManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class ForgetPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout objLayout1, objLayout2;
    private ImageView img_password;
    private LinearLayout objLayout3, lout_fpwd_npwd, lout_fpwd_ncpwd, lout_fpwd_enterotp, lout_fpwd_emailormobile;
    private Button btnGenerateOTP, btnVerifyOTP, btnChangePwd, btnResendOTP;
    private EditText ed_emailmobile, ed_otp, ed_npwd, ed_ncpwd;
    private String str_newPass, str_cnewPass, str_email, str_otp, str_otpkey, validMob;
    private TextView otpsentText;
    private Boolean ed_password_flag = false;
    private ProgressDialog pDialog = null;
    private JSONParser jsonParser = new JSONParser();
    private JSONObject jsonOTPValidate, jsonOTP, jsonCPWD, jsonVUser;
    private NetConnection netConnection;
    private ForgetPasswordActivity fthis;
    private static ForgetPasswordActivity inst;
    private User user;
    private UserPref userPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        fthis = this;
        userPref = new UserPref(fthis);
        user = userPref.getUser(fthis);
        img_password = (ImageView)findViewById(R.id.img_password);
        img_password.setOnClickListener(this);

        objLayout1 = (RelativeLayout)findViewById(R.id.step_1);
        objLayout2 = (RelativeLayout)findViewById(R.id.step_2);
        objLayout3 = (LinearLayout) findViewById(R.id.step_3);

        lout_fpwd_npwd = (LinearLayout) findViewById(R.id.lout_fpwd_npwd);
        lout_fpwd_ncpwd = (LinearLayout) findViewById(R.id.lout_fpwd_ncpwd);
        lout_fpwd_enterotp = (LinearLayout) findViewById(R.id.lout_fpwd_enterotp);
        lout_fpwd_emailormobile = (LinearLayout) findViewById(R.id.lout_fpwd_emailormobile);


        otpsentText = (TextView) findViewById(R.id.otpsentText);

        ed_emailmobile = (EditText)findViewById(R.id.ed_emailormobile);
        ed_emailmobile.requestFocus();
        ed_emailmobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

            }
            @Override
            public void beforeTextChanged(CharSequence s, int arg1, int arg2, int arg3) {

            }
            @Override
            public void afterTextChanged(Editable arg0) {
                if(ed_emailmobile.getText().toString().trim().length() < 1){
                    lout_fpwd_emailormobile.setBackgroundResource(R.drawable.bottom_brdr_style_error);
                }else{
                    lout_fpwd_emailormobile.setBackgroundResource(R.drawable.bottom_brdr_style_default);
                }
            }
        });
        ed_otp = (EditText)findViewById(R.id.ed_enterotp);
        ed_otp.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

            }
            @Override
            public void beforeTextChanged(CharSequence s, int arg1, int arg2, int arg3) {

            }
            @Override
            public void afterTextChanged(Editable arg0) {
                if(ed_otp.getText().toString().trim().length() < 1){
                    lout_fpwd_enterotp.setBackgroundResource(R.drawable.bottom_brdr_style_error);
                }else{
                    lout_fpwd_enterotp.setBackgroundResource(R.drawable.bottom_brdr_style_default);
                }
            }
        });
        ed_npwd = (EditText)findViewById(R.id.ed_npwd);
        ed_npwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

            }
            @Override
            public void beforeTextChanged(CharSequence s, int arg1, int arg2, int arg3) {

            }
            @Override
            public void afterTextChanged(Editable arg0) {
                if(ed_npwd.getText().toString().trim().length() < 1){
                    lout_fpwd_npwd.setBackgroundResource(R.drawable.bottom_brdr_style_error);
                }else{
                    lout_fpwd_npwd.setBackgroundResource(R.drawable.bottom_brdr_style_default);
                }
            }
        });
        ed_ncpwd = (EditText)findViewById(R.id.ed_ncpwd);
        ed_ncpwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

            }
            @Override
            public void beforeTextChanged(CharSequence s, int arg1, int arg2, int arg3) {

            }
            @Override
            public void afterTextChanged(Editable arg0) {
                if(ed_ncpwd.getText().toString().trim().length() < 1){
                    lout_fpwd_ncpwd.setBackgroundResource(R.drawable.bottom_brdr_style_error);
                }else{
                    lout_fpwd_ncpwd.setBackgroundResource(R.drawable.bottom_brdr_style_default);
                }
            }
        });

        btnGenerateOTP = (Button)findViewById(R.id.btn_submit);
        btnVerifyOTP = (Button)findViewById(R.id.btn_submitotp);
        btnResendOTP = (Button)findViewById(R.id.btn_resendotp);
        btnChangePwd = (Button)findViewById(R.id.btn_chnagepwd);
        btnGenerateOTP.setOnClickListener(this);
        btnVerifyOTP.setOnClickListener(this);
        btnResendOTP.setOnClickListener(this);
        btnChangePwd.setOnClickListener(this);

        if(user.getLoggedIn()){
            objLayout1.setVisibility(View.GONE);
            objLayout2.setVisibility(View.VISIBLE);
            ed_emailmobile.setText(user.getMobile());
            str_email = ed_emailmobile.getText().toString();
            otpsentText.setText(getResources().getString(R.string.otp_txt11)+" "+user.getMobile()+getResources().getString(R.string.otp_txt12));
            netConnection = new NetConnection();
            Map<String, String> networkDetails = netConnection.getConnectionDetails(ForgetPasswordActivity.this);
            if (!networkDetails.isEmpty()) {
                ed_otp.setText("");
                new ValidatingUser().execute();
            } else {
                Toast.makeText(getApplicationContext(), ErrorMessages.getOTPMissing(fthis), Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public void onClick(View v) {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        switch(v.getId()){
            case R.id.img_password:
                ed_password_flag = !ed_password_flag;
                if(ed_password_flag){
                    ed_npwd.setInputType(InputType.TYPE_NULL);
                    ed_npwd.setRawInputType(InputType.TYPE_CLASS_TEXT);
                    ed_npwd.setSelection(ed_npwd.getText().length());

                    ed_ncpwd.setInputType(InputType.TYPE_NULL);
                    ed_ncpwd.setRawInputType(InputType.TYPE_CLASS_TEXT);
                    //ed_npwd.setSelection(ed_npwd.getText().length());
                    img_password.setImageResource(R.drawable.ic_view_password_enable);
                }else{
                    ed_npwd.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD );
                    ed_npwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ed_npwd.setSelection(ed_npwd.getText().length());
                    img_password.setImageResource(R.drawable.ic_view_password_disable);
                }
                break;
            case R.id.btn_submit:
                if(ed_emailmobile.getText().toString().trim().length() < 1){
                    lout_fpwd_emailormobile.setBackgroundResource(R.drawable.bottom_brdr_style_error);
                    ed_emailmobile.requestFocus();
                    Toast.makeText(getApplicationContext(), ErrorMessages.getMobileEmailMissing(fthis), Toast.LENGTH_LONG).show();
                }else if(ed_emailmobile.getText().toString().trim().length() != 10){
                    lout_fpwd_emailormobile.setBackgroundResource(R.drawable.bottom_brdr_style_error);
                    ed_emailmobile.requestFocus();
                    Toast.makeText(getApplicationContext(), "Please enter correct Mobile Number", Toast.LENGTH_LONG).show();
                }else {
                    str_email = ed_emailmobile.getText().toString();
                    netConnection = new NetConnection();
                    Map<String, String> networkDetails = netConnection.getConnectionDetails(ForgetPasswordActivity.this);
                    if (!networkDetails.isEmpty()) {
                        otpsentText.setText(getResources().getString(R.string.otp_txt11)+" "+str_email+" "+getResources().getString(R.string.otp_txt12));
                        new ValidatingUser().execute();
                    } else {
                        Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(fthis), Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.btn_submitotp:
                if(ed_otp.getText().toString().trim().length()<1){
                    lout_fpwd_enterotp.setBackgroundResource(R.drawable.bottom_brdr_style_error);
                    ed_otp.requestFocus();
                    Toast.makeText(getApplicationContext(), ErrorMessages.getOTPMissing(fthis), Toast.LENGTH_LONG).show();
                }else {
                    str_otp = ed_otp.getText().toString();
                    netConnection = new NetConnection();
                    Map<String, String> networkDetails = netConnection.getConnectionDetails(ForgetPasswordActivity.this);
                    if (!networkDetails.isEmpty()) {
                        new ValidatingOtp().execute();
                    } else {
                        Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(fthis), Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.btn_resendotp:
                if(ed_emailmobile.getText().toString().trim().length() < 1){
                    lout_fpwd_emailormobile.setBackgroundResource(R.drawable.bottom_brdr_style_error);
                    ed_emailmobile.requestFocus();
                    Toast.makeText(getApplicationContext(), ErrorMessages.getMobileEmailMissing(fthis), Toast.LENGTH_LONG).show();
                }else if(ed_emailmobile.getText().toString().trim().length() != 10){
                    lout_fpwd_emailormobile.setBackgroundResource(R.drawable.bottom_brdr_style_error);
                    ed_emailmobile.requestFocus();
                    Toast.makeText(getApplicationContext(), "Please enter correct Mobile Number", Toast.LENGTH_LONG).show();
                }else {
                    netConnection = new NetConnection();
                    Map<String, String> networkDetails = netConnection.getConnectionDetails(ForgetPasswordActivity.this);
                    if (!networkDetails.isEmpty()) {
                        ed_otp.setText("");
                        new ValidatingUser().execute();
                    } else {
                        Toast.makeText(getApplicationContext(), ErrorMessages.getOTPMissing(fthis), Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.btn_chnagepwd:
                if(ed_npwd.getText().toString().toString().length()<1){
                    lout_fpwd_npwd.setBackgroundResource(R.drawable.bottom_brdr_style_error);
                    ed_npwd.requestFocus();
                    Toast.makeText(getApplicationContext(), ErrorMessages.getPasswordMissing(fthis), Toast.LENGTH_LONG).show();
                }else if(ed_ncpwd.getText().toString().toString().length()<1){
                    lout_fpwd_ncpwd.setBackgroundResource(R.drawable.bottom_brdr_style_error);
                    ed_ncpwd.requestFocus();
                    Toast.makeText(getApplicationContext(), ErrorMessages.getConfirmPasswordMissing(fthis), Toast.LENGTH_LONG).show();
                }else if(!ed_npwd.getText().toString().trim().equals(ed_ncpwd.getText().toString().trim())){
                    lout_fpwd_ncpwd.setBackgroundResource(R.drawable.bottom_brdr_style_error);
                    ed_ncpwd.requestFocus();
                    Toast.makeText(getApplicationContext(), ErrorMessages.getPasswordMismatch(fthis), Toast.LENGTH_LONG).show();
                }else{
                    str_newPass = ed_npwd.getText().toString();
                    str_cnewPass = ed_ncpwd.getText().toString();
                    netConnection = new NetConnection();
                    Map<String, String> networkDetails = netConnection.getConnectionDetails(ForgetPasswordActivity.this);
                    if (!networkDetails.isEmpty()) {
                        new ChangePassword().execute();
                    } else {
                        Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(fthis), Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    class ValidatingUser extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog =  CProgress.ctor(ForgetPasswordActivity.this);;
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... args) {
            JSONObject objData = new JSONObject();
            try {
                objData.put("userMobNo", str_email);
                jsonVUser = jsonParser.makeHttpRequestJSON(URLManager.getValidateUserURL(), "POST", objData);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String responseData) {
            try {
                if (jsonVUser != null) {
                    if(jsonVUser.getString("Message").toString().trim().equals("error")){
                        new GeneratingOtp().execute();
                    }else if(jsonVUser.getString("Message").toString().trim().equals("success")){
                        pDialog.dismiss();
                        Toast.makeText(getApplicationContext(), CustMessages.getNoMoNoFound(fthis),Toast.LENGTH_LONG ).show();
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
                objData.put("mobileno", str_email);
                jsonOTP = jsonParser.makeHttpRequestJSON(URLManager.getGenereateOTPURL(), "POST", objData);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String responseData) {
            pDialog.dismiss();
            try {
                if (jsonOTP != null) {
                    if(jsonOTP.getString("Message").toString().trim().equals("error")){
                        Toast.makeText(getApplicationContext(), jsonOTP.getString("MessageText").toString(),Toast.LENGTH_LONG ).show();
                    }else if(jsonOTP.getString("Message").toString().trim().equals("success")){
                        str_otpkey = jsonOTP.getJSONObject("ResponseObject").getString("otpkey");
                        ed_otp.requestFocus();
                        objLayout1.setVisibility(View.GONE);
                        objLayout2.setVisibility(View.VISIBLE);

                    }else {

                    }
                }else{

                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }

    class ValidatingOtp extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = CProgress.ctor(ForgetPasswordActivity.this);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... args) {
            JSONObject objData = new JSONObject();
            try {
                objData.put("mobileno", str_email);
                objData.put("otpkey", str_otpkey);
                objData.put("otp", str_otp);
                jsonOTPValidate = jsonParser.makeHttpRequestJSON(URLManager.getValidateOTPURL(), "POST", objData);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String responseData) {
            if(jsonOTPValidate != null){
                try {
                    if (jsonOTPValidate != null) {
                        if(jsonOTPValidate.getString("Message").toString().trim().equals("error")){
                            pDialog.dismiss();
                            Toast.makeText(getApplicationContext(), jsonOTPValidate.getString("MessageText").toString(), Toast.LENGTH_LONG).show();
                        }else if(jsonOTPValidate.getString("Message").toString().trim().equals("success")){
                            ed_npwd.requestFocus();
                            objLayout2.setVisibility(View.GONE);
                            pDialog.dismiss();
                        }else {
                            pDialog.dismiss();
                        }
                    }else{
                        pDialog.dismiss();
                    }
                }catch(Exception ex){
                    pDialog.dismiss();
                }
            }else{
                pDialog.dismiss();
            }
        }
    }


    class ChangePassword extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = CProgress.ctor(ForgetPasswordActivity.this);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... args) {
            JSONObject objData = new JSONObject();
            try {
                objData.put("userMobNo", str_email);
                objData.put("userPassword", str_newPass);
                jsonCPWD = jsonParser.makeHttpRequestJSON(URLManager.getUserChnagePwdUrl(), "POST", objData);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String responseData) {
            if(jsonCPWD != null){
                try {
                    if (jsonCPWD != null) {
                        if(jsonCPWD.getString("Message").toString().trim().equals("error")){
                            pDialog.dismiss();
                            Toast.makeText(getApplicationContext(), jsonCPWD.getString("MessageText").toString(), Toast.LENGTH_LONG).show();
                        }else if(jsonCPWD.getString("Message").toString().trim().equals("success")){
                            pDialog.dismiss();
                            Toast.makeText(getApplicationContext(),CustMessages.getPwdChngSu(fthis), Toast.LENGTH_LONG).show();
                            finish();
                        }else {
                            pDialog.dismiss();                            ;
                        }
                    }
                }catch(Exception ex){
                    pDialog.dismiss();
                }
            }else{
                pDialog.dismiss();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
    @Override
    public void onResume() {
        //this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onResume();
    }

    @Override
    public void onStart() {
        //this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onStart();
        super.onStart();
        inst = this;
    }
    public static ForgetPasswordActivity instance() {
        return inst;
    }
    public void updateOTP(final String smsMessage) {
        ed_otp.setText(smsMessage);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
