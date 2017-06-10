package com.accelity.wow21.models;

import java.io.Serializable;

/**
 * Created by Rahul on 09-07-2016.
 */
public class OrderDetail implements Serializable {
    private String dealid;
    private Double dealamount;
    private String orderid;
    private int dealQty;
    private String dealname;
    private Double dealMrp;
    public OrderDetail(){

    }
    public OrderDetail(String orderid, String dealid, Double dealamount, int dealQty, Double dealMrp, String dealname){
        this.dealamount = dealamount;
        this.dealid = dealid;
        this.orderid = orderid;
        this.dealQty = dealQty;
        this.dealMrp = dealMrp;
        this.dealname = dealname;
    }
    public String getDealid(){
        return this.dealid;
    }
    public Double getDealamount(){
        return this.dealamount;
    }
    public String getOrderid(){ return  this.orderid; }
    public int getDealQty(){ return this.dealQty; }
    public Double getDealMrp(){ return this.dealMrp; }
    public String getDealname(){ return this.dealname; }

    public void setDealamount(Double dealamount){
        this.dealamount = dealamount;
    }
    public void setDealid(String dealid){
        this.dealid = dealid;
    }
    public void setOrderid(String orderid){
        this.orderid = orderid;
    }
    public void setDealQty(int dealQty){
        this.dealQty = dealQty;
    }
    public void setDealMrp(Double dealMrp){
        this.dealMrp = dealMrp;
    }
    public void setDealname(String dealname){
        this.dealname = dealname;
    }
}
