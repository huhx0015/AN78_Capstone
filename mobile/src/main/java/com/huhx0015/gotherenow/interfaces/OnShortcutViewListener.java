package com.huhx0015.gotherenow.interfaces;


import com.huhx0015.gotherenow.model.GTNShortcut;

/**
 * -------------------------------------------------------------------------------------------------
 * [OnShortcutViewListener] INTERFACE
 * PROGRAMMER: Michael Yoon Huh (Huh X0015)
 * DESCRIPTION: This is an interface class that is used as a signalling conduit between the
 * GTNMainActivity class and GTNListAdapter & GTNShortcutFragment classes.
 * -------------------------------------------------------------------------------------------------
 */

public interface OnShortcutViewListener {

    // editExistingShortcut(): Signals the attached activity to launch GTNActionFragment in edit
    // shortcut mode with the selected GTNShortcut object.
    void editExistingShortcut(GTNShortcut shortcut, Boolean isAnimated);

    // getMapIntent(): Signals the attached activity to launch Google Maps in Navigation Mode
    // with the selected GTNShortcut object.
    void launchNavigation(GTNShortcut shortcut);

    // openActionView(): Signals the attached activity to display the GTNActionFragment view.
    void openActionView(String address, Boolean isAnimated);
}