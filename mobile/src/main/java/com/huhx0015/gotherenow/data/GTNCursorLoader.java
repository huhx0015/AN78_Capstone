package com.huhx0015.gotherenow.data;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.CursorLoader;

/** -----------------------------------------------------------------------------------------------
 *  [GTNCursorLoader] CLASS
 *  DESCRIPTION: Helper for loading a list of articles or a single article.
 *  -----------------------------------------------------------------------------------------------
 */
public class GTNCursorLoader extends CursorLoader {

    /** CONSTRUCTOR METHODS ____________________________________________________________________ **/

    // GTNCursorLoader(): Constructor for the GTNCursorLoader class.
    public GTNCursorLoader(Context context, Uri uri) {
        super(context, uri, Query.PROJECTION, null, null, GTNContract.GTNShortcutEntry.COLUMN_ID);
    }

    // newAllShortcutsInstance(): Creates a new instance of all shortcut items.
    public static GTNCursorLoader newAllShortcutsInstance(Context context) {
        return new GTNCursorLoader(context, GTNContract.GTNShortcutEntry.CONTENT_URI);
    }

    // newInstanceForItemId(): Creates a new instance of a single shortcut item.
    public static GTNCursorLoader newInstanceForItemId(Context context, long itemId) {
        return new GTNCursorLoader(context, GTNContract.GTNShortcutEntry.buildShortcutUri(itemId));
    }

    /** INTERFACE METHODS ______________________________________________________________________ **/

    // Query: Interface class for querying the database item.
    public interface Query {
        String[] PROJECTION = {
                GTNContract.GTNShortcutEntry.COLUMN_ID,
                GTNContract.GTNShortcutEntry.COLUMN_NAME,
                GTNContract.GTNShortcutEntry.COLUMN_ADDRESS,
                GTNContract.GTNShortcutEntry.COLUMN_TYPE,
                GTNContract.GTNShortcutEntry.COLUMN_IMAGE_ID,
        };

        int _ID = 0;
        int NAME = 1;
        int ADDRESS = 2;
        int TYPE = 3;
        int IMAGE_ID = 4;
    }
}