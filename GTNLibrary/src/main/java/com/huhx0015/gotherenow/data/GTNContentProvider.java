package com.huhx0015.gotherenow.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

/**
 * Created by Michael Yoon Huh on 5/26/2016.
 */
public class GTNContentProvider extends ContentProvider {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // CONTENT PROVIDER VARIABLES
    private static final int SHORTCUT = 100;
    private static final int SHORTCUT_ID = 101;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    // DATABASE VARIABLES
    private GTNDatabaseHelper mOpenHelper;

    // LOGGING VARIABLES
    private static final String LOG_TAG = GTNContentProvider.class.getSimpleName();

    /** CONTENT PROVIDER OVERRIDE METHODS ______________________________________________________ **/

    @Override
    public boolean onCreate() {
        mOpenHelper = new GTNDatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Cursor retCursor;
        switch(sUriMatcher.match(uri)){
            case SHORTCUT:
                retCursor = db.query(
                        GTNContract.GTNShortcutEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case SHORTCUT_ID:
                long _id = ContentUris.parseId(uri);
                retCursor = db.query(
                        GTNContract.GTNShortcutEntry.TABLE_NAME,
                        projection,
                        GTNContract.GTNShortcutEntry._ID + " = ?",
                        new String[]{String.valueOf(_id)},
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                Log.e(LOG_TAG, "query(): ERROR: Unknown URI: " + uri);
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }

        // Set the notification URI for the cursor to the one passed into the function. This
        // causes the cursor to register a content observer to watch for changes that happen to
        // this URI and any of it's descendants. By descendants, we mean any URI that begins
        // with this path.
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        switch(sUriMatcher.match(uri)){
            case SHORTCUT:
                return GTNContract.GTNShortcutEntry.CONTENT_TYPE;
            case SHORTCUT_ID:
                return GTNContract.GTNShortcutEntry.CONTENT_ITEM_TYPE;
            default:
                Log.e(LOG_TAG, "getType(): ERROR: Unknown URI: " + uri);
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long _id;
        Uri returnUri;

        switch(sUriMatcher.match(uri)){
            case SHORTCUT:
                _id = db.insert(GTNContract.GTNShortcutEntry.TABLE_NAME, null, values);
                if (_id > 0){
                    returnUri = GTNContract.GTNShortcutEntry.buildShortcutUri(_id);
                } else {
                    Log.e(LOG_TAG, "insert(): ERROR: Unable to insert rows into: " + uri);
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                }
                break;
            default:
                Log.e(LOG_TAG, "insert(): ERROR: Unknown URI: " + uri);
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }

        // Use this on the URI passed into the function to notify any observers that the uri has
        // changed.
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rows; // Number of rows effected

        switch(sUriMatcher.match(uri)){
            case SHORTCUT:
                rows = db.delete(GTNContract.GTNShortcutEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }

        // Because null could delete all rows:
        if(selection == null || rows != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    /** CONTENT PROVIDER METHODS _______________________________________________________________ **/

    // buildUriMatcher(): Builds a UriMatcher that is used to determine witch database request is
    // being made.
    public static UriMatcher buildUriMatcher(){

        // All paths to the UriMatcher have a corresponding code to return
        // when a match is found (the ints above).
        String contentAuthority = GTNContract.CONTENT_AUTHORITY;
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        //matcher.addURI(contentAuthority, GTNContract.PATH_SHORTCUT, SHORTCUT);
        matcher.addURI(contentAuthority, GTNContract.PATH_SHORTCUT, SHORTCUT);
        matcher.addURI(contentAuthority, GTNContract.PATH_SHORTCUT + "/#", SHORTCUT_ID);

        return matcher;
    }
}