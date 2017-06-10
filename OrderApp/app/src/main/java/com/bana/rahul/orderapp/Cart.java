package com.bana.rahul.orderapp;

/**
 * Created by Rahul on 29-01-2016.
 */
public class Cart {
    private String pname;
    private String pprice;
    private String pqty;
    public Cart(String pname, String pprice, String pqty){
        this.pname = pname;
        this.pprice = pprice;
        this.pqty = pqty;
    }
    public String getPname(){
        return this.pname;
    }
    public String getPprice(){
        return this.pprice;
    }
    public String getPqty(){
        return this.pqty;
    }
}
