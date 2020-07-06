package com.practice.kenny.remotecart;

public class Cards {

    private String cardNum, nameOnCard, expiryDateMonth, getExpiryDateYear;

    public Cards () {}

    //constructor
    public Cards(String num, String name, String exMonth, String exYear) {
        cardNum = num;
        nameOnCard = name;
        expiryDateMonth = exMonth;
        getExpiryDateYear = exYear;
    }

    //get
    public String getCardNum() { return cardNum;}

    public String getNameOnCard() { return nameOnCard; }

    public String getExpiryDateMonth() {return expiryDateMonth; }

    public String getGetExpiryDateYear() {return getExpiryDateYear; }

    //set
    public void setCardNum(String cNum) { cardNum = cNum; }

    public void setNameOnCard(String cName) { nameOnCard = cName; }

    public void setExpiryDateMonth(String cMonth) { expiryDateMonth = cMonth; }

    public void setGetExpiryDateYear(String cYear) { getExpiryDateYear = cYear; }
}
