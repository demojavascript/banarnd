package com.nitinguru.app.nitinguru.utils;

import android.content.Context;
import android.content.res.Resources;

import com.nitinguru.app.nitinguru.R;

/**
 * Created by Rahul on 01-09-2016.
 */
public class CustMessages {

    public static String getAppName(Context ctx){
        return getResources(ctx).getString(R.string.app_name);
    }
    public static String getFeedbackMailTO(Context ctx){
        return getResources(ctx).getString(R.string.feedback_mail_to);
    }
    public static String getFeedbackMailSubject(Context ctx){
        return getResources(ctx).getString(R.string.feedback_mail_subject);
    }
    public static String getFeedbackMailStringBody(Context ctx){
        return getResources(ctx).getString(R.string.feedback_mail_body);
    }
    public static String getCustomerCareNo(Context ctx){
        return getResources(ctx).getString(R.string.customer_care_no);
    }
    public static String getAboutContent(Context ctx){
        return getResources(ctx).getString(R.string.aboutus);
    }
    public static String getContactContent(Context ctx){
        return getResources(ctx).getString(R.string.contactus);
    }
    private static Resources getResources(Context ctx){
        return ctx.getResources();
    }
}
