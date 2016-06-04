package com.huhx0015.gotherenow.activities;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.speech.RecognizerIntent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.huhx0015.gotherenow.R;
import com.huhx0015.gotherenow.fragments.GTNActionFragment;
import com.huhx0015.gotherenow.fragments.GTNSettingsFragment;
import com.huhx0015.gotherenow.intents.GTNMapsIntent;
import com.huhx0015.gotherenow.interfaces.OnFragmentUpdateListener;
import com.huhx0015.gotherenow.interfaces.OnShortcutUpdateListener;
import com.huhx0015.gotherenow.interfaces.OnShortcutViewListener;
import com.huhx0015.gotherenow.model.GTNShortcut;
import com.huhx0015.gotherenow.ui.input.GTNConditions;
import com.huhx0015.gotherenow.ui.layout.FragmentListPagerAdapter;
import com.huhx0015.gotherenow.ui.layout.GTNUnbind;
import com.huhx0015.gotherenow.ui.language.GTNLanguage;
import com.huhx0015.gotherenow.fragments.GTNShortcutsFragment;
import com.huhx0015.gotherenow.preferences.GTNPreference;
import com.huhx0015.gotherenow.ui.resources.GTNTypes;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import butterknife.Bind;
import butterknife.ButterKnife;

/** ------------------------------------------------------------------------------------------------
 *  [GTNMainActivity] CLASS
 *  PROGRAMMER: Michael Yoon Huh (HUHX0015)
 *  DESCRIPTION: This class is the main activity for the application. The user can create a custom
 *  location shortcut that launches Google Maps and begins navigation mode immediately. The activity
 *  loads and displays an advertising banner for monetization purposes.
 *  ------------------------------------------------------------------------------------------------
 */

public class GTNMainActivity extends AppCompatActivity implements LocationListener,
        OnFragmentUpdateListener, OnShortcutViewListener {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // CLASS VARIABLES
    private GTNLanguage gtn_languages; // An object that is used for language-related functionality.

    // FRAGMENT VARIABLES
    private boolean isRemovingFragment = false; // Used to determine if the fragment is currently being removed.
    private GTNShortcutsFragment gtn_shortcuts_fragment; // References the GTNShortcutsFragment fragment object.

    // LAYOUT VARIABLES
    private boolean showActionFragmentEdit = false; // Used to determine if the action fragment in EDIT SHORTCUT mode is currently being shown or not.
    private boolean showActionFragmentNew = false; // Used to determine if the action fragment in NEW SHORTCUT mode is currently being shown or not.
    private boolean showSettingsFragment = false; // Used to determine if the settings fragment is currently being shown or not.

    // LOCATION VARIABLES
    private static final long MIN_DISTANCE_UPDATE = 10;
    private static final long UPDATE_INTERVAL = 1000 * 60;
    private Location currentLocation; // Used for referencing the current location.

    // LOGGING VARIABLES
    private static final String LOG_TAG = GTNMainActivity.class.getSimpleName();

    // SHORTCUT VARIABLES
    private GTNShortcut currentShortcut; // References the current GTNShortcut in focus if GTNActionFragment is active and in EDIT SHORTCUT mode.

    // SYSTEM VARIABLES
    private static final int VOICE_INPUT_CODE = 6400; // Used for identifying voice input requests.
    private SharedPreferences GTN_prefs; // Main SharedPreferences objects that store settings for the application.
    private SharedPreferences GTN_temps; // Temporary SharedPreferences objects that store settings for the application.
    private String language; // Stores the String value of the language currently used by the application.
    private static final String GTN_OPTIONS = "gtn_options"; // Used to reference the name of the preference XML file.
    private static final String GTN_TEMPS = "gtn_temps"; // Used to reference the name of the temporary preference XML file.
    private static WeakReference<GTNMainActivity> weakRefActivity = null; // Used to maintain a weak reference to the activity.

    // VIEW INJECTION VARIABLES
    @Bind(R.id.adView) AdView adView; // Used to reference the AdView that contains the advertising banner.
    @Bind(R.id.gtn_main_layout) CoordinatorLayout mainLayout; // Used to reference the main layout for the activity.
    @Bind(R.id.gtn_fragment_container) FrameLayout fragmentDisplay; // Used to reference the fragment container.
    @Bind(R.id.gtn_main_bottom_layout) LinearLayout bottomLayoutContainer; // References the bottom layout container.
    @Bind(R.id.gtn_main_toolbar) Toolbar gtn_main_toolbar; // Used for referencing the Toolbar object.
    @Bind(R.id.gtn_main_activity_fragment_pager) ViewPager gtn_viewpager; // References the fragment ViewPager object.

    /** ACTIVITY LIFECYCLE FUNCTIONALITY _______________________________________________________ **/

    // onCreate(): The initial function that is called when the activity is run. onCreate() only runs
    // when the activity is first started.
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // INITIALIZATION:
        super.onCreate(savedInstanceState);
        weakRefActivity = new WeakReference<GTNMainActivity>(this); // Creates a weak reference of this activity.
        setContentView(R.layout.gtn_main_activity); // Assigns the layout for the activity.
        ButterKnife.bind(this); // ButterKnife view injection initialization.

        // PREFERENCES: Retrieves any preferences that may have been changed in options and applies them accordingly.
        // Sets the default preferences values if the user is running application for the first time.
        GTNPreference.setDefaultPreferences(GTN_OPTIONS, false, this);
        SharedPreferences GTN_prefs = GTNPreference.initializePreferences(GTN_OPTIONS, this);

        // Resets the temporary preference values to default values.
        GTNPreference.setDefaultPreferences(GTN_TEMPS, true, this);

        loadPreferences(); // Retrieves all preference values for the application.

        // LAYOUT:
        setUpLayout(); // Sets up the layout, buttons and advertising banner for the activity.
        setUpSlider(); // Initializes the fragment slides for the PagerAdapter.

        // LOCATION: Initializes user location retrieval.
        getLocation();

        // INTENT EXTRAS:
        getIntentBundle(); // Retrieves any additional data that was passed from the previous intent.
    }

    // onResume(): This function runs immediately after onCreate() finishes and is always re-run
    // whenever the activity is resumed from an onPause() state.
    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) { adView.resume(); } // Resumes the advertising banner functionality.
    }

    // onPause(): This function is called whenever the current activity is suspended or another
    // activity is launched.
    @Override
    public void onPause() {
        if (adView != null) { adView.pause(); } // Pauses the advertising banner functionality.
        super.onPause();
    }

    // onDestroy(): This function runs when the activity has terminated and is being destroyed.
    // Calls recycleMemory() to free up memory allocation.
    @Override
    public void onDestroy() {
        recycleMemory(); // Recycles all View objects to free up memory resources.
        super.onDestroy();
    }

    /** ACTIVITY EXTENSION FUNCTIONALITY _______________________________________________________ **/

    // onActivityResult(): This function handles the results from the voice recognition activity.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Retrieves the current language value and updates the language format for the application layout.
        language = GTNPreference.getLanguage(GTN_prefs);
        setLanguage(); // Sets the current language values.

        // If the request returned is of a voice input format, the data received is parsed into a
        // string format and then sent to the GTNShortcutsFragment to set the QUICK NAVIGATION
        // input line.
        if (requestCode == VOICE_INPUT_CODE && resultCode == RESULT_OK) {

            // Retrieves the voice input result and assigns it to a variable.
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String voiceResult = result.get(0);

            updateVoice(voiceResult); // Sends the voice input data to the GTNShortcutsFragment.
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    // onConfigurationChanged(): If the screen orientation changes, this function loads the proper
    // layout, as well as updating all layout-related objects.
    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);

        setUpLayout(); // Updates the layout for the GTNMainActivity activity.

        // If the fragment was in focus prior to the screen orientation change, the fragment is
        // re-set to being in focus.
        if (showSettingsFragment) { openSettingsView(false); }

        // GTNActionFragment (NEW SHORTCUT): Refreshes the GTNActionFragment if it was open before
        // the orientation change.
        if (showActionFragmentNew) {
            GTNPreference.setOrientationChange(true, GTN_temps); // Indicates a orientation change.
            openActionView(null, false); // Displays the GTNActionFragment view.
        }

        // GTNActionFragment (EDIT SHORTCUT): Refreshes the GTNActionFragment if it was open before
        // the orientation change.
        if (showActionFragmentEdit) {

            // If the currentShortcut object is not null, the GTNActionFragment is re-created with
            // the currentShortcut object in focus.
            if (currentShortcut != null) {
                GTNPreference.setOrientationChange(true, GTN_temps); // Indicates a orientation change.
                editExistingShortcut(currentShortcut, false); // Displays the GTNActionFragment view.
            }
        }
    }

    // onCreateOptionsMenu(): Inflates the menu when the menu key is pressed. This adds items to
    // the action bar if it is present.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflates the menu settings specified in menu.xml.
        getMenuInflater().inflate(R.menu.gtn_main_activity_menu, menu);
        return true;
    }

    // onOptionsItemSelected(): Defines the action to take when the menu options are selected. The
    // GTN_Preferences preference activity is launched when "Settings" is selected.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId(); // Retrieves the menu ID references from gtn_main_activity_menu.xml.

        // If "Settings" is selected, an intent is created to launch the GTN_Preferences activity.
        if (id == R.id.action_settings) {
            openSettingsView(true); // Sets up and displays the GTNSettingsFragment view.
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /** PHYSICAL BUTTON FUNCTIONALITY __________________________________________________________ **/

    // BACK KEY:
    // onBackPressed(): Defines the action to take when the physical back button key is pressed.
    @Override
    public void onBackPressed() {

        // ACTION: If the ACTION fragment is currently being displayed, pressing the back button
        // will remove the fragment view and display the primary view.
        if ( (showActionFragmentNew) || (showActionFragmentEdit)) {

            // Checks to see if fragment removal is already underway.
            if (!isRemovingFragment) {
                removeFragment("ACTION");
                isRemovingFragment = true;
            }
        }

        // SETTINGS: If the SETTINGS fragment is currently being displayed, pressing the back button
        // will remove the fragment view and display the primary view.
        else if (showSettingsFragment) {

            // Checks to see if fragment removal is already underway.
            if (!isRemovingFragment) {
                setLanguage(); // Sets the current language values.
                updateLanguage(); // Signals GTNShortcutFragment to update it's language parameters.
                removeFragment("SETTINGS");
                isRemovingFragment = true;
            }
        }

        else { finish(); } // Finishes the activity.
    }

    /** INTENT FUNCTIONALITY ___________________________________________________________________ **/

    // getIntentBundle(): Retrieves the data from the previous activity.
    private void getIntentBundle() {

        Bundle extras = getIntent().getExtras();

        // Tries to retrieve the additional information from the bundle.
        if (extras != null) {

            // Retrieves the data values from the bundled extras.
            boolean isNewLocation = extras.getBoolean("gtn_new_location");

            // Opens the GTNActionFragment in NEW SHORTCUT mode.
            if (isNewLocation) { openActionView(null, false); }
        }
    }

    /** LAYOUT FUNCTIONALITY ___________________________________________________________________ **/

    // setUpLayout(): This function is responsible for setting up the layout for the application.
    private void setUpLayout() {
        setUpToolbar(); // Sets up the toolbar for the activity.
        setUpAd(); // Sets up the ad banner functionality.
    }

    // setUpToolbar(): Sets up the Material Design style toolbar for the activity.
    private void setUpToolbar() {

        // Initializes the Material Design style Toolbar object for the activity.
        if (gtn_main_toolbar != null) {
            gtn_main_toolbar.setTitle(getResources().getString(R.string.app_name));
            setSupportActionBar(gtn_main_toolbar);
        }
    }

    /** FRAGMENT FUNCTIONALITY _________________________________________________________________ **/

    // setUpFragment(): Sets up the fragment view and the fragment view animation.
    private void setUpFragment(Fragment fragment, final String fragType, boolean isAnimated) {

        if ((weakRefActivity.get() != null) && (!weakRefActivity.get().isFinishing())) {

            // Initializes the manager and transaction objects for the fragments.
            FragmentManager fragMan = weakRefActivity.get().getSupportFragmentManager();
            FragmentTransaction fragTrans = fragMan.beginTransaction();
            fragTrans.replace(R.id.gtn_fragment_container, fragment);

            // Makes the changes to the fragment manager and transaction objects.
            fragTrans.addToBackStack(null);
            fragTrans.commitAllowingStateLoss();

            // Sets up the transition animation.
            if (isAnimated) {

                int animationResource; // References the animation XML resource file.

                // Sets the animation XML resource file, based on the fragment type.
                if (fragType.equals("ACTION")) { animationResource = R.anim.bottom_up; } // ACTION
                else { animationResource = R.anim.slide_down; } // SETTINGS

                Animation fragmentAnimation = AnimationUtils.loadAnimation(this, animationResource);

                // Sets the AnimationListener for the animation.
                fragmentAnimation.setAnimationListener(new Animation.AnimationListener() {

                    // onAnimationStart(): Runs when the animation is started.
                    @Override
                    public void onAnimationStart(Animation animation) {
                        fragmentDisplay.setVisibility(View.VISIBLE); // Displays the fragment.
                    }

                    // onAnimationEnd(): The fragment is removed after the animation ends.
                    @Override
                    public void onAnimationEnd(Animation animation) {

                        Log.d(LOG_TAG, "setUpFragment(): Fragment animation has ended.");

                        // Hides the gtn_main_activity_fragment_pager View object.
                        gtn_viewpager.setVisibility(View.INVISIBLE);
                    }

                    // onAnimationRepeat(): Runs when the animation is repeated.
                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });

                fragmentDisplay.startAnimation(fragmentAnimation); // Starts the animation.
            }

            // Displays the fragment view without any transition animations.
            else {

                fragmentDisplay.setVisibility(View.VISIBLE); // Displays the fragment.

                // Hides the gtn_main_activity_fragment_pager View object.
                gtn_viewpager.setVisibility(View.INVISIBLE);
            }
        }
    }

    // removeFragment(): This method is responsible for displaying the remove fragment animation, as
    // well as removing the fragment view.
    private void removeFragment(final String fragType) {

        if ((weakRefActivity.get() != null) && (!weakRefActivity.get().isFinishing())) {

            // Displays the gtn_main_activity_fragment_pager View object.
            gtn_viewpager.setVisibility(View.VISIBLE);

            int animationResource; // References the animation XML resource file.

            // Sets the animation XML resource file, based on the fragment type.
            if (fragType.equals("ACTION")) { animationResource = R.anim.bottom_down; } // ACTION
            else { animationResource = R.anim.slide_up; } // SETTINGS

            Animation fragmentAnimation = AnimationUtils.loadAnimation(this, animationResource);

            // Sets the AnimationListener for the animation.
            fragmentAnimation.setAnimationListener(new Animation.AnimationListener() {

                // onAnimationStart(): Runs when the animation is started.
                @Override
                public void onAnimationStart(Animation animation) {  }

                // onAnimationEnd(): The fragment is removed after the animation ends.
                @Override
                public void onAnimationEnd(Animation animation) {

                    Log.d(LOG_TAG, "removeFragment(): Fragment animation has ended. Attempting to remove fragment.");

                    // Initializes the manager and transaction objects for the fragments.
                    FragmentManager fragMan = getSupportFragmentManager();
                    fragMan.popBackStack(); // Pops the fragment from the stack.
                    fragmentDisplay.removeAllViews(); // Removes all views in the layout.

                    // ACTION: Indicates that the action fragment is no longer active.
                    if (fragType.equals("ACTION")) {
                        GTNPreference.setOrientationChange(false, GTN_temps);
                        showActionFragmentEdit = false;
                        showActionFragmentNew = false;
                    }

                    // SETTINGS: Indicates that the settings fragment is no longer active.
                    else if (fragType.equals("SETTINGS")) { showSettingsFragment = false; }

                    gtn_main_toolbar.setTitle(getResources().getString(R.string.app_name)); // Changes the title of the toolbar.
                    fragmentDisplay.setVisibility(View.INVISIBLE); // Hides the fragment.
                    isRemovingFragment = false; // Indicates that the fragment is no longer being removed.

                    Log.d(LOG_TAG, "removeFragment(): Fragment has been removed.");
                }

                // onAnimationRepeat(): Runs when the animation is repeated.
                @Override
                public void onAnimationRepeat(Animation animation) { }
            });

            fragmentDisplay.startAnimation(fragmentAnimation); // Starts the animation.
        }
    }

    // openSettingsView(): Sets up and displays the GTNSettingsFragment view.
    private void openSettingsView(boolean isAnimated) {

        // Changes the title of the toolbar.
        gtn_main_toolbar.setTitle(gtn_languages.getSettingsText());

        // Sets up the GTNSettingsFragment view.
        GTNSettingsFragment fragment = new GTNSettingsFragment();
        setUpFragment(fragment, "SETTINGS", isAnimated);

        // Indicates that the GTNSettingsFragment is currently being shown.
        showSettingsFragment = true;
        showActionFragmentEdit = false;
        showActionFragmentNew = false;
    }

    /** SLIDER FUNCTIONALITY ___________________________________________________________________ **/

    // createSlideFragments(): Sets up the slide fragments for the PagerAdapter object.
    private List<Fragment> createSlideFragments() {

        final List<Fragment> fragments = new Vector<Fragment>(); // List of fragments in which the fragments is stored.

        // Initializes the fragment objects for the slider.
        gtn_shortcuts_fragment = new GTNShortcutsFragment();

        // Sets up the fragment list for the PagerAdapter object.
        fragments.add(gtn_shortcuts_fragment);

        return fragments;
    }

    // setUpSlider(): Initializes the slides for the PagerAdapter object.
    private void setUpSlider() {

        // Initializes and creates a new FragmentListPagerAdapter objects using the List of slides
        // created from createSlideFragments.
        PagerAdapter dgPageAdapter = new FragmentListPagerAdapter(getSupportFragmentManager(), createSlideFragments());
        gtn_viewpager.setAdapter(dgPageAdapter); // Sets the PagerAdapter object for the activity.
    }

    /** LANGUAGE FUNCTIONALITY _________________________________________________________________ **/

    // setLanguage(): Retrieves the current language settings from the application's
    // SharedPreferences.
    private void setLanguage() {
        language = GTNPreference.getLanguage(GTN_prefs); // Retrieves the current language value.
        gtn_languages = new GTNLanguage(); // Initializes the GTNLanguage object.
        gtn_languages.initializeLanguages(language, this); // Sets the current language values.
    }

    /** ADVERTISING FUNCTIONALITY_______________________________________________________________ **/

    // setUpAd(): Sets up the advertising banner functionality.
    private void setUpAd() {

        // Creates an ad request.
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("5E3A8FAEE969D3E3E25651FC8784D985")
                .build();
        adView.loadAd(adRequest); // Starts loading the ad in the background.

        // Sets a listener for the advertising banner. If the advertising banner has been loaded,
        // the bottom layout container containing the AdView banner is rendered visible.
        adView.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                bottomLayoutContainer.setVisibility(View.VISIBLE);
            }
        });
    }

    /** LOCATION METHODS _______________________________________________________________________ **/

    // onLocationChanged(): This method is invoked when the location is updated.
    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            currentLocation = location;
            updateLocation(currentLocation); // Updates the Location data in GTNShortcutsFragment.
        }
    }

    // onStatusChanged(): This method is invoked when when the location status changes.
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    // onProviderEnabled(): Invoked when the Location provider has been enabled.
    @Override
    public void onProviderEnabled(String provider) {
        Log.d(LOG_TAG, "onProviderEnabled(): GPS Provider has been enabled.");
    }

    // onProviderDisabled(): Invoked when the Location provider has been disabled.
    @Override
    public void onProviderDisabled(String provider) {
        Log.d(LOG_TAG, "onProviderDisabled(): GPS Provider has been disabled.");
    }

    // getLocation(): Retrieves the device's current coordinates.
    private void getLocation() {

        // Initializes the LocationManager for accessing the GPS sensors.
        // TODO: Must request run-time permissions when target SDK is changed to 23.
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Initializes the LocationManager object and checks to see if the GPS and network
        // settings have been enabled.
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        // Defines the criteria on how to select the Location provider.
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);

        // NETWORK ENABLED:
        if (isNetworkEnabled) {

            // Requests a location update from the network.
            // TODO: Must request run-time permissions when target SDK is changed to 23.
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    UPDATE_INTERVAL,
                    MIN_DISTANCE_UPDATE, this);

            if (locationManager != null) {

                // Retrieves the last known reported location.
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                // Queries the Location object to retrieve the current coordinates.
                if (location != null) {
                    Log.d(LOG_TAG, "getLocation(): Querying network for the current location.");
                    onLocationChanged(location); // Invokes the onLocationChanged method.
                }

                // Outputs an error logcat message, indicating the Location object is not ready.
                else { Log.e(LOG_TAG, "getLocation(): Location retrieval is not ready."); }
            }
        }

        // GPS ENABLED:
        if (isGPSEnabled) {

            if (location == null) {

                // Requests a location update from the network.
                // TODO: Must request run-time permissions when target SDK is changed to 23.
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        UPDATE_INTERVAL,
                        MIN_DISTANCE_UPDATE, this);

                if (locationManager != null) {

                    // Retrieves the last known reported location.
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    // Queries the Location object to retrieve the current coordinates.
                    if (location != null) {
                        Log.d(LOG_TAG, "getLocation(): Querying GPS for the current location.");
                        onLocationChanged(location); // Invokes the onLocationChanged method.
                    }

                    // Outputs an error logcat message, indicating the Location object is not ready.
                    else { Log.e(LOG_TAG, "getLocation(): Location retrieval is not ready."); }
                }
            }
        }
    }

    /** PREFERENCES FUNCTIONALITY ______________________________________________________________ **/

    // loadPreferences(): Loads the SharedPreference values from the stored SharedPreferences object.
    private void loadPreferences() {

        // Initializes the SharedPreferences object.
        GTN_prefs = GTNPreference.initializePreferences(GTN_OPTIONS, this);
        GTN_temps = GTNPreference.initializePreferences(GTN_TEMPS, this);
        language = GTNPreference.getLanguage(GTN_prefs); // Retrieves the current language settings.

        // Retrieves the current language settings from the application's SharedPreferences object.
        setLanguage();
    }

    /** RECYCLE FUNCTIONALITY __________________________________________________________________ **/

    // recycleMemory(): Recycles ImageView and View objects to clear up memory resources prior to
    // Activity destruction.
    private void recycleMemory() {

        if (adView != null) { adView.destroy(); } // Destroy the AdView object.

        // Unbinds all Drawable objects attached to the current layout.
        try { GTNUnbind.unbindView(findViewById(R.id.gtn_main_layout)); }
        catch (NullPointerException e) { e.printStackTrace(); } // Prints error message.
    }

    /** INTERFACE FUNCTIONALITY _______________________________________________________________ **/

    // closeFragmentView(): An interface method that is invoked via the OnFragmentUpdateListener
    // interface class, which closes the fragment view.
    @Override
    public void closeFragmentView(String type) {
        removeFragment(type); // Removes the current displayed fragment.
    }

    // displaySnackbar(): An interface method that is invoked via the OnFragmentUpdateListener
    // interface class, which displays a Snackbar message.
    @Override
    public void displaySnackbar(String message) {

        // Displays a snackbar message display at the bottom of the screen.
        Snackbar
                .make(mainLayout, message, Snackbar.LENGTH_LONG)
                .show();
    }

    // editExistingShortcut(): An interface method that is invoked from the GTNShortcutsFragment to
    // prepare the GTNActionFragment view for editing the shortcut.
    @Override
    public void editExistingShortcut(GTNShortcut shortcut, Boolean isAnimated) {

        currentShortcut = shortcut; // Sets the current GTNShortcut in focus.
        setLanguage(); // Retrieves the current language settings.

        // Sets up and displays the GTNActionFragment view.
        gtn_main_toolbar.setTitle(gtn_languages.getModifyShortcutText()); // Changes the title of the toolbar.
        GTNActionFragment fragment = new GTNActionFragment(); // Creates a new GTNActionFragment object.
        fragment.initializeEditFragment(shortcut); // Initializes the GTNActionFragment with the GTNShortcut data.
        setUpFragment(fragment, "ACTION", isAnimated); // Displays the GTNActionFragment in EDIT SHORTCUT mode.

        // Indicates that the GTNActionFragment is currently shown in EDIT SHORTCUT mode.
        showActionFragmentEdit = true;
    }

    // getMapIntent(): An interface method that is invoked from the GTNListView class, which
    // launches navigation mode for the selected shortcut.
    @Override
    public void launchNavigation(GTNShortcut shortcut) {

        // Checks to see if Google Maps is installed first. If it exists, the Google Maps
        // intent is launched directly in navigation mode.
        if (GTNConditions.checkForAppInstalled(this)) {

            // Retrieves the shortcut type.
            String shortcutType = shortcut.getShortcutType();

            // Checks the shortcut type for legacy String types and corrects the
            // type for the current String type.
            shortcutType = GTNTypes.checkShortcutType(shortcutType);

            // Launches an intent to start Google Maps in navigation mode.
            Intent mapIntent = GTNMapsIntent.getMapIntent(shortcut.getShortcutAddress(), shortcutType, true, this);
            startActivity(mapIntent); // Launches the intent to Google Maps.
        }

        // Displays a Snackbar message, notifying the user that Google Maps needs to be installed.
        else { displaySnackbar(gtn_languages.getGoogleMapsErrorText()); }
    }

    // openActionView(): An interface method that is invoked from the GTNShortcutsFragment class,
    // which opens the GTNActionFragment view.
    @Override
    public void openActionView(String address, Boolean isAnimated) {

        setLanguage(); // Retrieves the current language settings.

        // Changes the title of the toolbar.
        gtn_main_toolbar.setTitle(gtn_languages.getAddShortcutText());

        // Sets up the GTNActionFragment view.
        GTNActionFragment fragment = new GTNActionFragment();
        fragment.initializeNewFragment(address);
        setUpFragment(fragment, "ACTION", isAnimated);

        // Indicates that the GTNActionFragment is currently being shown in NEW SHORTCUT mode.
        showActionFragmentNew = true;
    }

    // updateLanguage(): An interface method that signals the GTNShortcutsFragment class to update
    // it's language attributes.
    public void updateLanguage() {
        try { ((OnShortcutUpdateListener) gtn_shortcuts_fragment).updateShortcutLanguage(); }
        catch (ClassCastException cce) { } // Catch for class cast exception errors.
    }

    // updateShortcuts(): An interface method that signals the GTNShortcutsFragment class to update
    // the shortcuts listing.
    @Override
    public void updateShortcuts(LinkedList<GTNShortcut> shortcuts) {

        // Signals the GTNShortcutsFragment to update the shortcut ListView.
        try { ((OnShortcutUpdateListener) gtn_shortcuts_fragment).updateShortcutView(); }
        catch (ClassCastException cce) { } // Catch for class cast exception errors.
    }

    // updateToolbar(): An interface method from the GTNSettingsFragment class, which updates the
    // toolbar name.
    @Override
    public void updateToolbar(String title) {
        gtn_main_toolbar.setTitle(title); // Sets the name of the toolbar.
    }

    // updateLocation(): An interface method that signals GTNShortcutsFragment to update the Location
    // to use with Google Places API.
    public void updateLocation(Location location) {
        try { ((OnShortcutUpdateListener) gtn_shortcuts_fragment).updateLocation(location); }
        catch (ClassCastException cce) { } // Catch for class cast exception errors.
    }

    // updateVoice(): An interface method that signals GTNShortcutsFragment to update the quick
    // navigation input line with the data received from the user's voice input.
    public void updateVoice(String voiceInput) {
        try { ((OnShortcutUpdateListener) gtn_shortcuts_fragment).updateVoiceInput(voiceInput); }
        catch (ClassCastException cce) { } // Catch for class cast exception errors.
    }
}