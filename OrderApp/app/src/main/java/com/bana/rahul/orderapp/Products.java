package com.bana.rahul.orderapp;

/**
 * Created by Rahul on 29-01-2016.
 */
public class Products {
    private String name;
    private String op;
    private String cp;
    private String off;
    public Products(String name, String cp, String op, String off){
        this.name = name;
        this.cp = cp;
        this.op = op;
        this.off = off;
    }
    public String getName(){
        return  this.name;
    }
    public String getOp(){
        return  this.op;
    }
    public String getCp(){
        return  this.cp;
    }
    public String getOff(){
        return  this.off;
    }
}
