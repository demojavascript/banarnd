package com.accelity.wow21.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class ChangePwdActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private User user;
    private UserPref userPref;
    private ChangePwdActivity fthis;
    private ProgressDialog pDialog = null;
    private JSONParser jsonParser = new JSONParser();
    private Button btn_changepwd, btn_forgetpwd;
    private EditText ed_cpwd, ed_npwd, ed_cnpwd;
    private ImageView img_cpwd, img_cnpwd;
    String newpassword = "";
    private NetConnection netConnection;
    private Boolean isShowing = false, isChangable = false;
    private JSONObject jsonCPWD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);

        fthis = this;
        userPref = new UserPref(this);
        user = userPref.getUser(this);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.change_pwd_title));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btn_changepwd = (Button)findViewById(R.id.btn_changepwd);
        btn_changepwd.setOnClickListener(this);
        btn_forgetpwd = (Button)findViewById(R.id.btn_forgetpwd);
        btn_forgetpwd.setOnClickListener(this);
        ed_cpwd = (EditText)findViewById(R.id.ed_cpwd);
        ed_npwd = (EditText)findViewById(R.id.ed_npwd);
        ed_cnpwd = (EditText)findViewById(R.id.ed_cnpwd);
        ed_cnpwd.setOnClickListener(this);
        img_cpwd = (ImageView)findViewById(R.id.img_cpwd);
        img_cpwd.setOnClickListener(this);
        img_cnpwd = (ImageView)findViewById(R.id.img_cnpwd);


        ed_cnpwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

            }
            @Override
            public void beforeTextChanged(CharSequence s, int arg1, int arg2, int arg3) {

            }
            @Override
            public void afterTextChanged(Editable arg0) {
                if (ed_cnpwd.getText().toString().trim().equals(ed_npwd.getText().toString().trim())) {
                    img_cnpwd.setVisibility(View.VISIBLE);
                    img_cnpwd.setImageResource(R.drawable.ic_correct);
                } else {
                    img_cnpwd.setVisibility(View.VISIBLE);
                    img_cnpwd.setImageResource(R.drawable.ic_error);
                }

            }
        });
        isChangable = false;

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(pDialog!= null){
            pDialog.dismiss();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.ed_cnpwd:
                isChangable = true;
                break;
            case R.id.btn_forgetpwd:
                Intent intent = new Intent(ChangePwdActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_changepwd:

                if(ed_cpwd.getText().toString().length() < 1){
                    Toast.makeText(getApplicationContext(), CustMessages.getCurrentPwdMissText(fthis), Toast.LENGTH_LONG).show();
                    ed_cpwd.requestFocus();
                }else if(!ed_cpwd.getText().toString().equals(user.getPassword())){
                    Toast.makeText(getApplicationContext(), CustMessages.getCurrentPwdIncText(fthis), Toast.LENGTH_LONG).show();
                    ed_cpwd.requestFocus();
                }else if(ed_npwd.getText().toString().length() < 1){
                    Toast.makeText(getApplicationContext(), CustMessages.getNewPwdMissText(fthis), Toast.LENGTH_LONG).show();
                    ed_npwd.requestFocus();
                }else if(ed_cnpwd.getText().toString().length() < 1){
                    Toast.makeText(getApplicationContext(), "Confirm Password", Toast.LENGTH_LONG).show();
                    ed_cnpwd.requestFocus();
                    img_cnpwd.setImageResource(R.drawable.ic_error);
                }else if(!ed_cnpwd.getText().toString().equals(ed_npwd.getText().toString())){
                    Toast.makeText(getApplicationContext(), CustMessages.getMismatchPwd(fthis), Toast.LENGTH_LONG).show();
                    ed_cnpwd.requestFocus();
                    img_cnpwd.setImageResource(R.drawable.ic_error);
                }else{
                    newpassword = ed_npwd.getText().toString().trim();
                    netConnection = new NetConnection();
                    Map<String, String> networkDetails = netConnection.getConnectionDetails(ChangePwdActivity.this);
                    if(!networkDetails.isEmpty()) {
                        new ChangePassword().execute();
                    } else {
                        Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(fthis), Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.img_cpwd:
                //isChangable = false;
                img_cnpwd.setVisibility(View.GONE);
                isShowing = !isShowing;
                if(isShowing){
                    ed_cpwd.setInputType(InputType.TYPE_NULL);
                    ed_cpwd.setRawInputType(InputType.TYPE_CLASS_TEXT);
                    ed_npwd.setInputType(InputType.TYPE_NULL);
                    ed_npwd.setRawInputType(InputType.TYPE_CLASS_TEXT);
                    ed_cnpwd.setInputType(InputType.TYPE_NULL);
                    ed_cnpwd.setRawInputType(InputType.TYPE_CLASS_TEXT);
                    img_cpwd.setImageResource(R.drawable.ic_view_password_enable);
                }else{
                    ed_cpwd.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD );
                    ed_cpwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ed_npwd.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD );
                    ed_npwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ed_cnpwd.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD );
                    ed_cnpwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    img_cpwd.setImageResource(R.drawable.ic_view_password_disable);
                }
                img_cnpwd.setVisibility(View.GONE);
                break;
        }
    }



    class ChangePassword extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = CProgress.ctor(ChangePwdActivity.this);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... args) {
            JSONObject objData = new JSONObject();
            try {
                objData.put("userMobNo", user.getMobile());
                objData.put("userPassword", newpassword);
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
                            Toast.makeText(getApplicationContext(), CustMessages.getPwdChngSu(fthis), Toast.LENGTH_LONG).show();
                            userPref.setPassword(newpassword);
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

}
