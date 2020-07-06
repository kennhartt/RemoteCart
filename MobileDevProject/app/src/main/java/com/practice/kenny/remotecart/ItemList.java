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

import java.util.ArrayList;
import java.util.List;

public class ItemList extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private DrawerLayout drawerLayout;

    public List<Items> itemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        recyclerView = findViewById(R.id.itemListRecycler);
        String storeID = getIntent().getStringExtra("StoreID");

        drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar5);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionbar.setTitle("Item List");

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
                        navigator.DrawerNavClick(ItemList.this, menuId);
                        return true;
                    }
                });

        DBAdapter mDBAdapter = new DBAdapter(this);
        mDBAdapter.createDatabase();
        mDBAdapter.open();
        Cursor myCursor = mDBAdapter.StoreItems(storeID);
        if(myCursor != null && myCursor.moveToFirst()) {
            do {
                Items item = new Items();
                item.setItemName(myCursor.getString(myCursor.getColumnIndex("ItemName")));
                item.setItemPrice(myCursor.getString(myCursor.getColumnIndex("ItemPrice")));
                item.setItemID(myCursor.getString((myCursor.getColumnIndex("ItemID"))));
                item.setStoreID(storeID);
                itemList.add(item);
            } while (myCursor.moveToNext());

        }
        mDBAdapter.close();
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new ItemRecycle(itemList);
        recyclerView.setAdapter(mAdapter);

        final Button btnCart = findViewById(R.id.buttonCart);

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ItemList.this, OrderDelivery.class));
            }
        });
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
