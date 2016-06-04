package com.huhx0015.gotherenow.interfaces;

import com.huhx0015.gotherenow.model.GTNShortcut;
import java.util.LinkedList;

/**
 * -------------------------------------------------------------------------------------------------
 * [OnFragmentUpdateListener] INTERFACE
 * PROGRAMMER: Michael Yoon Huh (Huh X0015)
 * DESCRIPTION: This is an interface class that is used as a signalling conduit between the
 * GTNMainActivity class and GTNActionFragment class.
 * -------------------------------------------------------------------------------------------------
 */

public interface OnFragmentUpdateListener {

    // closeFragmentView(): Signals attached activity to close the fragment view.
    void closeFragmentView(String type);

    // displaySnackbar(): Signals attached activity to display a Snackbar message.
    void displaySnackbar(String message);

    // updateShortcuts(): Signals attached activity to signal GTNShortcutFragment to update the
    // shortcuts list.
    void updateShortcuts(LinkedList<GTNShortcut> shortcuts);

    // updateToolbar(): Signals attached activity to update the toolbar title.
    void updateToolbar(String title);
}