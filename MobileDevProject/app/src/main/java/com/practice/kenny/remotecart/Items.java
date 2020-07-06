package com.practice.kenny.remotecart;

import android.os.Parcel;
import android.os.Parcelable;

public class Items implements Parcelable {
    private String itemName, itemPrice, itemImgSrc, itemCat, itemID, itemQuant, storeID;


    public Items(){

    }

    public Items(String name, String price, String imgSrc, String cat, String itemID, String itemQuant, String storeID) {
        this.itemName = name;
        this.itemPrice = price;
        this.itemImgSrc = imgSrc;
        this.itemCat = cat;
        this.itemID = itemID;
        this.itemQuant = itemQuant;
        this.storeID = storeID;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public String getItemImgSrc() {
        return itemImgSrc;
    }

    public String getItemCat() {
        return itemCat;
    }

    public String getItemID() {
        return itemID;
    }

    public String getItemQuant() {
        return itemQuant;
    }

    public String getStoreID() {
        return storeID;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public void setItemImgSrc(String itemImgSrc) {
        this.itemImgSrc = itemImgSrc;
    }

    public void setItemCat(String itemCat) {
        this.itemCat = itemCat;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public void setItemQuant(String itemQuant) {
        this.itemQuant = itemQuant;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Items createFromParcel(Parcel in) {
            return new Items(in);
        }

        public Items[] newArray(int size) {
            return new Items[size];
        }
    };

    public Items(Parcel in) {
        this.itemID = in.readString();
        this.itemCat = in.readString();
        this.itemImgSrc= in.readString();
        this.itemPrice = in.readString();
        this.itemName = in.readString();
        this.itemQuant = in.readString();
        this.storeID = in.readString();
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.itemID);
        dest.writeString(this.itemCat);
        dest.writeString(this.itemImgSrc);
        dest.writeString(this.itemPrice);
        dest.writeString(this.itemName);
        dest.writeString(this.itemQuant);
        dest.writeString(this.storeID);
    }

}
