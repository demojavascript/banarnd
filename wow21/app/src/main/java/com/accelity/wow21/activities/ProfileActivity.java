package com.accelity.wow21.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.accelity.wow21.R;
import com.accelity.wow21.components.CProgress;
import com.accelity.wow21.etc.DatabaseHandler;
import com.accelity.wow21.library.JSONParser;
import com.accelity.wow21.models.CustomAdsPref;
import com.accelity.wow21.models.User;
import com.accelity.wow21.models.UserPref;
import com.accelity.wow21.utilities.ErrorMessages;
import com.accelity.wow21.utilities.NetConnection;
import com.accelity.wow21.utilities.URLManager;
import com.bruce.pickerview.popwindow.DatePickerPopWin;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private User user;
    private UserPref userPref;
    private CustomAdsPref customAdsPref;
    private ProfileActivity fthis;
    private Boolean isEdit = false, isPopOpen = false;
    private ImageView img_userpic, img_calendar, img_password;
    private TextView ed_userdob, id_userMobile;
    private EditText ed_userfname, ed_userlname,ed_useremail,  ed_usergender2, ed_usermstatus2, ed_mobileno, ed_otp, ed_nowpassword, ed_mobilenoNew;
    private Button btnChnagePwd, btnlogoutt, img_profile, btn_submitOtp, btn_cancelMobilePwd, btn_submitPwd, btn_changepwd, btn_changemobileno,  btn_cancelMobile, btn_submit, btn_resendOTP;
    private Spinner ed_usermstatus, ed_usergender;
    private ArrayAdapter<String> mStatusAdapter, genderAdapter;
    private ArrayList<String> mStatusData, genderData;
    private ArrayList<String> listMonths;
    private ProgressDialog pDialog = null;
    private JSONParser jsonParser = new JSONParser();
    private JSONObject json, jsonOTP, jsonLogout,jsonOTPValidate, jsonProfileImg, jsonVUser;
    private String dob, gender, mstatus, fname, lname, email, str_otpkey, str_mobno, str_otp, cuufImgname = "", validMobno;
    private NetConnection netConnection;
    private RelativeLayout rout_stepmobile, rout_steppwd, rout_stepotp;
    private Boolean ed_password_flag = false, isChEnableEmail = false;
    private static ProfileActivity inst;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 124;
    private DatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        fthis = this;
        userPref = new UserPref(this);
        user = userPref.getUser(this);
        customAdsPref = new CustomAdsPref(this);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.Profile_title));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHandler(this);

        listMonths = new ArrayList<String>();
        listMonths.add("Jan");
        listMonths.add("Feb");
        listMonths.add("Mar");
        listMonths.add("Apr");
        listMonths.add("May");
        listMonths.add("Jun");
        listMonths.add("Jul");
        listMonths.add("Aug");
        listMonths.add("Sep");
        listMonths.add("Oct");
        listMonths.add("Nov");
        listMonths.add("Dec");

        btnlogoutt = (Button) findViewById(R.id.btnlogoutt);
        btnlogoutt.setPaintFlags(btnlogoutt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        btnlogoutt.setOnClickListener(this);

        btnChnagePwd = (Button) findViewById(R.id.btnChnagePwd);
        btnChnagePwd.setPaintFlags(btnChnagePwd.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        btnChnagePwd.setOnClickListener(this);

        rout_stepmobile = (RelativeLayout)findViewById(R.id.rout_stepmobile);
        rout_steppwd = (RelativeLayout)findViewById(R.id.rout_steppwd);
        rout_stepotp = (RelativeLayout)findViewById(R.id.rout_stepotp);

        img_password = (ImageView)findViewById(R.id.img_password);
        img_password.setOnClickListener(this);
        img_userpic = (ImageView)findViewById(R.id.img_userpic);
        img_userpic.setOnClickListener(this);
        img_profile = (Button) findViewById(R.id.img_profile);
        img_profile.setOnClickListener(this);
        img_calendar = (ImageView)findViewById(R.id.img_calendar);
        img_calendar.setOnClickListener(this);
        ed_mobilenoNew = (EditText)findViewById(R.id.ed_mobilenoNew);
        ed_userfname = (EditText)findViewById(R.id.ed_userfname);
        ed_userlname= (EditText)findViewById(R.id.ed_userlname);
        ed_useremail= (EditText)findViewById(R.id.ed_useremail);
        ed_mobileno = (EditText)findViewById(R.id.ed_mobileno);
        ed_otp = (EditText)findViewById(R.id.ed_otp);
        ed_usergender= (Spinner) findViewById(R.id.ed_usergender);
        ed_userdob = (TextView) findViewById(R.id.ed_userdob);
        ed_usergender2 = (EditText)findViewById(R.id.ed_usergender2);
        ed_usermstatus2 = (EditText)findViewById(R.id.ed_usermstatus2);

        id_userMobile = (TextView) findViewById(R.id.id_userMobile);


        btn_submitOtp = (Button)findViewById(R.id.btn_submitOtp);
        btn_submitOtp.setOnClickListener(this);
        btn_submitPwd = (Button)findViewById(R.id.btn_submitPwd);
        btn_submitPwd.setOnClickListener(this);

        ed_nowpassword = (EditText)findViewById(R.id.ed_nowpassword);

        mStatusData = new ArrayList<String>();
        for(int i=0;i<getResources().getStringArray(R.array.mStatusOptions).length; i++){
            mStatusData.add(getResources().getStringArray(R.array.mStatusOptions)[i].toString());
        }

        genderData = new ArrayList<String>();
        for(int i=0;i<getResources().getStringArray(R.array.genderOptions).length; i++){
            genderData.add(getResources().getStringArray(R.array.genderOptions)[i].toString());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.row_spinner_mstatus,R.id.textview, getResources().getStringArray(R.array.mStatusOptions));
        ed_usermstatus = (Spinner)findViewById(R.id.ed_usermstatus);
        ed_usermstatus.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, R.layout.row_spinner_mstatus,R.id.textview, getResources().getStringArray(R.array.genderOptions));
        ed_usergender = (Spinner)findViewById(R.id.ed_usergender);
        ed_usergender.setAdapter(adapter2);

        if(user.getMStatus() != null && user.getMStatus().length() > 0 && user.getMStatus().length() < 2){
            ed_usermstatus.setSelection(Integer.parseInt(user.getMStatus()));
            ed_usermstatus.getSelectedItemPosition();
            if(Integer.parseInt(user.getMStatus()) > 0 ){
                ed_usermstatus2.setText(mStatusData.get(Integer.parseInt(user.getMStatus())).toString());
            }
        }else{
            ed_usermstatus.setSelection(0);
            ed_usermstatus.getSelectedItemPosition();
        }

        if(user.getGender() != null && user.getGender().length() > 0 && user.getGender().length() < 2){
            ed_usergender.setSelection(Integer.parseInt(user.getGender()));
            ed_usergender.getSelectedItemPosition();
            if(Integer.parseInt(user.getGender()) > 0 ){
                ed_usergender2.setText(genderData.get(Integer.parseInt(user.getGender())).toString());
            }
        }else{
            ed_usergender.setSelection(0);
            ed_usergender.getSelectedItemPosition();
        }

        if(user.getFName() != null){
            ed_userfname.setText(user.getFName());
        }
        if(user.getLName() != null){
            ed_userlname.setText(user.getLName());
        }
        if(user.getEmail() != null){
            ed_useremail.setText(user.getEmail());
        }
        if(user.getDob() != null && user.getDob().length()>4){
            String[] splitStr = user.getDob().split("-");
            String hhh = splitStr[1]+" "+listMonths.get(Integer.parseInt(splitStr[0])-1)+" "+splitStr[2];
            ed_userdob.setText(hhh);
            //ed_userdob.setText(user.getDob());
            dob = user.getDob();
        }else{
            ed_userdob.setText("");
        }

        btn_changemobileno = (Button)findViewById(R.id.btn_changemobileno);
        btn_changemobileno.setOnClickListener(this);
        btn_changemobileno.setPaintFlags(btn_changemobileno.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        btn_changepwd = (Button)findViewById(R.id.btn_changepwd);
        btn_changepwd.setPaintFlags(btn_changepwd.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        btn_changepwd.setOnClickListener(this);
        //btn_logout = (ImageView) findViewById(R.id.btn_logout);
        //btn_logout.setOnClickListener(this);


        btn_resendOTP = (Button)findViewById(R.id.btn_resendOTP);
        btn_resendOTP.setOnClickListener(this);
        btn_submit = (Button)findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);


        if(user.getImg() != null && user.getImg().trim().length() > 0){
            if(readFileToInternalStorage() != null){
                img_userpic.setImageBitmap(readFileToInternalStorage());
            }else{
                img_userpic.setImageResource(R.drawable.user_profile_p);
            }
        }else{
            if(user.getGender() != null && user.getGender().length() > 0 && user.getGender().length() < 2){
                if(user.getGender().equals("1")){
                    img_userpic.setImageResource(R.drawable.user_profile_p);
                }else if(user.getGender().equals("2")){
                    img_userpic.setImageResource(R.drawable.user_profile_f);
                }
            }else{
                img_userpic.setImageResource(R.drawable.user_profile_p);
            }
        }
        isEdit = false;
        isPopOpen = false;
        id_userMobile.setText(user.getMobile());

    }

    public Bitmap readFileToInternalStorage(){
        Bitmap bitmap = null;
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("profile", Context.MODE_PRIVATE);
        File mypath = new File(directory, user.getImg());
        try {
            bitmap = BitmapFactory.decodeFile(mypath.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public void confirmlogout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.logout_title))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.logout_btn_confirm_text), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        netConnection = new NetConnection();
                        Map<String, String> networkDetails2 = netConnection.getConnectionDetails(ProfileActivity.this);
                        if (!networkDetails2.isEmpty()) {
                            new StatusLogout().execute();
                        } else {
                            logout();
                        }
                    }
                })
                .setNegativeButton(getResources().getString(R.string.logout_btn_cancel_text), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onResume() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onResume();
        userPref = new UserPref(this);
        user = userPref.getUser(this);
    }

    @Override
    public void onStart() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onStart();
        inst = this;
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(!isPopOpen){
            int id = item.getItemId();
            if (id == R.id.action_editprofile) {
                if(isEdit) {
                    if(ed_usergender.getSelectedItemPosition() > 0){
                        gender = ed_usergender.getSelectedItemPosition()+"";
                    }
                    if(ed_usermstatus.getSelectedItemPosition() > 0){
                        mstatus = ed_usermstatus.getSelectedItemPosition()+"";
                    }
                    fname = ed_userfname.getText().toString().trim();
                    lname = ed_userlname.getText().toString().trim();
                    //email = ed_useremail.getText().toString().trim();

                    if(fname.length()<1){
                        ed_userfname.requestFocus();
                        Toast.makeText(getApplicationContext(), "First Name can not be blank", Toast.LENGTH_SHORT).show();
                    }else if(lname.length()<1){
                        ed_userlname.requestFocus();
                        Toast.makeText(getApplicationContext(), "Last Name can not be blank", Toast.LENGTH_SHORT).show();
                    }else{
                        netConnection = new NetConnection();
                        Map<String, String> networkDetails = netConnection.getConnectionDetails(ProfileActivity.this);
                        if(!networkDetails.isEmpty()) {
                            new UpdateUser().execute();
                        }else{
                            Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(fthis), Toast.LENGTH_LONG).show();
                        }
                    }


                }else{
                    isEdit = true;
                    ActionMenuItemView itemm = (ActionMenuItemView) findViewById(R.id.action_editprofile);
                    itemm.setTitle("Save");
                    btnChnagePwd.setVisibility(View.VISIBLE);
                    btnChnagePwd.setTextColor(getResources().getColor(R.color.colorAnchorBlue));
                    ed_userfname.setEnabled(true);
                    ed_userfname.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    ed_userfname.requestFocus();
                    ed_userlname.setEnabled(true);
                    ed_userlname.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    //ed_useremail.setEnabled(true);
                    //ed_useremail.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    ed_userdob.setEnabled(true);
                    ed_userdob.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    ed_usergender2.setVisibility(View.GONE);
                    ed_usermstatus2.setVisibility(View.GONE);
                    ed_usergender.setVisibility(View.VISIBLE);
                    ed_usermstatus.setVisibility(View.VISIBLE);
                    img_calendar.setVisibility(View.VISIBLE);
                }
            }
            if(id == android.R.id.home){
                if(rout_stepmobile.getVisibility() == View.VISIBLE){
                    rout_stepmobile.setVisibility(View.GONE);
                }else if(rout_stepotp.getVisibility() == View.VISIBLE){
                    rout_stepotp.setVisibility(View.GONE);
                }else if(rout_steppwd.getVisibility() == View.VISIBLE){
                    rout_steppwd.setVisibility(View.GONE);
                }else {
                    finish();
                }
            }
        }

        return super.onOptionsItemSelected(item);
    }
    private void logout(){
        userPref.setLogout();
        customAdsPref.setAdsId("");
        db.deleteNotification();
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        startActivity(intent);
        finishAffinity();
    }
    @Override
    public void onBackPressed() {
        if(rout_stepmobile.getVisibility() == View.VISIBLE){
            rout_stepmobile.setVisibility(View.GONE);
        }else if(rout_stepotp.getVisibility() == View.VISIBLE){
            rout_stepotp.setVisibility(View.GONE);
        }else if(rout_steppwd.getVisibility() == View.VISIBLE){
            rout_steppwd.setVisibility(View.GONE);
        }else {
            super.onBackPressed();
            finish();
        }
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.btnChnagePwd:
                if(isChEnableEmail){
                    if (ed_useremail.getText().toString().trim().length() < 1) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.profile_email_missing), Toast.LENGTH_LONG).show();
                    }else if (ed_useremail.getText().toString().trim().equals(user.getEmail())) {
                        Toast.makeText(getApplicationContext(), "Email updated successfully.", Toast.LENGTH_LONG).show();
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                    } else {
                        email = ed_useremail.getText().toString().trim();
                        netConnection = new NetConnection();
                        Map<String, String> networkDetails1 = netConnection.getConnectionDetails(ProfileActivity.this);
                        if (!networkDetails1.isEmpty()) {
                            new UpdateEmailId().execute();
                        } else {
                            Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(fthis), Toast.LENGTH_LONG).show();
                        }
                    }
                }else{
                    btnChnagePwd.setText("Update Email ID");
                    isChEnableEmail = true;
                    ed_useremail.setEnabled(true);
                    ed_useremail.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    ed_useremail.requestFocus();
                    ed_useremail.setSelection(ed_useremail.getText().length());
                    InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
                }
                break;
            case R.id.img_password:
                InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
                ed_password_flag = !ed_password_flag;
                if(ed_password_flag){
                    ed_nowpassword.setInputType(InputType.TYPE_NULL);
                    ed_nowpassword.setRawInputType(InputType.TYPE_CLASS_TEXT);
                    ed_nowpassword.setSelection(ed_nowpassword.getText().length());
                    img_password.setImageResource(R.drawable.ic_view_password_enable);
                }else{
                    ed_nowpassword.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD );
                    ed_nowpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ed_nowpassword.setSelection(ed_nowpassword.getText().length());
                    img_password.setImageResource(R.drawable.ic_view_password_disable);
                }
                break;
            case R.id.btn_submitOtp:
                InputMethodManager imm2 =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm2.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
                if(ed_otp.getText().toString().trim().length()<4){
                    Toast.makeText(getApplicationContext(), "Please enter OTP", Toast.LENGTH_LONG).show();
                    ed_otp.requestFocus();
                }else{
                    str_otp = ed_otp.getText().toString().trim();
                    netConnection = new NetConnection();
                    Map<String, String> networkDetails1 = netConnection.getConnectionDetails(ProfileActivity.this);
                    if (!networkDetails1.isEmpty()) {
                        new ValidatingOtp().execute();
                    } else {
                        Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(fthis), Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.btn_submitPwd:
                InputMethodManager imm22 =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm22.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
                if(ed_nowpassword.getText().toString().trim().length() < 1){
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.profile_cpwd_missing), Toast.LENGTH_LONG).show();
                    ed_nowpassword.requestFocus();
                }else if(!ed_nowpassword.getText().toString().equals(user.getPassword())){
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.profile_pwd_incorrect), Toast.LENGTH_LONG).show();
                    ed_nowpassword.requestFocus();
                }else{

                    rout_steppwd.setVisibility(View.GONE);
                    rout_stepmobile.setVisibility(View.VISIBLE);
                    ed_mobileno.requestFocus();
                    ed_mobileno.setText("");
                    InputMethodManager input = (InputMethodManager) this
                            .getSystemService(Activity.INPUT_METHOD_SERVICE);
                    input.showSoftInput(ed_mobileno, InputMethodManager.SHOW_FORCED);
                }
                break;
            case R.id.btn_resendOTP:
                InputMethodManager imm3 =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm3.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
                netConnection = new NetConnection();
                Map<String, String> networkDetails1 = netConnection.getConnectionDetails(ProfileActivity.this);
                if (!networkDetails1.isEmpty()) {
                    new GeneratingOtp().execute();
                } else {
                    Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(fthis), Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_submit:
                InputMethodManager imm39 =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm39.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
                if(ed_mobileno.getText().toString().trim().length() < 1){
                    Toast.makeText(getApplicationContext(), "Please enter Mobile Number", Toast.LENGTH_LONG).show();
                }else if( ed_mobileno.getText().toString().trim().length() != 10){
                    Toast.makeText(getApplicationContext(), "Please enter valid Mobile Number", Toast.LENGTH_LONG).show();
                }else if(ed_mobileno.getText().toString().trim().equals(user.getMobile())){
                    Toast.makeText(getApplicationContext(), "Please enter different Mobile no", Toast.LENGTH_LONG).show();
                }else{
                    validMobno = ed_mobileno.getText().toString().trim();

                    //ValidatingUser
                    netConnection = new NetConnection();
                    Map<String, String> networkDetails = netConnection.getConnectionDetails(ProfileActivity.this);
                    if (!networkDetails.isEmpty()) {
                        new ValidatingUser().execute();
                    } else {
                        Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(fthis), Toast.LENGTH_LONG).show();
                    }


                }
                break;
            case R.id.btn_changemobileno:
                InputMethodManager imm4 =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm4.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);

                netConnection = new NetConnection();
                Map<String, String> networkDetails = netConnection.getConnectionDetails(ProfileActivity.this);
                if (!networkDetails.isEmpty()) {
                    //new GeneratingOtp().execute();
                    rout_steppwd.setVisibility(View.VISIBLE);
                    ed_nowpassword.requestFocus();
                    ed_nowpassword.setText("");
                    InputMethodManager input = (InputMethodManager) this
                            .getSystemService(Activity.INPUT_METHOD_SERVICE);
                    input.showSoftInput(ed_nowpassword, InputMethodManager.SHOW_FORCED);
                } else {
                    Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(fthis), Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_changepwd:
                InputMethodManager imm44 =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm44.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
                Intent intent = new Intent(ProfileActivity.this, ChangePwdActivity.class);
                startActivity(intent);
                break;
            case R.id.btnlogoutt:
                confirmlogout();

                break;
            case R.id.img_userpic:
                if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                        requestPermissions(new String[] {
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        }, REQUEST_CODE_ASK_PERMISSIONS);
                    }else{
                        updateProfile();
                    }
                }else {
                    updateProfile();
                }
                break;
            case R.id.img_profile:
                if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                        requestPermissions(new String[] {
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        }, REQUEST_CODE_ASK_PERMISSIONS);
                    }else{
                        updateProfile();
                    }
                }else {
                    updateProfile();
                }
                break;
            case R.id.img_calendar:
                //Toast.makeText(ProfileActivity.this, "dateDescs", Toast.LENGTH_SHORT).show();
                DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(ProfileActivity.this, new DatePickerPopWin.OnDatePickedListener() {
                    @Override
                    public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
                        dob = month+"-"+day+"-"+year;
                        ed_userdob.setText(day+" "+listMonths.get(month-1).toString()+" "+year);
                    }
                }).textConfirm(getResources().getString(R.string.profile_calendar_btn_confirm_text)) //text of confirm button
                        .textCancel(getResources().getString(R.string.profile_calendar_btn_cancel_text)) //text of cancel button
                        .btnTextSize(16) // button text size
                        .viewTextSize(25) // pick view text size
                        .colorCancel(Color.parseColor(getResources().getString(R.string.profile_calendar_btn_cancel_text_color))) //color of cancel button
                        .colorConfirm(Color.parseColor(getResources().getString(R.string.profile_calendar_btn_confirm_text_color)))//color of confirm button
                        .minYear(Integer.parseInt(getResources().getString(R.string.profile_calendar_min_year))) //min year in loop
                        .maxYear(Integer.parseInt(getResources().getString(R.string.profile_calendar_max_year))) // max year in loop
                        .dateChose(getResources().getString(R.string.profile_calendar_selected_date)) // date chose when init popwindow
                        .build();
                pickerPopWin.showPopWin(ProfileActivity.this);
                break;
        }
    }

    class ValidatingUser extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog =  CProgress.ctor(ProfileActivity.this);;
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... args) {
            JSONObject objData = new JSONObject();
            try {
                objData.put("userMobNo", validMobno);
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
                        pDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "This Mobile Number is already registered",Toast.LENGTH_LONG ).show();
                    }else if(jsonVUser.getString("Message").toString().trim().equals("success")){
                        pDialog.dismiss();
                        netConnection = new NetConnection();
                        Map<String, String> networkDetails2 = netConnection.getConnectionDetails(ProfileActivity.this);
                        if (!networkDetails2.isEmpty()) {
                            str_mobno = ed_mobileno.getText().toString().trim();
                            rout_stepmobile.setVisibility(View.GONE);
                            rout_stepotp.setVisibility(View.VISIBLE);
                            ed_otp.setText("");
                            ed_otp.requestFocus();
                            InputMethodManager input = (InputMethodManager) fthis
                                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
                            input.showSoftInput(ed_otp, InputMethodManager.SHOW_FORCED);
                            new GeneratingOtp().execute();
                        } else {
                            Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(fthis), Toast.LENGTH_LONG).show();
                        }
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

    class ValidatingOtp extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = CProgress.ctor(ProfileActivity.this);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... args) {
            JSONObject objData = new JSONObject();
            try {
                objData.put("mobileno", str_mobno);
                objData.put("otpkey", str_otpkey);
                objData.put("otp", str_otp);
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
                        rout_stepotp.setVisibility(View.GONE);
                        //Toast.makeText(getApplicationContext(), "Mobile No updated successfully.", Toast.LENGTH_LONG).show();
                        //userPref.setMobile(str_mobno);
                        //user = userPref.getUser(fthis);
                        new UpdateMobileNo().execute();
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



    class UpdateEmailId extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = CProgress.ctor(ProfileActivity.this);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            JSONObject objData = new JSONObject();
            try {
                objData.put("userID", user.getID());
                objData.put("userEmail", email);

                json = jsonParser.makeHttpRequestJSON(URLManager.getChangeEmail(), "POST", objData);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String responseData) {
            isPopOpen = false;
            try {
                if (json != null) {
                    if(json.getString("Message").toString().trim().equals("error")){
                        pDialog.dismiss();
                        Toast.makeText(getApplicationContext(), json.getString("MessageText").toString(),Toast.LENGTH_LONG ).show();
                    }else if(json.getString("Message").toString().trim().equals("success")){
                        userPref.setEmail(email);
                        isEdit = false;
                        pDialog.dismiss();
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.profile_email_update_succ), Toast.LENGTH_LONG).show();
                        user = userPref.getUser(fthis);
                        ed_useremail.setText(user.getEmail());
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);

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

    class UpdateUser extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = CProgress.ctor(ProfileActivity.this);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            JSONObject objData = new JSONObject();
            try {
                objData.put("userID", user.getID());
                objData.put("userMobNo", user.getMobile());
                objData.put("userlastname", lname);
                objData.put("userName", fname);
                objData.put("userPassword", user.getPassword());
                objData.put("userEmail", user.getEmail());
                objData.put("deviceId", user.getDeviceID());
                objData.put("os", "android");
                objData.put("latitude", user.getLat());
                objData.put("longitude", user.getLong());
                objData.put("dob", dob);
                objData.put("gender", gender);
                objData.put("maritialstatus", mstatus);
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
                        //Toast.makeText(getApplicationContext(), "Profile updated successfully.", Toast.LENGTH_LONG ).show();
                        User objuser = new User();
                        JSONObject objj = json.getJSONObject("ResponseObject");
                        objuser.setID(json.getString("ID").toString());
                        objuser.setPassword(objj.getString("userPassword"));
                        objuser.setMobile(objj.getString("userMobNo"));
                        objuser.setFName(objj.getString("userName"));
                        objuser.setLName(objj.getString("userlastname"));
                        objuser.setEmail(objj.getString("userEmail"));
                        objuser.setCountry(user.getCountry());
                        objuser.setState(user.getState());
                        objuser.setCity(user.getCity());
                        objuser.setAddress(user.getAddress());
                        objuser.setLat(user.getLat());
                        objuser.setLong(user.getLong());
                        objuser.setImg(user.getImg());
                        objuser.setLoggedIn(true);
                        objuser.setDob(objj.getString("dob"));
                        objuser.setGender(objj.getString("gender"));
                        objuser.setMStatus(objj.getString("maritialstatus"));
                        objuser.setDeviceID(user.getDeviceID());
                        objuser.setOS(user.getOS());
                        userPref.setUser(objuser);
                        user = userPref.getUser(fthis);
                        isEdit = false;
                        pDialog.dismiss();
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
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


    class StatusLogout extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = CProgress.ctor(ProfileActivity.this);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            JSONObject objData = new JSONObject();
            try {
                objData.put("userID", user.getID());
                objData.put("islogedin", false);
                jsonLogout = jsonParser.makeHttpRequestJSON(URLManager.getUserLogoutUrl(), "POST", objData);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String responseData) {
            pDialog.dismiss();
            logout();
        }
    }

    class UpdateMobileNo extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = CProgress.ctor(ProfileActivity.this);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            JSONObject objData = new JSONObject();
            try {
                objData.put("userID", user.getID());
                objData.put("userMobNo", str_mobno);

                Log.d("jsonfinal", objData+"");
                json = jsonParser.makeHttpRequestJSON(URLManager.getChangeMobileno(), "POST", objData);
                Log.d("jsonfinal", json+"");
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String responseData) {
            isPopOpen = false;
            try {
                if (json != null) {
                    if(json.getString("Message").toString().trim().equals("error")){
                        pDialog.dismiss();
                        Toast.makeText(getApplicationContext(), json.getString("MessageText").toString(),Toast.LENGTH_LONG ).show();
                    }else if(json.getString("Message").toString().trim().equals("success")){
                        userPref.setMobile(str_mobno);
                        isEdit = false;
                        pDialog.dismiss();
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.profile_mobile_update_succ), Toast.LENGTH_LONG).show();
                        user = userPref.getUser(fthis);
                        id_userMobile.setText(user.getMobile());

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


    class GeneratingOtp extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog =  CProgress.ctor(ProfileActivity.this);;
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... args) {
            JSONObject objData = new JSONObject();
            try {
                objData.put("mobileno", str_mobno);
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

                        ed_otp.setText("");
                        ed_otp.requestFocus();
                        str_otpkey = jsonOTP.getJSONObject("ResponseObject").getString("otpkey");
                        ed_mobilenoNew.setText(str_mobno);

                        isPopOpen = true;
                        pDialog.dismiss();
                    }else {
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
    public static ProfileActivity instance() {
        return inst;
    }
    public void updateOTP(final String smsMessage) {
        ed_otp.setText(smsMessage);
    }

    private void updateProfile(){
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 299);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)   {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 299){
            if(resultCode == RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                    try {
                        Bitmap thePic = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                        writeFileToInternalStorage(thePic);
                    }catch(Exception ee){
                        ee.printStackTrace();
                    }
                }else{
                    cropCapturedImage(selectedImage);
                }
            }else{

            }
        }if(requestCode == 300) {
            if(resultCode == RESULT_OK && null != data) {
                Bundle extras = data.getExtras();
                Bitmap thePic = extras.getParcelable("data");
                writeFileToInternalStorage(thePic);
            }
        }
    }

    class AttemptProfile extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = CProgress.ctor(ProfileActivity.this);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            JSONObject objData = new JSONObject();
            try {
                objData.put("userID", user.getID());
                objData.put("imagename", cuufImgname);
                jsonProfileImg = jsonParser.makeHttpRequestJSON(URLManager.getUserImageUrl(), "POST", objData);

            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String responseData) {
            pDialog.dismiss();
            if(jsonProfileImg != null) {
                try {
                    if (jsonProfileImg.getString("Message").equals("success")){
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.profile_profile_pic_update_succ), Toast.LENGTH_LONG).show();
                        userPref.setImage(cuufImgname);
                        user.setImg(cuufImgname);
                        Bitmap newPic = readFileToInternalStorage();
                        img_userpic.setImageBitmap(newPic);
                    }else{
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.profile_profile_pic_update_fail), Toast.LENGTH_LONG).show();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }else{

            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateProfile();
                } else {

                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    public void cropCapturedImage(Uri picUri){
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(picUri, "image/*");
        cropIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        cropIntent.putExtra("crop", "true");
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        cropIntent.putExtra("outputX", 256);
        cropIntent.putExtra("outputY", 256);
        cropIntent.putExtra("return-data", true);
        startActivityForResult(cropIntent, 300);

    }


    public void writeFileToInternalStorage(Bitmap outputImage) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
        String  currentTimeStamp = dateFormat.format(new Date());
        String filename = currentTimeStamp+".png";
        cuufImgname = filename;
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("profile", Context.MODE_PRIVATE);
        if (!directory.exists()) {
            directory.mkdir();
        }
        File mypath = new File(directory, filename);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            outputImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            netConnection = new NetConnection();
            Map<String, String> networkDetails = netConnection.getConnectionDetails(ProfileActivity.this);
            if(!networkDetails.isEmpty()) {
                new AttemptProfile().execute();
            } else {
                Toast.makeText(getApplicationContext(), ErrorMessages.getNoInternet(fthis), Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {

        }
    }

}
