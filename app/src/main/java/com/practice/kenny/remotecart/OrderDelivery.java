package com.practice.kenny.remotecart;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class OrderDelivery extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private DrawerLayout drawerLayout;

    public List<Items> itemList = new ArrayList<>();
    public List<Items> prices = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_delivery);

        final DecimalFormat df = new DecimalFormat("$###.###");

        recyclerView = findViewById(R.id.orderList);
        final DBAdapter mDBAdapter = new DBAdapter(this);
        mDBAdapter.createDatabase();
        mDBAdapter.open();
        Cursor myCursor = mDBAdapter.Holder();
        if (myCursor.getCount() == 0) {
            startActivity(new Intent(this, EmptyCart.class));
            this.finish();
        } else {
            myCursor.moveToFirst();
            final String storeID = myCursor.getString(myCursor.getColumnIndex("StoreID"));
            final TextView priceTxt = findViewById(R.id.price);
            double priceTotal = 0;
            final TextView storeNameText = findViewById(R.id.storeName);
            final String storeName = mDBAdapter.StoreName(storeID);
            storeNameText.setText(storeName);
            drawerLayout = findViewById(R.id.drawer_layout);
            Toolbar toolbar = findViewById(R.id.toolbar4);
            setSupportActionBar(toolbar);
            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionbar.setTitle("Order and Delivery");
            String cardNumb = mDBAdapter.GetDefCard(SessionState.getLoggedInIDUser(this));
//            final TextView cardChoice = findViewById(R.id.cardChoice);
//            cardChoice.setText(cardNumb);


            final NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(
                    new NavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(MenuItem menuItem) {
                            // set item as selected to persist highlight
                            menuItem.setChecked(true);
                            // close drawer when item is tapped
                            drawerLayout.closeDrawers();

                            String menuId = menuItem.getTitle().toString();
                            DrawerNav navigator = new DrawerNav();
                            navigator.DrawerNavClick(OrderDelivery.this, menuId);
                            return true;
                        }
                    });

            if (myCursor != null && myCursor.moveToFirst()) {
                do {
                    Items item = new Items();
                    item.setItemName(myCursor.getString(myCursor.getColumnIndex("ItemName")) + " x" + myCursor.getString(myCursor.getColumnIndex("ItemQuant")));
                    item.setItemPrice(myCursor.getString(myCursor.getColumnIndex("ItemPrice")) + " Each.");
                    item.setItemID(myCursor.getString((myCursor.getColumnIndex("ItemID"))));
                    item.setStoreID(storeID);
                    itemList.add(item);
                } while (myCursor.moveToNext());

            }

            if (myCursor != null && myCursor.moveToFirst()) {
                do {
                    Items item = new Items();
                    item.setItemName(myCursor.getString(myCursor.getColumnIndex("ItemName")));
                    item.setItemPrice(myCursor.getString(myCursor.getColumnIndex("ItemPrice")));
                    item.setItemQuant(myCursor.getString(myCursor.getColumnIndex("ItemQuant")));

                    prices.add(item);
                } while (myCursor.moveToNext());

            }


            for (int i = 0; i < prices.size(); i++) {
                priceTotal += Double.parseDouble(prices.get(i).getItemPrice()) * Double.parseDouble(prices.get(i).getItemQuant());
            }
            priceTxt.setText(df.format(priceTotal));

            final double priceTotalOuter = priceTotal;

            final RadioButton deliveryOpt = findViewById(R.id.delivery);
            final RadioButton pickupOption = findViewById(R.id.pickup);
            final RadioGroup rG = findViewById(R.id.radioGroup);
            rG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if(deliveryOpt.isChecked()) {
                        int referredCount = 0;
                        SessionState.setPrefUserDel(OrderDelivery.this, "Delivery");
                        mDBAdapter.createDatabase();
                        mDBAdapter.open();
                        referredCount = Integer.parseInt(mDBAdapter.Referred(SessionState.getLoggedInIDUser(OrderDelivery.this)));
                        if(referredCount > 0) {
                            final TextView discount = findViewById(R.id.discount);
                            discount.setText("%10 Off your order for referring a friend!");
                            final TextView finalPriceTxt = findViewById(R.id.finalPrice);
                            double finalPrice = ((priceTotalOuter + 3.5) / 100) * 90;
                            finalPriceTxt.setText("Final Price: " + df.format(finalPrice));
                            SessionState.setPrefUserRef(OrderDelivery.this, true);
                            SessionState.setPrefUserPrice(OrderDelivery.this, String.valueOf(finalPrice));
                        } else {
                            final TextView finalPriceTxt = findViewById(R.id.finalPrice);
                            double finalPrice = priceTotalOuter + 3.5;
                            finalPriceTxt.setText("Final Price: " + df.format(finalPrice));
                            SessionState.setPrefUserPrice(OrderDelivery.this, String.valueOf(finalPrice));

                        }
                        mDBAdapter.close();
                    } else if(pickupOption.isChecked()) {
                        mDBAdapter.createDatabase();
                        mDBAdapter.open();
                        final TextView finalPriceTxt = findViewById(R.id.finalPrice);
                        int referredCount = 0;
                        SessionState.setPrefUserDel(OrderDelivery.this, "Pick Up");
                        referredCount = Integer.parseInt(mDBAdapter.Referred(SessionState.getLoggedInIDUser(OrderDelivery.this)));
                        boolean referredFlag = false;
                        double finalPrice = priceTotalOuter;
                        if(referredCount > 0) {
                            final TextView discount = findViewById(R.id.discount);
                            discount.setText("%10 Off your order for referring a friend!");
                            double finalPrice2 = ((priceTotalOuter) / 100) * 90;
                            finalPriceTxt.setText("Final Price: " + df.format(finalPrice2));
                            SessionState.setPrefUserRef(OrderDelivery.this, true);
                            SessionState.setPrefUserPrice(OrderDelivery.this, String.valueOf(finalPrice2));
                        } else {
                            double finalPrice2 = priceTotalOuter;
                            finalPriceTxt.setText("Final Price: " + df.format(finalPrice2));
                            SessionState.setPrefUserPrice(OrderDelivery.this, String.valueOf(finalPrice2));

                        }
                        mDBAdapter.close();
                    }
                }
            });

            final Button purchaseButton = findViewById(R.id.purchaseBtn);
            purchaseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(SessionState.getPrefUserRef(OrderDelivery.this)) {
                        mDBAdapter.createDatabase();
                        mDBAdapter.open();
                        mDBAdapter.UpdateReferredMinus(SessionState.getLoggedInIDUser(OrderDelivery.this));
                        SessionState.setPrefUserRef(OrderDelivery.this, false);
                        mDBAdapter.close();
                    }
                    Intent i = new Intent(OrderDelivery.this, OrderConfirmation.class);
                    i.putExtra("UserID", SessionState.getLoggedInIDUser(OrderDelivery.this));
                    i.putExtra("StoreID", storeID);
                    i.putExtra("Delivery", SessionState.getPrefUserRDel(OrderDelivery.this));
                    i.putExtra("Price", SessionState.getPrefUserPrice(OrderDelivery.this));
                    i.putExtra("StoreName", storeName);
                    mDBAdapter.createDatabase();
                    mDBAdapter.open();
                    mDBAdapter.OrderConfirm(SessionState.getLoggedInIDUser(OrderDelivery.this), storeID, SessionState.getPrefUserRDel(OrderDelivery.this), SessionState.getPrefUserPrice(OrderDelivery.this), storeName);
                    mDBAdapter.DeleteHolder();
                    mDBAdapter.close();
                    startActivity(i);
                }
            });

            mDBAdapter.close();
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            mAdapter = new ItemRecycle(itemList);
            recyclerView.setAdapter(mAdapter);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
