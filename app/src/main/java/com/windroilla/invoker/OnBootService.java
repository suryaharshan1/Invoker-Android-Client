package com.windroilla.invoker;

import android.app.AlarmManager;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import com.windroilla.invoker.data.BlocktimeContract;

import java.text.SimpleDateFormat;

public class OnBootService extends Service {

    private static final String TAG = "INVOKER_ONBOOTSERVICE";
    private static final String[] BLOCKTIME_COLUMNS = {
            BlocktimeContract.BlocktimeEntry.TABLE_NAME + "." + BlocktimeContract.BlocktimeEntry._ID,
            BlocktimeContract.BlocktimeEntry.COLUMN_START_TIME,
            BlocktimeContract.BlocktimeEntry.COLUMN_END_TIME,
            BlocktimeContract.BlocktimeEntry.COLUMN_CREATED_TIME
    };
    Cursor mCursor;


    public OnBootService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        String sortOrder = BlocktimeContract.BlocktimeEntry.COLUMN_CREATED_TIME + " ASC";
        Uri blockListUri = BlocktimeContract.BlocktimeEntry.buildBlocktimeUri();
        AlarmManager mgrAlarm = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setLenient(false);
        mCursor = getContentResolver().query(
                blockListUri,
                BLOCKTIME_COLUMNS,
                null,
                null,
                sortOrder);
        // Some providers return null if an error occurs, others throw an exception
        if (null == mCursor) {
            Log.e(TAG, "some error while querying the content provider");
            // If the Cursor is empty, the provider found no matches
        } else if (mCursor.getCount() < 1) {

            /*
             * Insert code here to notify the user that the search was unsuccessful. This isn't necessarily
             * an error. You may want to offer the user the option to insert a new row, or re-type the
             * search term.
             */

        } else {
            // Insert code here to do something with the results
            mCursor.moveToFirst();
            while (!mCursor.isAfterLast()) {
                //Set alarms for the intervals which are greater than or in between the current interval
                mCursor.moveToNext();
            }

        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
