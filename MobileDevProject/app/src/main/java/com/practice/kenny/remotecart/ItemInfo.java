package com.practice.kenny.remotecart;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ItemInfo extends AppCompatActivity implements View.OnClickListener{
    private DrawerLayout drawerLayout;
    int quantity = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);

        final TextView itemNameTxt = findViewById(R.id.itemName);
        final ImageView itemImg = findViewById(R.id.itemImg);
        final ImageView add = findViewById(R.id.imageView6);
        final ImageView subtract = findViewById(R.id.minus);
        final Button addCart = findViewById(R.id.addBtn);
        final Button removeItem = findViewById(R.id.removeBtn);
        addCart.setOnClickListener(this);
        removeItem.setOnClickListener(this);
        add.setOnClickListener(this);
        subtract.setOnClickListener(this);

        drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionbar.setTitle("Item Information");

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
                        navigator.DrawerNavClick(ItemInfo.this, menuId);
                        return true;
                    }
                });


        String itemID = this.getIntent().getStringExtra("ItemID");

        DBAdapter mDB = new DBAdapter(this);
        mDB.createDatabase();
        mDB.open();
        Cursor mCursor = mDB.GetItem(itemID);
        if(mCursor != null) {
            if(mCursor.moveToFirst()){
                String itemName = mCursor.getString(mCursor.getColumnIndex("ItemName"));
                itemNameTxt.setText(itemName);
            }
        }
        mDB.close();
    }

    @Override
    public void onClick(View v){

        switch (v.getId()){
            case R.id.addBtn:
                DBAdapter mDB = new DBAdapter(this);
                mDB.createDatabase();
                mDB.open();
                Cursor mCursor = mDB.Holder();
                Boolean flag = false;
                String intentStoreID = this.getIntent().getStringExtra("StoreID");

                if(!mCursor.moveToFirst()) {
                    flag = false;
                } else {
                    String storeID = mCursor.getString(mCursor.getColumnIndex("StoreID"));
                    if(storeID.equals(intentStoreID)){
                        flag = false;
                    } else {
                        flag = true;
                    }
                }
                if(flag){
                    if(mDB.DeleteHolder() == -1){
                        Toast.makeText(this, "Holder not deleted", Toast.LENGTH_LONG).show();
                    } else {
                        final EditText quantityTxt = findViewById(R.id.itemAmount);
                        if(TextUtils.isEmpty(quantityTxt.getText().toString())) {
                            quantity = 1;
                        } else {
                            quantity = Integer.parseInt(quantityTxt.getText().toString());
                        }
                        String itemID = this.getIntent().getStringExtra("ItemID");
                        String itemName = this.getIntent().getStringExtra("ItemName");
                        String itemPrice = this.getIntent().getStringExtra("ItemPrice");
                        mDB.AddHolder(intentStoreID, itemID, quantity, itemName, itemPrice);

                    }
                } else {
                    final EditText quantityTxt = findViewById(R.id.itemAmount);
                    String empty = quantityTxt.getText().toString();
                    if(empty.matches("")) {
                        quantity = 1;
                    } else {
                        quantity = Integer.parseInt(quantityTxt.getText().toString());
                    }
                    String itemID = this.getIntent().getStringExtra("ItemID");
                    String itemName = this.getIntent().getStringExtra("ItemName");
                    String itemPrice = this.getIntent().getStringExtra("ItemPrice");
                    mDB.AddHolder(intentStoreID, itemID, quantity, itemName, itemPrice);
                }
                mDB.close();
                Intent i = new Intent(ItemInfo.this, ItemList.class);
                i.putExtra("StoreID", intentStoreID);
                startActivity(i);
                ItemInfo.this.finish();

                break;
            case R.id.removeBtn:
                DBAdapter removeDb = new DBAdapter(ItemInfo.this);
                removeDb.createDatabase();
                removeDb.open();
                String itemId= this.getIntent().getStringExtra("ItemID");
                if(removeDb.DeleteItem(itemId) == -1) {
                    Toast.makeText(ItemInfo.this, "Couldnt Delete, sorry", Toast.LENGTH_SHORT);
                } else {
                    startActivity(new Intent(ItemInfo.this, OrderDelivery.class));
                }
                removeDb.close();
                String intentStoreID2 = this.getIntent().getStringExtra("StoreID");
                Intent i2 = new Intent(ItemInfo.this, ItemList.class);
                i2.putExtra("StoreID", intentStoreID2);
                startActivity(i2);
                ItemInfo.this.finish();
                break;
            case R.id.imageView6:
                final EditText quantityTxt = findViewById(R.id.itemAmount);
                String empty = quantityTxt.getText().toString();
                if(empty.matches("")) {
                    quantity = 1;
                } else {
                    quantity++;
                }
                quantityTxt.setText(String.valueOf(quantity));
                break;
            case R.id.minus:
                final EditText quantityTxt2 = findViewById(R.id.itemAmount);
                String empty2 = quantityTxt2.getText().toString();
                if(empty2.matches("")) {
                    quantity = 1;
                } else {
                    quantity--;
                    if(quantity <= 1) {
                        quantity = 1;
                    }
                }
                quantityTxt2.setText(String.valueOf(quantity));
                break;
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
