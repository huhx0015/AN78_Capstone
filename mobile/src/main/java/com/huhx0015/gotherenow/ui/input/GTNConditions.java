package com.huhx0015.gotherenow.ui.input;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.EditText;

/**
 * -------------------------------------------------------------------------------------------------
 * [GTNConditions] CLASS
 * PROGRAMMER: Michael Yoon Huh (Huh X0015)
 * DESCRIPTION: This class contains methods to check the shortcut input and application installation
 * status.
 * -------------------------------------------------------------------------------------------------
 */
public class GTNConditions {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // LOGGING VARIABLES
    private static final String LOG_TAG = GTNConditions.class.getSimpleName();

    // PACKAGE VARIABLES
    private static final String GOOGLE_MAPS_APP = "com.google.android.apps.maps";

    /** CONDITIONS FUNCTIONALITY _______________________________________________________________ **/

    // checkForAddressInput(): Checks to see if the address input field is blank or not.
    public static Boolean checkForAddressInput(EditText address) {

        String location = ""; // Stores the address/location value from the address_1 EditText.
        Boolean addressExists = true; // Stores the Boolean value for determining if the address input is valid.

        // Retrieves the address from the user's input.
        try { location = address.getText().toString(); }

        // NullPointerException handler.
        catch (NullPointerException e) {
            Log.d(LOG_TAG, "ERROR: Null pointer exception was encountered while retrieving the address input.");
        }

        location = location.trim(); // Removes the spaces from the string.

        // If the address input is blank, the address has failed the valid address check.
        if (location.length() < 1) { addressExists = false; }

        return addressExists;
    }

    // checkForAppInstall(): Checks to see if the application is installed on the device.
    public static Boolean checkForAppInstalled(Context context) {

        // Sets up the PackageManager class object to check the device's installed applications.
        PackageManager pm = context.getPackageManager();
        String packName = GOOGLE_MAPS_APP; // "GOOGLE MAPS" package name.
        Boolean isInstalled;

        // Checks to see if the package is installed or not.
        try {
            pm.getPackageInfo(packName, PackageManager.GET_ACTIVITIES);
            isInstalled = true;
        }

        // NameNotFoundException handler.
        catch (PackageManager.NameNotFoundException e) { isInstalled = false; }

        return isInstalled;
    }

    // checkForShortcutInput(): Checks to see if the shortcut input field is blank or not.
    public static Boolean checkForShortcutInput(String name) {

        // Stores the Boolean value for determining if the address input is valid.
        Boolean shortcutValid = true;

        // Checks if the name value is null.
        if (name == null) { shortcutValid = false; }

        else {

            name = name.trim(); // Removes the spaces from the string.

            // Checks to see if the length of the string is less than 1.
            if (name.length() < 1) { shortcutValid = false; }
        }

        return shortcutValid;
    }

    // getAddressInput(): Retrieves the address/location value from the EditText object.
    public static String getAddressInput(boolean requiresFormat, EditText address) {

        String location = ""; // Stores the address/location value from the address_1 EditText.

        // Retrieves the address from the user's input.
        try { location = address.getText().toString(); }

        // NullPointerException handler.
        catch (NullPointerException e) {
            Log.d(LOG_TAG, "ERROR: Null pointer exception was encountered while retrieving the address input.");
        }

        // If launching an intent or creating a shortcut, the proper format for the address/location string is set.
        if (requiresFormat) { location = location.replace(" ", "+"); }

        return location;
    }
}
