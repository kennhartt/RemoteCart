package com.practice.kenny.remotecart;

public class Orders {
    private String orderID, storeID, userID, delivery, price, storeName;

    public Orders(){

    }

    public String getStoreID() {
        return storeID;
    }

    public String getDelivery() {
        return delivery;
    }

    public String getOrderID() {
        return orderID;
    }

    public String getPrice() {
        return price;
    }

    public String getUserID() {
        return userID;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}
