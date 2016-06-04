package com.huhx0015.gotherenow.intents;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.huhx0015.gotherenow.ui.resources.GTNTypes;
import com.huhx0015.gotherenow.ui.toast.GTNToast;

/**
 * Created by Michael Yoon Huh on 10/10/2015.
 */
public class GTNMapsIntent {

    // getMapIntent(): This method sets up an Intent to launch Google Maps in Navigation mode.
    public static Intent getMapIntent(String address, String type, boolean showToast, Context context) {

        String navigatorName = "GOOGLE_MAPS"; // References the name of the default navigator app.
        String packageName, activityName, url; // References the Intent package, class, and url data.

        // Displays a Toast message, notifying the user that navigation mode is being launched.
        if (showToast) { GTNToast.toastyPopUp("Launching Navigation Mode:\n" + address, context); }

        // Sets the package name, activity name, and the properly formatted URL, based on the
        // default navigator application.
        // NOKIA HERE:
        if (navigatorName.equals("NOKIA_HERE")) {
            packageName = "com.here.app.maps";
            activityName = null;
            url = "geo:?q=" + address;
        }

        // WAZE:
        else if (navigatorName.equals("WAZE")) {
            packageName = "com.waze";
            activityName = null;
            url = "geo:?q=" + address;
        }

        // BAIDU MAPS:
        // Android API Documentation: http://developer.baidu.com/map/index.php?title=uri/api/android
        else if (navigatorName.equals("BAIDU_MAPS")) {
            packageName = "com.baidu.BaiduMap.pad";
            activityName = null;
            url = "geo:?q=" + address;
        }

        // GOOGLE MAPS:
        else {
            packageName = "com.google.android.apps.maps";
            activityName = "com.google.android.maps.MapsActivity";
            url = GTNTypes.createURL(address, type);
        }

        // Sets up a the customized Google Maps intent directly in navigation mode.
        Intent mapIntent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));

        // If the activityName is not null, the both the package and class values are set.
        if (activityName != null) { mapIntent.setClassName(packageName, activityName); }

        // Only the package name is set.
        else { mapIntent.setPackage(packageName); }

        return mapIntent;
    }
}