package com.practice.kenny.remotecart;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.DecimalFormat;

public class OrderConfirmation extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);
        recyclerView = findViewById(R.id.historyView);

        drawerLayout = findViewById(R.id.drawer_layout);
        final DecimalFormat df = new DecimalFormat("$###.###");

        Toolbar toolbar = findViewById(R.id.toolbar9);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionbar.setTitle("");

        final TextView orderNumTxt = findViewById(R.id.orderNum);
        final TextView storeNameTxt = findViewById(R.id.storeName);
        final TextView totalTxt = findViewById(R.id.total);

        String storeID = this.getIntent().getStringExtra("StoreID");
        String total = this.getIntent().getStringExtra("Price");
        double priceDouble = Double.parseDouble(total);
        totalTxt.setText(df.format(priceDouble));

        DBAdapter mDb = new DBAdapter(this);
        mDb.createDatabase();
        mDb.open();
        String orderID = mDb.OrderIDLast();
        String storeName = this.getIntent().getStringExtra("StoreName");
        storeNameTxt.setText(storeName);
        orderNumTxt.setText(orderID);

        mDb.close();
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
                        navigator.DrawerNavClick(OrderConfirmation.this, menuId);
                        return true;
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
