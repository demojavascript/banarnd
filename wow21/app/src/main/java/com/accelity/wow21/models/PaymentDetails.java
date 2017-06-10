package com.accelity.wow21.models;

import java.io.Serializable;

/**
 * Created by Rahul on 16-09-2016.
 */
public class PaymentDetails implements Serializable {
    private String id;
    private String orderid;
    private String mode;
    private String modetranid;
    private Double amount;
    private String wdate;
    private String paymentmode;
    private String returntype;
    public PaymentDetails(){

    }
    public PaymentDetails(String id, String orderid, String mode, String modetranid, Double amount, String wdate, String paymentmode, String returntype){
        this.id = id;
        this.mode = mode;
        this.orderid = orderid;
        this.modetranid = modetranid;
        this.wdate = wdate;
        this.amount = amount;
        this.paymentmode = paymentmode;
        this.returntype = returntype;
    }
    public String getId(){
        return this.id;
    }
    public String getOrderid(){
        return this.orderid;
    }
    public String getMode(){
        return this.mode;
    }
    public String getModetranid(){
        return this.modetranid;
    }
    public Double getAmount(){
        return this.amount;
    }
    public String getWdate(){
        return this.wdate;
    }
    public String getPaymentmode(){
        return this.paymentmode;
    }
    public String getReturntype(){
        return this.returntype;
    }

    public void setId(String id){
        this.id = id;
    }
    public void setOrderid(String orderid){
        this.orderid = orderid;
    }
    public void setMode(String mode){
        this.mode = mode;
    }
    public void setModetranid(String modetranid){
        this.modetranid = modetranid;
    }
    public void setAmount(Double amount){
        this.amount = amount;
    }
    public void setWdate(String paymentmode){
        this.paymentmode = paymentmode;
    }
    public void setPaymentmode(String paymentmode){
        this.paymentmode = paymentmode;
    }
    public void setReturntype(String returntype){
        this.returntype = returntype;
    }
}
