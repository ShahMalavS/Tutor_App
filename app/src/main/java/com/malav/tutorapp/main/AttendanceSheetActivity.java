package com.malav.tutorapp.main;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.malav.tutorapp.R;
import com.malav.tutorapp.utils.CommonUtils;
import com.malav.tutorapp.utils.Constants;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by shahmalav on 05/03/17.
 */

public class AttendanceSheetActivity extends AppCompatActivity{

    //sharedPreferences
    SharedPreferences someData;
    private boolean undo = false;
    private CaldroidFragment caldroidFragment;
    private CaldroidFragment dialogCaldroidFragment;
    private String TAG = "EXAMSCHEDULEACTIVITY";
    private MaterialDialog dialog;
    private String selectedStartDate, selectedEndDate;
    private EditText edtStartDate, edtEndDate;
    ImageView imgStartDate, imgEndDate;
    private Calendar calendar;
    private int year, day, month, flag=0;
    private TextView txtLeaves;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_sheet);
        someData = getSharedPreferences(Constants.filename, 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Attendance Sheet");

        txtLeaves = (TextView) findViewById(R.id.leaves);

        Button appleLeaves = (Button) findViewById(R.id.apply_leaves);
        appleLeaves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new MaterialDialog.Builder(AttendanceSheetActivity.this)
                        .title("Apply Leaves")
                        .customView(R.layout.dialog_apply_leaves, true)
                        .positiveText("Apply")
                        .negativeText(android.R.string.cancel)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                selectedStartDate = edtStartDate.getText().toString();
                                selectedEndDate = edtEndDate.getText().toString();
                                if(CommonUtils.isNotNull(selectedStartDate) && CommonUtils.isNotNull(selectedEndDate)){
                                    new applyLeaves().execute();
                                }else{
                                    Toast.makeText(AttendanceSheetActivity.this, "Please enter dates", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }).build();

                edtStartDate = (EditText) dialog.findViewById(R.id.edtStartDate);
                edtEndDate = (EditText) dialog.findViewById(R.id.edtEndDate);
                imgStartDate = (ImageView) dialog.findViewById(R.id.imgStartDate);
                imgEndDate = (ImageView) dialog.findViewById(R.id.imgEndDate);

                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                imgStartDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        flag=0;
                        DatePickerDialog datePickerDialog = new DatePickerDialog(AttendanceSheetActivity.this, myDateListener, year, month, day);
                        datePickerDialog.show();
                    }
                });

                imgEndDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        flag=1;
                        DatePickerDialog datePickerDialog = new DatePickerDialog(AttendanceSheetActivity.this, myDateListener, year, month, day);
                        datePickerDialog.show();
                    }
                });

                dialog.show();
            }
        });

        final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");

        caldroidFragment = new CaldroidFragment();

        // If Activity is created after rotation
        if (savedInstanceState != null) {
            caldroidFragment.restoreStatesFromKey(savedInstanceState, "CALDROID_SAVED_STATE");
        }
        // If activity is created from fresh
        else {
            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);
            caldroidFragment.setArguments(args);
        }

        new fetchingAttendanceData(month+1, year).execute();

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar1, caldroidFragment);
        t.commit();

        // Setup listener
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
            }

            @Override
            public void onChangeMonth(int month, int year) {
                String text = "month: " + month + " year: " + year;
                new fetchingAttendanceData(month, year).execute();
            }

            @Override
            public void onLongClickDate(Date date, View view) {
            }

            @Override
            public void onCaldroidViewCreated() {

                new fetchingAttendanceData(month+1, year).execute();
                if (caldroidFragment.getLeftArrowButton() != null) {
                }
            }

        };

        // Setup Caldroid
        caldroidFragment.setCaldroidListener(listener);
    }

    public DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            String arg2String = Integer.toString(arg2+1), arg3String = Integer.toString(arg3);
            if ((arg2 + 1) <= 9) {
                arg2String = "0" + (arg2 + 1);
            }
            if ((arg3) <= 9) {
                arg3String = "0" + arg3;
            }
            if(flag==0){
                edtStartDate.setText(new StringBuilder().append(arg3String).append("-").append(arg2String).append("-").append(arg1));
            }else if(flag==1){
                edtEndDate.setText(new StringBuilder().append(arg3String).append("-").append(arg2String).append("-").append(arg1));
            }
        }
    };

    private void setCustomResourceForDates() {
        List<Date> selectedDates = new ArrayList<>();
        Date date = null, date2 = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

            String stringDate = "22-03-2017";
            date = dateFormat.parse(stringDate);
            selectedDates.add(date);

            String stringDate2 = "25-03-2017";
            date2 = dateFormat.parse(stringDate2);
            selectedDates.add(date);

        }catch(Exception e){
            Log.e(TAG, "setCustomResourceForDates: ",e );
        }

        if (caldroidFragment != null) {
            Log.d(TAG, "setCustomResourceForDates: inside caldroidFragmennt" + selectedDates.size());
            Log.d(TAG, "setCustomResourceForDates: d: "+date.toString());
            ColorDrawable red = new ColorDrawable(getResources().getColor(R.color.roboto_calendar_circle_1));
            ColorDrawable green = new ColorDrawable(Color.GREEN);
            caldroidFragment.setBackgroundDrawableForDate(red, date);
            caldroidFragment.setTextColorForDate(R.color.white, date);

            caldroidFragment.setBackgroundDrawableForDate(green, date2);
            caldroidFragment.setTextColorForDate(R.color.white, date2);
        }
    }

    /**
     * Save current states of the Caldroid here
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);

        if (caldroidFragment != null) {
            caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
        }

        if (dialogCaldroidFragment != null) {
            dialogCaldroidFragment.saveStatesToKey(outState,
                    "DIALOG_CALDROID_SAVED_STATE");
        }
    }

    private class fetchingAttendanceData extends AsyncTask<String, String, String> {
        String success, message;
        ProgressDialog progress;
        int month, year, absentCount, presentCount, totalCount, leavesCount, holidayCount;
        List<Date> absentDates = new ArrayList<>();
        List<Date> presentDates = new ArrayList<>();
        List<Date> leaveDates = new ArrayList<>();
        List<Date> holidayDates = new ArrayList<>();
        Date date = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String stringDate;

        fetchingAttendanceData(int month, int year){
            this.month = month;
            this.year = year;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(AttendanceSheetActivity.this);
            progress.setIndeterminate(Boolean.FALSE);
            progress.setCancelable(Boolean.FALSE);
            progress.setTitle("Fethcing Attendance..");
            progress.setMessage("Please Wait..");
            progress.show();
        }

        @Override
        protected String doInBackground(String... params)
        {
            List<NameValuePair> para = new ArrayList<>();
            para.add(new BasicNameValuePair("user_id", someData.getString("user_id","")));
            para.add(new BasicNameValuePair("month", Integer.toString(month)));
            para.add(new BasicNameValuePair("year", Integer.toString(year)));
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



                if(month==1 && year==2017){

                    stringDate = "02-01-2017";
                    date = dateFormat.parse(stringDate);
                    absentDates.add(date);
                    stringDate = "04-01-2017";
                    date = dateFormat.parse(stringDate);
                    absentDates.add(date);
                    absentCount = 2;

                    stringDate = "03-01-2017";
                    date = dateFormat.parse(stringDate);
                    leaveDates.add(date);
                    leavesCount = 1;

                    stringDate = "30-01-2017";
                    date = dateFormat.parse(stringDate);
                    holidayDates.add(date);
                    holidayCount = 1;

                    totalCount = 31;
                    presentCount = totalCount-absentCount-leavesCount-holidayCount;

                }else if(month==2 && year==2017){

                    Log.d(TAG, "doInBackground: inside 2 && 2017");

                    stringDate = "09-02-2017";
                    date = dateFormat.parse(stringDate);
                    absentDates.add(date);
                    absentCount = 1;

                    stringDate = "06-02-2017";
                    date = dateFormat.parse(stringDate);
                    leaveDates.add(date);
                    stringDate = "18-02-2017";
                    date = dateFormat.parse(stringDate);
                    leaveDates.add(date);
                    leavesCount = 2;

                    holidayCount = 0;

                    totalCount = 28;
                    presentCount = totalCount-absentCount-leavesCount-holidayCount;

                }else if(month==3 && year==2017){

                    Log.d(TAG, "doInBackground: inside 3 && 2017");

                    absentCount = 0;

                    leavesCount = 0;

                    stringDate = "30-03-2017";
                    date = dateFormat.parse(stringDate);
                    holidayDates.add(date);
                    holidayCount = 1;

                    totalCount = 31;
                    presentCount = totalCount-absentCount-leavesCount-holidayCount;

                }else if(month==4 && year==2017){

                    Log.d(TAG, "doInBackground: inside 4 && 2017");

                    stringDate = "14-04-2017";
                    date = dateFormat.parse(stringDate);
                    absentDates.add(date);
                    stringDate = "15-04-2017";
                    date = dateFormat.parse(stringDate);
                    absentDates.add(date);
                    absentCount = 2;

                    totalCount = 30;
                    presentCount = totalCount-absentCount-leavesCount-holidayCount;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            progress.dismiss();
            if (caldroidFragment != null) {

                Log.d(TAG, "onPostExecute: caldroidFragment not null");
                ColorDrawable red = new ColorDrawable(getResources().getColor(R.color.roboto_calendar_circle_1));
                ColorDrawable green = new ColorDrawable(Color.GREEN);
                ColorDrawable blue = new ColorDrawable(Color.BLUE);

                for(Date date : leaveDates){
                    caldroidFragment.setBackgroundDrawableForDate(blue, date);
                    caldroidFragment.setTextColorForDate(R.color.white, date);
                }

                for(Date date : holidayDates){
                    caldroidFragment.setBackgroundDrawableForDate(green, date);
                    caldroidFragment.setTextColorForDate(R.color.white, date);
                }

                for(Date date : absentDates){
                    caldroidFragment.setBackgroundDrawableForDate(red, date);
                    caldroidFragment.setTextColorForDate(R.color.white, date);
                }

                TextView txtTotal = (TextView) findViewById(R.id.total);
                TextView txtPresent = (TextView) findViewById(R.id.present);
                TextView txtAbsent = (TextView) findViewById(R.id.absent);
                TextView txtHoliday = (TextView) findViewById(R.id.holidays);

                txtTotal.setText(Integer.toString(totalCount));
                txtPresent.setText(Integer.toString(presentCount));
                txtAbsent.setText(Integer.toString(absentCount));
                txtLeaves.setText(Integer.toString(leavesCount));
                txtHoliday.setText(Integer.toString(holidayCount));
            }

        }
    }

    private class applyLeaves extends AsyncTask<String, String, String> {
        String success, message;
        ProgressDialog progress;
        int month, year, absentCount;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(AttendanceSheetActivity.this);
            progress.setIndeterminate(Boolean.FALSE);
            progress.setCancelable(Boolean.FALSE);
            progress.setTitle("Fethcing Attendance..");
            progress.setMessage("Please Wait..");
            progress.show();
        }

        @Override
        protected String doInBackground(String... params)
        {
            List<NameValuePair> para = new ArrayList<>();
            para.add(new BasicNameValuePair("user_id", someData.getString("user_id","")));
            para.add(new BasicNameValuePair("startDate", selectedStartDate));
            para.add(new BasicNameValuePair("endDate", selectedEndDate));
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
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            progress.dismiss();
            if (caldroidFragment != null) {

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                dialog.dismiss();
                try {
                    Log.d(TAG, "onPostExecute: caldroidFragment not null");
                    ColorDrawable blue = new ColorDrawable(Color.BLUE);

                    Date startDate = dateFormat.parse(selectedStartDate);
                    Date endDate = dateFormat.parse(selectedEndDate);

                    int newCount = 0;
                    for (Date date = startDate; !date.after(endDate); date.setTime(date.getTime() + 1 * 24 * 60 * 60 * 1000)) {
                        caldroidFragment.setBackgroundDrawableForDate(blue, date);
                        caldroidFragment.setTextColorForDate(R.color.white, date);
                        newCount++;
                    }

                    int temp = Integer.parseInt(txtLeaves.getText().toString());
                    txtLeaves.setText(Integer.toString(temp+newCount));

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

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
