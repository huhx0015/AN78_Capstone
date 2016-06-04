package com.huhx0015.gotherenow.graphics;

import android.content.Context;
import android.util.DisplayMetrics;

/** -----------------------------------------------------------------------------------------------
 *  [GTNImages] CLASS
 *  PROGRAMMER: Huh X0015
 *  DESCRIPTION: GTNImages class is used to provide advanced image decoding and loading functionality
 *  for all activity classes.
 *  -----------------------------------------------------------------------------------------------
 */

public class GTNImages {

    /** IMAGE FUNCTIONALITY ____________________________________________________________________ **/

    // densityToPixels(): Converts density values to pixel values.
    public static int densityToPixels(int dp, Context con) {
        DisplayMetrics displayMetrics = con.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
}