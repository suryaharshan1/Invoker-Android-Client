package com.windroilla.invoker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.windroilla.invoker.adapter.CourseAdapter;
import com.windroilla.invoker.api.ApiService;
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
                for (int i = 0; i < courseList.size(); i++) {
                    if (courseList.get(i).isChecked()) {

                    }
                }
                Snackbar.make(view, "Institute id " + institute_id, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
