package com.malav.tutorapp.main;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.malav.tutorapp.R;
import com.malav.tutorapp.utils.Constants;

/**
 * Created by shahmalav on 04/03/17.
 */

public class ForgotPasswordActivity extends AppCompatActivity{

    //sharedPreferences
    SharedPreferences someData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        someData = getSharedPreferences(Constants.filename, 0);


    }

    @Override
    public void onBackPressed(){
        finish();
    }
}
