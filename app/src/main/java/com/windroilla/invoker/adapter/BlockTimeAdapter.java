package com.windroilla.invoker.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.windroilla.invoker.BlockTimeActivity;
import com.windroilla.invoker.R;

/**
 * Created by Surya Harsha Nunnaguppala on 13/11/15.
 */
public class BlockTimeAdapter extends CursorAdapter {

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public BlockTimeAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    public BlockTimeAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_blocktime_item, null);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.startTime.setText(cursor.getString(BlockTimeActivity.COL_BLOCKTIME_STARTTIME));
        viewHolder.endTime.setText(cursor.getString(BlockTimeActivity.COL_BLOCKTIME_ENDTIME));
        viewHolder.updateTime.setText(cursor.getString(BlockTimeActivity.COL_BLOCKTIME_UPDATETIME));
    }

    /**
     * Cache of the children views for a blocktime list item.
     */
    public static class ViewHolder {
        public final TextView startTime;
        public final TextView endTime;
        public final TextView updateTime;

        public ViewHolder(View view) {
            startTime = (TextView) view.findViewById(R.id.list_blocktime_item_starttime);
            endTime = (TextView) view.findViewById(R.id.list_blocktime_item_endtime);
            updateTime = (TextView) view.findViewById(R.id.list_blocktime_item_updatetime);
        }
    }
}
