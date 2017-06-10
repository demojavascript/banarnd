package com.nitinguru.app.nitinguru.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Rahul on 29-04-2017.
 */

public class Helper {
    public static boolean validateEmailAddress(String emailAddress){
        String  expression="^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = emailAddress;
        Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        return matcher.matches();
    }
    public static void playVideo(String key, Context ctx){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
        if (intent.resolveActivity(ctx.getPackageManager()) == null) {
            intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + key));
        }
        ctx.startActivity(intent);
    }
    public static JSONObject loadJSON(Context ctx, String filename){
        String json = null;
        JSONObject jsonObject;
        try {
            InputStream is = ctx.getAssets().open(filename+".json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        try{
            jsonObject = new JSONObject(json);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
        return jsonObject;
    }
}
