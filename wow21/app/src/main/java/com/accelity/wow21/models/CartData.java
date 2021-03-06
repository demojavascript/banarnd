package com.accelity.wow21.models;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Rahul on 03-09-2016.
 */
public class CartData implements Serializable {
    private String variantID;
    private String variantTitle;
    private String dealID;
    private String dealName;
    private String dealDesc;
    private String dealIcon;
    private int maxPerCustomer;
    private int stockAvailable;
    private int dealTotalQty;
    private Double mrp;
    private Double sellingprice;
    private String dealDiscount;
    private Boolean isSuper;
    private Double superDealMinAmount;
    private Date startDate;
    private Date endDate;
    private int dealQty;
    private int mineQty;
    private String catID;
    private String brandID;

    public CartData(){

    }
    public CartData(String variantID, String variantTitle, String dealID, String dealName, String dealDesc, String dealIcon,
                    int maxPerCustomer, int stockAvailable, int dealTotalQty,
                    Double mrp, Double sellingprice, String dealDiscount,
                    Boolean isSuper, Double superDealMinAmount,   Date startDate,   Date endDate, int dealQty, int mineQty, String catID, String brandID){
        this.variantID = variantID;
        this.variantTitle = variantTitle;
        this.dealID = dealID;
        this.dealName = dealName;
        this.dealDesc = dealDesc;
        this.dealIcon = dealIcon;
        this.maxPerCustomer = maxPerCustomer;
        this.stockAvailable = stockAvailable;
        this.dealTotalQty = dealTotalQty;
        this.mrp = mrp;
        this.sellingprice = sellingprice;
        this.dealDiscount = dealDiscount;
        this.isSuper = isSuper;
        this.superDealMinAmount = superDealMinAmount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.dealQty = dealQty;
        this.mineQty = mineQty;
        this.catID = catID;
        this.brandID = brandID;
    }
    public String getVariantID(){
        return this.variantID;
    }
    public String getVariantTitle(){
        return this.variantTitle;
    }
    public String getDealID(){
        return this.dealID;
    }
    public String getDealName(){
        return this.dealName;
    }
    public String getDealDesc(){
        return this.dealDesc;
    }
    public String getDealIcon(){
        return this.dealIcon;
    }
    public int getMaxPerCustomer(){
        return this.maxPerCustomer;
    }
    public int getStockAvailable(){
        return this.stockAvailable;
    }
    public int getDealTotalQty(){
        return this.dealTotalQty;
    }
    public int getDealQty(){
        return this.dealQty;
    }
    public int getMineQty(){
        return this.mineQty;
    }
    public void setDealQty(int dealQty){
        this.dealQty = dealQty;
    }
    public void setmMineQty(int dealQty){
        this.mineQty = dealQty;
    }
    public Double getMrp(){
        return this.mrp;
    }
    public Double getSellingPrice(){
        return this.sellingprice;
    }
    public String getDealDiscount(){
        return this.dealDiscount;
    }
    public Boolean getIsSuper(){
        return this.isSuper;
    }
    public Double getSuperDealMinAmount(){
        return this.superDealMinAmount;
    }
    public Date getStartDate(){
        return this.startDate;
    }
    public Date getEndDate(){
        return this.endDate;
    }
    public void setCatID(String catid){
        this.catID = catid;
    }
    public void setBrandID(String brandid){
        this.brandID = brandid;
    }
    public String getCatID(){
        return this.catID;
    }
    public String getBrandID(){
        return this.brandID;
    }
}
