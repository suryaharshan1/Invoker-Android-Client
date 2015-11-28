package com.windroilla.invoker.gcm;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.android.gms.iid.InstanceIDListenerService;
import com.windroilla.invoker.InvokerApp;
import com.windroilla.invoker.R;
import com.windroilla.invoker.api.ApiService;
import com.windroilla.invoker.api.requestclasses.RequestSetRegistrationToken;
import com.windroilla.invoker.api.responseclasses.UserProfile;

import java.io.IOException;

import javax.inject.Inject;

import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by vishnu on 13/11/15.
 */
public class MyInstanceIDListenerService extends InstanceIDListenerService {

    private static final String TAG = "MyInstanceIDLS";

    @Inject
    ApiService apiService;

    @Inject
    String deviceID;

    public MyInstanceIDListenerService() {
        super();
        InvokerApp.getsInstance().graph().inject(this);
    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. This call is initiated by the
     * InstanceID provider.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Fetch updated Instance ID token and notify our app's server of any changes (if applicable).
        InstanceID instanceID = InstanceID.getInstance(this);
        try {
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
    // [END refresh_token]

    private void sendRegistrationToServer(String token) {
        //send the token to server
        Log.i("device ID ", deviceID);
        Log.i("token" , token);
        apiService.setRegToken(
                new RequestSetRegistrationToken(
                        deviceID,
                        token
                )
        )
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new Action1<UserProfile>() {
                               @Override
                               public void call(UserProfile userProfile) {
                                   if (userProfile == null)
                                       return;
                               }
                           },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                Log.e(TAG, "Registration failed! " + throwable);

                            }
                        });
    }

}
