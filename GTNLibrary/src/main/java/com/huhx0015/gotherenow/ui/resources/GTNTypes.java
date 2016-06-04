package com.huhx0015.gotherenow.ui.resources;

import com.huhx0015.gotherenow.library.R;

/** -----------------------------------------------------------------------------------------------
 *  [GTNToast] CLASS
 *  PROGRAMMER: Michael Yoon Huh (Huh X0015)
 *  DESCRIPTION: GTNToast contains functions that utilize the Toast message functionality.
 *  -----------------------------------------------------------------------------------------------
 */

public class GTNTypes {

    /** TYPE FUNCTIONALITY _____________________________________________________________________ **/

    // createURL(): Creates a String URL based on the shortcut type that is readable by Google Maps.
    public static String createURL(String address, String type) {

        String url; // Holds the URL request string that is processed by Google Maps.

        // TRANSIT TYPE:
        if (type.equals("t")) {
            url = "http://maps.google.com/maps?daddr=" + address + "&dirflg=r";
        }

        // DRIVING | BIKING | WALKING TYPES:
        else { url = "google.navigation:q=" + address + "&mode=" + type; }

        return url;
    }

    // checkShortcutType(): This function checks the type string for a valid navigation type. If the
    // the old type string version is found, it is automatically converted to a DRIVING mode type.
    public static String checkShortcutType(String type) {

        // Checks the type object to see if it is a valid navigation type. If not, the type value
        // is set to DRIVING mode type by default.
        if ( !(type.equals("d")) && !(type.equals("w")) && !(type.equals("b")) && !(type.equals("t")) ) {
            type = "d";
        }

        return type; // Returns the type String object.
    }

    // getTypeImage(): Returns the proper image resource ID value based on the type value.
    public static int getTypeImage(String type) {

        int typeRes; // Stores the image reference ID of the shortcut type.

        // DRIVING:
        if (type.equals("d")) { typeRes = R.drawable.gtn_car_icon; }

        // TRANSIT
        else if (type.equals("t")) { typeRes = R.drawable.gtn_transit_icon; }

        // BIKING:
        else if (type.equals("b")) { typeRes = R.drawable.gtn_bike_icon; }

        // WALKING:
        else if (type.equals("w")) { typeRes = R.drawable.gtn_walk_icon; }

        // DEFAULT:
        else { typeRes = R.drawable.gtn_icon; }

        return typeRes;
    }

    // getSelectedType(): Returns the selected type Integer value based on the type String value.
    public static int getSelectedType(String type) {

        int typeValue = 0;

        // DRIVING:
        if (type.equals("d")) { typeValue = 0; }

        // TRANSIT
        else if (type.equals("t")) { typeValue = 1; }

        // BIKING:
        else if (type.equals("b")) { typeValue = 2; }

        // WALKING:
        else if (type.equals("w")) { typeValue = 3; }

        return typeValue;
    }

    // getSelectedType(): Returns the selected type String value based on the type Integer value.
    public static String getSelectedType(int type) {

        String typeValue; // Stores the shortcut type String value.

        switch (type) {

            // DRIVING:
            case 0:
                typeValue = "d";
                break;

            // TRANSIT:
            case 1:
                typeValue = "t";
                break;

            // BIKING:
            case 2:
                typeValue = "b";
                break;

            // WALKING:
            case 3:
                typeValue = "w";
                break;

            // DEFAULT: Defaults to DRIVING value.
            default:
                typeValue = "d";
                break;
        }

        return typeValue;
    }
}
