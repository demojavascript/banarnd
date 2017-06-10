package com.accelity.wow21.models;

/**
 * Created by abc on 09/10/16.
 * "stockdetail":{"percentagesold":"0","colour":"#1e8449","colour":""}
 */

public class StockDetail {
    private String percentagesold;
    private String colour;
    private String text;

    public StockDetail(){

    }
    public StockDetail(String percentagesold, String colour, String text){
        this.percentagesold = percentagesold;
        this.colour = colour;
        this.text = text;
    }
    public void setPercentagesold(String percentagesold){
        this.percentagesold = percentagesold;
    }
    public void setColour(String colour){
        this.colour = colour;
    }
    public void setText(String text){
        this.text = text;
    }
    public String getPercentagesold(){
        return this.percentagesold;
    }
    public String getColour(){
        return this.colour;
    }
    public String getText(){
        return this.text;
    }

}
