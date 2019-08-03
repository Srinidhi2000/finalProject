package com.example.android.project.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;

//To do operations on the database
public class places_provider extends ContentProvider {
    private display_data dbhelper;
    private static final int PLACES=1;
    private static final int PLACE_ITEM=2;
    private static final UriMatcher urimatcher=new UriMatcher(UriMatcher.NO_MATCH);
    static{
        urimatcher.addURI(contract.CONTENT_AUTHORITY,contract.PATH,PLACES);
        urimatcher.addURI(contract.CONTENT_AUTHORITY,contract.PATH+"/#",PLACE_ITEM);
    }
    @Override
    public boolean onCreate() {
        dbhelper=new display_data(getContext());
        return true;
    }

    
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database=dbhelper.getReadableDatabase();
        Cursor cursor;
        int match=urimatcher.match(uri);
        switch(match)
        {  case PLACES:
                cursor=database.query(contract.loginEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
            break;
                case PLACE_ITEM:
                selection=contract.loginEntry.rowid+"=?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor=database.query(contract.loginEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            default:throw new IllegalArgumentException("Cannot query"+uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }


    
    @Override
    public String getType(@NonNull Uri uri) {

        final int match=urimatcher.match(uri);
        switch(match)
        {
            case PLACES:
                return contract.loginEntry.CONTENT_LIST;
            case PLACE_ITEM:
                return contract.loginEntry.CONTENT_ITEM;
            default:
                throw new IllegalStateException("Unknown URI");
        }
    }

    
    @Override
    public Uri insert(@NonNull Uri uri,  ContentValues values) {
    SQLiteDatabase database=dbhelper.getWritableDatabase();
    long id=database.insert(contract.loginEntry.TABLE_NAME,null,values);
        getContext().getContentResolver().notifyChange(uri,null);
        return ContentUris.withAppendedId(uri,id);

    }

    @Override
    public int delete(@NonNull Uri uri,  String selection,  String[] selectionArgs)
    {
        SQLiteDatabase database=dbhelper.getWritableDatabase();
        selection=contract.loginEntry.rowid+"=?";
        selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
        int rows=database.delete(contract.loginEntry.TABLE_NAME,selection,selectionArgs);
        if(rows!=0)
        {
            getContext().getContentResolver().notifyChange(uri,null);
        }

        return rows;
    }

    @Override
    public int update(@NonNull Uri uri,  ContentValues values,  String selection,  String[] selectionArgs) {
        SQLiteDatabase database=dbhelper.getWritableDatabase();
        int no=database.update(contract.loginEntry.TABLE_NAME,values,selection,selectionArgs);
        getContext().getContentResolver().notifyChange(uri,null);
        return no;
    }
}
