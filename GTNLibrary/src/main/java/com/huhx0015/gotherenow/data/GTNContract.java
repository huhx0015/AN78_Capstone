package com.huhx0015.gotherenow.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Michael Yoon Huh on 5/26/2016.
 */
public class GTNContract {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    public static final String CONTENT_AUTHORITY = "com.huhx0015.gotherenow";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_SHORTCUT = "shortcut";

    /** SUBCLASSES _____________________________________________________________________________ **/

    public static final class GTNShortcutEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SHORTCUT).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_SHORTCUT;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_SHORTCUT;

        public static final String TABLE_NAME = "gtn_shortcuts"; // TABLE NAME
        public static final String COLUMN_ID = "_id"; // SHORTCUT ID
        public static final String COLUMN_NAME = "name"; // SHORTCUT NAME
        public static final String COLUMN_ADDRESS = "address"; // SHORTCUT ADDRESS
        public static final String COLUMN_TYPE = "type"; // SHORTCUT TYPE
        public static final String COLUMN_IMAGE_ID = "image_id"; // SHORTCUT IMAGE

        public static Uri buildShortcutUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
