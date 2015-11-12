package com.windroilla.invoker.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.windroilla.invoker.R;
import com.windroilla.invoker.api.responseclasses.Course;

import java.util.List;

/**
 * Created by Surya Harsha Nunnaguppala on 12/11/15.
 */
public class CourseAdapter extends BaseAdapter {
    private Activity activity;
    private List<Course> courseList;
    private LayoutInflater inflater;

    public CourseAdapter(Activity activity, List<Course> courseList) {
        this.courseList = courseList;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return courseList.size();
    }

    @Override
    public Object getItem(int position) {
        return courseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_course_item, null);
        CheckBox cb = (CheckBox) convertView.findViewById(R.id.list_course_item_checkBox);
        cb.setText(courseList.get(position).getName());
        return convertView;
    }
}
