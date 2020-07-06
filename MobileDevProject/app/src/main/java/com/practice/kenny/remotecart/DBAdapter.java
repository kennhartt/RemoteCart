package com.practice.kenny.remotecart;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBAdapter
{
    protected static final String TAG = "DataAdapter";

    private final Context mContext;
    private SQLiteDatabase mDb;
    private DataBaseHelper mDbHelper;
    private static final String TABLE_USERS = "Users";
    private static final String COL_USERS_ID = "UserID";
    private static final String COL_USERS_FIRSTNAME = "FirstName";
    private static final String COL_USERS_LASTNAME = "LastName";
    private static final String COL_USERS_EMAIL = "Email";
    private static final String COL_USERS_PASSWORD = "Password";
    private static final String COL_USERS_PHONE = "PhoneNumber";
    private static final String COL_USERS_ADRESSID = "DefAdress";
    private static final String COL_USERS_REFERRED = "Referred";
    private static final String COL_USERS_DEFCARD = "DefCard";

    private static final String TABLE_ADDRESS = "Address";
    private static final String COL_ADDRESS_ID = "AddressID";
    private static final String COL_ADDRESS_STREET = "Street";
    private static final String COL_ADDRESS_PROVINCE = "Province";
    private static final String COL_ADDRESS_ZIP = "ZIP";
    private static final String COL_ADDRESS_CITY = "City";
    private static final String COL_ADDRESS_APT = "AptNumb";
    private static final String COL_ADDRESS_LATITUDE = "Latitude";
    private static final String COL_ADDRESS_LONGITUDE = "Longitude";
    private static final String COL_ADDRESS_USERID = "UserID";

    private static final String TABLE_STORE = "StoreInfo";
    private static final String COL_STORE_NAME = "StoreName";
    private static final String COL_STORE_ADDRESS = "StoreAddress";
    private static final String COL_STORE_PROVINCE = "StoreProvince";

    private static final String TABLE_STORE_ITEMS = "Store_Items";
    private static final String COL_STORE_ITEMS_STOREID = "StoreID";
    private static final String COL_STORE_ITEMS_ITEMID = "ItemID";

    private static final String TABLE_ITEMS = "Items";
    private static final String COL_ITEMS_ID = "ItemID";

    private static final String TABLE_CARD = "Credit_Card";
    private static final String COL_CARD_NUM = "CardNum";
    private static final String COL_CARD_UID = "UserID";
    private static final String COL_CARD_NAME = "NameOnCard";
    private static final String COL_CARD_DATE = "ExpiryDateMonth";
    private static final String COL_CARD_YEAR = "ExpiryDateYear";

    private static final String TABLE_ORDER = "Orders";
    private static final String COL_ORDER_ORDERID = "OrderID";
    private static final String COL_ORDER_STOREID = "StoreID";
    private static final String COL_ORDER_USERID = "UserID";
    private static final String COL_ORDER_EMPLOYEEID = "EmployeeID";
    private static final String COL_ORDER_DELIVERY = "DeliveryOption";
    private static final String COL_ORDER_DATE = "DateOfOrder";
    private static final String COL_ORDER_PRICE = "Price";


    private static final String TABLE_HOLDER = "Order_Holder";
    private static final String COL_HOLDER_STORE = "StoreID";
    private static final String COL_HOLDER_ORDER = "OrderID";
    private static final String COL_HOLDER_ITEM= "ITemID";
    private static final String COL_HOLDER_QUANT = "ItemQuant";
    private static final String COL_HOLDER_NAME = "ItemName";
    private static final String COL_HOLDER_PRICE = "ItemPrice";

    public DBAdapter(Context context)
    {
        this.mContext = context;
        mDbHelper = new DataBaseHelper(mContext);
    }

    public DBAdapter createDatabase() throws SQLException
    {
        try
        {
            mDbHelper.createDataBase();
        }
        catch (IOException mIOException)
        {
            Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public DBAdapter open() throws SQLException
    {
        try
        {
            mDbHelper.openDataBase();
            mDbHelper.close();
            mDb = mDbHelper.getWritableDatabase();
        }
        catch (SQLException mSQLException)
        {
            Log.e(TAG, "open >>"+ mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }

    public void close()
    {
        mDbHelper.close();
    }

    public Cursor getTestData()
    {
        try
        {
            String sql ="SELECT * FROM myTable";

            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur!=null)
            {
                mCur.moveToNext();
            }
            return mCur;
        }
        catch (SQLException mSQLException)
        {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }

    public String Login(String username, String password) throws SQLException {
        Cursor mCursor = mDb.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE Email=? AND Password=?", new String[]{username, password});
        if (mCursor != null) {
            if (mCursor.getCount() > 0) {
                if (mCursor.moveToFirst()) {
                    return mCursor.getString(mCursor.getColumnIndex("UserID"));
                }
            }
        }
            return "false";
    }

    public String CheckAdress(String username) throws SQLException {
        Cursor defAdress = mDb.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE Email=?", new String[]{username});
        if(defAdress.moveToFirst()){
            return defAdress.getString(defAdress.getColumnIndex("DefAdress"));
        } else {
            return null;
        }

    }

    public long AddUser(String username, String password,String firstName, String lastName, String phoneNumber)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(COL_USERS_FIRSTNAME, firstName);
        initialValues.put(COL_USERS_LASTNAME, lastName);
        initialValues.put(COL_USERS_EMAIL, username);
        initialValues.put(COL_USERS_PHONE, phoneNumber);
        initialValues.put(COL_USERS_PASSWORD, password);
        initialValues.put(COL_USERS_REFERRED, 0);

        return mDb.insert(TABLE_USERS, null, initialValues);
    }

    public boolean CheckEmail(String username) throws SQLException{
        Cursor mCursor = mDb.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE Email=?", new String[]{username});
        if(mCursor != null){
            if(mCursor.getCount() > 0) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public long AddAddress(String apt, String street, String province, String zip, String city, String lat, String longitude, String userID) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(COL_ADDRESS_APT, apt);
        initialValues.put(COL_ADDRESS_STREET, street);
        initialValues.put(COL_ADDRESS_PROVINCE, province);
        initialValues.put(COL_ADDRESS_ZIP, zip);
        initialValues.put(COL_ADDRESS_CITY, city);
        initialValues.put(COL_ADDRESS_LATITUDE, lat);
        initialValues.put(COL_ADDRESS_LONGITUDE, longitude);
        initialValues.put(COL_ADDRESS_USERID, userID);

        return mDb.insert(TABLE_ADDRESS, null, initialValues);
    }

    public Cursor StoreList(){
        Cursor dbCursor = mDb.rawQuery("SELECT * FROM " + TABLE_STORE, null );
        if(dbCursor != null) {
            dbCursor.moveToNext();
        }
        return dbCursor;
    }

    public Cursor StoreItems(String storeID) {
        Cursor sICursor = mDb.rawQuery("SELECT * FROM " + TABLE_ITEMS + " INNER JOIN " + TABLE_STORE_ITEMS + " ON " +TABLE_STORE_ITEMS + "."
                + COL_STORE_ITEMS_ITEMID + " = " +TABLE_ITEMS + "." +COL_ITEMS_ID + " WHERE Store_Items.StoreID=?", new String[]{storeID});
        if(sICursor != null) {
            sICursor.moveToNext();
        }
        return sICursor;
    }

    public long AddCreditCard(String userID, String cardNo, String expDate, String expYear, String nameCard) {
        ContentValues cardValues = new ContentValues();
        cardValues.put(COL_CARD_UID, userID);
        cardValues.put(COL_CARD_NUM, cardNo);
        cardValues.put(COL_CARD_DATE, expDate);
        cardValues.put(COL_CARD_YEAR, expYear);
        cardValues.put(COL_CARD_NAME, nameCard);

        return mDb.insert(TABLE_CARD, null, cardValues);
    }

    public boolean CheckCard(String cardNo){
        Cursor mCursor = mDb.rawQuery("SELECT * FROM " + TABLE_CARD + " WHERE CardNum=?", new String[]{cardNo});
        if(mCursor != null){
            if(mCursor.getCount() > 0) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void DefAdress(String adressID, String userID) {
        ContentValues address = new ContentValues();
        address.put(COL_USERS_ADRESSID, adressID);
        mDb.update(TABLE_USERS, address, COL_USERS_ID + "=" + userID, null);
    }

    public Cursor UserAddresses(String userID) {
        Cursor addressCursor = mDb.rawQuery("SELECT * FROM " + TABLE_ADDRESS + " WHERE UserID=?", new String[]{userID});
        if(addressCursor != null) {
            addressCursor.moveToNext();
        }
        return addressCursor;
    }

    public Cursor GetItem(String itemID){
        Cursor itemCursor = mDb.rawQuery("SELECT * FROM " + TABLE_ITEMS + " WHERE ItemID=?", new String[]{itemID});
        if(itemCursor != null){
            return itemCursor;
        } else {
            return null;
        }
    }

    public void DefCard(String cardNum, String userID) {
        ContentValues cards = new ContentValues();
        cards.put(COL_USERS_DEFCARD, cardNum);
        mDb.update(TABLE_USERS, cards, COL_USERS_ID + "=" + userID, null);
    }

    public Cursor UserCreditCards(String userID) {
        Cursor cardCursor = mDb.rawQuery("SELECT * FROM " + TABLE_CARD + " WHERE UserID=?", new String[]{userID});
        if(cardCursor != null) {
            cardCursor.moveToNext();
        }
        return cardCursor;
    }

    public long DeleteCard(String cardNum) {
        return mDb.delete(TABLE_CARD, "CardNum=?", new String[] {cardNum});
    }


    public String OrderID() {
        Cursor orderCursor = mDb.rawQuery("SELECT * FROM " + TABLE_ORDER, null);
        if(orderCursor.moveToLast()){
            return orderCursor.getString(0);
        } else {
            return "Nope";
        }
    }

    public long DeleteHolder() {
        return mDb.delete(TABLE_HOLDER, null, null);
    }

    public Cursor Holder(){
        Cursor holderCursor = mDb.rawQuery("SELECT * FROM " + TABLE_HOLDER, null);
        return holderCursor;
    }

    public void AddHolder(String storeID, String itemID, int quantity, String itemName, String itemPrice){
        ContentValues holderValues = new ContentValues();
        holderValues.put(COL_HOLDER_STORE, storeID);
        holderValues.put(COL_HOLDER_ORDER, "1");
        holderValues.put(COL_HOLDER_ITEM, itemID);
        holderValues.put(COL_HOLDER_QUANT, quantity);
        holderValues.put(COL_HOLDER_NAME, itemName);
        holderValues.put(COL_HOLDER_PRICE, itemPrice);

        mDb.insert(TABLE_HOLDER, null, holderValues);
    }

    public String StoreName(String storeID){
        Cursor storeCursor = mDb.rawQuery("SELECT * FROM " + TABLE_STORE + " WHERE StoreID=?", new String[]{storeID});
        if(storeCursor.moveToFirst()) {
            return storeCursor.getString(storeCursor.getColumnIndex("StoreName"));
        } else {
            return "fuckoff";
        }
    }

    public long DeleteItem(String itemID){
       return mDb.delete(TABLE_HOLDER, "ItemID=?", new String[]{itemID});
    }

    public String GetDefCard(String userID){
        Cursor firstCursor = mDb.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE UserID=?", new String[]{userID});
        firstCursor.moveToFirst();
         return  firstCursor.getString(firstCursor.getColumnIndex(COL_USERS_DEFCARD));
    }

    public String Referred(String userID) {
        Cursor referCursor = mDb.rawQuery("SELECT Referred FROM " + TABLE_USERS + " WHERE UserID=?", new String[]{userID});
        referCursor.moveToFirst();
        return referCursor.getString(referCursor.getColumnIndex("Referred"));
    }

    public long UpdateReferredPlus(String userID){
    Cursor upRefCursor = mDb.rawQuery("SELECT Referred FROM " + TABLE_USERS + " WHERE UserID=?", new String[]{userID});
    upRefCursor.moveToFirst();
    int refCount = Integer.parseInt(upRefCursor.getString(upRefCursor.getColumnIndex("Referred")));
    refCount++;
    ContentValues cv = new ContentValues();
    cv.put("Referred", String.valueOf(refCount));
    return mDb.update(TABLE_USERS, cv, "UserID=?", new String[]{userID});
    }

    public void UpdateReferredMinus(String userID){
        Cursor upRefCursor = mDb.rawQuery("SELECT Referred FROM " + TABLE_USERS + " WHERE UserID=?", new String[]{userID});
        upRefCursor.moveToFirst();
        int refCount = Integer.parseInt(upRefCursor.getString(upRefCursor.getColumnIndex("Referred")));
        refCount--;
        if(refCount < 0){
            refCount = 0;
        }
        ContentValues cv = new ContentValues();
        cv.put("Referred", String.valueOf(refCount));
        mDb.update(TABLE_USERS, cv, "UserID=?", new String[]{userID});
    }

    public void OrderConfirm(String userID, String storeID, String deliveryOp, String price, String storeName) {
        ContentValues cv = new ContentValues();
        cv.put(COL_ORDER_DATE, storeName);
        cv.put(COL_ORDER_DELIVERY, deliveryOp);
        cv.put(COL_ORDER_EMPLOYEEID, "0");
        cv.put(COL_ORDER_USERID, userID);
        cv.put(COL_ORDER_STOREID, storeID);
        cv.put(COL_ORDER_PRICE, price);

        mDb.insert(TABLE_ORDER, null, cv);
    }

    public String OrderIDLast(){
        Cursor myCursor = mDb.query(TABLE_ORDER, null, null, null, null, null, null);
        myCursor.moveToLast();
        return myCursor.getString(myCursor.getColumnIndex("OrderID"));
    }

    public Cursor History(String userID){
        Cursor historyCursor = mDb.rawQuery("SELECT * FROM " + TABLE_ORDER + " WHERE UserID=?", new String[]{userID});
        historyCursor.moveToFirst();
        return historyCursor;
    }

    public void updatePassword(String UserID, String Password){
        ContentValues cb = new ContentValues();
        cb.put(COL_USERS_PASSWORD,Password);
        mDb.update(TABLE_USERS, cb," UserID=?", new String[]{UserID} );
    }
}