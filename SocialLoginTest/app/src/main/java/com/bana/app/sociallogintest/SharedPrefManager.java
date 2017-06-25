package com.bana.app.sociallogintest;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Rahul on 17-06-2017.
 */

public class SharedPrefManager {
    private Context context;
    private SharedPreferences sharedpreferences;
    private static final String MyPREFERENCES = "socialTestPrefs";

    private static final String loginType    = "logintypekey";
    private static final String userId       = "useridkey";
    private static final String email        = "emailkey";
    private static final String uname        = "unamekey";
    private static final String upicurl       = "picurlkey";
    private static final String accessToken  = "accesstokenkey";
    private static final String isLogin      = "isloginkey";

    public SharedPrefManager(Context context){
        this.context = context;
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }

    public void setUser(String uloginType, String uuserId, String uemail, String uuname, String picurl, String uaccessToken, boolean uisLogin){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(loginType, uloginType);
        editor.putString(userId, uuserId);
        editor.putString(email, uemail);
        editor.putString(uname, uuname);
        editor.putString(upicurl, picurl);
        editor.putString(accessToken, uaccessToken);
        editor.putBoolean(isLogin, uisLogin);
        editor.commit();
    }
    public void logout(){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(loginType, "");
        editor.putString(userId, "");
        editor.putString(uname, "");
        editor.putString(upicurl, "");
        editor.putString(email, "");
        editor.putString(accessToken, "");
        editor.putBoolean(isLogin, false);
        editor.commit();
    }
    public String getLoginType(){
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString(loginType, "");
    }
    public String getUserId(){
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString(userId, "");
    }
    public String getEmail(){
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString(email, "");
    }
    public String getUname(){
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString(uname, "");
    }
    public String getUpicurl(){
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString(upicurl, "");
    }
    public String getAccessToken(){
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString(accessToken, "");
    }
    public boolean getIsLogin(){
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getBoolean(isLogin, false);
    }

}
