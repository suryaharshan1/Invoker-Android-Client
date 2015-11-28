package com.windroilla.invoker.gcm;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmListenerService;
import com.windroilla.invoker.AlarmReceiver;
import com.windroilla.invoker.InvokerApp;
import com.windroilla.invoker.MainActivity;
import com.windroilla.invoker.R;
import com.windroilla.invoker.api.ApiService;
import com.windroilla.invoker.api.requestclasses.RequestBlockTimes;
import com.windroilla.invoker.api.responseclasses.BlockTime;
import com.windroilla.invoker.api.responseclasses.BlockTimeList;
import com.windroilla.invoker.data.BlocktimeContract;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Vector;

import javax.inject.Inject;

import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by vishnu on 13/11/15.
 * <p/>
 * Copyright 2015 Google Inc. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    @Inject
    ApiService apiService;
    @Inject
    String deviceID;

    private AlarmReceiver alarmReceiver;
    private IntentFilter filter;

    @Override
    public void onCreate() {
        super.onCreate();
        InvokerApp.getsInstance().graph().inject(this);
        alarmReceiver = new AlarmReceiver();
        filter = new IntentFilter();
        filter.addAction("com.windroilla.invoker.blockservice.start");
        filter.addAction("com.windroilla.invoker.blockservice.stop");
        getApplicationContext().registerReceiver(alarmReceiver, filter);

    }

    @Override
    public void onDestroy() {
        getApplicationContext().unregisterReceiver(alarmReceiver);
        super.onDestroy();
    }

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);

        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            Log.i("Device ID ",deviceID);
            // normal downstream message.
            apiService.getBlockTimes(
                    new RequestBlockTimes(
                            deviceID
                    )
            )
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(Schedulers.newThread())
                    .subscribe(
                            new Action1<BlockTimeList>() {
                                @Override
                                public void call(BlockTimeList blockTimeList) {
                                    Log.d(TAG, blockTimeList.access_time);
                                    Vector<ContentValues> cVVector = new Vector<ContentValues>(blockTimeList.getBlockTimes().size());
                                    AlarmManager mgrAlarm = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
                                    ArrayList<PendingIntent> intentArray = new ArrayList<PendingIntent>();
                                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    formatter.setLenient(false);

                                    for (int i = 0; i < blockTimeList.getBlockTimes().size(); i++) {
                                        BlockTime blockTime = blockTimeList.getBlockTimes().get(i);
                                        Log.d(TAG, blockTime.getCourse_id() + " " +
                                                blockTime.getId() + " " +
                                                blockTime.getStarttime() + " " +
                                                blockTime.getEndtime() + " " +
                                                blockTime.getCreated_time());
                                        ContentValues blockTimeValues = new ContentValues();
                                        blockTimeValues.put(BlocktimeContract.BlocktimeEntry.COLUMN_START_TIME, blockTime.getStarttime());
                                        blockTimeValues.put(BlocktimeContract.BlocktimeEntry.COLUMN_END_TIME, blockTime.getEndtime());
                                        blockTimeValues.put(BlocktimeContract.BlocktimeEntry.COLUMN_CREATED_TIME, blockTime.getCreated_time());
                                        Intent startIntent = new Intent(getBaseContext(), AlarmReceiver.class);
                                        startIntent.setAction("com.windroilla.invoker.blockservice.start");
                                        Intent endIntent = new Intent(getBaseContext(), AlarmReceiver.class);
                                        endIntent.setAction("com.windroilla.invoker.blockservice.stop");
                                        PendingIntent pendingStartIntent = PendingIntent.getBroadcast(getApplicationContext(), blockTime.getId(), startIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                        PendingIntent pendingEndIntent = PendingIntent.getBroadcast(getApplicationContext(), -blockTime.getId(), endIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                        try {
                                            mgrAlarm.set(AlarmManager.RTC_WAKEUP,
                                                    formatter.parse(blockTime.getStarttime()).getTime(),
                                                    pendingStartIntent);
                                            Log.d(TAG, formatter.parse(blockTime.getStarttime()).getTime() + " " + System.currentTimeMillis() + " " + (formatter.parse(blockTime.getStarttime()).getTime() - System.currentTimeMillis()));
                                            Log.d(TAG, formatter.parse(blockTime.getEndtime()).getTime() + " " + System.currentTimeMillis() + " " + (formatter.parse(blockTime.getEndtime()).getTime() - System.currentTimeMillis()));
                                            mgrAlarm.set(AlarmManager.RTC_WAKEUP,
                                                    formatter.parse(blockTime.getEndtime()).getTime(),
                                                    pendingEndIntent);
                                        } catch (ParseException e) {
                                            Log.e(TAG, e.toString());
                                        }
                                        intentArray.add(pendingStartIntent);
                                        intentArray.add(pendingEndIntent);
                                        cVVector.add(blockTimeValues);
                                    }
                                    Log.d(TAG, intentArray.size() + " PendingIntents have been progressed");
                                    int inserted = 0;
                                    // add to database
                                    if (cVVector.size() > 0) {
                                        ContentValues[] cvArray = new ContentValues[cVVector.size()];
                                        cVVector.toArray(cvArray);
                                        getBaseContext().getContentResolver().bulkInsert(BlocktimeContract.BlocktimeEntry.CONTENT_URI, cvArray);
                                    }

                                }
                            },
                            new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {
                                    Log.e(TAG, "BlockTimes Sync failed! " + throwable);
                                }
                            }
                    );


        }

        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        sendNotification(message);
        // [END_EXCLUDE]
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle("New Block Time")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}

