package com.accelity.wow21.models;

import android.content.Context;
import android.content.SharedPreferences;

import com.accelity.wow21.etc.DatabaseHandler;

/**
 * Created by Rahul on 13-01-2017.
 */

public class CustomAdsPref {
    private Context context;
    private SharedPreferences sharedpreferences;
    private static final String MyPREFERENCES = "wow21adsPrefs";
    private static final String _AdsID           = "addidKey";
    public CustomAdsPref(Context context){
        this.context = context;
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }
    public void setAdsId(String addid){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(_AdsID, addid);
        editor.commit();
    }
    public String getAdsId(Context context){
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString(_AdsID, "");
    }
}