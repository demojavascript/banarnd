package com.nitinguru.app.nitinguru.models;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Rahul on 20-05-2017.
 */

public class UserPref {
    private Context context;
    private SharedPreferences sharedpreferences;
    private static final String MyPREFERENCES = "nitinPrefs";

    private static final String user_id    = "uidkey";
    private static final String user_mob    = "umobkey";
    private static final String user_email = "uemailkey";
    private static final String device_Key = "devicekey";
    private static final String first_Key = "firstkey";

    public UserPref(Context context){
        this.context = context;
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }
    public void setUser(String firstKey){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(device_Key, firstKey);
        editor.commit();
    }
    public void setUser(String userid, String usermob, String useremail, String deviceKey){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(user_id, userid);
        editor.putString(user_mob, usermob);
        editor.putString(user_email, useremail);
        editor.putString(device_Key, deviceKey);
        editor.commit();
    }
    public void setUser_firstkey(String firstKey){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(first_Key, firstKey);
        editor.commit();
    }
    public void setUser_id(String userid){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(user_id, userid);
        editor.commit();
    }
    public void setUser_mob(String userMob){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(user_mob, userMob);
        editor.commit();
    }
    public void setUser_email(String useremail){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(user_email, useremail);
        editor.commit();
    }
    public void setDevice_Key(String deviceKey){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(device_Key, deviceKey);
        editor.commit();
    }
    public String getUser_id(){
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString(user_id, "");
    }
    public String getUser_firstkey(){
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString(first_Key, "");
    }
    public String getUser_mob(){
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString(user_mob, "");
    }
    public String getUser_email(){
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString(user_email, "");
    }
    public String getDevice_Key(){
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString(device_Key, "");
    }
}
