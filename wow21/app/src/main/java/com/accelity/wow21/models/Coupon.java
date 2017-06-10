package com.accelity.wow21.models;

/**
 * Created by Rahul on 03-09-2016.
 */
public class Coupon {
    private String couponCode;
    private String Desc;
    private String maxDiscount;
    private Boolean canapplicable;
    private String minOrderAmount;
    public Coupon(){

    }
    public Coupon(String couponCode, String Desc, String maxDiscount, Boolean canapplicable, String minOrderAmount){
        this.couponCode = couponCode;
        this.Desc = Desc;
        this.maxDiscount = maxDiscount;
        this.canapplicable = canapplicable;
        this.minOrderAmount = minOrderAmount;
    }

    public String getCouponCode(){
        return this.couponCode;
    }
    public String getDesc(){
        return this.Desc;
    }
    public String getMaxDiscount(){
        return this.maxDiscount;
    }
    public Boolean getCanapplicable(){
        return this.canapplicable;
    }
    public String getMinOrderAmount(){
        return this.minOrderAmount;
    }
}
