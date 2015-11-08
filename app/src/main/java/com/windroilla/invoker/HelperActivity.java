package com.windroilla.invoker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class HelperActivity extends AppCompatActivity {

    public final static String APP_PREF_KEY = "INVOKER_PREF_KEY";

    public final static String FLAG_FIRST_USE = "INVOKER_FIRST_USE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = getSharedPreferences(APP_PREF_KEY, MODE_PRIVATE);
        if (sp.getBoolean(FLAG_FIRST_USE, true)) {
            startActivity(new Intent(this, RegisterActivity.class));
        } else {
            startActivity(new Intent(this, MainActivity.class));
        }
        overridePendingTransition(0, 0);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_helper, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
