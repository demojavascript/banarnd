package com.bana.rahul.orderapp;

/**
 * Created by Rahul on 13-01-2016.
 */
public class Stores {
    private String name;
    private String city;
    private String distance;
    Stores(String name, String city, String distance){
        this.name = name;
        this.city = city;
        this.distance = distance;
    }
    public String getName(){
        return name;
    }
    public String getCity(){
        return city;
    }
    public String getDistance(){
        return distance;
    }
}
