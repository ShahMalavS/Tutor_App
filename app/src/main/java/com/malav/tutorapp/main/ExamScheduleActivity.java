package com.malav.tutorapp.main;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.malav.tutorapp.R;
import com.malav.tutorapp.utils.Constants;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by shahmalav on 05/03/17.
 */

public class ExamScheduleActivity extends AppCompatActivity{

    //sharedPreferences
    SharedPreferences someData;
    private boolean undo = false;
    private CaldroidFragment caldroidFragment;
    private CaldroidFragment dialogCaldroidFragment;
    private String TAG = "EXAMSCHEDULEACTIVITY";
    private LinearLayout schedule;
    private List<Date> selectedDates;
    private TextView examDate, batchA, batchB, course, noExam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_schedule);
        someData = getSharedPreferences(Constants.filename, 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Exam Schedule");

        schedule = (LinearLayout) findViewById(R.id.schedule);
        examDate = (TextView) findViewById(R.id.date);
        course = (TextView) findViewById(R.id.topic);
        batchA = (TextView) findViewById(R.id.batchA);
        batchB = (TextView) findViewById(R.id.batchB);
        noExam = (TextView) findViewById(R.id.noExam);

        final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        // Setup caldroid fragment
        // **** If you want normal CaldroidFragment, use below line ****
        caldroidFragment = new CaldroidFragment();

        // //////////////////////////////////////////////////////////////////////
        // **** This is to show customized fragment. If you want customized
        // version, uncomment below line ****
//		 caldroidFragment = new CaldroidSampleCustomFragment();

        // Setup arguments

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

            // Uncomment this to customize startDayOfWeek
            // args.putInt(CaldroidFragment.START_DAY_OF_WEEK,
            // CaldroidFragment.TUESDAY); // Tuesday

            // Uncomment this line to use Caldroid in compact mode
            // args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, false);

            // Uncomment this line to use dark theme
//            args.putInt(CaldroidFragment.THEME_RESOURCE, com.caldroid.R.style.CaldroidDefaultDark);

            caldroidFragment.setArguments(args);
        }

        setCustomResourceForDates();

        // Attach to the activity
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar1, caldroidFragment);
        t.commit();

        // Setup listener
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {

                Date date1 = null;
                try {
                    date1 = dateFormat.parse(dateFormat.format(date));
                    Log.d(TAG, "onSelectDate: date1:"+date1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(selectedDates.contains(date1)){
                    schedule.setVisibility(View.VISIBLE);
                    examDate.setText(dateFormat.format(date1));
                    noExam.setVisibility(View.GONE);
                }else{
                    schedule.setVisibility(View.GONE);
                    noExam.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onChangeMonth(int month, int year) {
                String text = "month: " + month + " year: " + year;
            }

            @Override
            public void onLongClickDate(Date date, View view) {
            }

            @Override
            public void onCaldroidViewCreated() {
                if (caldroidFragment.getLeftArrowButton() != null) {
                }
            }

        };

        // Setup Caldroid
        caldroidFragment.setCaldroidListener(listener);
    }

    private void setCustomResourceForDates() {
        selectedDates = new ArrayList<>();

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

            String stringDate = "22-03-2017";
            Date date = dateFormat.parse(stringDate);
            selectedDates.add(date);

            stringDate = "25-03-2017";
            date = dateFormat.parse(stringDate);
            selectedDates.add(date);

            stringDate = "18-03-2017";
            date = dateFormat.parse(stringDate);
            selectedDates.add(date);

            stringDate = "05-03-2017";
            date = dateFormat.parse(stringDate);
            selectedDates.add(date);

            stringDate = "30-04-2017";
            date = dateFormat.parse(stringDate);
            selectedDates.add(date);
        }catch(Exception e){
            Log.e(TAG, "setCustomResourceForDates: ",e );
        }

        if (caldroidFragment != null) {
            for(Date d : selectedDates) {
                ColorDrawable blue = new ColorDrawable(getResources().getColor(R.color.blue));
                ColorDrawable green = new ColorDrawable(Color.GREEN);
                caldroidFragment.setBackgroundDrawableForDate(blue, d);
                caldroidFragment.setTextColorForDate(R.color.white, d);
            }
        }
    }

    /**
     * Save current states of the Caldroid here
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (caldroidFragment != null) {
            caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
        }

        if (dialogCaldroidFragment != null) {
            dialogCaldroidFragment.saveStatesToKey(outState,
                    "DIALOG_CALDROID_SAVED_STATE");
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
