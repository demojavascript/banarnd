package com.nitinguru.app.nitinguru.models;

/**
 * Created by Rahul on 28-05-2017.
 */

public class DBNotes {
    private long id;
    private long topicid;
    private String title;
    private String desc;
    public DBNotes(){

    }
    public DBNotes(String title, String desc, long topicid){
        this.title = title;
        this.desc = desc;
        this.topicid = topicid;
    }
    public DBNotes(long id, String title, String desc){
        this.id = id;
        this.title = title;
        this.desc = desc;
    }
    public void setId(long id){
        this.id = id;
    }
    public void setTopicid(long id){
        this.topicid = id;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public void setDesc(String desc){
        this.desc = desc;
    }
    public long getId(){
        return this.id;
    }
    public long getTopicid(){
        return this.topicid;
    }
    public String getTitle(){
        return this.title;
    }
    public String getDesc(){
        return this.desc;
    }
}
