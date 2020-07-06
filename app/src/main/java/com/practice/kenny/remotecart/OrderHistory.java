package com.practice.kenny.remotecart;

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

import java.util.ArrayList;
import java.util.List;

public class OrderHistory extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public List<Orders> orders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        recyclerView = findViewById(R.id.historyView);
        drawerLayout = findViewById(R.id.drawer_layout);

        Toolbar toolbar = findViewById(R.id.toolbar10);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionbar.setTitle("Order History");

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
                        navigator.DrawerNavClick(OrderHistory.this, menuId);
                        return true;
                    }
                });
        DBAdapter mDB = new DBAdapter(this);
        mDB.createDatabase();
        mDB.open();
        Cursor myCursor = mDB.History(SessionState.getLoggedInIDUser(this));
        if(myCursor != null && myCursor.moveToFirst()) {
            do {
                Orders order = new Orders();
                order.setStoreName(myCursor.getString(myCursor.getColumnIndex("DateOfOrder")));
                order.setPrice(myCursor.getString(myCursor.getColumnIndex("Price")));
                order.setDelivery(myCursor.getString(myCursor.getColumnIndex("DeliveryOption")));
                orders.add(order);
            } while (myCursor.moveToNext());
        }
        mDB.close();

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new OrdersListAdapter(orders);
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
