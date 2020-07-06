package com.practice.kenny.remotecart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button signIn = findViewById(R.id.signIn);

        final TextView registerLink = findViewById(R.id.registerLink);

        signIn.setOnClickListener(this);
        registerLink.setOnClickListener(this);


    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registerLink:
                startActivity(new Intent(MainActivity.this, Registration.class));
                break;
            case R.id.signIn:
                final EditText userEmail = findViewById(R.id.email);
                final EditText userPassword = findViewById(R.id.password);
                final String username = userEmail.getText().toString();
                final String password = userPassword.getText().toString();
                if(username.length() > 0 & password.length() > 0 ) {
                    DBAdapter mDBAdapter = new DBAdapter(this);
                    mDBAdapter.createDatabase();
                    mDBAdapter.open();
                    String userID = mDBAdapter.Login(username, password);
                    if(userID != "false"){
                        SessionState.setLoggedInUserEmail(this, username);
                        SessionState.setLoggedInUserPassword(this, password);
                        SessionState.setLoggedInUserID(this, userID);
                        SessionState.setUserLoggedInStatus(this, true);
                        mDBAdapter.DeleteHolder();
                        if(mDBAdapter.CheckAdress(username) != null) {
                            mDBAdapter.close();
                            startActivity(new Intent(MainActivity.this, StoreList.class));
                        } else {
                            mDBAdapter.close();
                            startActivity(new Intent(MainActivity.this, StoreDetails.class));
                        }
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "E-mail or Password Incorrect",Toast.LENGTH_LONG).show();
                        mDBAdapter.close();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Please enter email and password",Toast.LENGTH_LONG).show();
                }

        }
    }
}
