package com.huhx0015.gotherenow.intents;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.huhx0015.gotherenow.ui.resources.GTNTypes;

/** -----------------------------------------------------------------------------------------------
 *  [GTNIntent] CLASS
 *  PROGRAMMER: Huh X0015
 *  DESCRIPTION: GTNIntent class is used to provide functions which launches an Intent to create
 *  a shortcut onto the device homescreen.
 *  -----------------------------------------------------------------------------------------------
 */

public class GTNShortcutIntent {

    /** INTENT FUNCTIONALITY ___________________________________________________________________ **/

    // createHomescreenShortcut(): This method is responsible for setting up an Intent to create
    // a shortcut on the device's homescreen.
    public static void createHomescreenShortcut(String name, String location, String type, int iconResource, Activity activity) {

        // Holds the URL request string that is processed by Google Maps.
        String url = GTNTypes.createURL(location, type);

        // Sets up the intent to create a shortcut on the homescreen.
        Intent shortcutIntent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
        shortcutIntent.setAction(Intent.ACTION_VIEW);

        // Adds the address in the customized intent for Google Maps.
        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);

        // Sets the icon resource for the shortcut.
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(activity,
                iconResource));

        // Installs the shortcut on the homescreen.
        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        activity.sendBroadcast(addIntent);
    }
}