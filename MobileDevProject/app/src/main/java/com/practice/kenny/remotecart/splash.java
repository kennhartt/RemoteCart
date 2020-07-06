package com.practice.kenny.remotecart;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final int SPLASH_SCREEN_DURATION = 3000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(SessionState.getUserLoggedInStatus(splash.this) == true) {
                    startActivity(new Intent(splash.this, StoreList.class));
                    splash.this.finish();

                } else {
                    startActivity(new Intent(splash.this, MainActivity.class));
                    splash.this.finish();
                }
            }
        }, SPLASH_SCREEN_DURATION);
    }
}
