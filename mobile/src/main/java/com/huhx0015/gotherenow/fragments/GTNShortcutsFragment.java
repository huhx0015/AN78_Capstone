package com.huhx0015.gotherenow.fragments;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.huhx0015.gotherenow.activities.GTNMainActivity;
import com.huhx0015.gotherenow.application.GTNApplication;
import com.huhx0015.gotherenow.data.GTNCursorLoader;
import com.huhx0015.gotherenow.graphics.GTNImages;
import com.huhx0015.gotherenow.intents.GTNVoiceIntent;
import com.huhx0015.gotherenow.interfaces.OnFragmentUpdateListener;
import com.huhx0015.gotherenow.interfaces.OnShortcutUpdateListener;
import com.huhx0015.gotherenow.interfaces.OnShortcutViewListener;
import com.huhx0015.gotherenow.preferences.GTNPreference;
import com.huhx0015.gotherenow.R;
import com.huhx0015.gotherenow.ui.adapters.PlaceAutocompleteAdapter;
import com.huhx0015.gotherenow.ui.adapters.GTNRecyclerAdapter;
import com.huhx0015.gotherenow.ui.input.GTNConditions;
import com.huhx0015.gotherenow.ui.language.GTNLanguage;
import com.huhx0015.gotherenow.widget.GTNWidgetProvider;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.melnykov.fab.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import butterknife.Bind;
import butterknife.ButterKnife;

/** -----------------------------------------------------------------------------------------------
 *  [GTNShortcutsFragment] CLASS
 *  PROGRAMMER: Huh X0015
 *  DESCRIPTION: GTNShortcutsFragment class is a Fragment class that is used for displaying the
 *  shortcut list fragment.
 *  -----------------------------------------------------------------------------------------------
 */

public class GTNShortcutsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        GoogleApiClient.OnConnectionFailedListener, OnShortcutUpdateListener {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // ACTIVITY VARIABLES
    private GTNMainActivity currentActivity; // Used to attach activity to this fragment.

    // GOOGLE PLACES VARIABLES
    private PlaceAutocompleteAdapter placeAdapter;
    public GoogleApiClient googleApiClient;

    // LANGUAGE VARIABLES
    private boolean isRTL = false; // Used to determine if device is in RTL mode.
    private GTNLanguage languageSettings; // Stores the language String values used by the layout.

    // LIST VARIABLES
    private GTNRecyclerAdapter adapterShortcuts;

    // LOCATION VARIABLES
    private Location currentLocation; // Used for referencing the current location.

    // LOGGING VARIABLES
    private static final String LOG_TAG = GTNShortcutsFragment.class.getSimpleName();

    // PREFERENCES VARIABLES
    private String currentTransType = ""; // Used to determine which transportation type to use for the QUICK NAVIGATION button.
    private String lastLocation = ""; // Used to reference the last location that was entered by the user.
    private SharedPreferences GTN_temps; // SharedPreferences objects that store temporary settings for the application.
    private static final String GTN_OPTIONS = "gtn_options"; // Used to reference the name of the preference XML file.
    private static final String GTN_TEMPS = "gtn_temps"; // Used to reference the name of the temporary preference XML file.

    // VIEW INJECTION VARIABLES
    @Bind(R.id.gtn_quick_nav_input) AutoCompleteTextView quickNavAddress; // References the quick navigation address input field object.
    @Bind(R.id.gtn_action_button) FloatingActionButton floatingActionButton; // References the floating action button object.
    @Bind(R.id.gtn_quick_nav_button) ImageButton quickNavButton; // References the quick navigation ImageButton object.
    @Bind(R.id.gtn_voice_input_button) ImageButton voiceInputButton; // References the voice input ImageButton object.
    @Bind(R.id.gtn_shortcuts_list) RecyclerView shortcutRecyclerView; // References the shortcuts RecyclerView object.
    @Bind(R.id.gtn_location_shortcuts_text) TextView locationShortcutsText; // References the LOCATION SHORTCUTS TextView object.
    @Bind(R.id.gtn_no_shortcuts_text) TextView noShortcutsText; // References the No Location Shortcuts TextView object.
    @Bind(R.id.gtn_quick_navigation_text) TextView quickNavText; // References the QUICK NAVIGATION TextView object.

    /** INITIALIZATION FUNCTIONALITY ___________________________________________________________ **/

    // GTNShortcutsFragment(): Constructor for the GTNShortcutsFragment fragment class.
    private final static GTNShortcutsFragment gtn_shortcuts_fragment = new GTNShortcutsFragment();

    // GTNShortcutsFragment(): Deconstructor for the GTNShortcutsFragment.
    public GTNShortcutsFragment() {}

    // getInstance(): Returns the gtn_shortcuts_fragment instance.
    public static GTNShortcutsFragment getInstance() { return gtn_shortcuts_fragment; }

    /** FRAGMENT LIFECYCLE FUNCTIONALITY _______________________________________________________ **/

    // onAttach(): The initial function that is called when the Fragment is run. The activity is
    // attached to the fragment.
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.currentActivity = (GTNMainActivity) getActivity(); // Sets the currentActivity to attached activity object.
    }

    // onCreateView(): Creates and returns the view hierarchy associated with the fragment.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // PREFERENCES:
        loadPreferences(); // Loads the application settings from SharedPreferences.

        // GOOGLE API CLIENT INITIALIZATION:
        setUpGoogleApiClient(); // Sets up the Google API client connection.

        // Sets the view to the specified XML layout file.
        View gtn_shortcuts_view = (ViewGroup) inflater.inflate(R.layout.gtn_shortcuts_fragment, container, false);
        ButterKnife.bind(this, gtn_shortcuts_view); // ButterKnife view injection initialization.
        setUpLayout(); // Sets up the layout for the fragment.
        getLoaderManager().initLoader(0, null, this); // Initializes the Loader.

        return gtn_shortcuts_view;
    }

    // onResume(): This function is called whenever the current fragment is resumed.
    @Override
    public void onResume() {
        super.onResume();
        googleApiClient.connect(); // Establishes a Google API client connection.
    }

    // onPause(): This function is called whenever the current fragment is suspended.
    @Override
    public void onPause() {
        super.onPause();

        // Saves the last location address value into SharedPreferences.
        GTNPreference.setLastLocation(retrieveQuickNavAddress(), GTN_temps);
    }

    // onDestroyView(): This function runs when the screen is no longer visible and the view is
    // destroyed.
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        googleApiClient.disconnect(); // Disconnects the Google API client connection.
        ButterKnife.unbind(this); // Sets all injected views to null.
    }

    /** GOOGLE API FUNCTIONALITY _______________________________________________________________ **/

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}

    // setUpGoogleApiClient(): Sets up the Google API client for the Google Places API.
    private void setUpGoogleApiClient() {

        // GOOGLE API CLIENT INITIALIZATION:
        googleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(getActivity(), this)
                .build();
    }

    /** LAYOUT FUNCTIONALITY ___________________________________________________________________ **/

    // setUpLayout(): Sets up the layout for the fragment.
    private void setUpLayout() {

        isRTL = GTNLanguage.isRTL(currentActivity); // Determines if device is in RTL mode.

        setUpButtons(); // Sets up the button listeners for the fragment.
        setUpLanguage(); // Applies the current language attributes onto the TextView objects.
        setUpSearch(null); // Sets up the AutoCompleteTextView input field.
        updateWidget(); // Updates the widget view.

        // Sets the saved last location value into the quick navigation address input.
        quickNavAddress.setText(lastLocation);
    }

    // setUpButtons(): Sets up the button images and listeners for the fragment.
    private void setUpButtons() {
        int voiceInputResource = R.drawable.gtn_mic_icon; // References the voice icon.
        int micSize = GTNImages.densityToPixels(32, currentActivity); // Sets the size of the mic button.

        // Loads the navigation icon era image into the quick navigation ImageButton object.
        changeQuickNavButtonImage(false);

        // Checks to see if the device has voice recognition capabilities. If it has at least one
        // such service, the voice input button is shown and is setup.
        if (GTNVoiceIntent.checkVoiceCapabilities(currentActivity)) {

            voiceInputButton.setVisibility(View.VISIBLE); // Voice input is made visible.

            // Loads the navigation icon era image into the voice input ImageButton object.
            Picasso.with(currentActivity)
                    .load(voiceInputResource)
                    .resize(micSize, micSize)
                    .into(voiceInputButton);
        }

        else { voiceInputButton.setVisibility(View.GONE); } // Hides the voice input button.

        // QUICK NAVIGATION BUTTON: Click listener.
        quickNavButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Retrieves the input from the QUICK NAVIGATION input field.
                String quickNavAddressText = retrieveQuickNavAddress();

                // Checks to see if the QUICK NAVIGATION input field is null or empty first.
                if (!(quickNavAddressText == null) && !(quickNavAddressText.isEmpty()) && !(quickNavAddressText.equals(""))) {

                    // Checks to see if Google Maps is installed first.
                    if (GTNConditions.checkForAppInstalled(currentActivity)) {

                        // Launches an Intent to Google Maps with the inputted address.
                        GTNApplication.instance.prepareNavigationIntent(quickNavAddressText, currentTransType, languageSettings);
                    }

                    // Displays a Snackbar message, notifying the user that Google Maps needs to be
                    // installed.
                    else {
                        displaySnackbarMessage(languageSettings.getGoogleMapsErrorText());
                    }
                }

                // Displays an error Snackbar message display at the bottom of the screen.
                else {
                    displaySnackbarMessage(languageSettings.getAddressErrorText());
                }
            }
        });

        // QUICK NAVIGATION BUTTON: Focus listener.
        quickNavButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    changeQuickNavButtonImage(true);
                } else {
                    changeQuickNavButtonImage(false);
                }
            }
        });

        // QUICK NAVIGATION BUTTON: Touch listener.
        quickNavButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        changeQuickNavButtonImage(true);
                        quickNavButton.performClick();
                        return true;
                    case MotionEvent.ACTION_UP:
                        changeQuickNavButtonImage(false);
                        return true;
                }
                return false;
            }
        });

        // VOICE INPUT BUTTON: Click listener.
        voiceInputButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                GTNVoiceIntent.startVoiceRecognitionActivity(currentActivity, languageSettings);
            }
        });

        // FLOATING ACTION BUTTON: Click listener.
        floatingActionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openActionFragment(retrieveQuickNavAddress());
            }
        });
    }

    // setUpLanguage(): Applies the current language settings to the TextView objects in the layout.
    private void setUpLanguage() {
        locationShortcutsText.setText(languageSettings.getLocationShortcutText());
        quickNavAddress.setGravity(isRTL ? Gravity.CENTER_VERTICAL | Gravity.END : Gravity.CENTER_VERTICAL | Gravity.START);
        quickNavAddress.setHint(languageSettings.getQuickNavigationHintText());
        quickNavText.setText(languageSettings.getQuickNavigationText());
    }

    // setUpRecyclerView(): Sets up the RecyclerView for the fragment.
    private void setUpRecyclerView(Cursor data) {

        // Sets the LinearLayoutManger focus to the recycler view object in this fragment layout.
        LinearLayoutManager layoutManager = new LinearLayoutManager(currentActivity);
        shortcutRecyclerView.setLayoutManager(layoutManager);

        // Sets up the RecyclerView object and sets the appropriate adapter to it.
        adapterShortcuts = new GTNRecyclerAdapter(data, true, currentActivity);
        adapterShortcuts.setHasStableIds(true);
        shortcutRecyclerView.setAdapter(adapterShortcuts);

        // Attaches the FloatingActionButton to the shortcutRecyclerView object.
        floatingActionButton.attachToRecyclerView(shortcutRecyclerView);

        // Hides the No Location Shortcuts TextView if location shortcuts exist.
        if (adapterShortcuts.getItemCount() > 0) {
            noShortcutsText.setVisibility(View.GONE);
        } else {
            noShortcutsText.setVisibility(View.VISIBLE);
        }
    }

    // setUpSearch(): Sets up the AutoCompleteTextView input field.
    private void setUpSearch(Location location) {

        // Sets the starting location value to the geographic center of the United States of
        // America.
        if (location == null) {
            location = new Location(getString(R.string.app_name));
            location.setLatitude(39.50);
            location.setLongitude(-98.35);
        }

        // Sets the location area to optimize the Google Places API AutoCompleteTextView results.
        LatLngBounds currentRange = new LatLngBounds(
                new LatLng(location.getLatitude(),
                        location.getLongitude()),
                new LatLng(location.getLatitude() + 1.0,
                        location.getLongitude() + 1.0));

        // Register a listener that receives callbacks when a suggestion has been selected.
        if (quickNavAddress != null) {
            quickNavAddress.setOnItemClickListener(autocompleteClickListener);

            // Set up the adapter that will retrieve suggestions from the Places Geo Data API that cover
            // the entire world.
            placeAdapter = new PlaceAutocompleteAdapter(getContext(), googleApiClient, currentRange, null);
            quickNavAddress.setAdapter(placeAdapter);
        }
    }

    // retrieveQuickNavAddress(): Retrieves the input from the QUICK NAVIGATION field.
    private String retrieveQuickNavAddress() {

        String quickNavAddressText; // Stores the input value from the QUICK NAVIGATION field.

        // Retrieves the text input from the QUICK NAVIGATION input field.
        try {
            quickNavAddressText = quickNavAddress.getText().toString();
            return quickNavAddressText;
        }

        // NullPointerException handler. Returns null if an error was encountered.
        catch (NullPointerException e) {
            Log.d(LOG_TAG, "ERROR: Null pointer exception was encountered while retrieving the shortcut input.");
            return null;
        }
    }

    // changeQuickNavButtonImage(): Changes the quick navigation button image.
    private void changeQuickNavButtonImage(boolean isSelected) {

        int buttonSize = GTNImages.densityToPixels(64, currentActivity); // Sets the size of ImageButtons.

        // Sets up the rounded shape attributes for the voice input and quick navigation ImageButton
        // object.
        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.LTGRAY)
                .borderWidthDp(1)
                .cornerRadiusDp(90)
                .oval(false)
                .build();

        // SELECTED:
        if (isSelected) {
            Picasso.with(currentActivity)
                    .load(R.drawable.gtn_nav_icon_focused)
                    .resize(buttonSize, buttonSize)
                    .centerCrop()
                    .transform(transformation)
                    .into(quickNavButton);
        }

        // NON-SELECTED:
        else {
            Picasso.with(currentActivity)
                    .load(R.drawable.gtn_nav_icon)
                    .resize(buttonSize, buttonSize)
                    .centerCrop()
                    .transform(transformation)
                    .into(quickNavButton);
        }
    }

    /** LOADER FUNCTIONALITY __________________________________________________________________  **/

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "onCreateLoader(): Loader created with ID: " + id);
        return GTNCursorLoader.newAllShortcutsInstance(getContext());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(LOG_TAG, "onLoadFinished(): Loader finished.");
        setUpRecyclerView(data); // Sets up the RecyclerView for the fragment.
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(LOG_TAG, "onLoadReset(): Loader reset.");
    }

    /** AUTOCOMPLETE FUNCTIONALITY _____________________________________________________________ **/

    // autocompleteClickListener(): Listener that handles selections from suggestions from the
    // AutoCompleteTextView that displays Place suggestions. Gets the place id of the selected item
    // and issues a request to the Places Geo Data API to retrieve more details about the place.
    private AdapterView.OnItemClickListener autocompleteClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            // Retrieve the place ID of the selected item from the Adapter. The adapter stores each
            // Place suggestion in a AutocompletePrediction from which we read the place ID and title.
            final AutocompletePrediction item = placeAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

            Log.i(LOG_TAG, "Autocomplete item selected: " + primaryText);

            // Issue a request to the Places Geo Data API to retrieve a Place object with additional
            // details about the place.
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(googleApiClient, placeId);
            placeResult.setResultCallback(updatePlaceDetailsCallback);

            Log.i(LOG_TAG, "Called getPlaceById to get Place details for " + placeId);
        }
    };

    // updatePlaceDetailsCallback(): Callback for results from a Places Geo Data API query that
    // shows the first place result in the details view on screen.
    private ResultCallback<PlaceBuffer> updatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {

        @Override
        public void onResult(PlaceBuffer places) {

            if (!places.getStatus().isSuccess()) {

                // Request did not complete successfully.
                Log.e(LOG_TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }

            // Hides the keyboard from the view.
            View view = getActivity().getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            Log.i(LOG_TAG, "Place details received: " + places.get(0).getName());

            places.release();
        }
    };

    /** ADDITIONAL FUNCTIONALITY _______________________________________________________________ **/

    // loadPreferences(): Loads the SharedPreference values from the stored SharedPreferences object.
    private void loadPreferences() {

        // Initializes the SharedPreferences object and retrieves values from SharedPreferences.
        SharedPreferences GTN_prefs = GTNPreference.initializePreferences(GTN_OPTIONS, currentActivity);
        GTN_temps = GTNPreference.initializePreferences(GTN_TEMPS, currentActivity);
        currentTransType = GTNPreference.getTransportationMode(GTN_prefs);
        lastLocation = GTNPreference.getLastLocation(GTN_temps);

        // Retrieves the language settings from the application preferences and sets up the
        // language attributes.
        String language = GTNPreference.getLanguage(GTN_prefs);
        languageSettings = new GTNLanguage();
        languageSettings.initializeLanguages(language, getContext());
    }

    // updateWidget(): Updates the view for any widgets being utilized.
    private void updateWidget() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getContext());
        int appWidgetIds[] = appWidgetManager.getAppWidgetIds(new ComponentName(getContext(), GTNWidgetProvider.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.gtn_shortcuts_widget_list);
    }

    /** INTERFACE FUNCTIONALITY ________________________________________________________________ **/

    // displaySnackbarMessage(): An interface method that signals the attached activity to display a
    // Snackbar message.
    private void displaySnackbarMessage(String message) {
        try { ((OnFragmentUpdateListener) currentActivity).displaySnackbar(message); }
        catch (ClassCastException cce) { } // Catch for class cast exception errors.
    }

    // openActionFragment(): Signals attached activity to display the GTNActionFragment view.
    private void openActionFragment(String address) {
        try { ((OnShortcutViewListener) currentActivity).openActionView(address, true); }
        catch (ClassCastException cce) {} // Catch for class cast exception errors.
    }

    // updateLocation(): Called from the attached activity to update the location bounds for the
    // Google Places API.
    @Override
    public void updateLocation(Location location) {
        currentLocation = location;
        setUpSearch(currentLocation);
    }

    // updateVoiceInput(): Called from the attached activity to update the QUICK NAVIGATION input
    // line with the voice input data received from the user.
    @Override
    public void updateVoiceInput(String voiceInput) {

        quickNavAddress.setText(voiceInput); // Sets the text for the QUICK NAVIGATION input.

        // Initiates a QUICK NAVIGATION button click event to launch navigation mode.
        quickNavButton.performClick();
    }

    // updateShortcutLanguage(): Called from the attached activity to update the language
    // attributes of the TextView parameters.
    @Override
    public void updateShortcutLanguage() {
        loadPreferences(); // Retrieves the latest SharedPreference values.
        setUpLanguage(); // Updates the TextView objects' language text.
    }

    // updateShortcutView(): Called from the attached activity to update the database.
    @Override
    public void updateShortcutView() {
        getLoaderManager().restartLoader(0, null, this);
    }
}