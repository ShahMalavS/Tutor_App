package com.malav.tutorapp.main;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.malav.tutorapp.R;
import com.malav.tutorapp.adapter.OnlineResourcesAdpater;
import com.malav.tutorapp.beans.OnlineResources;
import com.malav.tutorapp.utils.Constants;
import com.malav.tutorapp.utils.JSONfunctions;
import com.malav.tutorapp.utils.QueryMapper;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shahmalav on 05/03/17.
 */

public class OnlineResourcesActivity extends AppCompatActivity{

    //sharedPreferences
    SharedPreferences someData;
    private ArrayList<OnlineResources> listOnlineResources = new ArrayList<>();
    private String TAG = "OnlineResourcesActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_resources);
        someData = getSharedPreferences(Constants.filename, 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Online Resources");

        new fetchOnlineResources().execute();

    }

    private class fetchOnlineResources extends AsyncTask<String, String, String> {
        OnlineResources item;
        String success, message;
        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(OnlineResourcesActivity.this);
            progress.setIndeterminate(Boolean.FALSE);
            progress.setCancelable(Boolean.FALSE);
            progress.setTitle("Fethcing Results..");
            progress.setMessage("Please Wait..");
            progress.show();

        }

        @Override
        protected String doInBackground(String... params)
        {
            List<NameValuePair> para = new ArrayList<>();
            para.add(new BasicNameValuePair("common_L_Id", someData.getString("commonL_Id","")));
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

                OnlineResources onlineResources = new OnlineResources();
                onlineResources.setResourceId("1");
                onlineResources.setTitle("Newton 3rd Law");
                onlineResources.setUrl("http://www.google.com/newton3lawofnature");
                onlineResources.setDescription("It explains 3rd law of nature. Action and reaction are same in power and in opposite direction.");
                onlineResources.setDate("22-01-2017");
                listOnlineResources.add(onlineResources);

                onlineResources = new OnlineResources();
                onlineResources.setResourceId("2");
                onlineResources.setTitle("Isotopes");
                onlineResources.setUrl("https://www.facebook.com/malav");
                onlineResources.setDescription("Isotopes lorem ipsum lorem ipsum ipsum lorem");
                onlineResources.setDate("22-01-2017");
                listOnlineResources.add(onlineResources);

                onlineResources = new OnlineResources();
                onlineResources.setResourceId("3");
                onlineResources.setTitle("Chemicals");
                onlineResources.setUrl("www.twitter.com/malav");
                onlineResources.setDescription("");
                onlineResources.setDate("22-01-2017");
                listOnlineResources.add(onlineResources);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            progress.dismiss();
            ListView lstOnlineResources = (ListView) findViewById(R.id.lstOnlineResources);
            OnlineResourcesAdpater onlineResourcesAdpater = new OnlineResourcesAdpater(OnlineResourcesActivity.this, listOnlineResources);
            lstOnlineResources.setAdapter(onlineResourcesAdpater);
            //Utility.setListViewHeightBasedOnChildren(graphList);
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
