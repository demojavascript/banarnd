package com.nitinguru.app.nitinguru.models;

import java.util.ArrayList;

/**
 * Created by Rahul on 01-06-2017.
 */

public class Quiz {
    private String id;
    private String title;
    private int ans;
    private ArrayList<String> options;

    public Quiz(){

    }

    public Quiz(String id, String title, ArrayList<String> options){
        this.id = id;
        this.title = title;
        this.options = options;
    }

    public Quiz(String id, String title, int ans, ArrayList<String> options){
        this.id = id;
        this.title = title;
        this.ans = ans;
        this.options = options;
    }

    public void setId(String id){
        this.id = id;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public void setAns(int ans){
        this.ans = ans;
    }
    public void setOptions(ArrayList<String> options){
        this.options = options;
    }

    public String getId(){
        return this.id;
    }
    public String getTitle(){
        return this.title;
    }
    public int getAns(){
        return this.ans;
    }
    public ArrayList<String> getOptions(){
        return this.options;
    }

}
