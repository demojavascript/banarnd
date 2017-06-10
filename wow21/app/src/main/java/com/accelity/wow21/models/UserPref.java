package com.accelity.wow21.models;

import android.content.Context;
import android.content.SharedPreferences;

import com.accelity.wow21.etc.DatabaseHandler;

import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by Rahul on 09-07-2016.
 */
public class UserPref {
    private Context context;
    private DatabaseHandler db;
    private SharedPreferences sharedpreferences;
    private static final String MyPREFERENCES = "userPrefs";
    private static final String _ID           = "idKey";
    private static final String _Mobile       = "mobileKey";
    private static final String _Email        = "emailKey";
    private static final String _FName        = "nameKey";
    private static final String _LName        = "lnameKey";
    private static final String _Password     = "passwordKey";
    private static final String _Country      = "countryKey";
    private static final String _State        = "stateKey";
    private static final String _City         = "cityKey";
    private static final String _Address      = "addressKey";
    private static final String _Lat          = "latKey";
    private static final String _Long         = "longKey";
    private static final String _Img          = "imgKey";
    private static final String _loggedIn     = "loggedInKey";
    private static final String _DOB          = "dobKey";
    private static final String _Gender       = "genderKey";
    private static final String _MStatus      = "mstatusKey";
    private static final String _Deviceid     = "deviceidKey";
    private static final String _OS           = "osKey";
    private User user;

    public UserPref(Context context){
        this.context = context;
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        db = new DatabaseHandler(context);
    }
    public void setUser(User user){
        this.user = user;
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(_ID, user.getID());
        editor.putString(_Mobile, user.getMobile());
        editor.putString(_Email, user.getEmail());
        editor.putString(_FName, user.getFName());
        editor.putString(_LName, user.getLName());
        editor.putString(_Password, user.getPassword());
        editor.putString(_Country, user.getCountry());
        editor.putString(_State, user.getState());
        editor.putString(_City, user.getCity());
        editor.putString(_Address, user.getAddress());
        editor.putString(_Lat, user.getLat());
        editor.putString(_Long, user.getLong());
        editor.putString(_Img, user.getImg());
        editor.putBoolean(_loggedIn, user.getLoggedIn());
        editor.putString(_DOB, user.getDob());
        editor.putString(_Gender, user.getGender());
        editor.putString(_MStatus, user.getMStatus());
        editor.putString(_Deviceid, user.getDeviceID());
        editor.putString(_OS, user.getOS());
        editor.commit();
    }
    public User getUser(Context context){
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        User objuser = new User();
        objuser.setID(sharedpreferences.getString(_ID, ""));
        objuser.setMobile(sharedpreferences.getString(_Mobile, ""));
        objuser.setEmail(sharedpreferences.getString(_Email, ""));
        objuser.setFName(sharedpreferences.getString(_FName, ""));
        objuser.setLName(sharedpreferences.getString(_LName, ""));
        objuser.setPassword(sharedpreferences.getString(_Password, ""));
        objuser.setCountry(sharedpreferences.getString(_Country, ""));
        objuser.setState(sharedpreferences.getString(_State, ""));
        objuser.setCity(sharedpreferences.getString(_City, ""));
        objuser.setAddress(sharedpreferences.getString(_Address, ""));
        objuser.setLat(sharedpreferences.getString(_Lat, ""));
        objuser.setLong(sharedpreferences.getString(_Long, ""));
        objuser.setImg(sharedpreferences.getString(_Img, ""));
        objuser.setLoggedIn(sharedpreferences.getBoolean(_loggedIn, false));
        objuser.setDob(sharedpreferences.getString(_DOB, ""));
        objuser.setGender(sharedpreferences.getString(_Gender, ""));
        objuser.setMStatus(sharedpreferences.getString(_MStatus, ""));
        objuser.setDeviceID(sharedpreferences.getString(_Deviceid, ""));
        objuser.setOS(sharedpreferences.getString(_OS, ""));
        return objuser;
    }
    public void setPassword(String pwd){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(_Password, pwd);
        editor.commit();
    }
    public void setMobile(String mob){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(_Mobile, mob);
        editor.commit();
    }
    public void setEmail(String email){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(_Email, email);
        editor.commit();
    }
    public void setImage(String img){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(_Img, img);
        editor.commit();
    }
    public void setCountry(String country){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(_Country, country);
        editor.commit();
    }
    public void setState(String state){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(_State, state);
        editor.commit();
    }
    public void setCity(String city){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(_City, city);
        editor.commit();
    }
    public void setAddress(String address){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(_Address, address);
        editor.commit();
    }
    public void setLat(String lat){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(_Lat, lat.toString());
        editor.commit();
    }
    public void setLong(String longg){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(_Long, longg.toString());
        editor.commit();
    }

    public void setOS(String os){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(_OS, os);
        editor.commit();
    }


    public void setLogout(){
        try{
            db.deleteNotification();
            ShortcutBadger.applyCount(context, 0);
        }catch(Exception e){
            e.printStackTrace();
        }
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(_ID, "");
        editor.putString(_Mobile, "");
        editor.putString(_Email, "");
        editor.putString(_FName, "");
        editor.putString(_LName, "");
        editor.putString(_Password, "");
        editor.putString(_Country, "");
        editor.putString(_State, "");
        editor.putString(_City, "");
        editor.putString(_Address, "");
        editor.putString(_Lat, "");
        editor.putString(_Long, "");
        editor.putString(_Img, "");
        editor.putString(_DOB, "");
        editor.putString(_Gender, "");
        editor.putString(_MStatus, "");
        //editor.putString(_Deviceid, "");
        editor.putString(_OS, "");
        editor.putBoolean(_loggedIn, false);
        editor.commit();
    }

}
