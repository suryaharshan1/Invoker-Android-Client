package com.windroilla.invoker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public final static String APP_PREF_KEY = "INVOKER_PREF_KEY";

    public final static String FLAG_CHOSEN_INSTITUTE = "INVOKER_CHOSEN_INSTITUTE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = getSharedPreferences(APP_PREF_KEY, MODE_PRIVATE);
        if (sp.getString(FLAG_CHOSEN_INSTITUTE, "").isEmpty()) {
            startActivity(new Intent(this, InstituteActivity.class));
        } else {
            startActivity(new Intent(this, MainActivity.class));
        }
        overridePendingTransition(0, 0);
        finish();
    }

}
