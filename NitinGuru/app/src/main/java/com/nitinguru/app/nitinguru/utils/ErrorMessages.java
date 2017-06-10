package com.nitinguru.app.nitinguru.utils;

import android.content.Context;
import android.content.res.Resources;

import com.nitinguru.app.nitinguru.R;

/**
 * Created by Rahul on 29-08-2016.
 */
public class ErrorMessages {

    public static String getNoInternet(Context ctx){
        return getResources(ctx).getString(R.string.noConnection);
    }
    private static Resources getResources(Context ctx){
        return ctx.getResources();
    }
}
