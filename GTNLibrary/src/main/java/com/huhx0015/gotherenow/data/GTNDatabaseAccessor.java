package com.huhx0015.gotherenow.data;

import android.content.Context;
import com.huhx0015.gotherenow.model.GTNShortcut;
import java.util.LinkedList;

/**
 * -------------------------------------------------------------------------------------------------
 * [GTNDatabaseAccessor] CLASS
 * PROGRAMMER: Michael Yoon Huh (HUHX0015)
 * DESCRIPTION: This is a class that contains methods for accessing the shortcut database file for
 * shortcut data retrieval.
 * -------------------------------------------------------------------------------------------------
 */

public class GTNDatabaseAccessor {

    /** DATA ACCESS FUNCTIONALITY ______________________________________________________________ **/

    // getAllShortcuts(): Retrieves all shortcuts from the database.
    public static LinkedList<GTNShortcut> getAllShortcuts(Context con) {

        // Initializes and sets up the database.
        GTNDatabaseUtil datasource = new GTNDatabaseUtil(con);
        datasource.open(); // The database is opened.

        // Creates the LinkedList of shortcuts from the database.
        LinkedList<GTNShortcut> shortcuts = datasource.getAllShortcuts();
        datasource.close(); // The database is closed.

        return shortcuts;
    }
}
