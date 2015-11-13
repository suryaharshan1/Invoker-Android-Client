package com.windroilla.invoker;

import android.content.Intent;
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
import com.windroilla.invoker.api.responseclasses.BlockTimeList;
import com.windroilla.invoker.api.responseclasses.Course;

import java.util.ArrayList;
import java.util.List;

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
                                        for (int i = 0; i < blockTimeList.getBlockTimes().size(); i++) {
                                            Log.d(TAG, blockTimeList.getBlockTimes().get(i).getCourse_id() + " " +
                                                    blockTimeList.getBlockTimes().get(i).getId() + " " +
                                                    blockTimeList.getBlockTimes().get(i).getStarttime() + " " +
                                                    blockTimeList.getBlockTimes().get(i).getEndtime() + " " +
                                                    blockTimeList.getBlockTimes().get(i).getCreated_time());
                                        }
                                        /*
                                        SharedPreferences sp = getSharedPreferences(APP_PREF_KEY, MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.putString(FLAG_CHOSEN_INSTITUTE, "" + institute_id);
                                        editor.commit();
                                        */
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

}
