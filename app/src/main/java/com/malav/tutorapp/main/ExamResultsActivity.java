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
import android.widget.ListView;
import android.widget.TextView;

import com.malav.tutorapp.R;
import com.malav.tutorapp.adapter.TopFiveStudentsResultsAdapter;
import com.malav.tutorapp.beans.Results;
import com.malav.tutorapp.utils.AppUtils;
import com.malav.tutorapp.utils.Constants;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by shahmalav on 05/03/17.
 */

public class ExamResultsActivity extends AppCompatActivity{

    //sharedPreferences
    SharedPreferences someData;
    private boolean undo = false;
    private CaldroidFragment caldroidFragment;
    private CaldroidFragment dialogCaldroidFragment;
    private String TAG = "EXAMSCHEDULEACTIVITY";
    private LinearLayout llResults;
    private List<Date> selectedDates;
    private TextView txtTopic, txtYourMarks;
    private HashMap<String,ArrayList<Results>> listResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_results);
        someData = getSharedPreferences(Constants.filename, 0);

        listResults = new HashMap<>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Exam Results");

        llResults = (LinearLayout) findViewById(R.id.results);
        txtTopic = (TextView) findViewById(R.id.topic);
        txtYourMarks = (TextView) findViewById(R.id.marks);

        final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

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
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(selectedDates.contains(date1)){
                    llResults.setVisibility(View.VISIBLE);
                    ArrayList<Results> selectedList = listResults.get(dateFormat.format(date));

                    Results result = selectedList.get(0);

                    txtTopic.setText(result.getTopic());
                    txtYourMarks.setText(result.getMarksObtained()+" / "+result.getMarksTotal());

                    selectedList.remove(0);
                    ListView topFive = (ListView) findViewById(R.id.topFive);
                    TopFiveStudentsResultsAdapter topFiveStudentsResultsAdapter = new TopFiveStudentsResultsAdapter(ExamResultsActivity.this, selectedList);
                    topFive.setAdapter(topFiveStudentsResultsAdapter);
                    AppUtils.setListViewHeightBasedOnChildren(topFive);

                }else{
                    llResults.setVisibility(View.GONE);
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

            ArrayList<Results> listTopFive = new ArrayList<>();

            Results results = new Results();
            results.setExamDate(stringDate);
            results.setExamId("1");
            results.setMarksObtained("30");
            results.setMarksTotal("50");
            results.setName("Prem");
            results.setResultId("10");
            results.setSrNo("0");
            results.setTopic("Chapter 2");
            results.setUserId("10");
            listTopFive.add(results);

            results = new Results();
            results.setExamDate(stringDate);
            results.setExamId("1");
            results.setMarksObtained("45");
            results.setMarksTotal("50");
            results.setName("Malav");
            results.setResultId("1");
            results.setSrNo("1");
            results.setTopic("Chapter 2");
            results.setUserId("1");
            listTopFive.add(results);

            results = new Results();
            results.setExamDate(stringDate);
            results.setExamId("1");
            results.setMarksObtained("44");
            results.setMarksTotal("50");
            results.setName("Jaini");
            results.setResultId("2");
            results.setSrNo("2");
            results.setTopic("Chapter 2");
            results.setUserId("2");
            listTopFive.add(results);

            results = new Results();
            results.setExamDate(stringDate);
            results.setExamId("1");
            results.setMarksObtained("44");
            results.setMarksTotal("50");
            results.setName("Aayushi");
            results.setResultId("3");
            results.setSrNo("3");
            results.setTopic("Chapter 2");
            results.setUserId("3");
            listTopFive.add(results);

            results = new Results();
            results.setExamDate(stringDate);
            results.setExamId("1");
            results.setMarksObtained("43");
            results.setMarksTotal("50");
            results.setName("Rakshit");
            results.setResultId("4");
            results.setSrNo("4");
            results.setTopic("Chapter 2");
            results.setUserId("4");
            listTopFive.add(results);

            results = new Results();
            results.setExamDate(stringDate);
            results.setExamId("1");
            results.setMarksObtained("40");
            results.setMarksTotal("50");
            results.setName("Tirth");
            results.setResultId("5");
            results.setSrNo("5");
            results.setTopic("Chapter 2");
            results.setUserId("5");
            listTopFive.add(results);

            HashMap<String, List<Results>> tempHashMap = new HashMap<>();
            tempHashMap.put(stringDate,listTopFive);
            listResults.put(stringDate,listTopFive);

            stringDate = "25-03-2017";
            date = dateFormat.parse(stringDate);
            selectedDates.add(date);
            tempHashMap = new HashMap<>();
            tempHashMap.put(stringDate,listTopFive);
            listResults.put(stringDate, listTopFive);

            stringDate = "18-03-2017";
            date = dateFormat.parse(stringDate);
            selectedDates.add(date);
            tempHashMap = new HashMap<>();
            tempHashMap.put(stringDate,listTopFive);
            listResults.put(stringDate, listTopFive);

            stringDate = "05-03-2017";
            date = dateFormat.parse(stringDate);
            selectedDates.add(date);
            tempHashMap = new HashMap<>();
            tempHashMap.put(stringDate,listTopFive);
            listResults.put(stringDate, listTopFive);

            stringDate = "30-04-2017";
            date = dateFormat.parse(stringDate);
            selectedDates.add(date);
            tempHashMap = new HashMap<>();
            tempHashMap.put(stringDate,listTopFive);
            listResults.put(stringDate, listTopFive);
        }catch(Exception e){
            Log.e(TAG, "setCustomResourceForDates: ",e );
        }

        if (caldroidFragment != null) {
            Log.d(TAG, "setCustomResourceForDates: inside caldroidFragmennt" + selectedDates.size());
            for(Date d : selectedDates) {
                Log.d(TAG, "setCustomResourceForDates: d: "+d.toString());
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
