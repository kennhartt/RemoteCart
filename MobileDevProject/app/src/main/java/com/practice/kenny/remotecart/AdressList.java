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

public class AdressList extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    public List<Addresses> addresses = new ArrayList<>();
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adress_list);
        recyclerView = findViewById(R.id.adressListRecycler);
        drawerLayout = findViewById(R.id.drawer_layout);

        Toolbar toolbar = findViewById(R.id.toolbar7);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionbar.setTitle("Address List");

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
                        navigator.DrawerNavClick(AdressList.this, menuId);
                        return true;
                    }
                });


        DBAdapter mDB = new DBAdapter(this);
        mDB.createDatabase();
        mDB.open();
        Cursor myCursor = mDB.UserAddresses(SessionState.getLoggedInIDUser(this));
        if(myCursor != null && myCursor.moveToFirst()) {
            do {
                Addresses address = new Addresses();
                address.setAptNo(myCursor.getString(myCursor.getColumnIndex("AptNumb")));
                address.setStreet(myCursor.getString(myCursor.getColumnIndex("Street")));
                address.setZip(myCursor.getString(myCursor.getColumnIndex("ZIP")));
                address.setProvince(myCursor.getString(myCursor.getColumnIndex("Province")));
                address.setCity(myCursor.getString(myCursor.getColumnIndex("City")));
                address.setAddressID(myCursor.getString(myCursor.getColumnIndex("AddressID")));
                addresses.add(address);
            } while (myCursor.moveToNext());
        }
        mDB.close();

        final Button addAddress = findViewById(R.id.addAddress);
        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdressList.this, StoreDetails.class));
            }
        });

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new AddressListAdapter(addresses);
        recyclerView.setAdapter(mAdapter);
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
