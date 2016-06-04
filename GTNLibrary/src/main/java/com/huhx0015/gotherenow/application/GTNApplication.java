package com.huhx0015.gotherenow.application;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;

import com.huhx0015.gotherenow.data.GTNDatabaseUtil;
import com.huhx0015.gotherenow.model.GTNShortcut;
import com.huhx0015.gotherenow.ui.language.GTNLanguage;
import com.huhx0015.gotherenow.ui.resources.GTNTypes;
import com.huhx0015.gotherenow.ui.toast.GTNToast;
import java.util.LinkedList;

/**
 * -------------------------------------------------------------------------------------------------
 * [GTNApplication] CLASS
 * PROGRAMMER: Michael Yoon Huh (HUHX0015)
 * DESCRIPTION: This is a top-level Application-type class that contains methods that are globally
 * accessible to all classes throughout the application.
 * -------------------------------------------------------------------------------------------------
 */

public class GTNApplication extends Application {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // DATABASE VARIABLES
    private static final String DATABASE_NAME = "gtn_shortcuts.db";

    /** INSTANCE VARIABLES _____________________________________________________________________ **/

    // GTNApplication(): Static variable for easy access to process global Application instance.
    public static GTNApplication instance; { instance = this; }

    /** APPLICATION FUNCTIONALITY ______________________________________________________________ **/

    // prepareGTNIntent(): Prepares an Intent to launch GO THERE NOW directly into the
    // GTNMainActivity class, which will display the ADD A NEW SHORTCUT action menu on launch.
    public void prepareGTNIntent() {

        // Launches an Intent to Go There Now to display the ADD A NEW SHORTCUT menu.
        Intent i = new Intent("com.huhx0015.gotherenow.MAINACTIVITY");
        i.putExtra("gtn_new_location", true);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(i); // Launches the intent.
    }

    // prepareNavigationIntent(): Queries the shortcut from the database and launches an Intent
    // that starts Google Maps in navigation mode.
    public void prepareNavigationIntent(String address, String type, GTNLanguage language) {

        // Displays a Toast message indicating that Navigation Mode is launching.
        GTNToast.toastyPopUp(language.getLaunchNavigationText() + "\n" + address, this);

        // Holds the URL request string that is processed by Google Maps.
        String url = GTNTypes.createURL(address, type);

        // Launches the customized Google Maps intent directly in navigation mode.
        Intent i = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
        i.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(i); // Launches the intent to Google Maps.
    }

    // prepareSaveLocationIntent(): Launches an Intent to Go There Now to query the user to save the
    // current location as a shortcut.
    public void prepareSaveLocationIntent(GTNLanguage language) {

        // Displays a Toast message indicating that the current location will be saved.
        GTNToast.toastyPopUp(language.getSavingLocationText(), this);

        // Launches an Intent to Go There Now to immediately obtain the address for the current
        // location and to display the SAVE CURRENT LOCATION dialog window.
        Intent i = new Intent("com.huhx0015.gotherenow.MAINACTIVITY");
        i.putExtra("gtn_save_location", true);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(i); // Launches the intent.
    }

    // recreateDatabase(): Deletes and recreates the database.
    public void recreateDatabase(LinkedList<GTNShortcut> shortcuts) {

        // Deletes the database, re-creates it with the received shortcuts.
        this.deleteDatabase(DATABASE_NAME); // Deletes the database.

        // Initializes the new database.
        GTNDatabaseUtil datasource = new GTNDatabaseUtil(this);
        datasource.open(); // Opens the database.

        // Creates the shortcut database from the LinkedList<GTNShortcut> list.
        datasource.createShortcutDatabase(shortcuts);
        datasource.close(); // Closes the database.
    }
}