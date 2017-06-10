package com.accelity.wow21.models;

/**
 * Created by Rahul on 09-07-2016.
 */
public class Store {
    private String distance;
    private String name;
    private String city;
    private String ID;
    private String Icon;
    private String WarehouseID;
    private String Address;
    private String contactno;
    private String latitude;
    private String longitude;
    private String closingDay;


    public Store(String name, String city, String distance, String ID, String Icon, String WarehouseID, String Address, String contactno, String latitude, String longitude, String closingDay) {
        this.name = name;
        this.city = city;
        this.distance = distance;
        this.ID = ID;
        this.Icon = Icon;
        this.WarehouseID = WarehouseID;
        this.Address = Address;
        this.contactno = contactno;
        this.latitude = latitude;
        this.longitude = longitude;
        this.closingDay = closingDay;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getDistance() {
        return distance;
    }

    public String getID() {
        return ID;
    }

    public String getIcon() {
        return Icon;
    }

    public String getWarehouseID() {
        return WarehouseID;
    }

    public String getAddress() {
        return Address;
    }

    public String getContactno() {
        return contactno;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setClosingDay(String closingDay){
        this.closingDay = closingDay;
    }
    public String getClosingDay(){
        return this.closingDay;
    }
}