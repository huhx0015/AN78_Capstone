package com.huhx0015.gotherenow.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.huhx0015.gotherenow.library.R;

/** ------------------------------------------------------------------------------------------------
 *  [GTNPreference] CLASS
 *  PROGRAMMER: Michael Yoon Huh (HUHX0015)
 *  DESCRIPTION: This class is a class that contains functionality that pertains to the use and
 *  manipulation of shared preferences data.
 *  ------------------------------------------------------------------------------------------------
 */
public class GTNPreference {

    /** SHARED PREFERENCES FUNCTIONALITY _______________________________________________________ **/

    // getPreferenceResource(): Selects the appropriate resource based on the shared preference type.
    private static int getPreferenceResource(String prefType) {

        // Temporary preferences resource file.
        if (prefType.equals("gtn_temps")) { return R.xml.gtn_temps; }

        // Main preferences resource file.
        else { return R.xml.gtn_options; }
    }

    // initializePreferences(): Initializes and returns the SharedPreferences object.
    public static SharedPreferences initializePreferences(String prefType, Context context) {
        return context.getSharedPreferences(prefType, Context.MODE_PRIVATE);
    }

    // setDefaultPreferences(): Sets the shared preference values to default values.
    public static void setDefaultPreferences(String prefType, Boolean isReset, Context context) {

        // Determines the appropriate resource file to use.
        int prefResource = getPreferenceResource(prefType);

        // Resets the preference values to default values.
        if (isReset) {
            SharedPreferences preferences = initializePreferences(prefType, context);
            preferences.edit().clear().apply();
        }

        // Sets the default values for the SharedPreferences object.
        PreferenceManager.setDefaultValues(context, prefType, Context.MODE_PRIVATE, prefResource, true);
    }

    /** GET PREFERENCES FUNCTIONALITY __________________________________________________________ **/

    // getCurrentAddress(): Retrieves the current address value from preferences.
    public static String getCurrentAddress(SharedPreferences preferences) {
        return preferences.getString("gtn_current_address", ""); // Retrieves the current address setting.
    }

    // getCurrentName(): Retrieves the current shortcut name value from preferences.
    public static String getCurrentName(SharedPreferences preferences) {
        return preferences.getString("gtn_current_name", ""); // Retrieves the current shortcut name setting.
    }

    // getCurrentTransportation(): Retrieves the current transportation value from preferences.
    public static String getCurrentTransportation(SharedPreferences preferences) {
        return preferences.getString("gtn_current_trans_mode", "d"); // Retrieves the current transportation mode setting.
    }

    // getInitialLaunch(): Retrieves the "gtn_initial_launch" value from preferences.
    public static Boolean getInitialLaunch(SharedPreferences preferences) {
        return preferences.getBoolean("gtn_initial_launch_a", true); // Retrieves the initial application launch value.
    }

    // getLanguage(): Retrieves the "gtn_language" value from preferences.
    public static String getLanguage(SharedPreferences preferences) {
        return preferences.getString("gtn_language", "English"); // Retrieves the current language setting.
    }

    // getLastLocation(): Retrieves the last location value from preferences.
    public static String getLastLocation(SharedPreferences preferences) {
        return preferences.getString("gtn_last_location", ""); // Retrieves the last location value.
    }

    // getOrientationChange(): Retrieves the "gtn_orientation_change" value from preferences.
    public static Boolean getOrientationChange(SharedPreferences preferences) {
        return preferences.getBoolean("gtn_orientation_change", false); // Retrieves the orientation change value.
    }

    // getTransportationMode(): Retrieves the default transportation mode value from preferences.
    public static String getTransportationMode(SharedPreferences preferences) {
        return preferences.getString("gtn_trans_mode", "d"); // Retrieves the current default transportation mode setting.
    }

    /** SET PREFERENCES FUNCTIONALITY __________________________________________________________ **/

    // setCurrentAddress(): Sets the "gtn_current_address" value to preferences.
    public static void setCurrentAddress(String address, SharedPreferences preferences) {

        // Prepares the SharedPreferences object for editing.
        SharedPreferences.Editor prefEdit = preferences.edit();

        prefEdit.putString("gtn_current_address", address); // Sets the current address setting.
        prefEdit.apply(); // Applies the changes to SharedPreferences.
    }

    // setCurrentName(): Sets the "gtn_current_name" value to preferences.
    public static void setCurrentName(String name, SharedPreferences preferences) {

        // Prepares the SharedPreferences object for editing.
        SharedPreferences.Editor prefEdit = preferences.edit();

        prefEdit.putString("gtn_current_name", name); // Sets the current shortcut name setting.
        prefEdit.apply(); // Applies the changes to SharedPreferences.
    }

    // setCurrentTransportation(): Sets the "gtn_current_trans_mode" value to preferences.
    public static void setCurrentTransportation(String type, SharedPreferences preferences) {

        // Prepares the SharedPreferences object for editing.
        SharedPreferences.Editor prefEdit = preferences.edit();

        prefEdit.putString("gtn_current_trans_mode", type); // Sets the current transportation type setting.
        prefEdit.apply(); // Applies the changes to SharedPreferences.
    }

    // setInitialLaunch(): Sets the "gtn_initial_launch" value to preferences.
    public static void setInitialLaunch(Boolean isLaunched, SharedPreferences preferences) {

        // Prepares the SharedPreferences object for editing.
        SharedPreferences.Editor prefEdit = preferences.edit();

        prefEdit.putBoolean("gtn_initial_launch_a", isLaunched); // Sets the initial launch setting.
        prefEdit.apply(); // Applies the changes to SharedPreferences.
    }

    // setLanguage(): Sets the "gtn_language" value to preferences.
    public static void setLanguage(String language, SharedPreferences preferences) {

        // Prepares the SharedPreferences object for editing.
        SharedPreferences.Editor prefEdit = preferences.edit();

        prefEdit.putString("gtn_language", language); // Sets the language setting.
        prefEdit.apply(); // Applies the changes to SharedPreferences.
    }

    // setLastLocation(): Sets the last location value to preferences.
    public static void setLastLocation(String location, SharedPreferences preferences) {

        // Prepares the SharedPreferences object for editing.
        SharedPreferences.Editor prefEdit = preferences.edit();

        prefEdit.putString("gtn_last_location", location); // Sets the last location value.
        prefEdit.apply(); // Applies the changes to SharedPreferences.
    }

    // setOrientationChange(): Sets the "gtn_orientation_change" value to preferences.
    public static void setOrientationChange(Boolean isChange, SharedPreferences preferences) {

        // Prepares the SharedPreferences object for editing.
        SharedPreferences.Editor prefEdit = preferences.edit();

        prefEdit.putBoolean("gtn_orientation_change", isChange); // Sets the orientation change setting.
        prefEdit.apply(); // Applies the changes to SharedPreferences.
    }

    // setTransportationMode(): Sets the default transporation mode value to preferences.
    public static void setTransportationMode(String mode, SharedPreferences preferences) {

        // Prepares the SharedPreferences object for editing.
        SharedPreferences.Editor prefEdit = preferences.edit();

        prefEdit.putString("gtn_trans_mode", mode); // Sets the default transportation mode value.
        prefEdit.apply(); // Applies the changes to SharedPreferences.
    }
}