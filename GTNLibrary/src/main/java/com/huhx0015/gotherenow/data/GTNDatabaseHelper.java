package com.huhx0015.gotherenow.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.huhx0015.gotherenow.data.GTNContract.GTNShortcutEntry;
/**
 * -------------------------------------------------------------------------------------------------
 * [GTNDatabaseHelper] CLASS
 * PROGRAMMER: Michael Yoon Huh (HUHX0015)
 * DESCRIPTION: This is a class that is responsible for initializing, creating, and updating the
 * shortcut database table.
 * -------------------------------------------------------------------------------------------------
 */

public class GTNDatabaseHelper extends SQLiteOpenHelper {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // LOGGING VARIABLES
    private static final String LOG_TAG = GTNDatabaseHelper.class.getName();

    // TABLE VARIABLES
    private static final int DATABASE_VERSION = 1; // DATABASE VERSION
    private static final String DATABASE_NAME = "gtn_shortcuts.db"; // DATABASE FILE NAME

    // SQL VARIABLES: SQL database creation statement.
    private static final String DATABASE_CREATE = "create table "+ GTNShortcutEntry.TABLE_NAME + "("
            + GTNShortcutEntry.COLUMN_ID + " integer primary key autoincrement, "
            + GTNShortcutEntry.COLUMN_NAME + " text not null,"
            + GTNShortcutEntry.COLUMN_ADDRESS + " text not null,"
            + GTNShortcutEntry.COLUMN_TYPE + " text not null,"
            + GTNShortcutEntry.COLUMN_IMAGE_ID + " integer value);";

    /** INITIALIZATION FUNCTIONALITY ___________________________________________________________ **/

    // GTNDatabaseHelper(): Initializes the GTNDatabaseHelper class.
    public GTNDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /** SQL LITE OPEN HELPER FUNCTIONALITY _____________________________________________________ **/

    // onCreate(): This method is responsible for creating the database.
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE); // SQL: Creates a new database table.
    }

    // onUpgrade(): This method is responsible for upgrading the database version.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.w(LOG_TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", "
                + "which will destroy all previous data.");

        db.execSQL("DROP TABLE IF EXISTS " + GTNShortcutEntry.TABLE_NAME); // SQL: Drops existing database table.
        onCreate(db); // Creates a new database table.
    }
}