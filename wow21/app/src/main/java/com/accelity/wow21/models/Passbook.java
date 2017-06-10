package com.accelity.wow21.models;

/**
 * Created by Rahul on 15-09-2016.
 */
public class Passbook {
    private String id;
    private String transid;
    private String appname;
    private String userid;
    private String source;
    private String sourceno;
    private Float openingbalance;
    private Float debit;
    private Float credit;
    private Float currentbalance;
    private String remark;
    private String whendate;

    public Passbook(){

    }
    public Passbook(String id, String transid, String appname, String userid, String source, String sourceno, Float openingbalance, Float debit, Float credit, Float currentbalance, String remark, String whendate){
        this.transid = transid;
        this.appname = appname;
        this.userid = userid;
        this.source = source;
        this.sourceno = sourceno;
        this.openingbalance = openingbalance;
        this.debit = debit;
        this.credit = credit;
        this.currentbalance = currentbalance;
        this.remark = remark;
        this.whendate = whendate;
    }

    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return this.id;
    }
    public void setTransid(String transid){
        this.transid = transid;
    }
    public String getTransid(){
        return this.transid;
    }
    public void setAppname(String appname){
        this.appname = appname;
    }
    public String getAppname(){
        return this.appname;
    }
    public void setUserid(String userid){
        this.userid = userid;
    }
    public String getUserid(){
        return this.userid;
    }
    public void setSource(String source){
        this.source = source;
    }
    public String getSource(){
        return this.source;
    }
    public void setSourceno(String sourceno){
        this.sourceno = sourceno;
    }
    public String getSourceno(){
        return this.sourceno;
    }
    public void setOpeningbalance(Float openingbalance){
        this.openingbalance = openingbalance;
    }
    public Float getOpeningbalance(){
        return this.openingbalance;
    }
    public void setDebit(Float debit){
        this.debit = debit;
    }
    public Float getDebit(){
        return this.debit;
    }
    public void setCredit(Float credit){
        this.credit = credit;
    }
    public Float getCredit(){
        return this.credit;
    }
    public void setCurrentbalance(Float currentbalance){
        this.currentbalance = currentbalance;
    }
    public Float getCurrentbalance(){
        return this.currentbalance;
    }
    public void setRemark(String remark){
        this.remark = remark;
    }
    public String getRemark(){
        return this.remark;
    }
    public void setWhendate(String whendate){
        this.whendate = whendate;
    }
    public String getWhendate(){
        return this.whendate;
    }

}
