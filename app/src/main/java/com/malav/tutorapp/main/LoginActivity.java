package com.malav.tutorapp.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.malav.tutorapp.R;
import com.malav.tutorapp.beans.User;
import com.malav.tutorapp.custom.MyEditText;
import com.malav.tutorapp.custom.MyTextView;
import com.malav.tutorapp.utils.CommonUtils;
import com.malav.tutorapp.utils.Constants;
import com.malav.tutorapp.utils.JSONfunctions;
import com.malav.tutorapp.utils.QueryMapper;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shahmalav on 04/03/17.
 */

public class LoginActivity extends AppCompatActivity{

    //sharedPreferences
    SharedPreferences someData;
    private MyEditText edtEmailId, edtPassword;
    private Boolean error = Boolean.FALSE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        someData = getSharedPreferences(Constants.filename, 0);

        edtEmailId = (MyEditText) findViewById(R.id.email);
        edtPassword = (MyEditText) findViewById(R.id.password);

        MyTextView btnForgotPassword = (MyTextView) findViewById(R.id.forgot_password);
        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(i);
            }
        });

        MyTextView btnLogin = (MyTextView) findViewById(R.id.login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CommonUtils.isNull(edtEmailId.getText().toString())){
                    edtEmailId.setError("EmailId cannot be null");
                    error = Boolean.TRUE;
                }
                if(CommonUtils.isNull(edtPassword.getText().toString())){
                    edtPassword.setError("Password cannot be null");
                    error = Boolean.TRUE;
                }
                if(!error){
                    new AttemptLogin().execute();
                }
            }
        });
    }

    private class AttemptLogin extends AsyncTask<Void, Void, Boolean> {

        private ProgressDialog progress;
        private String emailid, password;
        private User user;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            progress = new ProgressDialog(LoginActivity.this);
            progress.setIndeterminate(Boolean.FALSE);
            progress.setCancelable(Boolean.FALSE);
            progress.setTitle("Loggin in..");
            progress.setMessage("Please Wait..");
            progress.show();

            emailid = edtEmailId.getText().toString();
            password = edtPassword.getText().toString();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try{
                List<NameValuePair> para = new ArrayList<NameValuePair>();
                para.add(new BasicNameValuePair("emailId", emailid));
                para.add(new BasicNameValuePair("password", password));

                JSONObject json = JSONfunctions.makeHttpRequest("", "POST", para);
                Log.d("Login attempt", json.toString());

                user.setSuccess(json.getString("success"));
                user.setMessage(json.getString("message"));
                if(CommonUtils.equalIgnoreCase("1", user.getSuccess())){
                    user.setRole(json.getString("role"));
                    user.setName(json.getString("name"));
                    user.setProfilePic(json.getString("profilePic"));
                    user.setId(json.getString("l_Id"));
                    user.setIsRegistered(json.getString("registered"));
                    if(CommonUtils.equalIgnoreCase(user.getRole(),"Parent")){
                        user.setStudentId(json.getString("studentL_Id"));
                    }
                    return true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // TODO: register the new account here.
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {
                SharedPreferences.Editor editor = someData.edit();
                editor.putString("emailId", user.getEmailId());
                editor.putString("role", user.getRole());
                editor.putString("login", "true");
                editor.putString("name", user.getName());
                editor.putString("profilePic", user.getProfilePic());
                editor.putString("registered", user.getIsRegistered());
                editor.putString("user_id", user.getId());
                if(CommonUtils.equalIgnoreCase(user.getRole(),"Parent")){
                    editor.putString("student_id", user.getStudentId());
                }
                editor.apply();
                editor.commit();
                Intent i;
                if(CommonUtils.equalIgnoreCase("1",user.getIsRegistered())) {
                    if(CommonUtils.equalIgnoreCase(user.getRole(),"Admin")){
                        i = new Intent(LoginActivity.this, DashboardActivity.class);
                    }else if(CommonUtils.equalIgnoreCase(user.getRole(),"Student")){
                        i = new Intent(LoginActivity.this, DashboardActivity.class);
                    }else if(CommonUtils.equalIgnoreCase(user.getRole(),"Parent")){
                        i = new Intent(LoginActivity.this, DashboardActivity.class);
                    }else{
                        i = new Intent(LoginActivity.this, LoginActivity.class);
                    }
                }else{
                    i = new Intent(LoginActivity.this, SignUpActivity.class);
                }
                startActivity(i);
                finish();
            } else {
                edtPassword.setError(user.getMessage());
                edtPassword.requestFocus();
            }
        }
    }
}
