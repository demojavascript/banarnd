package com.nitinguru.app.nitinguru.models;

/**
 * Created by Rahul on 28-05-2017.
 */

public class DBTopic {
    private long id;
    private long chapterid;
    private String title;
    private int view;

    public DBTopic(){

    }
    public DBTopic(long chapterid, String title){
        this.chapterid = chapterid;
        this.title = title;
    }
    public DBTopic(long chapterid, String title, int view){
        this.chapterid = chapterid;
        this.title = title;
        this.view = view;
    }
    public DBTopic(long chapterid, String title, int view, long id){
        this.id = id;
        this.chapterid = chapterid;
        this.title = title;
        this.view = view;
    }
    public void setId(long id){
        this.id = id;
    }
    public void setChapterid(long chapterid){
        this.chapterid = chapterid;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public void setView(int view){
        this.view = view;
    }

    public long getId(){
        return this.id;
    }
    public long getChapterid(){
        return this.chapterid;
    }
    public String getTitle(){
        return this.title;
    }
    public int getView(){
        return this.view;
    }
}
