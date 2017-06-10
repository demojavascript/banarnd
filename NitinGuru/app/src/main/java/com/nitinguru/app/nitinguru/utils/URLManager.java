package com.nitinguru.app.nitinguru.utils;

/**
 * Created by Rahul on 15-06-2016.
 */
public class URLManager {
    private static final String DOMAIN = "http://banatechnologies.com/nitinapi/";
    private static final String DOMAIN2 = "http://banatechnologies.com/nitinadmin/apis/";
    private static final String BASEURL = DOMAIN + ".deal26.in/service.ashx?method=";
    private static final String BASEURLVER2 = DOMAIN + ".deal26.in/servicev2.ashx?method=";
    private static final String COUPON_BASEURL = DOMAIN + ".deal26.in/coupon.ashx?method=";

    public static String getAddUserUrl() {
        return DOMAIN + "addUser.php";
    }

    public static String getQuizByChapterAndLevel(String chapter, String level){
        return DOMAIN2+"getquiz.php?chapterid="+chapter+"&levelid="+level;
    }
    public static String postQuizByChapterAndLevel(){
        return DOMAIN2+"postquiz.php";
    }

    public static String getPlayStoreURL() {
        return "http://play.google.com/store/apps/details?id=com.nitinguru.app.nitinguru";
    }

    public static String getPlayStoreMarketURL() {
        return "market://details?id=com.nitinguru.app.nitinguru";
    }
}