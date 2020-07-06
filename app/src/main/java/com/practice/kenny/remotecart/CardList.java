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

public class CardList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    public List<Cards> cards = new ArrayList<>();
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);
        recyclerView = findViewById(R.id.storeListRecycler);
        drawerLayout = findViewById(R.id.drawer_layout);

        Toolbar toolbar = findViewById(R.id.toolbar12);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionbar.setTitle("Choose Your Card");

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
                        navigator.DrawerNavClick(CardList.this, menuId);
                        return true;
                    }
                });

        final Button addCard = findViewById(R.id.addBtn);
        addCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CardList.this, AddCard.class));
            }
        });
        DBAdapter mDB = new DBAdapter(this);
        mDB.createDatabase();
        mDB.open();
        Cursor myCursor = mDB.UserCreditCards(SessionState.getLoggedInIDUser(this));
        if(myCursor != null && myCursor.moveToFirst()) {
            do {
                Cards card = new Cards();
                card.setCardNum(myCursor.getString(myCursor.getColumnIndex("CardNum")));
                card.setNameOnCard(myCursor.getString(myCursor.getColumnIndex("NameOnCard")));
                card.setExpiryDateMonth(myCursor.getString(myCursor.getColumnIndex("ExpiryDateMonth")));
                card.setGetExpiryDateYear(myCursor.getString(myCursor.getColumnIndex("ExpiryDateYear")));
                cards.add(card);
            } while (myCursor.moveToNext());
        }
        mDB.close();

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new CardListAdapter(cards);
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
