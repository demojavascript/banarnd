package com.accelity.wow21.models;

/**
 * Created by Rahul on 08-03-2016.
 */

public class User {
    private String ID;
    private String Code;
    private String Mobile;
    private String Email;
    private String FName;
    private String LName;
    private String Password;
    private String Country;
    private String State;
    private String City;
    private String Address;
    private String Lat;
    private String Long;
    private String Img;
    private Boolean LoggedIn;
    private String Gender;
    private String Dob;
    private String MStatus;
    private String DeviceID;
    private String OS;
    public User(){

    }
    public User(String ID, String Code, String Mobile, String Email, String FName, String LName, String Password, String Country, String State,String City, String Address, String Lat, String Long, String Img, Boolean LoggedIn, String Gender, String Dob, String MStatus, String DeviceId, String os){
        this.ID        = ID;
        this.Code      = Code;
        this.Mobile    = Mobile;
        this.Email     = Email;
        this.FName     = FName;
        this.LName     = LName;
        this.Password  = Password;
        this.Country   = Country;
        this.State     = State ;
        this.City      = City;
        this.Address   = Address;
        this.Lat       = Lat;
        this.Long      = Long;
        this.Img       = Img;
        this.LoggedIn  = LoggedIn;
        this.Gender    = Gender;
        this.Dob       = Dob;
        this.MStatus   = MStatus;
        this.DeviceID  = DeviceId;
        this.OS        = os;
    }
    public String getID(){
        return this.ID;
    }
    public String getCode(){
        return this.Code;
    }
    public String getMobile(){
        return this.Mobile;
    }
    public String getEmail(){
        return this.Email;
    }
    public String getFName(){
        return this.FName;
    }
    public String getLName(){
        return this.LName;
    }

    public String getPassword(){
        return this.Password;
    }
    public String getCountry(){
        return this.Country;
    }
    public String getState(){
        return this.State;
    }
    public String getCity(){
        return this.City;
    }
    public String getAddress(){
        /*String faddress = "";
        if(this.Address.trim().length() > 0){
            faddress = this.Address+", ";
        }
        if(this.City.trim().length() > 0){
            faddress += this.getCity()+", ";
        }
        if(this.State.trim().length() > 0){
            faddress += this.getState()+", ";
        }
        if(this.Country.trim().length() > 0){
            faddress += this.getCountry();
        }*/
        return this.Address;
    }
    public String getLat(){
        return this.Lat;
    }
    public String getLong(){
        return this.Long;
    }
    public String getImg(){
        return this.Img;
    }
    public Boolean getLoggedIn(){
        return this.LoggedIn;
    }
    public String getGender(){
        return this.Gender;
    }
    public String getDob(){
        return this.Dob;
    }
    public String getMStatus(){
        return this.MStatus;
    }
    public String getDeviceID(){
        return this.DeviceID;
    }
    public String getOS(){
        return this.OS;
    }


    public void setID(String ID){
        this.ID = ID;
    }
    public void setCode(String Code){
        this.Code = Code;
    }
    public void setMobile(String Mobile){
        this.Mobile = Mobile;
    }
    public void setEmail(String Email){
        this.Email = Email;
    }
    public void setFName(String FName){
        this.FName = FName;
    }
    public void setLName(String LName){
        this.LName = LName;
    }
    public void setPassword(String Password){
        this.Password = Password;
    }
    public void setCountry(String Country){
        this.Country = Country;
    }
    public void setState(String State){
        this.State = State;
    }
    public void setCity(String City){
        this.City = City;
    }
    public void setAddress(String Address){
        this.Address = Address;
    }
    public void setLat(String Lat){
        this.Lat = Lat;
    }
    public void setLong(String Long){
        this.Long = Long;
    }
    public void setImg(String Img){
        this.Img = Img;
    }
    public void setLoggedIn(Boolean LoggedIn){
        this.LoggedIn = LoggedIn;
    }
    public void setGender(String Gender){
        this.Gender = Gender;
    }
    public void setMStatus(String MStatus){
        this.MStatus = MStatus;
    }
    public void setDob(String Dob){
        this.Dob = Dob;
    }
    public void setDeviceID(String DeviceID){
        this.DeviceID = DeviceID;
    }
    public void setOS(String os){
        this.OS = os;
    }
}
