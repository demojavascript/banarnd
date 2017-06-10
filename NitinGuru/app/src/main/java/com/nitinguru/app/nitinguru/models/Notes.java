package com.nitinguru.app.nitinguru.models;

import java.io.Serializable;

/**
 * Created by Rahul on 22-04-2017.
 */

public class Notes implements Serializable {
    private String title;
    private String desc;
    public Notes(){

    }
    public Notes(String title, String desc){
        this.title = title;
        this.desc = desc;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public void setDesc(String desc){
        this.desc = desc;
    }
    public String getTitle(){
        return this.title;
    }
    public String getDesc(){
        return this.desc;
    }
}
