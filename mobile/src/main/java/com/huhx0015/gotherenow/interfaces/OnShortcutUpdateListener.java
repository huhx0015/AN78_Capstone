package com.huhx0015.gotherenow.interfaces;

import android.location.Location;

/**
 * -------------------------------------------------------------------------------------------------
 * [OnShortcutUpdateListener] INTERFACE
 * PROGRAMMER: Michael Yoon Huh (Huh X0015)
 * DESCRIPTION: This is an interface class that is used as a signalling conduit between the
 * GTNMainActivity class and GTNListAdapter & GTNShortcutFragment classes.
 * -------------------------------------------------------------------------------------------------
 */

public interface OnShortcutUpdateListener {

    // updateLocation(): An interface method that is invoked when the Location object is updated.
    void updateLocation(Location location);

    // updateShortcutLanguage(): Signals the GTNShortcutFragment class to update the language view.
    void updateShortcutLanguage();

    // updateShortcutView(): Signals the GTNShortcutFragment class to update the shortcut ListView.
    void updateShortcutView();

    // updateVoiceInput(): Signals the GTNShortcutFragment class to update the QUICK NAVIGATION
    // EditText input line with the voice input value.
    void updateVoiceInput(String voiceInput);
}
