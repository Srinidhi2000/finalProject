package com.example.android.project.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;
//Helper class containing constants
public final class contract {

    private contract(){}
    public static final String CONTENT_AUTHORITY="com.example.android.project";
    public static final Uri BASE_CONTENT_URI=Uri.parse("content://"+CONTENT_AUTHORITY);
    public static final String PATH="places";
    public static class loginEntry implements BaseColumns {

        public static final Uri CONTENT_URI=Uri.withAppendedPath(BASE_CONTENT_URI,PATH);
        public static final String CONTENT_LIST=ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+PATH;
        public static final String CONTENT_ITEM=ContentResolver.CURSOR_ITEM_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+PATH;
        public static final String rowid=BaseColumns._ID;
        public  static final String TABLE_NAME="Showlogin";
        public static final String c1username="USERNAME";
        public static final String c2password="PASSWORD";
        public static final String c3rating="USER_RATING";
        public static final String c4visited="VISIT";
        public static final String c5from_time="FROM_TIME";
        public static final String c6to_time="TO_TIME";
        public static final String c7info="USER_DETAILS";
        public static final String c8name="VENUE";
        public static final String c9category="CATEGORY";
        public static final String c10address="ADDRESS";


    }
}


