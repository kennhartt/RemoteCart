package com.practice.kenny.remotecart;

public class Stores {
    private String storeName, storeStreet, storeCity;

    public Stores(){
    }

    public Stores(String name, String street, String city){
        this.storeName = name;
        this.storeStreet = street;
        this.storeCity = city;
    }

    public String getStoreCity() {
        return storeCity;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getStoreStreet() {
        return storeStreet;
    }

    public void setStoreCity(String storeCity) {
        this.storeCity = storeCity;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public void setStoreStreet(String storeStreet) {
        this.storeStreet = storeStreet;
    }
}
