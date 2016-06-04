package com.huhx0015.gotherenow.ui.adapters;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.huhx0015.gotherenow.data.GTNCursorLoader;
import com.huhx0015.gotherenow.data.GTNDatabaseUtil;
import com.huhx0015.gotherenow.interfaces.OnShortcutViewListener;
import com.huhx0015.gotherenow.model.GTNShortcut;
import com.huhx0015.gotherenow.R;
import com.huhx0015.gotherenow.ui.resources.GTNTypes;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

/** -----------------------------------------------------------------------------------------------
 *  [GTNRecyclerAdapter] CLASS
 *  PROGRAMMER: Michael Yoon Huh (Huh X0015)
 *  DESCRIPTION: GTNRecyclerAdapter is a RecyclerView adapter class that is used for setting up and
 *  loading GTNShortcut data into a RecyclerView list object.
 *  -----------------------------------------------------------------------------------------------
 */
public class GTNRecyclerAdapter extends RecyclerView.Adapter<GTNRecyclerAdapter.GTNRecyclerViewHolder> {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // ACTIVITY VARIABLES
    private Activity currentActivity; // References the attached activity.

    // CURSOR VARIABLES
    private Cursor recyclerAdapterCursor;

    // LAYOUT VARIABLES:
    private boolean isClickable = true; // Used to determine if the items are clickable or not.

    // LOGGING VARIABLES:
    private static final String LOG_TAG = GTNRecyclerAdapter.class.getSimpleName();

    /** INITIALIZATION METHODS _________________________________________________________________ **/

    // GTNRecyclerAdapter(): Constructor method for GTNRecyclerAdapter.
    public GTNRecyclerAdapter(Cursor cursor, boolean clickable, Activity act) {
        this.recyclerAdapterCursor = cursor;
        this.isClickable = clickable;
        this.currentActivity = act;
    }

    /** EXTENSION METHODS ______________________________________________________________________ **/

    // onCreateViewHolder: This method is called when the custom ViewHolder needs to be initialized.
    // The layout of each item of the RecyclerView is inflated using LayoutInflater, passing the
    // output to the constructor of the custom ViewHolder.
    @Override
    public GTNRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Inflates the layout given the XML layout file for the item view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gtn_card_view, parent, false);

        // Sets the action if the RecyclerView item property is set to be clickable.
        if (isClickable) {

            // Sets the view holder for the item view. This is needed to handle the individual item
            // clicks.
            final GTNRecyclerViewHolder viewHolder = new GTNRecyclerViewHolder(view, new GTNRecyclerViewHolder.OnRecyclerViewHolderClick() {

                // onItemClick(): Defines an action to take when the item in the list is clicked.
                @Override
                public void onItemClick(View caller, int position) {

                    // Displays the GTNActionFragment view in new shortcut mode.
                    getItemId(position);
                    navigateShortcut(GTNDatabaseUtil.cursorToShortcut(recyclerAdapterCursor));
                }
            });

            return viewHolder;
        }

        return new GTNRecyclerViewHolder(view, null);
    }

    // onBindViewHolder(): Overrides the onBindViewHolder to specify the contents of each item of
    // the RecyclerView. This method is similar to the getView method of a ListView's adapter.
    @Override
    public void onBindViewHolder(final GTNRecyclerViewHolder holder, int position) {

        // Moves the cursor to the item position.
        final int holderPosition = position;
        recyclerAdapterCursor.moveToPosition(holderPosition);

        // EDIT BUTTON: Sets an on click listener
        holder.shortcutEditImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Displays the GTNActionFragment in edit shortcut mode.
                getItemId(holderPosition);
                editShortcut(GTNDatabaseUtil.cursorToShortcut(recyclerAdapterCursor));
            }
        });

        // Sets the shortcut name and address for the shortcut row item.
        holder.shortcutNameText.setText(recyclerAdapterCursor.getString(GTNCursorLoader.Query.NAME));
        holder.shortcutAddressText.setText(recyclerAdapterCursor.getString(GTNCursorLoader.Query.ADDRESS));

        // Sets the shadow layer effect for the TextView objects.
        holder.shortcutNameText.setShadowLayer(2, 1, 1, Color.BLACK);
        holder.shortcutAddressText.setShadowLayer(2, 1, 1, Color.DKGRAY);

        // Retrieves the image resource for the shortcut type.
        int imageRes = GTNTypes.getTypeImage(recyclerAdapterCursor.getString(GTNCursorLoader.Query.TYPE));

        // Sets up the rounded shape attributes for the ImageView object.
        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.LTGRAY)
                .borderWidthDp(1)
                .cornerRadiusDp(90)
                .oval(false)
                .build();

        // Loads the type image into the ImageView object.
        Picasso.with(currentActivity)
                .load(imageRes)
                .transform(transformation)
                .into(holder.shortcutImage);
    }

    // getItemId(): Returns the item ID value.
    @Override
    public long getItemId(int position) {
        recyclerAdapterCursor.moveToPosition(position);
        return recyclerAdapterCursor.getLong(GTNCursorLoader.Query._ID);
    }

    // getItemCount(): Returns the number of items present in the data.
    @Override
    public int getItemCount() {
        if (recyclerAdapterCursor != null) {
            return recyclerAdapterCursor.getCount();
        } else {
            return 0;
        }
    }

    /** INTERFACE METHODS ______________________________________________________________________ **/

    // editShortcut(): Signals the attached activity to display the GTNActionFragment view.
    private void editShortcut(GTNShortcut shortcut) {
        try { ((OnShortcutViewListener) currentActivity).editExistingShortcut(shortcut, true); }
        catch (ClassCastException cce) {} // Catch for class cast exception errors.
    }

    // navigateShortcut(): Signals the attached activity to launch navigation mode.
    private void navigateShortcut(GTNShortcut shortcut) {
        try { ((OnShortcutViewListener) currentActivity).launchNavigation(shortcut); }
        catch (ClassCastException cce) { } // Catch for class cast exception errors.
    }

    /** SUBCLASSES _____________________________________________________________________________ **/

    /**
     * --------------------------------------------------------------------------------------------
     * [GTNRecyclerViewHolder] CLASS
     * DESCRIPTION: This subclass is responsible for referencing the view for an item in the
     * RecyclerView list view object.
     * --------------------------------------------------------------------------------------------
     */
    public static class GTNRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /** SUBCLASS VARIABLES _________________________________________________________________ **/

        // LAYOUT VARIABLES:
        CardView shortcutCardView;
        ImageButton shortcutEditImage;
        ImageView shortcutImage;
        TextView shortcutNameText;
        TextView shortcutAddressText;

        // LISTENER VARIABLES
        public OnRecyclerViewHolderClick recyclerItemListener; // Interface on-click listener variable.

        /** SUBCLASS METHODS ___________________________________________________________________ **/

        GTNRecyclerViewHolder(View itemView, OnRecyclerViewHolderClick listener) {

            super(itemView);

            // Sets the references for the View objects in the adapter layout.
            shortcutCardView = (CardView) itemView.findViewById(R.id.gtn_recycler_view_cardview_container);
            shortcutEditImage = (ImageButton) itemView.findViewById(R.id.gtn_edit_button);
            shortcutImage = (ImageView) itemView.findViewById(R.id.gtn_shortcut_image);
            shortcutNameText = (TextView) itemView.findViewById(R.id.gtn_shortcut_name);
            shortcutAddressText = (TextView) itemView.findViewById(R.id.gtn_shortcut_address);

            // Sets the listener for the item view.
            if (listener != null) {
                recyclerItemListener = listener; // Sets the OnRecyclerViewHolderClick listener.
                itemView.setOnClickListener(this);
            }
        }

        // onClick(): Defines an action to take when an item is clicked.
        @Override
        public void onClick(View v) {

            int itemPos = getAdapterPosition(); // Retrieves the clicked item position.
            recyclerItemListener.onItemClick(v, itemPos); // Sets the item listener.
        }

        /** INTERFACE METHODS __________________________________________________________________ **/

        /**
         * -----------------------------------------------------------------------------------------
         * [OnRecyclerViewHolderClick] INTERFACE
         * DESCRIPTION: This is an interface subclass that is used to provide methods to call when
         * the RecyclerView items are clicked.
         * -----------------------------------------------------------------------------------------
         */
        public interface OnRecyclerViewHolderClick {

            // onItemClick(): The method that is called when an item in the RecyclerView is clicked.
            void onItemClick(View caller, int position);
        }
    }
}