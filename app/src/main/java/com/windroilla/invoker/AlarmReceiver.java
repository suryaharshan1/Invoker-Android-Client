package com.windroilla.invoker;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class AlarmReceiver extends BroadcastReceiver {
    public final static String APP_PREF_KEY = "INVOKER_PREF_KEY";
    public final static String INVOKER_SERVICE_COUNT = "INVOKER_SERVICE_COUNT";
    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREF_KEY, Activity.MODE_PRIVATE);
        int serviceCount = sp.getInt(INVOKER_SERVICE_COUNT, 0);
        SharedPreferences.Editor editor = sp.edit();
        if (intent.getAction().equals("com.windroilla.invoker.blockservice.start")) {
            if (serviceCount == 0) {
                Intent i = new Intent(context, TouchBlockService.class);
                context.startService(i);
            }
            editor.putInt(INVOKER_SERVICE_COUNT, serviceCount + 1);
        } else if (intent.getAction().equals("com.windroilla.invoker.blockservice.stop")) {
            if (serviceCount == 1) {
                Intent i = new Intent(context, TouchBlockService.class);
                context.stopService(i);
            }
            editor.putInt(INVOKER_SERVICE_COUNT, serviceCount - 1);
        }
        editor.commit();
    }
}
