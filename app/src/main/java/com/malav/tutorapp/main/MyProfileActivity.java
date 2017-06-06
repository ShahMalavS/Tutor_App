package com.malav.tutorapp.main;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.malav.tutorapp.R;
import com.malav.tutorapp.adapter.OnlineResourcesAdpater;
import com.malav.tutorapp.beans.OnlineResources;
import com.malav.tutorapp.utils.AppUtils;
import com.malav.tutorapp.utils.CommonUtils;
import com.malav.tutorapp.utils.Constants;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shahmalav on 05/03/17.
 */

public class MyProfileActivity extends AppCompatActivity {

    //sharedPreferences
    SharedPreferences someData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        someData = getSharedPreferences(Constants.filename, 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("My Profile");

        if(AppUtils.isOnline(MyProfileActivity.this)){
            new fetchMyProfile().execute();
        }
    }

    private class fetchMyProfile extends AsyncTask<String, String, String> {
        String success, message, name, school, number, email, standard, batch;
        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(MyProfileActivity.this);
            progress.setIndeterminate(Boolean.FALSE);
            progress.setCancelable(Boolean.FALSE);
            progress.setTitle("Fethcing Info..");
            progress.setMessage("Please Wait..");
            progress.show();

        }

        @Override
        protected String doInBackground(String... params)
        {
            List<NameValuePair> para = new ArrayList<>();
            para.add(new BasicNameValuePair("user_id", someData.getString("user_id","")));
            try{
                /*Log.d(TAG, "doInBackground: resuesting");
                JSONObject jsonobject = JSONfunctions.makeHttpRequest(QueryMapper.URL_ALL_FEEDS, "GET", para);

                JSONArray feedArray = jsonobject.getJSONArray("All_Feeds");

                for (int i = 0; i < feedArray.length(); i++) {
                    JSONObject feedObj = (JSONObject) feedArray.get(i);

                    FeedItem item = new FeedItem();
                    item.setFf_Id(feedObj.getInt("ff_Id"));
                    item.setL_Id(feedObj.getInt("l_Id"));
                    item.setName(feedObj.getString("name"));
                    item.setLikes(feedObj.getString("likes"));
                    item.setComments(feedObj.getString("comments"));
                    item.setLikedStatus(feedObj.getString("likedStatus"));

                    String image = feedObj.isNull("image") ? null : feedObj.getString("image");
                    item.setImage(image);
                    String status = feedObj.isNull("status") ? null : feedObj.getString("status");
                    item.setStatus(status);
                    String profilePic = feedObj.isNull("profilePic") ? null : feedObj.getString("profilePic");
                    item.setProfilePic(profilePic);

                    String video = feedObj.isNull("video") ? null : feedObj.getString("video");
                    item.setVideo(video);

                    String audio = feedObj.isNull("audio") ? null : feedObj.getString("audio");
                    item.setAudio(audio);

                    item.setDate(feedObj.getString("date"));

                    listFlexibleItems.add(newInstagramItem(item));

                    if(i==0) {
                        lastId = feedObj.getInt("ff_Id");
                    }
                }*/

                name = "Malav Shah";
                school = "BVM";
                number = "9033137266";
                email = "malavshah502@gmail.com";
                standard = "12";
                batch = "A";

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            progress.dismiss();
            TextView txtStudentName = (TextView) findViewById(R.id.student_name);
            TextView txtStudentSchool = (TextView) findViewById(R.id.student_school);
            TextView txtStudentNumber = (TextView) findViewById(R.id.student_number);
            TextView txtStudentEmail = (TextView) findViewById(R.id.student_email);
            TextView txtStudentStandard = (TextView) findViewById(R.id.student_standard);
            TextView txtStudentBatch = (TextView) findViewById(R.id.student_batch);

            txtStudentName.setText(name);
            txtStudentSchool.setText(school);
            txtStudentNumber.setText(number);
            txtStudentEmail.setText(email);
            txtStudentStandard.setText(standard);
            txtStudentBatch.setText(batch);
        }
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
