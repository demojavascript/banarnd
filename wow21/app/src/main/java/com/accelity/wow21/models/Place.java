package com.accelity.wow21.models;

/**
 * Created by Rahul on 22-07-2016.
 */
public class Place {
    private String desc;
    private String id;
    public Place(){

    }
    public Place(String id, String desc){
        this.desc = desc;
        this.id = id;
    }
    public String getDesc(){
        return this.desc;
    }
    public String getId(){
        return this.id;
    }
}
