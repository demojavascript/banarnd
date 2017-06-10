package com.banatechnologies.dbapp;

/**
 * Created by Rahul on 22-05-2016.
 */
public class Contact {
    int _id;
    String _name;
    String _phone_number;

    public Contact(){

    }
    public Contact(String name, String _phone_number){
        this._name = name;
        this._phone_number = _phone_number;
    }
    public Contact(int _id, String _name, String _phone_number){
        this._name = _name;
        this._id = _id;
        this._phone_number = _phone_number;
    }
    // getting ID
    public int getID(){
        return this._id;
    }

    // setting id
    public void setID(int id){
        this._id = id;
    }

    // getting name
    public String getName(){
        return this._name;
    }

    // setting name
    public void setName(String name){
        this._name = name;
    }

    // getting phone number
    public String getPhoneNumber(){
        return this._phone_number;
    }

    // setting phone number
    public void setPhoneNumber(String phone_number){
        this._phone_number = phone_number;
    }
}