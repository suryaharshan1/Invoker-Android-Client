package com.windroilla.invoker.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by vishnu on 13/11/15.
 */
public class BlocktimeContract {

    public static final String CONTENT_AUTHORITY = "com.windroilla.invoker.app";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_BLOCKTIME = "blocktime";

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



    }

}
