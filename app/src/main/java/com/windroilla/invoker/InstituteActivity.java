package com.windroilla.invoker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.windroilla.invoker.adapter.InstituteAdapter;
import com.windroilla.invoker.api.ApiService;
import com.windroilla.invoker.api.responseclasses.Institute;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class InstituteActivity extends AppCompatActivity {

    public final static String TAG = "INVOKER_INSTITUTE";
    public final static String INVOKER_PASS_INS_ID = "INVOKER_PASS_INS_ID";
    @Inject
    ApiService apiService;
    private ListView lv;
    private InstituteAdapter ia;
    private List<Institute> instituteList = new ArrayList<Institute>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InvokerApp.getsInstance().graph().inject(this);
        setContentView(R.layout.activity_institute);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        lv = (ListView) findViewById(R.id.institute_listView);
        ia = new InstituteAdapter(this, instituteList);
        lv.setAdapter(ia);
        apiService.getInstituteList()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Action1<List<Institute>>() {
                            @Override
                            public void call(List<Institute> institutes) {
                                instituteList.addAll(institutes);
                                ia.notifyDataSetChanged();
                            }
                        },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                Log.e(TAG, "Institute Sync failed! " + throwable);
                                Toast.makeText(getBaseContext(), "Institute sync failed! Please try again!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                );
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Institute ins = (Institute) parent.getItemAtPosition(position);
                Intent i = new Intent(getBaseContext(), CourseActivity.class);
                i.putExtra(INVOKER_PASS_INS_ID, ins.getId());
                startActivity(i);
            }
        });
    }

}
