package com.practice.kenny.remotecart;

import android.content.Intent;
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
import android.widget.Toast;

public class Registration extends AppCompatActivity {
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Button signUp = findViewById(R.id.RegBtn);

        Toolbar toolbar = findViewById(R.id.toolbar6);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(false);
        actionbar.setTitle("Registration");

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText userEmail = findViewById(R.id.email);
                final EditText userPassword = findViewById(R.id.password);
                final EditText firstNameTxt = findViewById(R.id.firstName);
                final EditText lastNameTxt = findViewById(R.id.lastName);
                final EditText conPassTxt= findViewById(R.id.conPass);
                final EditText phoneNumberTxt = findViewById(R.id.phoneNumber);

                String username = userEmail.getText().toString();
                String password = userPassword.getText().toString();
                String firstName = firstNameTxt.getText().toString();
                String lastName = lastNameTxt.getText().toString();
                String phoneNumber = phoneNumberTxt.getText().toString();
                String conPassword = conPassTxt.getText().toString();

                if(TextUtils.isEmpty(username)) {
                    userEmail.setError("Cannot be empty");
                    return;
                }
                if(TextUtils.isEmpty(password)) {
                    userPassword.setError("Cannot be empty");
                    return;
                }
                if(TextUtils.isEmpty(firstName)) {
                    firstNameTxt.setError("Cannot be empty");
                    return;
                }
                if(TextUtils.isEmpty(lastName)) {
                    lastNameTxt.setError("Cannot be empty");
                    return;
                }
                if(TextUtils.isEmpty(conPassword)) {
                    conPassTxt.setError("Cannot be empty");
                    return;
                }
                if(TextUtils.isEmpty(password)) {
                    conPassTxt.setError("Cannot be empty");
                    return;
                }

                if(password.equals(conPassword)) {
                    DBAdapter mDBAdapter = new DBAdapter(Registration.this);
                    mDBAdapter.createDatabase();
                    mDBAdapter.open();
                    if(!mDBAdapter.CheckEmail(username)){
                        long result = mDBAdapter.AddUser(username, password, firstName, lastName, phoneNumber);
                        if(-1 == result){
                            Toast.makeText(Registration.this, "Please give us an A++", Toast.LENGTH_SHORT).show();
                        } else {
                            final EditText referIDTxt = findViewById(R.id.referID);
                            String referID = referIDTxt.getText().toString();
                            if(!TextUtils.isEmpty(referID)) {
                                int referINT = Integer.parseInt(referID) - 200000;
                                if (referINT > 0) {
                                    Toast.makeText(Registration.this, String.valueOf(mDBAdapter.UpdateReferredPlus(String.valueOf(referINT))), Toast.LENGTH_LONG).show();
                                }
                            }
                            startActivity(new Intent(Registration.this, MainActivity.class));
                        }

                    } else {
                        Toast.makeText(Registration.this, "This email is already in use", Toast.LENGTH_LONG).show();
                    }
                    mDBAdapter.close();
                } else {
                    Toast.makeText(Registration.this, "Passwords do not match", Toast.LENGTH_LONG).show();

                }

            }
        });
    }
}
