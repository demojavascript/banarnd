package com.nitinguru.app.nitinguru.models;

/**
 * Created by Rahul on 29-04-2017.
 */

public class Video {
    private String title;
    private String url;
    private String key;
    public Video(){}
    public Video(String title, String url, String key){
        this.title = title;
        this.url = url;
        this.key = key;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public void setUrl(String url){
        this.url = url;
    }
    public void setKey(String key){
        this.key = key;
    }
    public String getTitle(){
        return this.title;
    }
    public String getUrl(){
        return this.url;
    }
    public String getKey(){
        return this.key;
    }
}
