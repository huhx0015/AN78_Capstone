package com.huhx0015.gotherenow.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.huhx0015.gotherenow.R;
import com.huhx0015.gotherenow.data.GTNContract;
import com.huhx0015.gotherenow.data.GTNCursorLoader;
import com.huhx0015.gotherenow.ui.resources.GTNTypes;

/** -----------------------------------------------------------------------------------------------
 *  [GTNWidgetRemoteViewsFactory] CLASS
 *  DEVELOPER: Michael Yoon Huh (HUHX0015)
 *  -----------------------------------------------------------------------------------------------
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class GTNWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // CLASS VARIABLES
    private Context context;
    private final static String LOG_TAG = GTNWidgetRemoteViewsFactory.class.getSimpleName();

    // DATABASE VARIABLES
    private Cursor shortcutCursor;
    public static final int COLUMN_NAME = 1;
    public static final int COLUMN_ADDRESS = 2;
    public static final int COLUMN_TYPE = 3;

    /** CONSTRUCTOR METHODS ____________________________________________________________________ **/

    public GTNWidgetRemoteViewsFactory(Context context) {
        this.context = context;
    }

    /** REMOTE VIEWS FACTORY METHODS ___________________________________________________________ **/

    @Override
    public void onCreate() {}

    @Override
    public void onDestroy() {
        if (shortcutCursor != null) { shortcutCursor.close(); }
    }

    @Override
    public void onDataSetChanged() {

        if (shortcutCursor != null) {
            shortcutCursor.close();
        }

        // Needed to be run in a separate thread or a permission error will occur.
        Thread thread = new Thread() {
            public void run() {
                shortcutCursor = retrieveShortcuts();
            }
        };

        thread.start(); // Starts the thread.

        try { thread.join(); }
        catch (InterruptedException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "onDataSetChanged(): An error occurred while attempting to fetch the matches.");
        }
    }

    @Override
    public int getCount() {
        if (shortcutCursor != null) { return shortcutCursor.getCount(); }
        else { return 0; }
    }

    @Override
    public long getItemId(int position) {
        if (shortcutCursor != null) {
            return shortcutCursor.getLong(shortcutCursor.getColumnIndex(GTNContract.GTNShortcutEntry.COLUMN_ID));
        }
        else { return 0; }
    }

    @Override
    public RemoteViews getLoadingView() {
        return new RemoteViews(context.getPackageName(), R.layout.gtn_shortcuts_widget_initial);
    }

    @Override
    public RemoteViews getViewAt(int position) {

        // Sets up the fill in intent.
        Intent fillInIntent = new Intent();
        RemoteViews shortcutsRemoteView = new RemoteViews(context.getPackageName(), R.layout.gtn_shortcuts_widget_list_item);

        // Retrieves the shortcut name, address, and type from the database and sets it to the
        // TextView objects in the widget RecyclerView.
        if (shortcutCursor.moveToPosition(position)) {
            String shortcutName = shortcutCursor.getString(COLUMN_NAME);
            String shortcutAddress = shortcutCursor.getString(COLUMN_ADDRESS);
            String shortcutType = shortcutCursor.getString(COLUMN_TYPE);
            shortcutsRemoteView = setRemoteView(shortcutsRemoteView, shortcutName, shortcutAddress, shortcutType);
        }

        shortcutsRemoteView.setOnClickFillInIntent(R.id.gtn_shortcuts_widget_list_item_layout, fillInIntent);
        return shortcutsRemoteView;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    /** LAYOUT METHODS _________________________________________________________________________ **/

    // setRemoteView(): Sets the string values for the TextViews in the remote view widget layout.
    private RemoteViews setRemoteView(RemoteViews remoteView, String name, String address, String type) {
        remoteView.setTextViewText(R.id.gtn_shortcut_widget_name, name);
        remoteView.setTextViewText(R.id.gtn_shortcut_widget_address, address);

        Log.d(LOG_TAG, "setRemoteView(): Shortcut Type: " + type);

        // Retrieves the image resource for the shortcut type.
        int imageRes = GTNTypes.getTypeImage(type);
        remoteView.setImageViewResource(R.id.gtn_shortcut_widget_image, imageRes);

        // Adds a content description if the device is running Android 4.0.3 or higher.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            remoteView.setContentDescription(R.id.gtn_shortcut_widget_image, context.getString(R.string.gtn_shortcuts_widget_transportation_type_icon));
        }

        return remoteView;
    }

    /** DATABASE METHODS _______________________________________________________________________ **/

    // retrieveShortcuts(): Retrieves the game match data from the database.
    private Cursor retrieveShortcuts() {
        return context.getContentResolver().query(GTNContract.GTNShortcutEntry.CONTENT_URI, GTNCursorLoader.Query.PROJECTION, null, null, null);
    }
}