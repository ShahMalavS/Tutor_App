package com.malav.tutorapp.main;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.malav.tutorapp.R;
import com.malav.tutorapp.utils.Constants;

/**
 * Created by shahmalav on 24/09/16.
 */

public class SplashActivity extends  Activity {
    private static int SPLASH_TIME_OUT = 2000;

    //sharedPreferences
    SharedPreferences someData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        someData = getSharedPreferences(Constants.filename, 0);
        new Handler().postDelayed(new Runnable() {

	            /*sd
	             * Showing splash screen with a timer. This will be useful when you
	             * want to show case your app logo / company
	             */

            @Override
            public void run() {
                if (someData.contains("isLoggedIn") && someData.getBoolean("isLoggedIn", Boolean.FALSE)) {

                    if (someData.contains("isRegistered") && someData.getBoolean("isRegistered", Boolean.FALSE)) {
                        Intent i = new Intent(SplashActivity.this, DashboardActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Intent i = new Intent(SplashActivity.this, SignUpActivity.class);
                        startActivity(i);
                        finish();
                    }
                } else {
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }

            }
        }, SPLASH_TIME_OUT);
    }
}
