package com.huhx0015.gotherenow.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.widget.RemoteViews;
import com.huhx0015.gotherenow.R;
import com.huhx0015.gotherenow.activities.GTNMainActivity;

/** -----------------------------------------------------------------------------------------------
 *  [GTNWidgetProvider] CLASS
 *  DEVELOPER: Michael Yoon Huh (HUHX0015)
 *  -----------------------------------------------------------------------------------------------
 */

public class GTNWidgetProvider extends AppWidgetProvider {

    /** WIDGET METHODS _________________________________________________________________________ **/

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        // Loops through the array of widget IDs for the matching widget ID.
        for (int widgetId : appWidgetIds) {

            // Sets the layout for the widget.
            RemoteViews shortcutsRemoveView = new RemoteViews(context.getPackageName(), R.layout.gtn_shortcuts_widget_list);

            // Sets the service intent.
            Intent shortcutWidgetServiceIntent = new Intent(context, GTNWidgetService.class);
            shortcutWidgetServiceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
            shortcutWidgetServiceIntent.setData(Uri.parse(shortcutWidgetServiceIntent.toUri(Intent.URI_INTENT_SCHEME)));
            shortcutsRemoveView.setRemoteAdapter(R.id.gtn_shortcuts_widget_list, shortcutWidgetServiceIntent);

            // Sets the widget intent.
            Intent shortcutWidgetIntent = new Intent(context, GTNMainActivity.class);
            shortcutWidgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, shortcutWidgetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            shortcutsRemoveView.setPendingIntentTemplate(R.id.gtn_shortcuts_widget_list, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, shortcutsRemoveView);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}