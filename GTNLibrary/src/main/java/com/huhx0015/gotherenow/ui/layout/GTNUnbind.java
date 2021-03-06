package com.huhx0015.gotherenow.ui.layout;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

/** -----------------------------------------------------------------------------------------------
 *  [GTNUnbind] CLASS
 *  PROGRAMMER: Huh X0015
 *  DESCRIPTION: GTNUnbind class is a class that contains methods which unbind View groups that are
 *  no longer needed by activities.
 *  -----------------------------------------------------------------------------------------------
 */

public class GTNUnbind {

    /** RECYCLE FUNCTIONALITY __________________________________________________________________ **/

    // unbindView(): Unbinds all Drawable objects attached to the view layout by setting them
    // to null, freeing up memory resources and preventing Context-related memory leaks. This code
    // is borrowed from Roman Guy at www.curious-creature.org.
    public static void unbindView(View view) {

        // If the View object's background is not null, a Callback is set to render them null.
        if (view.getBackground() != null) { view.getBackground().setCallback(null); }

        if (view instanceof ViewGroup && !(view instanceof AdapterView)) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindView(((ViewGroup) view).getChildAt(i));
            }

            ((ViewGroup) view).removeAllViews(); // Removes all View objects in the ViewGroup.
        }
    }
}