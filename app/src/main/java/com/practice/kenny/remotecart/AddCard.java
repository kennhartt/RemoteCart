package com.practice.kenny.remotecart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.view.GravityCompat;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

public class AddCard extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);
        final EditText cardMonth = findViewById(R.id.month);
        final EditText cardYear = findViewById(R.id.year);
        final EditText cvcTxt = findViewById(R.id.cvc);
        final Button addCardBtn = findViewById(R.id.addBtn);

        drawerLayout = findViewById(R.id.drawer_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionbar.setTitle("Credit Card Information");

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
                        navigator.DrawerNavClick(AddCard.this, menuId);
                        return true;
                    }
                });
        addCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText cardNameTxt = findViewById(R.id.cardName);
                final EditText cardNoTxt = findViewById(R.id.cardNumTxt);

                String month = cardMonth.getText().toString();
                String year = cardYear.getText().toString();
                String cardName = cardNameTxt.getText().toString();
                String cardNo = cardNoTxt.getText().toString();
                String cvc = cvcTxt.getText().toString();

                if(TextUtils.isEmpty(cardName)) {
                    cardNameTxt.setError("Cannot be empty");
                    return;
                }
                if(TextUtils.isEmpty(cardNo) | cardNo.length() < 16) {
                    cardNoTxt.setError("Cannot be empty, At least 16 characters");
                    return;
                }
                if(TextUtils.isEmpty(month)) {
                    cardMonth.setError("Cannot be empty");
                    return;
                }
                if(TextUtils.isEmpty(year)) {
                    cardYear.setError("Cannot be empty");
                    return;
                }

                if(TextUtils.isEmpty(cvc)) {
                    cvcTxt.setError("Cannot be empty");
                    return;
                }

                if(Integer.parseInt(month) >= 1 && Integer.parseInt(month) <= 12) {


                    DBAdapter mDBAdapter = new DBAdapter(AddCard.this);
                    mDBAdapter.createDatabase();
                    mDBAdapter.open();
                    if(!mDBAdapter.CheckCard(cardNo)){
                        mDBAdapter.AddCreditCard(SessionState.getLoggedInIDUser(AddCard.this), cardNo, month, year, cardName);
                    } else {
                        Toast.makeText(AddCard.this, String.valueOf(mDBAdapter.CheckCard(cardNo)), Toast.LENGTH_LONG).show();
                    }
                    mDBAdapter.close();
                }
                startActivity(new Intent(AddCard.this, CardList.class));
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
