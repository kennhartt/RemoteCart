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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class CardDetails extends AppCompatActivity implements View.OnClickListener{
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_details);

        //use view
        String cardNH = getIntent().getStringExtra("cardNumHidden");
        String expiryDate = getIntent().getStringExtra("expiryDate");

        final TextView cardNum = findViewById(R.id.cardNum);
        final TextView expDate = findViewById(R.id.expDate);

        drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionbar.setTitle("Card Details");

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
                        navigator.DrawerNavClick(CardDetails.this, menuId);
                        return true;
                    }
                });

        cardNum.setText(cardNH);
        expDate.setText(expiryDate);

        final Button rmvCard = findViewById(R.id.removeBtn);
        final Button defCard = findViewById(R.id.defCard);

        rmvCard.setOnClickListener(this);
        defCard.setOnClickListener(this);
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

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.removeBtn:
                final String cardN = getIntent().getStringExtra("cardNum");
                DBAdapter dbAdapter = new DBAdapter(CardDetails.this);
                dbAdapter.createDatabase();
                dbAdapter.open();
//                Cursor checkCursor = dbAdapter.GetDefCard(SessionState.getLoggedInIDUser(CardDetails.this));
                String defCardNum = dbAdapter.GetDefCard(SessionState.getLoggedInIDUser(CardDetails.this));
                if(cardN.equals(defCardNum)) {
                    Toast.makeText(CardDetails.this, "Change your default card first", Toast.LENGTH_SHORT).show();
                } else {
                    if (dbAdapter.DeleteCard(cardN) == -1)
                        Toast.makeText(CardDetails.this, "not Deleted", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(CardDetails.this, "Deleted", Toast.LENGTH_SHORT).show();
                    dbAdapter.close();
                    startActivity(new Intent(CardDetails.this, CardList.class));
                    CardDetails.this.finish();
                }
                break;
            case R.id.defCard:
                final String cardNN = getIntent().getStringExtra("cardNum");
                String userID = SessionState.getLoggedInIDUser(CardDetails.this);
                DBAdapter defDBAdapter = new DBAdapter(CardDetails.this);
                defDBAdapter.createDatabase();
                defDBAdapter.open();
                defDBAdapter.DefCard(cardNN, userID);
                defDBAdapter.close();
                startActivity(new Intent(CardDetails.this, StoreList.class));
                break;
        }
    }
}
