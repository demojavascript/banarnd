package com.accelity.wow21.models;

/**
 * Created by Rahul on 15-09-2016.
 */
public class Notification {
    private String id;
    private String title;
    private String desc;
    private String date;
    private String largeIMGURL;
    private String smallIMGURL;
    private Boolean isClickable;
    private String screenId;
    private String beforeTime;
    public Notification(){}
    public Notification(String id, String title, String desc, String date, String largeIMGURL, String smallIMGURL, Boolean isClickable, String screenId){
        this.title = title;
        this.desc = desc;
        this.date = date;
        this.largeIMGURL = largeIMGURL;
        this.smallIMGURL = smallIMGURL;
        this.isClickable = isClickable;
        this.screenId = screenId;
    }
    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return this.id;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return this.title;
    }
    public void setDesc(String desc){
        this.desc = desc;
    }
    public String getDesc(){
        return this.desc;
    }
    public void setDate(String date){
        this.date = date;
    }
    public String getDate(){
        return this.date;
    }
    public String getBeforeTime(){
        return "";
    }
    public void setLargeIMGURL(String largeIMGURL){
        this.largeIMGURL = largeIMGURL;
    }
    public String getLargeIMGURL(){
        return this.largeIMGURL;
    }
    public void setSmallIMGURL(String smallIMGURL){
        this.smallIMGURL = smallIMGURL;
    }
    public String getSmallIMGURL(){
        return this.smallIMGURL;
    }
    public void setIsClickable(Boolean isClickable){
        this.isClickable = isClickable;
    }
    public Boolean getIsClickable(){
        return this.isClickable;
    }
    public void setScreenId(String screenId){
        this.screenId = screenId;
    }
    public String getScreenId(){
        return this.screenId;
    }
}
