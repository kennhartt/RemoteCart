package com.practice.kenny.remotecart;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.EditText;
import android.widget.Toast;

public class PasswordChange extends AppCompatActivity {
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);
        drawerLayout = findViewById(R.id.drawer_layout);

        Toolbar toolbar = findViewById(R.id.toolbar5);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionbar.setTitle("Change Your Password");

        final String currentPassword = SessionState.getLoggedInPasswordUser(this);
        final EditText curPassword = findViewById(R.id.curPass);
        final EditText newPassword = findViewById(R.id.newPass);
        final EditText confirmPassword = findViewById(R.id.confirmPass);
        Button confirmBtn = findViewById(R.id.confirmBtn);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(curPassword.getText().toString().equals(currentPassword)) {
                    if (newPassword.length() != 0 && newPassword.length()> 4) {
                        if (newPassword.getText().toString().equals(confirmPassword.getText().toString())) {
                            final String newPass = newPassword.getText().toString();
                            DBAdapter myDB = new DBAdapter(PasswordChange.this);
                            myDB.createDatabase();
                            myDB.open();
                            String UserID = SessionState.getLoggedInIDUser(PasswordChange.this);
                            myDB.updatePassword(UserID, newPass);
                            myDB.close();
                            SessionState.setLoggedInUserPassword(PasswordChange.this, newPass);
                            startActivity(new Intent(PasswordChange.this, StoreList.class));
                            Toast.makeText(PasswordChange.this, "Password Changed", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(PasswordChange.this, "Please confirm your new password", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if( newPassword == null){
                        Toast.makeText(PasswordChange.this, "Please enter new password", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(PasswordChange.this, "Please re-enter your current password", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
                        navigator.DrawerNavClick(PasswordChange.this, menuId);
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
