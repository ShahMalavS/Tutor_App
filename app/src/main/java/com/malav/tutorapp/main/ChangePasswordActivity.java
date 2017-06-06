package com.malav.tutorapp.main;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.malav.tutorapp.R;
import com.malav.tutorapp.custom.MyEditText;
import com.malav.tutorapp.custom.MyTextView;
import com.malav.tutorapp.utils.Constants;

/**
 * Created by shahmalav on 04/03/17.
 */

public class ChangePasswordActivity extends AppCompatActivity{

    //sharedPreferences
    SharedPreferences someData;
    private MyEditText password, newPassword, reNewPassword, emailId;
    private MyTextView submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        someData = getSharedPreferences(Constants.filename, 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Change Password");

        emailId = (MyEditText) findViewById(R.id.emailId);
        password = (MyEditText) findViewById(R.id.password);
        newPassword = (MyEditText) findViewById(R.id.newPass);
        reNewPassword = (MyEditText) findViewById(R.id.reNewPassword);
        submit = (MyTextView) findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_blank, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        finish();
    }
}
