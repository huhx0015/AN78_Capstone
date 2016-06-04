package com.huhx0015.gotherenow.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.huhx0015.gotherenow.graphics.GTNImages;
import com.huhx0015.gotherenow.interfaces.OnFragmentUpdateListener;
import com.huhx0015.gotherenow.R;
import com.huhx0015.gotherenow.preferences.GTNPreference;
import com.huhx0015.gotherenow.ui.language.GTNLanguage;
import com.huhx0015.gotherenow.ui.resources.GTNTypes;
import com.squareup.picasso.Picasso;
import butterknife.Bind;
import butterknife.ButterKnife;

/** -----------------------------------------------------------------------------------------------
 *  [GTNSettingsFragment] CLASS
 *  PROGRAMMER: Huh X0015
 *  DESCRIPTION: GTNSettingsFragment class is a Fragment class that is used for displaying the
 *  settings fragment view.
 *  -----------------------------------------------------------------------------------------------
 */
public class GTNSettingsFragment extends Fragment {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // ACTIVITY VARIABLES
    private Activity currentActivity; // Used to attach activity to this fragment.

    // CLASS VARIABLES
    private GTNLanguage gtn_languages; // An object that is used for language-related functionality.

    // LAYOUT VARIABLES
    private int currentTransType = 0; // Used to determine the current selected shortcut type.

    // PREFERENCES VARIABLES
    private SharedPreferences GTN_prefs; // SharedPreferences objects that store settings for the application.
    private String language; // Stores the String value of the language currently used by the application.
    private static final String GTN_OPTIONS = "gtn_options"; // Used to reference the name of the preference XML file.

    // SYSTEM VARIABLES
    private final int api_level = android.os.Build.VERSION.SDK_INT; // Used to determine the device's Android API version.

    // VIEW INJECTION VARIABLES
    @Bind(R.id.gtn_settings_transportation_driving_button) ImageButton drivingButton; // References the DEFAULT TRANSPORTATION driving button.
    @Bind(R.id.gtn_settings_transportation_transit_button) ImageButton transitButton; // References the DEFAULT TRANSPORTATION transit button.
    @Bind(R.id.gtn_settings_transportation_biking_button) ImageButton bikingButton; // References the DEFAULT TRANSPORTATION biking button.
    @Bind(R.id.gtn_settings_transportation_walking_button) ImageButton walkingButton; // References the DEFAULT TRANSPORTATION walking button.
    @Bind(R.id.gtn_settings_transportation_category) TextView transportationCategoryText; // References the transportation category TextView object.

    /** FRAGMENT LIFECYCLE FUNCTIONALITY _______________________________________________________ **/

    // onAttach(): The initial function that is called when the Fragment is run. The activity is
    // attached to the fragment.
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.currentActivity = getActivity(); // Sets the currentActivity to attached activity object.
    }

    // onCreateView(): Creates and returns the view hierarchy associated with the fragment.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Sets the view to the specified XML layout file.
        View gtn_settings_view = (ViewGroup) inflater.inflate(R.layout.gtn_settings_fragment, container, false);
        ButterKnife.bind(this, gtn_settings_view); // ButterKnife view injection initialization.
        setUpLayout(); // Sets up the layout for the fragment.

        return gtn_settings_view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    /** LAYOUT FUNCTIONALITY ___________________________________________________________________ **/

    // setUpLayout(): Sets up the layout for the fragment.
    private void setUpLayout() {
        setUpButtons(); // Sets up the listeners for the buttons in the fragment.
        setUpLanguage(); // Sets the current language properties for the fragment.
        setTransType(currentTransType); // Sets the current selected transportation type.
    }

    // setUpButtons(): Sets up the listeners for the button objects in the layout.
    private void setUpButtons() {

        // Calculates the size based on the device's screen density.
        int size = GTNImages.densityToPixels(36, currentActivity);

        // TRANSPORTATION TYPE BUTTON IMAGES:
        // -----------------------------------------------------------------------------------------
        // DRIVING Button: Sets the driving icon for the button.
        Picasso.with(currentActivity)
                .load(R.drawable.gtn_car_icon)
                .resize(size, size)
                .centerCrop()
                .into(drivingButton);

        // TRANSIT Button: Sets the transit icon for the button.
        Picasso.with(currentActivity)
                .load(R.drawable.gtn_transit_icon)
                .resize(size, size)
                .centerCrop()
                .into(transitButton);

        // BIKING Button: Sets the biking icon for the button.
        Picasso.with(currentActivity)
                .load(R.drawable.gtn_bike_icon)
                .resize(size, size)
                .centerCrop()
                .into(bikingButton);

        // WALKING Button: Sets the walking icon for the button.
        Picasso.with(currentActivity)
                .load(R.drawable.gtn_walk_icon)
                .resize(size, size)
                .centerCrop()
                .into(walkingButton);

        // SHORTCUT TYPE BUTTONS:
        // -----------------------------------------------------------------------------------------
        // DRIVING Button:
        drivingButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setTransType(0);
            }
        });

        drivingButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                setTransType(0);
            }
        });

        // TRANSIT Button:
        transitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setTransType(1);
            }
        });

        transitButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                setTransType(1);
            }
        });

        // BIKING Button:
        bikingButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setTransType(2);
            }
        });

        bikingButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                setTransType(2);
            }
        });

        // WALKING Button:
        walkingButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setTransType(3);
            }
        });

        walkingButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                setTransType(3);
            }
        });
    }

    // setTransType(): Sets the transportation type and highlights the button background.
    private void setTransType(int type) {

        switch (type) {

            // DRIVING:
            case 0:

                currentTransType = 0; // Sets the current type value.

                // Sets the appropriate button background colors.
                drivingButton.setBackgroundColor(currentActivity.getResources().getColor(R.color.gtn_edit_text_color));
                transitButton.setBackgroundColor(currentActivity.getResources().getColor(android.R.color.transparent));
                bikingButton.setBackgroundColor(currentActivity.getResources().getColor(android.R.color.transparent));
                walkingButton.setBackgroundColor(currentActivity.getResources().getColor(android.R.color.transparent));
                break;

            // TRANSIT:
            case 1:

                currentTransType = 1; // Sets the current type value.

                // Sets the appropriate button background colors.
                drivingButton.setBackgroundColor(currentActivity.getResources().getColor(android.R.color.transparent));
                transitButton.setBackgroundColor(currentActivity.getResources().getColor(R.color.gtn_edit_text_color));
                bikingButton.setBackgroundColor(currentActivity.getResources().getColor(android.R.color.transparent));
                walkingButton.setBackgroundColor(currentActivity.getResources().getColor(android.R.color.transparent));
                break;

            // BIKING:
            case 2:

                currentTransType = 2; // Sets the current type value.

                // Sets the appropriate button background colors.
                drivingButton.setBackgroundColor(currentActivity.getResources().getColor(android.R.color.transparent));
                transitButton.setBackgroundColor(currentActivity.getResources().getColor(android.R.color.transparent));
                bikingButton.setBackgroundColor(currentActivity.getResources().getColor(R.color.gtn_edit_text_color));
                walkingButton.setBackgroundColor(currentActivity.getResources().getColor(android.R.color.transparent));
                break;

            // WALKING:
            case 3:

                currentTransType = 3; // Sets the current type value.

                // Sets the appropriate button background colors.
                drivingButton.setBackgroundColor(currentActivity.getResources().getColor(android.R.color.transparent));
                transitButton.setBackgroundColor(currentActivity.getResources().getColor(android.R.color.transparent));
                bikingButton.setBackgroundColor(currentActivity.getResources().getColor(android.R.color.transparent));
                walkingButton.setBackgroundColor(currentActivity.getResources().getColor(R.color.gtn_edit_text_color));
                break;

            // DEFAULT: Sets default to DRIVING type.
            default:

                currentTransType = 0; // Sets the current type value.

                // Sets the background color to white.
                drivingButton.setBackgroundColor(currentActivity.getResources().getColor(R.color.gtn_edit_text_color));
                transitButton.setBackgroundColor(currentActivity.getResources().getColor(android.R.color.transparent));
                bikingButton.setBackgroundColor(currentActivity.getResources().getColor(android.R.color.transparent));
                walkingButton.setBackgroundColor(currentActivity.getResources().getColor(android.R.color.transparent));
                break;
        }

        // Sets the default transportation mode into SharedPreferences.
        GTNPreference.setTransportationMode(GTNTypes.getSelectedType(currentTransType), GTN_prefs);
    }

    // setUpLanguage(): Sets the current language attributes onto the TextView objects.
    private void setUpLanguage() {

        loadPreferences(); // Retrieves the updated language settings.

        // Initializes the language settings.
        gtn_languages = new GTNLanguage();
        gtn_languages.initializeLanguages(language, getContext());

        // Sets the text for the TextView objects.
        transportationCategoryText.setText(gtn_languages.getTransportationTitleText());

        // Signals the attached activity to update the Toolbar title name for the current
        // language setting.
        updateToolbarName(gtn_languages.getSettingsText());
    }

    /** ADDITIONAL FUNCTIONALITY _______________________________________________________________ **/

    // loadPreferences(): Loads the SharedPreference values from the stored SharedPreferences object.
    private void loadPreferences() {

        // Initializes the SharedPreferences object.
        GTN_prefs = GTNPreference.initializePreferences(GTN_OPTIONS, currentActivity);
        language = GTNPreference.getLanguage(GTN_prefs); // Retrieves the current language setting.

        // Retrieves the default transportation mode from SharedPreferences and sets it to the
        // current transportation type.
        currentTransType = GTNTypes.getSelectedType(GTNPreference.getTransportationMode(GTN_prefs));
    }

    /** INTERFACE FUNCTIONALITY ________________________________________________________________ **/

    // updateToolbarName(): An interface method that signals the attached activity to update the
    // Toolbar title name.
    private void updateToolbarName(String name) {
        try { ((OnFragmentUpdateListener) currentActivity).updateToolbar(name); }
        catch (ClassCastException cce) { } // Catch for class cast exception errors.
    }
}