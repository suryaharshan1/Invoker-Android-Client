package com.windroilla.invoker;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import com.windroilla.invoker.data.BlocktimeContract;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class OnBootService extends Service {

    public final static String APP_PREF_KEY = "INVOKER_PREF_KEY";
    public final static String INVOKER_SERVICE_COUNT = "INVOKER_SERVICE_COUNT";
    private static final String TAG = "INVOKER_ONBOOTSERVICE";
    private static final String[] BLOCKTIME_COLUMNS = {
            BlocktimeContract.BlocktimeEntry.TABLE_NAME + "." + BlocktimeContract.BlocktimeEntry._ID,
            BlocktimeContract.BlocktimeEntry.COLUMN_START_TIME,
            BlocktimeContract.BlocktimeEntry.COLUMN_END_TIME,
            BlocktimeContract.BlocktimeEntry.COLUMN_CREATED_TIME
    };
    private static final int COL_BLOCKTIME_ID = 0;
    private static final int COL_BLOCKTIME_STARTTIME = 1;
    private static final int COL_BLOCKTIME_ENDTIME = 2;
    private static final int COL_BLOCKTIME_UPDATETIME = 3;
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
        } else {
            if (mCursor.getCount() < 1) {

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
                    try {
                        if (System.currentTimeMillis() < formatter.parse(mCursor.getString(COL_BLOCKTIME_STARTTIME)).getTime()) {
                            Intent startIntent = new Intent(getBaseContext(), AlarmReceiver.class);
                            startIntent.setAction("com.windroilla.invoker.blockservice.start");
                            Intent endIntent = new Intent(getBaseContext(), AlarmReceiver.class);
                            endIntent.setAction("com.windroilla.invoker.blockservice.stop");
                            PendingIntent pendingStartIntent = PendingIntent.getBroadcast(getApplicationContext(), mCursor.getInt(COL_BLOCKTIME_ID), startIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                            PendingIntent pendingEndIntent = PendingIntent.getBroadcast(getApplicationContext(), -mCursor.getInt(COL_BLOCKTIME_ID), endIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                            mgrAlarm.set(AlarmManager.RTC_WAKEUP,
                                    formatter.parse(mCursor.getString(COL_BLOCKTIME_STARTTIME)).getTime(),
                                    pendingStartIntent);
                            Log.d(TAG, formatter.parse(mCursor.getString(COL_BLOCKTIME_STARTTIME)).getTime() + " " + System.currentTimeMillis() + " " + (formatter.parse(mCursor.getString(COL_BLOCKTIME_STARTTIME)).getTime() - System.currentTimeMillis()));
                            Log.d(TAG, formatter.parse(mCursor.getString(COL_BLOCKTIME_ENDTIME)).getTime() + " " + System.currentTimeMillis() + " " + (formatter.parse(mCursor.getString(COL_BLOCKTIME_ENDTIME)).getTime() - System.currentTimeMillis()));
                            mgrAlarm.set(AlarmManager.RTC_WAKEUP,
                                    formatter.parse(mCursor.getString(COL_BLOCKTIME_ENDTIME)).getTime(),
                                    pendingEndIntent);
                        } else if (System.currentTimeMillis() >= formatter.parse(mCursor.getString(COL_BLOCKTIME_STARTTIME)).getTime() && System.currentTimeMillis() < formatter.parse(mCursor.getString(COL_BLOCKTIME_ENDTIME)).getTime()) {
                            Intent endIntent = new Intent(getBaseContext(), AlarmReceiver.class);
                            endIntent.setAction("com.windroilla.invoker.blockservice.stop");
                            PendingIntent pendingEndIntent = PendingIntent.getBroadcast(getApplicationContext(), -mCursor.getInt(COL_BLOCKTIME_ID), endIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                            Log.d(TAG, formatter.parse(mCursor.getString(COL_BLOCKTIME_ENDTIME)).getTime() + " " + System.currentTimeMillis() + " " + (formatter.parse(mCursor.getString(COL_BLOCKTIME_ENDTIME)).getTime() - System.currentTimeMillis()));
                            mgrAlarm.set(AlarmManager.RTC_WAKEUP,
                                    formatter.parse(mCursor.getString(COL_BLOCKTIME_ENDTIME)).getTime(),
                                    pendingEndIntent);
                            SharedPreferences sp = getApplicationContext().getSharedPreferences(APP_PREF_KEY, Activity.MODE_PRIVATE);
                            int serviceCount = sp.getInt(INVOKER_SERVICE_COUNT, 0);
                            SharedPreferences.Editor editor = sp.edit();
                            Log.d(TAG, "OnBootService OnCreate with serviceCount " + serviceCount);
                            if (serviceCount == 0) {
                                Intent i = new Intent(getApplicationContext(), TouchBlockService.class);
                                getApplicationContext().startService(i);
                                Log.d(TAG, "OnBootService OnCreate start TouchBlockService");
                            }
                            serviceCount++;
                            editor.putInt(INVOKER_SERVICE_COUNT, serviceCount);
                            editor.commit();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    mCursor.moveToNext();
                }
            }
        }
        stopSelf();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
