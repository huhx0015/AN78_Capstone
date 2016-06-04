package com.huhx0015.gotherenow.ui.toast;

import android.content.Context;
import android.widget.Toast;

/** -----------------------------------------------------------------------------------------------
 *  [GTNToast] CLASS
 *  PROGRAMMER: Michael Yoon Huh (Huh X0015)
 *  DESCRIPTION: GTNToast contains functions that utilize the Toast message functionality.
 *  -----------------------------------------------------------------------------------------------
 */

public class GTNToast {

    /** TOAST FUNCTIONALITY ____________________________________________________________________ **/

    // toastyPopUp(): Creates and displays a Toast popup.
    public static void toastyPopUp(String message, Context con) {
        Toast.makeText(con, message, Toast.LENGTH_SHORT).show();
    }
}
