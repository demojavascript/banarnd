package com.accelity.wow21.utilities;

/**
 * Created by Rahul on 15-06-2016.
 */
public class URLManager {
    private static final String DOMAIN = "http://api";
    private static final String BASEURL = DOMAIN+".deal26.in/service.ashx?method=";
    private static final String BASEURLVER2 = DOMAIN+".deal26.in/servicev2.ashx?method=";
    private static final String COUPON_BASEURL = DOMAIN+".deal26.in/coupon.ashx?method=";
//http://uatapi.deal26.in/service.ashx?method=getstore&
    public static String getUserLoginUrl(){
        return BASEURL+"checkuser";
    }
    public static String getStoreDetail(){
        return BASEURL+"getstore&";
    }
    public static String getUserLogoutUrl(){
        return BASEURL+"userloginstatus";
    }
    public static String getUserSignupUrl(){
        return BASEURL+"savedealuser";
    }
    public static String getUserChnagePwdUrl(){
        return BASEURLVER2+"changepassword";
    }
    public static String getUserForgotURL(){
        return BASEURL+"forgetpassword";
    }
    public static String getUserCodeURL(){
        return BASEURL+"getdealuser";
    }
    public static String getHomeDealsUrl(){
        return BASEURL+"userwarehousev2";
    }
    public static String getChangeMobileno(){
        return BASEURL+"changemobile";
    }
    public static String getChangeEmail(){
        return BASEURL+"changeemail";
    }
    //
    public static String getHomeStoreListURL(){
        return BASEURL+"usernearbystorelist&";
    }
    public static String getUserImageUrl(){
        return BASEURL+"updateuserimage";
    }
    public static String getNearStoreUrl(){
        return BASEURL+"usernearbystorev2&";
    }
    public static String getStorelistURL(){
        return BASEURL+"usernearbystorelist";
    }
    public static String getNearDealsUrl(){
        return BASEURL+"usernearbystorev2&";
    }
    public static String getStoreDetailUrl(){
        return BASEURL+"getstore";
    }
    //
    public static String getStockCheckURL(){
        return BASEURL+"checkavailablestockv2";
    }
    public static String getPlaceOrderURL(){
        return BASEURLVER2+"placeorder";
    }
    public static String getOrderSuccessURL(){
        return BASEURL+"getorder";
    }
    public static String getUserOrderListURL(){
        return BASEURL+"getorder";
    }
    public static String getUserOrderDetailURL(){
        return BASEURL+"getorder";
    }
    public static String getStoreReviewURL(){
        return BASEURL+"savestorereview";
    }
    public static String getDiscountURL(){
        return COUPON_BASEURL+"discount";
    }
    public static String getValidateUserURL(){
        return BASEURL+"validateuser";
    }
    public static String getGenereateOTPURL(){
        return BASEURLVER2+"generateotp";
    }
    public static String getValidateOTPURL(){
        return BASEURLVER2+"validateotp";
    }
    public static String getPlayStoreURL(){
        return "http://play.google.com/store/apps/details?id=com.accelity.wow21";
    }
    public static String getPlayStoreMarketURL(){
        return "market://details?id=com.accelity.wow21";
    }
    public static String getUserPramotionURL(){
        return BASEURL + "userpramotion&appname=wow&";
    }
    public static String getUsereWalletUrl(){
        return DOMAIN+".deal26.in/coupon.ashx?method=usercashback&appname=wow&";
    }
    public static String getUsereWalletUrl2(){
        return DOMAIN+".deal26.in/coupon.ashx?method=usercashbackv2&appname=wow&";
    }
    public static String getLatLanggUpdateURL(){
        return BASEURL+"updatelatlng";
    }
    public static String getPassbookURL(){
        return DOMAIN+".deal26.in/coupon.ashx?method=cashbackpassbook&appname=wow&";
    }
    public static String getOrderRatingURL(){
        return BASEURL+"orderrating";
    }
    public static String getNotificationBanner(){
        return BASEURL+"getnotificationbanner&";
    }
}
