package com.windroilla.invoker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.windroilla.invoker.api.ApiService;
import com.windroilla.invoker.api.requestclasses.RequestRegistration;
import com.windroilla.invoker.api.responseclasses.UserProfile;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class RegisterActivity extends AppCompatActivity {

    public final static String APP_PREF_KEY = "INVOKER_PREF_KEY";
    public final static String FLAG_FIRST_USE = "INVOKER_FIRST_USE";
    public final static String TAG = "INVOKER_REGISTER";
    public final static String INVOKER_USER_ID = "INVOKER_USER_ID";
    EditText phone;

    @Inject
    ApiService apiService;

    @Inject
    String deviceID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InvokerApp.getsInstance().graph().inject(this);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        phone = (EditText) findViewById(R.id.register_phone_editText);
        phone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    //TODO make network call to store the details in server
                    apiService.registerNewUser(
                            new RequestRegistration(
                                    v.getText().toString(),
                                    deviceID
                            )
                    )
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<UserProfile>() {
                                           @Override
                                           public void call(UserProfile userProfile) {
                                               if (userProfile == null)
                                                   return;
                                               SharedPreferences sp = getSharedPreferences(APP_PREF_KEY, MODE_PRIVATE);
                                               SharedPreferences.Editor editor = sp.edit();
                                               editor.putBoolean(FLAG_FIRST_USE, false);
                                               editor.putInt(INVOKER_USER_ID, userProfile.getId());
                                               editor.commit();
                                               startActivity(new Intent(getBaseContext(), MainActivity.class));
                                               finish();
                                           }
                                       },
                                    new Action1<Throwable>() {
                                        @Override
                                        public void call(Throwable throwable) {
                                            Log.e(TAG, "Registration failed! " + throwable);
                                            Toast.makeText(getBaseContext(), "User registration failed! Please try again!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                }
                return false;
            }
        });
    }

}
