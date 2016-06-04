package com.huhx0015.gotherenow.application;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import com.huhx0015.gotherenow.ui.language.GTNLanguage;
import com.huhx0015.gotherenow.ui.resources.GTNTypes;
import com.huhx0015.gotherenow.ui.toast.GTNToast;

/**
 * -------------------------------------------------------------------------------------------------
 * [GTNApplication] CLASS
 * PROGRAMMER: Michael Yoon Huh (HUHX0015)
 * DESCRIPTION: This is a top-level Application-type class that contains methods that are globally
 * accessible to all classes throughout the application.
 * -------------------------------------------------------------------------------------------------
 */

public class GTNApplication extends Application {

    /** INSTANCE VARIABLES _____________________________________________________________________ **/

    // GTNApplication(): Static variable for easy access to process global Application instance.
    public static GTNApplication instance; { instance = this; }

    /** APPLICATION FUNCTIONALITY ______________________________________________________________ **/

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
}