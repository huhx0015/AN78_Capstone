package com.huhx0015.gotherenow.data;

import java.util.LinkedList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.huhx0015.gotherenow.model.GTNShortcut;
import com.huhx0015.gotherenow.data.GTNContract.GTNShortcutEntry;

/**
 * -------------------------------------------------------------------------------------------------
 * [GTNDatabaseUtil] CLASS
 * PROGRAMMER: Michael Yoon Huh (HUHX0015)
 * DESCRIPTION: This is a class that contains methods for accessing and modifying the shortcut
 * database table.
 * -------------------------------------------------------------------------------------------------
 */

public class GTNDatabaseUtil {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // DATABASE VARIABLES
    private GTNDatabaseHelper dbHelper;
    private SQLiteDatabase database;

    // References all data columns in the MySQL database table.
    private String[] allColumns = {
            GTNShortcutEntry.COLUMN_ID,
            GTNShortcutEntry.COLUMN_NAME,
            GTNShortcutEntry.COLUMN_ADDRESS,
            GTNShortcutEntry.COLUMN_TYPE,
            GTNShortcutEntry.COLUMN_IMAGE_ID
    };

    // LOGGING VARIABLES
    private static final String LOG_TAG = GTNDatabaseUtil.class.getName();

    /** INITIALIZATION FUNCTIONALITY ___________________________________________________________ **/

    // GTNDatabaseUtil(): Initialization method for the GTNDatabaseUtil class.
    public GTNDatabaseUtil(Context context) {
        dbHelper = new GTNDatabaseHelper(context);
    }

    /** ACCESSOR FUNCTIONALITY _________________________________________________________________ **/

    // open(): Opens the database table with read/write permissions.
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    // close(): Closes the database table.
    public void close() {
        dbHelper.close();
    }

    /** DATABASE FUNCTIONALITY _________________________________________________________________ **/

    // createShortcutDatabase(): This method adds shortcuts from an existing LinkedList<GTNShortcut>
    // object.
    public void createShortcutDatabase(LinkedList<GTNShortcut> shortcuts) {

        int numberOfShortcuts = shortcuts.size(); // Size of the LinkedList<GTNShortcut> object.

        // Add shortcuts into the table.
        for (int i = 0; i < numberOfShortcuts; i++) {

            GTNShortcut currentShortcut = shortcuts.get(i); // Retrieves the shortcut.

            // Creates the shortcut and adds it to the database table.
            createShortcut(currentShortcut.getShortcutName(),
                    currentShortcut.getShortcutAddress(),
                    currentShortcut.getShortcutType(),
                    (int) currentShortcut.getShortcutImageId());

            Log.d(LOG_TAG, "Creating shortcut database... | Current Shortcut: " + i);
        }
    }

    // createShortcut(): Creates a new shortcut in the database table.
    public GTNShortcut createShortcut(String shortcut, String address, String type, int imageRes) {

        ContentValues values = new ContentValues(); // Retrieves the content values.
        values.put(GTNShortcutEntry.COLUMN_NAME, shortcut); // Adds the name of the shortcut.
        values.put(GTNShortcutEntry.COLUMN_ADDRESS, address); // Adds the shortcut address.
        values.put(GTNShortcutEntry.COLUMN_TYPE, type); // Adds shortcut type.
        values.put(GTNShortcutEntry.COLUMN_IMAGE_ID, imageRes); // Adds the shortcut image resource.

        // Retrieves the row ID in which to insert the new shortcut data.
        long insertId = database.insert(GTNShortcutEntry.TABLE_NAME, null, values);

        // Sets up the SQL cursor for accessing the SQL database table and adds the shortcut.
        Cursor cursor = database.query(
                GTNShortcutEntry.TABLE_NAME, allColumns,
                GTNShortcutEntry.COLUMN_ID + " = " + insertId, null,
                null, null, null, null);

        Log.d(LOG_TAG, "Shortcut created with id: " + insertId);

        cursor.moveToFirst(); // Moves the cursor into position.

        // Retrieves the shortcut at the cursor position.
        GTNShortcut newShortcut = cursorToShortcut(cursor);
        cursor.close();

        return newShortcut;
    }

    // deleteShortcut(): Removes the shortcut from the SQL database table.
    public void deleteShortcut(GTNShortcut shortcut) {

        long id = shortcut.getId(); // Retrieves the ID from the GTNShortcut object.

        // Deletes the specified shortcut.
        database.delete(GTNShortcutEntry.TABLE_NAME, GTNShortcutEntry.COLUMN_ID  + " = " + id, null);

        Log.d(LOG_TAG, "Shortcut deleted at position: " + id);
    }

    // editShortcut(): Edits the existing shortcut data.
    public void editShortcut(String shortcut, String address, long id, int imageRes, String type) {

        // Prepares the updated shortcut data to be stored into the SQL database table.
        ContentValues newShortcutContents = new ContentValues();
        newShortcutContents.put(GTNShortcutEntry.COLUMN_NAME, shortcut);
        newShortcutContents.put(GTNShortcutEntry.COLUMN_ADDRESS, address);
        newShortcutContents.put(GTNShortcutEntry.COLUMN_ID, id);
        newShortcutContents.put(GTNShortcutEntry.COLUMN_IMAGE_ID, imageRes);
        newShortcutContents.put(GTNShortcutEntry.COLUMN_TYPE, type);

        // Updates the shortcut entry data with the new attributes.
        database.update(GTNShortcutEntry.TABLE_NAME, newShortcutContents, "_id " + "=" + id, null);
    }

    // getAllShortcuts(): Retrieves all available shortcuts from the SQL database table.
    public LinkedList<GTNShortcut> getAllShortcuts() {

        // The LinkedList in which the retrieved GTNShortcut objects will be stored.
        LinkedList<GTNShortcut> shortcuts = new LinkedList<GTNShortcut>();

        // Sets up the cursor for accessing the SQL database table.
        Cursor cursor = database.query(GTNShortcutEntry.TABLE_NAME,
                allColumns, null, null, null, null, null, null);

        cursor.moveToFirst(); // Moves the cursor into position.

        // Adds all shortcuts until the cursor has reached the last position.
        while (!cursor.isAfterLast()) {

            // Retrieves the shortcut at the cursor position.
            GTNShortcut shortcut = cursorToShortcut(cursor);
            shortcuts.add(shortcut); // Adds the retrieved shortcut to the LinkedList.
            cursor.moveToNext(); // Moves the shortcut to the next shortcut row.
        }

        cursor.close(); // Closes the cursor.

        return shortcuts;
    }

    // cursorToShortcut(): This function is used to create a GTNShortcut object from the shortcut
    // data read from the SQL database table.
    public static GTNShortcut cursorToShortcut(Cursor cursor) {

        GTNShortcut shortcut = new GTNShortcut(); // Initializes the new GTNShortcut object.

        // Stores the shortcut data read from the cursor into the new GTNShortcut object.
        shortcut.setId(cursor.getLong(0)); // Sets the id.
        shortcut.setShortcutName(cursor.getString(1)); // Sets the shortcut name.
        shortcut.setShortcutAddress(cursor.getString(2)); // Sets the shortcut address.
        shortcut.setShortcutType(cursor.getString(3)); // Sets the shortcut type.
        shortcut.setShortcutImageId(cursor.getLong(4)); // Sets the image id.

        return shortcut;
    }
}