package com.accelity.wow21.utilities;

import android.content.Context;
import android.content.res.Resources;

import com.accelity.wow21.R;

/**
 * Created by Rahul on 29-08-2016.
 */
public class ErrorMessages {

    public static String getNoInternet(Context ctx){
        return getResources(ctx).getString(R.string.noConnection);
    }
    public static String getEmailMissing(Context ctx){
        return getResources(ctx).getString(R.string.eMsg_email_missing);
    }
    public static String getEmailValidation(Context ctx){
        return getResources(ctx).getString(R.string.eMsg_email_validation);
    }
    public static String getMobileEmailMissing(Context ctx){
        return getResources(ctx).getString(R.string.eMsg_emailmobile_missing);
    }
    public static String getPasswordMissing(Context ctx){
        return getResources(ctx).getString(R.string.eMsg_pwd_missing);
    }
    public static String getConfirmPasswordMissing(Context ctx){
        return getResources(ctx).getString(R.string.eMsg_cpwd_missing);
    }
    public static String getPasswordMismatch(Context ctx){
        return getResources(ctx).getString(R.string.eMsg_cpwd_mismatch);
    }
    public static String getFirstNameMissing(Context ctx){
        return getResources(ctx).getString(R.string.eMsg_fname_missing);
    }
    public static String getLastNameMissing(Context ctx){
        return getResources(ctx).getString(R.string.eMsg_lname_missing);
    }
    public static String getOTPMissing(Context ctx){
        return getResources(ctx).getString(R.string.eMsg_otp_missing);
    }
    public static String getMobileMissing(Context ctx){
        return getResources(ctx).getString(R.string.eMsg_mobile_missing);
    }
    public static String getMobileValidation(Context ctx){
        return getResources(ctx).getString(R.string.eMsg_mobile_validation);
    }
    public static String getHomeNoDeal(Context ctx){
        return getResources(ctx).getString(R.string.msg_txt_nodeal);
    }
    public static String getHomeNoStore(Context ctx){
        return getResources(ctx).getString(R.string.msg_txt_nostore);
    }
    public static String getUnexpectedError(Context ctx){
        return getResources(ctx).getString(R.string.eMsg_unexpect_error);
    }
    public static String getLocUpdateFail(Context ctx){
        return getResources(ctx).getString(R.string.eMsg_location_update_failed);
    }
    public static String getSelectAnyDeal(Context ctx){
        return getResources(ctx).getString(R.string.eMsg_select_nodeal);
    }
    private static Resources getResources(Context ctx){
        return ctx.getResources();
    }
}
