package com.windroilla.invoker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    public final static String APP_PREF_KEY = "INVOKER_PREF_KEY";
    public final static String FLAG_FIRST_USE = "INVOKER_FIRST_USE";
    public final static String TAG = "INVOKER_REGISTER";
    EditText phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        phone = (EditText) findViewById(R.id.register_phone_editText);
        phone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    SharedPreferences sp = getSharedPreferences(APP_PREF_KEY, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putBoolean(FLAG_FIRST_USE, false);
                    editor.commit();
                    //TODO make network call to store the details in server
                    startActivity(new Intent(getBaseContext(), MainActivity.class));
                    finish();
                }
                return false;
            }
        });
    }

}
