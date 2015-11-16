package com.windroilla.invoker;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    public final static String APP_PREF_KEY = "INVOKER_PREF_KEY";
    public final static String INVOKER_SERVICE_COUNT = "INVOKER_SERVICE_COUNT";
    public final static String TAG = "INVOKER_ALARM_RECEIVER";
    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(APP_PREF_KEY, Activity.MODE_PRIVATE);
        int serviceCount = sp.getInt(INVOKER_SERVICE_COUNT, 0);
        SharedPreferences.Editor editor = sp.edit();
        Log.d(TAG, "OnReceive with serviceCount " + serviceCount);
        if (intent.getAction().equals("com.windroilla.invoker.blockservice.start")) {
            if (serviceCount == 0) {
                Intent i = new Intent(context.getApplicationContext(), TouchBlockService.class);
                context.getApplicationContext().startService(i);
                Log.d(TAG, "OnReceive start TouchBlockService");
            }
            serviceCount++;
            editor.putInt(INVOKER_SERVICE_COUNT, serviceCount);
        } else if (intent.getAction().equals("com.windroilla.invoker.blockservice.stop")) {
            if (serviceCount == 1) {
                Intent i = new Intent(context.getApplicationContext(), TouchBlockService.class);
                context.getApplicationContext().stopService(i);
                Log.d(TAG, "OnReceive stop TouchBlockService");
            }
            serviceCount--;
            editor.putInt(INVOKER_SERVICE_COUNT, serviceCount);
        }
        Log.d(TAG, "OnReceive with new serviceCount " + serviceCount);
        editor.commit();
    }
}
