package com.windroilla.invoker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.windroilla.invoker.adapter.CourseAdapter;
import com.windroilla.invoker.api.ApiService;
import com.windroilla.invoker.api.requestclasses.RequestSetUserCourseList;
import com.windroilla.invoker.api.responseclasses.BlockTime;
import com.windroilla.invoker.api.responseclasses.BlockTimeList;
import com.windroilla.invoker.api.responseclasses.Course;
import com.windroilla.invoker.data.BlocktimeContract;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class CourseActivity extends AppCompatActivity {

    public final static String INVOKER_PASS_INS_ID = "INVOKER_PASS_INS_ID";
    public final static String TAG = "INVOKER_COURSE";
    public final static String APP_PREF_KEY = "INVOKER_PREF_KEY";
    public final static String FLAG_CHOSEN_INSTITUTE = "INVOKER_CHOSEN_INSTITUTE";
    @Inject
    ApiService apiService;
    @Inject
    String deviceID;
    private ListView lv;
    private CourseAdapter courseAdapter;

    private List<Course> courseList = new ArrayList<Course>();
    private AlarmReceiver alarmReceiver;
    private IntentFilter filter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InvokerApp.getsInstance().graph().inject(this);
        setContentView(R.layout.activity_course);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent i = getIntent();
        final int institute_id = i.getIntExtra(INVOKER_PASS_INS_ID, -1);
        if (institute_id == -1) {
            Log.e(TAG, "Invalid institute id passed to the course activity");
            Toast.makeText(this, "Invalid institute id passed to the course activity", Toast.LENGTH_LONG).show();
            finish();
        }
        lv = (ListView) findViewById(R.id.course_listView);
        courseAdapter = new CourseAdapter(this, courseList);
        lv.setAdapter(courseAdapter);
        alarmReceiver = new AlarmReceiver();
        filter = new IntentFilter();
        filter.addAction("com.windroilla.invoker.blockservice.start");
        filter.addAction("com.windroilla.invoker.blockservice.stop");
        getApplicationContext().registerReceiver(alarmReceiver, filter);
        apiService.getInstituteCourseList(institute_id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Action1<List<Course>>() {
                            @Override
                            public void call(List<Course> courses) {
                                courseList.addAll(courses);
                                courseAdapter.notifyDataSetChanged();
                            }
                        },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                Log.e(TAG, "Course Sync failed! " + throwable);
                                Toast.makeText(getBaseContext(), "Course sync failed! Please try again!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                );
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestSetUserCourseList req = new RequestSetUserCourseList();
                req.setMobile_id(deviceID);
                for (int i = 0; i < courseList.size(); i++) {
                    if (courseList.get(i).isChecked()) {
                        req.addCourseId(courseList.get(i).getId());
                    }
                }
                apiService.setUserCourseList(req)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(Schedulers.newThread())
                        .subscribe(
                                new Action1<BlockTimeList>() {
                                    @Override
                                    public void call(BlockTimeList blockTimeList) {
                                        Log.d(TAG, blockTimeList.access_time);
                                        Vector<ContentValues> cVVector = new Vector<ContentValues>(blockTimeList.getBlockTimes().size());
                                        AlarmManager mgrAlarm = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
                                        ArrayList<PendingIntent> intentArray = new ArrayList<PendingIntent>();
                                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        formatter.setLenient(false);
                                        for (int i = 0; i < blockTimeList.getBlockTimes().size(); i++) {
                                            BlockTime blockTime = blockTimeList.getBlockTimes().get(i);
                                            Log.d(TAG, blockTime.getCourse_id() + " " +
                                                    blockTime.getId() + " " +
                                                    blockTime.getStarttime() + " " +
                                                    blockTime.getEndtime() + " " +
                                                    blockTime.getCreated_time());
                                            ContentValues blockTimeValues = new ContentValues();
                                            blockTimeValues.put(BlocktimeContract.BlocktimeEntry.COLUMN_START_TIME, blockTime.getStarttime());
                                            blockTimeValues.put(BlocktimeContract.BlocktimeEntry.COLUMN_END_TIME, blockTime.getEndtime());
                                            blockTimeValues.put(BlocktimeContract.BlocktimeEntry.COLUMN_CREATED_TIME, blockTime.getCreated_time());
                                            Intent startIntent = new Intent(getBaseContext(), AlarmReceiver.class);
                                            startIntent.setAction("com.windroilla.invoker.blockservice.start");
                                            Intent endIntent = new Intent(getBaseContext(), AlarmReceiver.class);
                                            endIntent.setAction("com.windroilla.invoker.blockservice.stop");
                                            PendingIntent pendingStartIntent = PendingIntent.getBroadcast(getApplicationContext(), blockTime.getId(), startIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                            PendingIntent pendingEndIntent = PendingIntent.getBroadcast(getApplicationContext(), -blockTime.getId(), endIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                            try {
                                                mgrAlarm.set(AlarmManager.RTC_WAKEUP,
                                                        formatter.parse(blockTime.getStarttime()).getTime(),
                                                        pendingStartIntent);
                                                Log.d(TAG, formatter.parse(blockTime.getStarttime()).getTime() + " " + System.currentTimeMillis() + " " + (formatter.parse(blockTime.getStarttime()).getTime() - System.currentTimeMillis()));
                                                Log.d(TAG, formatter.parse(blockTime.getEndtime()).getTime() + " " + System.currentTimeMillis() + " " + (formatter.parse(blockTime.getEndtime()).getTime() - System.currentTimeMillis()));
                                                mgrAlarm.set(AlarmManager.RTC_WAKEUP,
                                                        formatter.parse(blockTime.getEndtime()).getTime(),
                                                        pendingEndIntent);
                                            } catch (ParseException e) {
                                                Log.e(TAG, e.toString());
                                            }
                                            intentArray.add(pendingStartIntent);
                                            intentArray.add(pendingEndIntent);
                                            cVVector.add(blockTimeValues);
                                        }
                                        Log.d(TAG, intentArray.size() + " PendingIntents have been progressed");
                                        int inserted = 0;
                                        // add to database
                                        if (cVVector.size() > 0) {
                                            ContentValues[] cvArray = new ContentValues[cVVector.size()];
                                            cVVector.toArray(cvArray);
                                            getBaseContext().getContentResolver().delete(BlocktimeContract.BlocktimeEntry.CONTENT_URI, null, null);
                                            getBaseContext().getContentResolver().bulkInsert(BlocktimeContract.BlocktimeEntry.CONTENT_URI, cvArray);
                                        }

                                        SharedPreferences sp = getSharedPreferences(APP_PREF_KEY, MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.putString(FLAG_CHOSEN_INSTITUTE, "" + institute_id);
                                        editor.commit();

                                        Intent i = new Intent(getBaseContext(), BlockTimeActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                },
                                new Action1<Throwable>() {
                                    @Override
                                    public void call(Throwable throwable) {
                                        Log.e(TAG, "setCourseList Sync failed! " + throwable);
                                        Toast.makeText(getBaseContext(), "Setting courses failed! Please try again!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                        );

            }
        });
    }

    @Override
    protected void onDestroy() {
        getApplicationContext().unregisterReceiver(alarmReceiver);
        super.onDestroy();
    }

}
