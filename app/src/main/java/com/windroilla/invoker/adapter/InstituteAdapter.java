package com.windroilla.invoker.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.windroilla.invoker.api.responseclasses.Institute;

import java.util.List;

/**
 * Created by Surya Harsha Nunnaguppala on 12/11/15.
 */
public class InstituteAdapter extends BaseAdapter {
    private List<Institute> instituteList;
    private Activity activity;

    private LayoutInflater inflater;

    public InstituteAdapter(Activity activity, List<Institute> instituteList) {
        this.instituteList = instituteList;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return instituteList.size();
    }

    @Override
    public Object getItem(int position) {
        return instituteList.get(position);
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
            convertView = inflater.inflate(android.R.layout.activity_list_item, null);
        TextView title = (TextView) convertView.findViewById(android.R.id.text1);
        title.setText(instituteList.get(position).getName());
        return convertView;
    }
}
