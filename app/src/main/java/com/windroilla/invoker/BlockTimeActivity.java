package com.windroilla.invoker;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.windroilla.invoker.adapter.BlockTimeAdapter;
import com.windroilla.invoker.data.BlocktimeContract;

public class BlockTimeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final int COL_BLOCKTIME_ID = 0;
    public static final int COL_BLOCKTIME_STARTTIME = 1;
    public static final int COL_BLOCKTIME_ENDTIME = 2;
    public static final int COL_BLOCKTIME_UPDATETIME = 3;

    private static final int BLOCKTIME_LOADER = 0;

    private static final String[] BLOCKTIME_COLUMNS = {
            BlocktimeContract.BlocktimeEntry.TABLE_NAME + "." + BlocktimeContract.BlocktimeEntry._ID,
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
        getLoaderManager().initLoader(BLOCKTIME_LOADER, null, this);
        blockTimeAdapter = new BlockTimeAdapter(this, null, 0);
        lv = (ListView) findViewById(R.id.blocktime_listview);
        lv.setAdapter(blockTimeAdapter);
        startService(new Intent(this, TouchBlockService.class));
        finish();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = BlocktimeContract.BlocktimeEntry.COLUMN_CREATED_TIME + " ASC";
        Uri blockListUri = BlocktimeContract.BlocktimeEntry.buildBlocktimeUri();
        return new CursorLoader(
                this,
                blockListUri,
                BLOCKTIME_COLUMNS,
                null,
                null,
                sortOrder
        );
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
