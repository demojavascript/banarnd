package com.nitinguru.app.nitinguru.models;

/**
 * Created by Rahul on 28-05-2017.
 */

public class DBChapter {
    private long id;
    private String title;
    private int topics;
    private int visits;

    public DBChapter(){

    }
    public DBChapter(String title, int topics){
        this.topics = topics;
        this.title = title;
    }
    public DBChapter(long id, String title){
        this.id = id;
        this.title = title;
    }
    public void setId(long id){
        this.id = id;
    }
    public void setTopics(int topics){
        this.topics = topics;
    }
    public void setVisits(int visits){
        this.visits = visits;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public long getId(){
        return this.id;
    }
    public int getTopics(){
        return this.topics;
    }
    public int getVisits(){
        return this.visits;
    }
    public String getTitle(){
        return this.title;
    }

}

