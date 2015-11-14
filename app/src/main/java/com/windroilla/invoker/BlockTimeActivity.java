package com.windroilla.invoker;

import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.windroilla.invoker.adapter.BlockTimeAdapter;
import com.windroilla.invoker.data.BlocktimeContract;

public class BlockTimeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final int COL_BLOCKTIME_ID = 0;
    public static final int COL_BLOCKTIME_STARTTIME = 1;
    public static final int COL_BLOCKTIME_ENDTIME = 2;
    public static final int COL_BLOCKTIME_UPDATETIME = 3;

    private static final String[] BLOCKTIME_COLUMNS = {
            BlocktimeContract.BlocktimeEntry.TABLE_NAME + BlocktimeContract.BlocktimeEntry._ID,
            BlocktimeContract.BlocktimeEntry.COLUMN_START_TIME,
            BlocktimeContract.BlocktimeEntry.COLUMN_END_TIME,
            BlocktimeContract.BlocktimeEntry.COLUMN_CREATED_TIME
    };

    private BlockTimeAdapter blockTimeAdapter;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_time);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        blockTimeAdapter = new BlockTimeAdapter(this, null, 0);
        lv = (ListView) findViewById(R.id.blocktime_listview);
        lv.setAdapter(blockTimeAdapter);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        blockTimeAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        blockTimeAdapter.swapCursor(null);
    }
}
