package com.accelity.wow21.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Rahul on 09-07-2016.
 */
public class Order implements Serializable {
    private String orderno;
    private String orderid;
    private String userid;
    private int dealqty;
    private Double totalamount;
    private int orderstatus;
    private String orderstatusdesc;
    private String paymenttype;
    private String date;
    private Double totalsavings;
    private String storename;
    private ArrayList<OrderDetail> orderDetails;
    private ArrayList<PaymentDetails> paymentDetails ;
    private float orderrating;
    public Order(){

    }
    public Order(String orderno, String orderid, String userid, int dealqty, Double totalamount, int orderstatus, String orderstatusdesc, String paymenttype, String date, Double totalsavings, String storename, ArrayList<OrderDetail> orderDetails, ArrayList<PaymentDetails> paymentDetails, float orderrating){
        this.orderno = orderno;
        this.orderid = orderid;
        this.userid = userid;
        this.dealqty = dealqty;
        this.totalamount = totalamount;
        this.orderstatus = orderstatus;
        this.paymenttype = paymenttype;
        this.date = date;
        this.totalsavings = totalsavings;
        this.storename = storename;
        this.orderDetails = orderDetails;
        this.paymentDetails = paymentDetails;
        this.orderrating = orderrating;
    }

    public String getOrderid(){
        return this.orderid;
    }
    public float getOrderrating(){
        return this.orderrating;
    }
    public void setOrderrating(float orderrating){
        this.orderrating = orderrating;
    }
    public String getOrderno(){ return this.orderno; }
    public String getuserid(){
        return this.userid;
    }
    public int getTotaldeal(){
        return this.dealqty;
    }
    public Double getTotalamount(){
        return this.totalamount;
    }
    public int getOrderstatus(){
        return this.orderstatus;
    }
    public String getOrderStatusDesc(){
        return this.orderstatusdesc;
    }
    public String getPaymenttype(){
        return this.paymenttype;
    }

    public String getDate(){
        return this.date;
    }
    public Double getTotalsavings(){
        return this.totalsavings;
    }
    public String getStorname(){
        return this.storename;
    }
    public ArrayList<OrderDetail> getOrderDetails(){
        return this.orderDetails;
    }
    public ArrayList<PaymentDetails> getPaymentDetails(){
        return this.paymentDetails;
    }

    public void setOrderid(String orderid){
        this.orderid = orderid;
    }
    public void setOrderno(String orderno){
        this.orderno = orderno;
    }
    public void setuserid(String userid){
        this.userid = userid;
    }
    public void setTotaldeal(int dealqty){
        this.dealqty = dealqty;
    }
    public void setTotalamount(Double totalamount){
        this.totalamount = totalamount;
    }
    public void setOrderstatus(int orderstatus){
        this.orderstatus = orderstatus;
    }
    public void setOrderStatusDesc(String orderstatusdesc){
        this.orderstatusdesc = orderstatusdesc;
    }
    public void setPaymenttype(String paymenttype){
        this.paymenttype = paymenttype;
    }

    public void setDate(String date){
        this.date = date;
    }
    public void setTotalsavings(Double totalsavings){
        this.totalsavings = totalsavings;
    }
    public void setStorname(String storename){
        this.storename = storename;
    }
    public void setOrderDetails(ArrayList<OrderDetail> orderDetails){
        this.orderDetails = orderDetails;
    }
    public void setPaymentDetails(ArrayList<PaymentDetails> paymentDetails){
        this.paymentDetails = paymentDetails;
    }
}
