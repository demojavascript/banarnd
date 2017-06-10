package com.accelity.wow21.models;

/**
 * Created by Rahul on 14-09-2016.
 */
public class Promotions {
    private String promotionType;
    private String promotionCode;
    private String promotionDesc;
    private String promotionFrom;
    private String promotionTo;
    public Promotions(){

    }
    public Promotions(String promotionType, String promotionCode, String promotionDesc, String promotionFrom, String promotionTo){
        this.promotionType = promotionType;
        this.promotionCode = promotionCode;
        this.promotionDesc = promotionDesc;
        this.promotionFrom = promotionFrom;
        this.promotionTo = promotionTo;
    }

    public void setPromotionType(String promotionType){
        this.promotionType = promotionType;
    }
    public String getPromotionType(){
        return this.promotionType;
    }
    public void setPromotionCode(String promotionCode){
        this.promotionCode = promotionCode;
    }
    public String getPromotionCode(){
        return this.promotionCode;
    }
    public void setPromotionDesc(String promotionDesc){
        this.promotionDesc = promotionDesc;
    }
    public String getPromotionDesc(){
        return this.promotionDesc;
    }
    public void setPromotionFrom(String promotionFrom){
        this.promotionFrom = promotionFrom;
    }
    public String getPromotionFrom(){
        return this.promotionFrom;
    }
    public void setPromotionTo(String promotionTo){
        this.promotionTo = promotionTo;
    }
    public String getPromotionTo(){
        return this.promotionTo;
    }
}
