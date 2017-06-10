package com.accelity.wow21.utilities;

import android.content.Context;
import android.content.res.Resources;

import com.accelity.wow21.R;

/**
 * Created by Rahul on 01-09-2016.
 */
public class CustMessages {

    public static String getAppName(Context ctx){
        return getResources(ctx).getString(R.string.app_name);
    }
    public static String getShareAppDecs(Context ctx){
        return URLManager.getPlayStoreMarketURL();
    }
    public static String getFeedbackPopupTitle(Context ctx){
        return getResources(ctx).getString(R.string.feedbackPopupTitle);
    }
    public static String getFeedbackGoodBtnTitle(Context ctx){
        return getResources(ctx).getString(R.string.feedback_btn_good);
    }
    public static String getFeedbackBadBtnTitle(Context ctx){
        return getResources(ctx).getString(R.string.feedback_btn_bad);
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
    public static String getUserCodeMSG(Context ctx){
        return ctx.getResources().getString(R.string.txt_usercode);
    }
    public static String getNoMoreDeal(Context ctx){
        return getResources(ctx).getString(R.string.nomore_deal);
    }
    public static String getOutOfStockText(Context ctx){
        return getResources(ctx).getString(R.string.out_of_stock);
    }
    public static String getMinOrderAmountfCou(Context ctx){
        return getResources(ctx).getString(R.string.min_orderamount_forthis_order);
    }
    public static String getNoTransHistory(Context ctx){
        return getResources(ctx).getString(R.string.noTranHistory);
    }
    public static String getNoOrderHist(Context ctx){
        return getResources(ctx).getString(R.string.noOrderHistory);
    }
    public static String getCurrentPwdMissText(Context ctx){
        return getResources(ctx).getString(R.string.currentPasswordText);
    }
    public static String getCurrentPwdIncText(Context ctx){
        return getResources(ctx).getString(R.string.currentcorrectPasswordText);
    }
    public static String getNewPwdMissText(Context ctx){
        return getResources(ctx).getString(R.string.newPasswordText);
    }
    public static String getMismatchPwd(Context ctx){
        return getResources(ctx).getString(R.string.passmismatchText);
    }
    public static String getPwdChngSu(Context ctx){
        return getResources(ctx).getString(R.string.pwdchnagedsuccess);
    }
    public static String getCouponCodeMissText(Context ctx){
        return getResources(ctx).getString(R.string.couponCodemissing);
    }
    public static String getNoCouponFoundText(Context ctx){
        return getResources(ctx).getString(R.string.noCouponFound);
    }
    public static String getNoMoNoFound(Context ctx){
        return getResources(ctx).getString(R.string.MobileNoNotFound);
    }
    private static Resources getResources(Context ctx){
        return ctx.getResources();
    }
}
