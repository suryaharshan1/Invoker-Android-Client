package com.windroilla.invoker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, TouchBlockService.class);
        //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(i);
    }
}
