package com.windroilla.invoker.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by vishnu on 13/11/15.
 */
public class BlocktimeContract {

    public static final String CONTENT_AUTHORITY = "com.windroilla.invoker.app";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_BLOCKTIME = "blocktime";

    public static long normalizeDate(long Date) {
        // normalize the start date to the beginning of the (UTC) day

        //says time android.text.format.Time is deprecated

            Time time = new Time();
            time.set(Date);
            int julianDay = Time.getJulianDay(Date, time.gmtoff);
            return time.setJulianDay(julianDay);

    }

    public static final class BlocktimeEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_BLOCKTIME).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BLOCKTIME;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BLOCKTIME;

        public static final String TABLE_NAME = "blocktime";
        public static final String COLUMN_START_TIME = "starttime";
        public static final String COLUMN_END_TIME = "endtime";
        public static final String COLUMN_CREATED_TIME = "created_time";



        public static long getStarttimeFromUri(Uri uri) {
            String starttimeString = uri.getQueryParameter(COLUMN_START_TIME);
            return Long.parseLong(starttimeString);
        }

        public static long getEndtimeFromUri(Uri uri) {
            String endtimeString = uri.getQueryParameter(COLUMN_END_TIME);
            return Long.parseLong(endtimeString);
        }

        public static Uri buildBlocktimeUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

}
