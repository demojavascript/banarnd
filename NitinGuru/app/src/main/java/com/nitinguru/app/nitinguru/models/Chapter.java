package com.nitinguru.app.nitinguru.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Rahul on 22-04-2017.
 */

public class Chapter implements Serializable {
    private String id;
    private String name;
    private ArrayList<Topic> topic;
    public Chapter(){

    }
    public Chapter(String id, String name, ArrayList<Topic> topic){
        this.name = name;
        this.id = id;
        this.topic = topic;
    }

    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return this.id;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setTopic(ArrayList<Topic> topic){
        this.topic = topic;
    }
    public ArrayList<Topic> getTopic(){
        return this.topic;
    }
}
