package com.accelity.wow21.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.accelity.wow21.R;
import com.accelity.wow21.components.CProgress;
import com.accelity.wow21.library.JSONParser;
import com.accelity.wow21.utilities.ErrorMessages;
import com.accelity.wow21.utilities.NetConnection;
import com.accelity.wow21.utilities.URLManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class PopupSignupOTPActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edfname, edlname, edmobileno, ed_otp;
    private String otp, otpkey, mobileno;
    private Button btn_signup, btn_resendotp, register_btn_tac, register_btn_pp;
    private ProgressDialog pDialog = null;
    private LinearLayout layout_otp;
    private JSONParser jsonParser = new JSONParser();
    private JSONObject jsonOTPValidate, jsonOTP;
    private NetConnection netConnection;
    private PopupSignupOTPActivity fthis;
    private static PopupSignupOTPActivity inst;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_signup_otp);
        fthis = this;
        btn_signup = (Button)findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(this);
        btn_resendotp = (Button)findViewById(R.id.btn_resendotp);
        btn_resendotp.setOnClickListener(this);


        layout_otp = (LinearLayout) findViewById(R.id.layout_otp);

        edfname = (EditText) findViewById(R.id.ed_firstname);
        edlname = (EditText) findViewById(R.id.ed_lastname);
        edmobileno = (EditText) findViewById(R.id.ed_mobileno);
        ed_otp = (EditText) findViewById(R.id.ed_otp);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(!bundle.isEmpty()) {
            edfname.setText(bundle.getString("fname"));
            edlname.setText(bundle.getString("lname"));
            edmobileno.setText(bundle.getString("mobileno"));
            mobileno = bundle.getString("mobileno");
            otp = bundle.getString("otp");
            otpkey = bundle.getString("otpkey");
            ed_otp.requestFocus();
        }
    }

    @Override
    public void onClick(View v) {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        if(v.getId() == R.id.register_btn_pp){
            Intent cnt3  = new Intent(PopupSignupOTPActivity.this, ContentActivity.class);
            cnt3.putExtra("contentpage", 3);
            startActivity(cnt3);
        }
        if(v.getId() == R.id.register_btn_tac){
            Intent cnt4  = new Intent(PopupSignupOTPActivity.this, ContentActivity.class);
            cnt4.putExtra("contentpage", 4);
            startActivity(cnt4);
        }
        if(v.getId() == R.id.btn_signup){
            if(ed_otp.getText().toString().trim().length()<4){
                layout_otp.setBackgroundResource(R.drawable.bottom_brdr_style_error);
                Toast.makeText(getApplicationContext(), ErrorMessages.getOTPMissing(fthis), Toast.LENGTH_LONG).show();
            }else{
                netConnection = new NetConnection();
                Map<String, String> networkDetails = netConnection.getConnectionDetails(PopupSignupOTPActivity.this);
                if(!networkDetails.isEmpty()) {
                    otp = ed_otp.getText().toString();
                    new ValidatingOtp().execute();
                }else{
                    Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(fthis), Toast.LENGTH_LONG).show();
                }
            }
        }
        if(v.getId() == R.id.btn_resendotp){
            netConnection = new NetConnection();
            Map<String, String> networkDetails = netConnection.getConnectionDetails(PopupSignupOTPActivity.this);
            if(!networkDetails.isEmpty()) {
                ed_otp.setText("");
                new GeneratingOtp().execute();
            }else{
                Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(fthis), Toast.LENGTH_LONG).show();
            }
        }
    }

    class GeneratingOtp extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = CProgress.ctor(PopupSignupOTPActivity.this);
            pDialog.show();
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
                        Toast.makeText(getApplicationContext(), jsonOTP.getString("MessageText").toString(),Toast.LENGTH_LONG ).show();
                    }else if(jsonOTP.getString("Message").toString().trim().equals("success")){
                        otp = jsonOTP.getJSONObject("ResponseObject").getString("otp");
                        otpkey = jsonOTP.getJSONObject("ResponseObject").getString("otpkey");
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
        }
    }

    class ValidatingOtp extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = CProgress.ctor(PopupSignupOTPActivity.this);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... args) {
            JSONObject objData = new JSONObject();
            try {
                objData.put("mobileno", mobileno);
                objData.put("otpkey", otpkey);
                objData.put("otp", otp);
                jsonOTPValidate = jsonParser.makeHttpRequestJSON(URLManager.getValidateOTPURL(), "POST", objData);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String responseData) {
            try {
                if (jsonOTPValidate != null) {
                    if(jsonOTPValidate.getString("Message").toString().trim().equals("error")){
                        pDialog.dismiss();
                        Toast.makeText(getApplicationContext(), jsonOTPValidate.getString("MessageText").toString(), Toast.LENGTH_LONG).show();
                    }else if(jsonOTPValidate.getString("Message").toString().trim().equals("success")){
                        pDialog.dismiss();
                        Intent intent = new Intent();
                        intent.putExtra("status", 1);
                        setResult(200, intent);
                        finish();

                    }else {
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("status", 0);
        setResult(200, intent);
        super.onBackPressed();
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
        inst = this;
    }
    public static PopupSignupOTPActivity instance() {
        return inst;
    }
    public void updateOTP(final String smsMessage) {
        ed_otp.setText(smsMessage);
    }
}
