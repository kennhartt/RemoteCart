package com.practice.kenny.remotecart;

public class Addresses {
    private String aptNo, street, city, zip, province, addressID;

    public Addresses(){}

    public Addresses(String aptNo, String street, String city, String zip, String province, String addressID) {
        this.aptNo = aptNo;
        this.street = street;
        this.city = city;
        this.zip = zip;
        this.province = province;
        this.addressID = addressID;
    }

    public String getAptNo() {
        return aptNo;
    }

    public String getCity() {
        return city;
    }

    public String getProvince() {
        return province;
    }

    public String getStreet() {
        return street;
    }

    public String getZip() {
        return zip;
    }

    public String getAddressID() {
        return addressID;
    }

    public void setAptNo(String aptNo) {
        this.aptNo = aptNo;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public void setAddressID(String addressID) {
        this.addressID = addressID;
    }
}
