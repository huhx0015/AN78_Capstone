package com.huhx0015.gotherenow.fragments;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import com.huhx0015.gotherenow.data.GTNDatabaseAccessor;
import com.huhx0015.gotherenow.data.GTNDatabaseUtil;
import com.huhx0015.gotherenow.graphics.GTNImages;
import com.huhx0015.gotherenow.intents.GTNShortcutIntent;
import com.huhx0015.gotherenow.intents.GTNMapsIntent;
import com.huhx0015.gotherenow.interfaces.OnFragmentUpdateListener;
import com.huhx0015.gotherenow.R;
import com.huhx0015.gotherenow.model.GTNShortcut;
import com.huhx0015.gotherenow.preferences.GTNPreference;
import com.huhx0015.gotherenow.ui.input.GTNConditions;
import com.huhx0015.gotherenow.ui.language.GTNLanguage;
import com.huhx0015.gotherenow.ui.resources.GTNTypes;
import com.huhx0015.gotherenow.widget.GTNWidgetProvider;
import com.squareup.picasso.Picasso;
import java.util.LinkedList;
import butterknife.Bind;
import butterknife.ButterKnife;

/** -----------------------------------------------------------------------------------------------
 *  [GTNActionFragment] CLASS
 *  PROGRAMMER: Huh X0015
 *  DESCRIPTION: GTNActionFragment class is a Fragment class that is used for displaying the
 *  action fragment view.
 *  -----------------------------------------------------------------------------------------------
 */
public class GTNActionFragment extends Fragment {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // LANGUAGE VARIABLES
    private GTNLanguage languageSettings; // Stores the language String values used by the layout.
    private String language; // Stores the String value of the language currently used by the application.

    // SHORTCUT VARIABLES
    private boolean editShortcutMode = false; // Used to determine whether NEW or EDIT shortcut mode is enabled.
    private GTNShortcut currentShortcut; // Used to reference the selected shortcut while editing existing shortcut data.

    // LAYOUT VARIABLES
    private int currentShortcutType = 0; // Used to determine the current selected shortcut type.
    private String currentAddress; // Used for storing the current address input.

    // LOGGING VARIABLES
    private static final String LOG_TAG = GTNActionFragment.class.getSimpleName();

    // SYSTEM VARIABLES
    private Activity currentActivity; // Used to attach activity to this fragment.
    private SharedPreferences GTN_temps; // Temporary SharedPreferences objects that store settings for the application.
    private static final String GTN_OPTIONS = "gtn_options"; // Used to reference the name of the preference XML file.
    private static final String GTN_TEMPS = "gtn_temps"; // Used to reference the name of the temporary preference XML file.

    // VIEW INJECTION VARIABLES
    @Bind(R.id.save_location_button) Button saveLocationButton;
    @Bind(R.id.create_shortcut_button) Button createShortcutButton;
    @Bind(R.id.navigation_button) Button navigationButton;
    @Bind(R.id.delete_button) Button deleteButton;
    @Bind(R.id.gtn_shortcut_input) EditText shortcutInput;
    @Bind(R.id.gtn_address_input) EditText addressInput;
    @Bind(R.id.gtn_delete_container) LinearLayout deleteContainer;
    @Bind(R.id.gtn_action_type_driving_button) ImageButton drivingButton;
    @Bind(R.id.gtn_action_type_transit_button) ImageButton transitButton;
    @Bind(R.id.gtn_action_type_biking_button) ImageButton bikingButton;
    @Bind(R.id.gtn_action_type_walking_button) ImageButton walkingButton;

    /** INITIALIZATION FUNCTIONALITY ___________________________________________________________ **/

    // GTNAcionFragment(): Constructor for the GTNActionFragment fragment class.
    private final static GTNActionFragment action_fragment = new GTNActionFragment();

    // GTNActionFragment(): Deconstructor for the GTNActionFragment fragment class.
    public GTNActionFragment() { }

    // getInstance(): Returns the action_fragment instance.
    public static GTNActionFragment getInstance() {
        return action_fragment;
    }

    // initializeEditFragment(): Initializes the fragment in MODIFY SHORTCUT mode.
    public void initializeEditFragment(GTNShortcut shortcut) {
        editShortcutMode = true; // Indicates that MODIFY SHORTCUT mode is enabled.
        currentShortcut = shortcut; // Sets the current GTNShortcut object.
    }

    // initializeNewFragment(): Initializes the fragment in NEW SHORTCUT mode.
    public void initializeNewFragment(String address) {

        editShortcutMode = false; // Indicates that MODIFY SHORTCUT mode is disabled.

        // Sets the current address to be the address found from the input in the QUICK NAVIGATION
        // input field in GTNShortcutsFragment.
        currentAddress = address;
    }

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

        // PREFERENCES: Retrieves all preference values for the application.
        loadPreferences();

        // Sets the view to the specified XML layout file.
        View gtn_action_view = (ViewGroup) inflater.inflate(R.layout.gtn_action_fragment, container, false);
        ButterKnife.bind(this, gtn_action_view); // ButterKnife view injection initialization.
        setUpLayout(); // Sets up the layout for the fragment.

        return gtn_action_view;
    }

    // onResume(): This function runs immediately after onCreate() finishes and is always re-run
    // whenever the activity is resumed from an onPause() state.
    @Override
    public void onResume() {
        super.onResume();

        // If a screen orientation change has occurred, the previously entered shortcut name,
        // address, and shortcut type are set.
        if (GTNPreference.getOrientationChange(GTN_temps)) {
            shortcutInput.setText(GTNPreference.getCurrentName(GTN_temps));
            addressInput.setText(GTNPreference.getCurrentAddress(GTN_temps));
            currentShortcutType = GTNTypes.getSelectedType(GTNPreference.getCurrentTransportation(GTN_temps));
            setShortcutType(currentShortcutType);
        }
    }

    // onPause(): This function is called whenever the current fragment is suspended.
    @Override
    public void onPause() {
        super.onPause();

        // Saves the current inputted shortcut name, address, and type into temporary
        // SharedPreferences, in preparation for a screen orientation change event.
        GTNPreference.setCurrentName(retrieveInputValue(shortcutInput), GTN_temps);
        GTNPreference.setCurrentAddress(retrieveInputValue(addressInput), GTN_temps);
        GTNPreference.setCurrentTransportation(GTNTypes.getSelectedType(currentShortcutType), GTN_temps);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    /** LAYOUT FUNCTIONALITY ___________________________________________________________________ **/

    // setUpLayout(): Sets up the layout for the fragment.
    private void setUpLayout() {

        // Calculates the size based on the device's screen density.
        int size = GTNImages.densityToPixels(36, currentActivity);

        // TYPE BUTTON IMAGES:
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

        // SHORTCUT MODE:
        // -----------------------------------------------------------------------------------------
        // MODIFY SHORTCUT MODE: Sets the shortcut name, address, and type objects with data from
        // the GTNShortcut object.
        if (editShortcutMode) {
            shortcutInput.setText(currentShortcut.getShortcutName());
            addressInput.setText(currentShortcut.getShortcutAddress());
            currentShortcutType = GTNTypes.getSelectedType(currentShortcut.getShortcutType());
            setShortcutType(currentShortcutType); // Highlights the shortcut type button.
        }

        // NEW SHORTCUT MODE:
        else {

            // If a address or location was entered in the QUICK NAVIGATION field in
            // GTNShortcutsFragment, that value is set as the address.
            if ( !(currentAddress == null) && !(currentAddress.isEmpty()) && !(currentAddress.equals("")) ) {
                addressInput.setText(currentAddress);
            }

            // Sets the shortcut type to default transportation type.
            setShortcutType(currentShortcutType);
        }

        setUpLanguage(); // Sets up the language text for the fragment.
        setUpButtons(); // Sets up the button listeners for the fragment.
    }

    // setUpButtons(): Sets up the button listeners for the fragment.
    private void setUpButtons() {

        // Hides the DELETE button if not in EDIT SHORTCUT mode.
        if (!editShortcutMode) { deleteContainer.setVisibility(View.GONE); }

        // SHORTCUT TYPE BUTTONS:
        // -----------------------------------------------------------------------------------------
        // DRIVING Button:
        drivingButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setShortcutType(0);
            }
        });

        // TRANSIT Button:
        transitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setShortcutType(1);
            }
        });

        // BIKING Button:
        bikingButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setShortcutType(2);
            }
        });

        // WALKING Button:
        walkingButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setShortcutType(3);
            }
        });

        // ACTION BUTTONS:
        // -----------------------------------------------------------------------------------------
        // CREATE SHORTCUT Button:
        createShortcutButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String shortcutName = retrieveInputValue(shortcutInput); // Used for storing the shortcut name input.
                String shortcutAddress = retrieveInputValue(addressInput); // Used for storing the address input.

                // If the shortcut name is empty, the shortcut name is set to the first line of the
                // address by default.
                if ( (shortcutName != null) && (shortcutAddress != null)) {
                    if (shortcutName.isEmpty()) { shortcutName = shortcutAddress.split("\n")[0]; }
                }

                // Determines the shortcut type based on the currently selected shortcut type button
                // value.
                String shortcutType = GTNTypes.getSelectedType(currentShortcutType);

                // Sets the proper shortcut type image based on the shortcutType value.
                int shortcutImage = GTNTypes.getTypeImage(shortcutType);

                // Checks the address input for valid length.
                Boolean isAddressValid = GTNConditions.checkForShortcutInput(shortcutAddress);

                // Creates a shortcut on the homescreen.
                if (isAddressValid) {

                    // Launches an background Intent to create a shortcut on the homescreen.
                    GTNShortcutIntent.createHomescreenShortcut(shortcutName, shortcutAddress, shortcutType, shortcutImage, currentActivity);

                    // Displays a snackbar message display at the bottom of the screen, indicating
                    // that the address shortcut has been added.
                    displaySnackbarMessage(languageSettings.getAddingShortcutText() + shortcutName);
                }

                // Displays a snackbar message display at the bottom of the screen, indicating that
                // the shortcut address input is invalid.
                else { displaySnackbarMessage(languageSettings.getAddressErrorText()); }
            }

        });

        // START NAVIGATION Button:
        navigationButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Checks to see if Google Maps is installed and if the address input is valid first.
                // If everything checks out, the Google Maps intent is launched directly in
                // navigation mode.
                if (GTNConditions.checkForAppInstalled(currentActivity)) {

                    // Launches the intent to Google Maps if the input is valid.
                    if (GTNConditions.checkForAddressInput(addressInput)) { launchIntent(); }

                    // Displays a Snackbar message, notifying the user that the address input is invalid.
                    else { displaySnackbarMessage(languageSettings.getAddressErrorText()); }
                }

                // Displays a Snackbar message, notifying the user that Google Maps needs to be
                // installed.
                else { displaySnackbarMessage(languageSettings.getGoogleMapsErrorText()); }
            }
        });

        // SAVE LOCATION Button:
        saveLocationButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String shortcutName = retrieveInputValue(shortcutInput); // Used for storing the shortcut name input.
                String shortcutAddress = retrieveInputValue(addressInput); // Used for storing the address input.

                // If the shortcut name is empty, the shortcut name is set to the first line of the
                // address by default.
                if ( (shortcutName != null) && (shortcutAddress != null)) {
                    if (shortcutName.isEmpty()) { shortcutName = shortcutAddress.split("\n")[0]; }
                }

                // Determines the shortcut type based on the currently selected shortcut type button
                // value.
                String shortcutType = GTNTypes.getSelectedType(currentShortcutType);

                // Sets the proper shortcut type image based on the shortcutType value.
                int shortcutImage = GTNTypes.getTypeImage(shortcutType);

                // Checks the address input for valid length.
                boolean isAddressValid = GTNConditions.checkForShortcutInput(shortcutAddress);

                // Opens the database to create a new shortcut.
                if (isAddressValid) {

                    // Initializes the GTNDatabaseUtil object and opens the database.
                    GTNDatabaseUtil datasource = new GTNDatabaseUtil(currentActivity);
                    datasource.open();

                    // Checks to see if the GTNActionFragment is in NEW SHORTCUT or EDIT SHORTCUT
                    // mode.
                    // EDIT SHORTCUT:
                    if (editShortcutMode) {

                        // Checks the shortcut type for legacy String types and corrects the
                        // type for the current String type.
                        shortcutType = GTNTypes.checkShortcutType(shortcutType);

                        // Modifies the existing shortcut with the updated parameters.
                        datasource.editShortcut(shortcutName, shortcutAddress, currentShortcut.getId(), shortcutImage, shortcutType);
                    }

                    // NEW SHORTCUT: Creates a new shortcut in the database.
                    else {
                        datasource.createShortcut(shortcutName, shortcutAddress, shortcutType, shortcutImage);
                    }

                    datasource.close(); // Closes the database.

                    // Signals attached activity to update shortcuts in the shortcut fragment and to
                    // synchronize Wear device.
                    updateShortcutView(GTNDatabaseAccessor.getAllShortcuts(currentActivity));

                    // Updates the view for the widget associated with this application.
                    updateWidget();

                    // Signals attached activity to display a Snackbar message, notifying the user
                    // that the location has been added.
                    displaySnackbarMessage(languageSettings.getSavingLocationText() + shortcutName);

                    // Signals to the attached activity to close this fragment view.
                    closeActionFragment();
                }

                // Displays a snackbar message display at the bottom of the screen, indicating that
                // the shortcut address input is invalid.
                else { displaySnackbarMessage(languageSettings.getAddressErrorText()); }
            }
        });

        // DELETE SHORTCUT Button:
        deleteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Initializes the GTNDatabaseUtil object and opens the database.
                GTNDatabaseUtil datasource = new GTNDatabaseUtil(currentActivity);
                datasource.open();

                // Deletes the shortcut entry from the database and closes the database.
                datasource.deleteShortcut(currentShortcut);
                datasource.close();

                // Signals attached activity to update shortcuts in the shortcut fragment and to synchronize Wear device.
                updateShortcutView(GTNDatabaseAccessor.getAllShortcuts(currentActivity));

                // Updates the view for the widget associated with this application.
                updateWidget();

                // Signals attached activity to display a snackbar message indicating that the
                // shortcut has been deleted.
                displaySnackbarMessage(languageSettings.getDeletingLocationText() + currentShortcut.getShortcutName());
                closeActionFragment(); // Signals to the attached activity to close this fragment view.
            }
        });
    }

    // setUpLanguage(): Sets the language text based on the current language settings.
    private void setUpLanguage() {

        // Initializes and sets up the language text values based on the current language settings.
        languageSettings = new GTNLanguage();
        languageSettings.initializeLanguages(language, getContext());

        // Sets the language text.
        shortcutInput.setHint(languageSettings.getShortcutText());
        addressInput.setHint(languageSettings.getAddressText());
        saveLocationButton.setText(languageSettings.getSaveLocationText());
        createShortcutButton.setText(languageSettings.getCreateShortcutText());
        navigationButton.setText(languageSettings.getNavigationText());
        deleteButton.setText(languageSettings.getDeleteLocationText());
    }

    // setShortcutType(): Sets the shortcut type and highlights the button background.
    private void setShortcutType(int type) {

        switch (type) {

            // DRIVING:
            case 0:

                currentShortcutType = 0; // Sets the current type value.

                // Sets the appropriate button background colors.
                drivingButton.setBackgroundColor(currentActivity.getResources().getColor(android.R.color.white));
                transitButton.setBackgroundColor(currentActivity.getResources().getColor(android.R.color.transparent));
                bikingButton.setBackgroundColor(currentActivity.getResources().getColor(android.R.color.transparent));
                walkingButton.setBackgroundColor(currentActivity.getResources().getColor(android.R.color.transparent));
                break;

            // TRANSIT:
            case 1:

                currentShortcutType = 1; // Sets the current type value.

                // Sets the appropriate button background colors.
                drivingButton.setBackgroundColor(currentActivity.getResources().getColor(android.R.color.transparent));
                transitButton.setBackgroundColor(currentActivity.getResources().getColor(android.R.color.white));
                bikingButton.setBackgroundColor(currentActivity.getResources().getColor(android.R.color.transparent));
                walkingButton.setBackgroundColor(currentActivity.getResources().getColor(android.R.color.transparent));
                break;

            // BIKING:
            case 2:

                currentShortcutType = 2; // Sets the current type value.

                // Sets the appropriate button background colors.
                drivingButton.setBackgroundColor(currentActivity.getResources().getColor(android.R.color.transparent));
                transitButton.setBackgroundColor(currentActivity.getResources().getColor(android.R.color.transparent));
                bikingButton.setBackgroundColor(currentActivity.getResources().getColor(android.R.color.white));
                walkingButton.setBackgroundColor(currentActivity.getResources().getColor(android.R.color.transparent));
                break;

            // WALKING:
            case 3:

                currentShortcutType = 3; // Sets the current type value.

                // Sets the appropriate button background colors.
                drivingButton.setBackgroundColor(currentActivity.getResources().getColor(android.R.color.transparent));
                transitButton.setBackgroundColor(currentActivity.getResources().getColor(android.R.color.transparent));
                bikingButton.setBackgroundColor(currentActivity.getResources().getColor(android.R.color.transparent));
                walkingButton.setBackgroundColor(currentActivity.getResources().getColor(android.R.color.white));
                break;

            // DEFAULT: Sets default to DRIVING type.
            default:

                currentShortcutType = 0; // Sets the current type value.

                // Sets the background color to white.
                drivingButton.setBackgroundColor(currentActivity.getResources().getColor(android.R.color.white));
                transitButton.setBackgroundColor(currentActivity.getResources().getColor(android.R.color.transparent));
                bikingButton.setBackgroundColor(currentActivity.getResources().getColor(android.R.color.transparent));
                walkingButton.setBackgroundColor(currentActivity.getResources().getColor(android.R.color.transparent));
                break;
        }
    }

    // retrieveInputValue(): Retrieves the String value from the EditText input object.
    private String retrieveInputValue(EditText input) {

        String value; // Stores the retrieved String value from the EditText object.

        // Attempts to retrieves the String value from the EditText input object.
        try {
            value = input.getText().toString();
            return value;
        }

        // NullPointerException handler.
        catch (NullPointerException e) {

            // A Toast message appears, notifying the user about the error.
            Log.d(LOG_TAG, "ERROR: Null pointer exception was encountered while retrieving the input.");
            return null;
        }
    }

    /** ADDITIONAL FUNCTIONALITY _______________________________________________________________ **/

    // loadPreferences(): Loads the SharedPreference values from the stored SharedPreferences object.
    private void loadPreferences() {

        // Initializes the SharedPreferences objects.
        SharedPreferences GTN_prefs = GTNPreference.initializePreferences(GTN_OPTIONS, currentActivity);
        GTN_temps = GTNPreference.initializePreferences(GTN_TEMPS, currentActivity);

        // Retrieves values from the application preferences.
        currentShortcutType = GTNTypes.getSelectedType(GTNPreference.getTransportationMode(GTN_prefs));
        language = GTNPreference.getLanguage(GTN_prefs);
    }

    // launchIntent(): Launches Google Maps directly in navigation mode.
    private void launchIntent() {

        String location = GTNConditions.getAddressInput(true, addressInput); // Retrieves the address from the user's input.

        // Launches the customized Google Maps intent directly in navigation mode.
        Intent mapIntent = GTNMapsIntent.getMapIntent(location, GTNTypes.getSelectedType(currentShortcutType), true, currentActivity);
        startActivity(mapIntent); // Launches the intent to Google Maps.
    }

    // updateWidget(): Updates the view for any widgets being utilized.
    private void updateWidget() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getContext());
        int appWidgetIds[] = appWidgetManager.getAppWidgetIds(new ComponentName(getContext(), GTNWidgetProvider.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.gtn_shortcuts_widget_list);
    }

    /** INTERFACE FUNCTIONALITY ________________________________________________________________ **/

    // closeActionFragment(): An interface method that signals the attached activity to close this
    // fragment.
    private void closeActionFragment() {

        try {
            ((OnFragmentUpdateListener) currentActivity).closeFragmentView("ACTION");
            GTNPreference.setOrientationChange(false, GTN_temps);
        }

        catch (ClassCastException cce) { } // Catch for class cast exception errors.
    }

    // displaySnackbarMessage(): An interface method that signals the attached activity to display a
    // Snackbar message.
    private void displaySnackbarMessage(String message) {
        try { ((OnFragmentUpdateListener) currentActivity).displaySnackbar(message); }
        catch (ClassCastException cce) { } // Catch for class cast exception errors.
    }

    // updateShortcutView(): An interface method that signals the attached activity to signal
    // GTNShortcutFragment to update the shortcuts list.
    private void updateShortcutView(LinkedList<GTNShortcut> shortcuts) {
        try { ((OnFragmentUpdateListener) currentActivity).updateShortcuts(shortcuts); }
        catch (ClassCastException cce) { } // Catch for class cast exception errors.
    }
}