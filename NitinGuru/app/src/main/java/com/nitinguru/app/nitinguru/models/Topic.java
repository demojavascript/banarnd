package com.nitinguru.app.nitinguru.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Rahul on 22-04-2017.
 */

public class Topic implements Serializable {

    private String id;
    private String name;
    private ArrayList<Notes> notes;
    public Topic(){

    }
    public Topic(String name, String id, ArrayList<Notes> notes){
        this.name = name;
        this.id = id;
        this.notes = notes;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return this.id;
    }
    public void setNotes(ArrayList<Notes> notes){
        this.notes = notes;
    }
    public ArrayList<Notes> getNotes(){
        return this.notes;
    }
}
