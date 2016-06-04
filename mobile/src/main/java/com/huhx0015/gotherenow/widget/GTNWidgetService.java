package com.huhx0015.gotherenow.widget;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViewsService;

/** -----------------------------------------------------------------------------------------------
 *  [GTNWidgetService] CLASS
 *  DEVELOPER: Michael Yoon Huh (HUHX0015)
 *  -----------------------------------------------------------------------------------------------
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class GTNWidgetService extends RemoteViewsService {

    /** SERVICE METHODS ________________________________________________________________________ **/

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GTNWidgetRemoteViewsFactory(getApplicationContext());
    }
}