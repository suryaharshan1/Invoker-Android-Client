package com.windroilla.invoker.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by vishnu on 13/11/15.
 */
public class BlocktimeProvider extends ContentProvider{

    private BlocktimeDbHelper mOpenHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    static final int BLOCKTIME = 100;
    static final int BLOCKTIME_WITH_STARTTIME = 101;
    static final int BLOCKTIME_WITH_ENDTIME = 102;
    static final int BLOCKTIME_WITH_STARTTIME_AND_ENDTIME = 103;

    private static final SQLiteQueryBuilder sBlocktimeQueryBuilder;

    static{
        sBlocktimeQueryBuilder = new SQLiteQueryBuilder();

        //This is an inner join which looks like
        //weather INNER JOIN location ON weather.location_id = location._id
        sBlocktimeQueryBuilder.setTables(
                BlocktimeContract.BlocktimeEntry.TABLE_NAME);
    }

    private static final String sStarttimeSelection =
            BlocktimeContract.BlocktimeEntry.TABLE_NAME+
                    "." + BlocktimeContract.BlocktimeEntry.COLUMN_START_TIME + " >= ? ";

    private static final String sEndtimeSelection =
            BlocktimeContract.BlocktimeEntry.TABLE_NAME+
                    "." + BlocktimeContract.BlocktimeEntry.COLUMN_END_TIME + " <= ? ";

    private static final String sStarttimeAndEndtimeSelection =
            BlocktimeContract.BlocktimeEntry.TABLE_NAME+
                    "." + BlocktimeContract.BlocktimeEntry.COLUMN_START_TIME + " >= ? AND " +
                    BlocktimeContract.BlocktimeEntry.COLUMN_END_TIME + " <= ? ";


    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = BlocktimeContract.CONTENT_AUTHORITY;

        matcher.addURI(authority,BlocktimeContract.PATH_BLOCKTIME,BLOCKTIME);
        matcher.addURI(authority,BlocktimeContract.PATH_BLOCKTIME + "/*", BLOCKTIME_WITH_STARTTIME);
        matcher.addURI(authority,BlocktimeContract.PATH_BLOCKTIME + "/*/#",BLOCKTIME_WITH_ENDTIME);

        return matcher;

    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new BlocktimeDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch(sUriMatcher.match(uri)){

            case BLOCKTIME:
                retCursor = mOpenHelper.getReadableDatabase().query(BlocktimeContract.BlocktimeEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case BLOCKTIME_WITH_STARTTIME:
                retCursor = getBlocktimeByStarttime(uri, projection, sortOrder);
                break;
            case BLOCKTIME_WITH_ENDTIME:
                retCursor = getBlocktimeByEndtime(uri, projection, sortOrder);
                break;
            case BLOCKTIME_WITH_STARTTIME_AND_ENDTIME:
                retCursor = getBlocktimeByStarttimeAndEndtime(uri, projection, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    private Cursor getBlocktimeByStarttimeAndEndtime(Uri uri, String[] projection, String sortOrder) {
        long starttime = BlocktimeContract.BlocktimeEntry.getStarttimeFromUri(uri);
        long endtime = BlocktimeContract.BlocktimeEntry.getEndtimeFromUri(uri);

        String[] selectionArgs;
        String selection;

        selectionArgs = new String[]{Long.toString(starttime),Long.toString(endtime)};
        selection = sStarttimeAndEndtimeSelection;

        return sBlocktimeQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getBlocktimeByEndtime(Uri uri, String[] projection, String sortOrder) {
        long endtime = BlocktimeContract.BlocktimeEntry.getEndtimeFromUri(uri);

        String[] selectionArgs;
        String selection;

        selectionArgs = new String[]{Long.toString(endtime)};
        selection = sEndtimeSelection;

        return sBlocktimeQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getBlocktimeByStarttime(Uri uri, String[] projection, String sortOrder) {
        long starttime = BlocktimeContract.BlocktimeEntry.getStarttimeFromUri(uri);

        String[] selectionArgs;
        String selection;

        selectionArgs = new String[]{Long.toString(starttime)};
        selection = sStarttimeSelection;

        return sBlocktimeQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            // Student: Uncomment and fill out these two cases
            case BLOCKTIME_WITH_STARTTIME_AND_ENDTIME:
                return BlocktimeContract.BlocktimeEntry.CONTENT_ITEM_TYPE;
            case BLOCKTIME_WITH_STARTTIME:
                return BlocktimeContract.BlocktimeEntry.CONTENT_TYPE;
            case BLOCKTIME_WITH_ENDTIME:
                return BlocktimeContract.BlocktimeEntry.CONTENT_TYPE;
            case BLOCKTIME:
                return BlocktimeContract.BlocktimeEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case BLOCKTIME: {
                normalizeDate(values);
                long _id = db.insert(BlocktimeContract.BlocktimeEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = BlocktimeContract.BlocktimeEntry.buildBlocktimeUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }


    private void normalizeDate(ContentValues values) {
        // normalize the date value
        if (values.containsKey(BlocktimeContract.BlocktimeEntry.COLUMN_START_TIME)) {
            long dateValue = values.getAsLong(BlocktimeContract.BlocktimeEntry.COLUMN_START_TIME);
            values.put(BlocktimeContract.BlocktimeEntry.COLUMN_START_TIME,BlocktimeContract.normalizeDate(dateValue));
        }
    }
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        if(selection == null) selection = "1";
        switch (match){
            case BLOCKTIME : {
                rowsDeleted = db.delete(BlocktimeContract.BlocktimeEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Student: A null value deletes all rows.  In my implementation of this, I only notified
        // the uri listeners (using the content resolver) if the rowsDeleted != 0 or the selection
        // is null.
        // Oh, and you should notify the listeners here.
        if(rowsDeleted != 0)
            getContext().getContentResolver().notifyChange(uri,null);
        // Student: return the actual rows deleted
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match){
            case BLOCKTIME:{
                rowsUpdated = db.update(BlocktimeContract.BlocktimeEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(rowsUpdated != 0)
            getContext().getContentResolver().notifyChange(uri,null);
        return rowsUpdated;
    }

    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BLOCKTIME :
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        normalizeDate(value);
                        long _id = db.insert(BlocktimeContract.BlocktimeEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    // You do not need to call this method. This is a method specifically to assist the testing
    // framework in running smoothly. You can read more at:
    // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
