package com.huhx0015.gotherenow.ui.language;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.view.View;

import com.huhx0015.gotherenow.library.R;
import java.util.Locale;

/** ------------------------------------------------------------------------------------------------
 *  [GTNLanguage] CLASS
 *  PROGRAMMER: Michael Yoon Huh (HUHX0015)
 *  DESCRIPTION: This class is used for defining the language strings and for providing language
 *  related functionality.
 *  ------------------------------------------------------------------------------------------------
 */

public class GTNLanguage {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // GTNActionFragment:
    private String addShortcutText; // Stores the string text value for ADD NEW SHORTCUT.
    private String modifyShortcutText; // Stores the string text value for MODIFY SHORTCUT.
    private String shortcutText; // Stores the string text value for SHORTCUT NAME.
    private String addressText; // Stores the string text value for ADDRESS.
    private String saveLocationText; // Stores the string text value for SAVE LOCATION.
    private String createShortcutText; // Stores the string text value for CREATE HOMESCREEN SHORTCUT.
    private String queryLocationText; // Stores the string text value for QUERY CURRENT LOCATION.
    private String navigationText; // Stores the string text value for START NAVIGATION.
    private String deleteLocationText; // Stores the string text value for DELETE LOCATION.

    // GTNSettingsFragment:
    private String settingsText; // Stores the string text value for SETTINGS.
    private String languageOptionsText; // Stores the string text for the LANGUAGE OPTIONS.
    private String languageTitleText; // Stores the string text for the LANGUAGE title.
    private String languageText; // Stores the string text for the LANGUAGE selection.
    private String syncTitleText; // Stores the string text for the ANDROID WEAR SYNCHRONIZATION title.
    private String syncOptionsText; // Stores the string text for the SYNCHRONIZATION SETTINGS option.
    private String syncOnText; // Stores the string text for the ANDROID WEAR SYNCHRONIZATION ON option.
    private String syncOffText; // Stores the string text for the ANDROID WEAR SYNCHRONIZATION OFF option.
    private String transportationTitleText; // Stores the string text for the DEFAULT TRANSPORATION title.
    private String cancelText; // Stores the string text for the CANCEL button.

    // GTNShortcutsFragment:
    private String locationShortcutText; // Stores the string text value for LOCATION SHORTCUTS.
    private String quickNavigationText; // Stores the string text value for QUICK NAVIGATION.
    private String quickNavigationHintText; // Stores the string text value for the QUICK NAVIGATION hint.

    // GTNVoiceIntent:
    private String voiceCommandHeaderText; // Stores the string text value for the VOICE COMMAND header text.

    // GTNCardActivity:
    private String wearInstructionText; // Stores the string text value for the ANDROID WEAR instructions text.
    private String voiceInstructionText; // Stores the string text value for the ANDROID WEAR VOICE instructions text.

    // Miscellaneous:
    private String addressErrorText; // Stores the address input error string message.
    private String addingShortcutText; // Stores the adding shortcut string message.
    private String deletingLocationText; // Stores the deleting location string message.
    private String googleMapsErrorText; // Stores the Google Maps error string message.
    private String launchNavigationText; // Stores the start navigation string message.
    private String locationFoundText; // Stores the location found string message.
    private String queryErrorText; // Stores the query error string message.
    private String savingLocationText; // Stores the saving location string message.
    private String shortcutNameErrorText; // Stores the shortcut name input error string message.

    /** INITIALIZATION FUNCTIONALITY ___________________________________________________________ **/

    // GTNLanguages(): Constructor for the GTNLanguages class.
    private final static GTNLanguage gtnLanguages = new GTNLanguage();

    // GTNLangauge(): Deconstructor for the GTNLanguages class.
    public GTNLanguage() {}

    // getInstance(): Returns the gtnLanguages instance.
    public static GTNLanguage getInstance() {
        return gtnLanguages;
    }

    // initializeGTN(): Initializes the GTNLanguage class variables by setting the default text
    // for the GTNLanguage class.
    public void initializeLanguages(String lang, Context context) { getLanguageText(lang, context); }

    /** GET FUNCTIONALITY ______________________________________________________________________ **/

    // GTNActionFragment:
    public String getAddShortcutText() { return addShortcutText; } // getAddShortcutText(): Retrieves addShortcutText string.
    public String getModifyShortcutText() { return modifyShortcutText; } // getModifyShortcutText(): Retrieves modifyShortcutText string.
    public String getShortcutText() { return shortcutText; } // getShortcutText(): Retrieves shortcutText string.
    public String getAddressText() { return addressText; } // getAddressText(): Retrieves addressText string.
    public String getSaveLocationText() { return saveLocationText; } // getSaveLocationText(): Retrieves saveLocationText string.
    public String getCreateShortcutText() { return createShortcutText; } // getCreateShortcutText(): Retrieves createShortcutText string.
    public String getQueryLocationText() { return queryLocationText; } // getQueryLocationText(): Retrieves queryLocationText string.
    public String getNavigationText() { return navigationText; } // getNavigationText(): Retrieves navigationText string.
    public String getDeleteLocationText() { return deleteLocationText; } // getDeleteLocationText(): Retrieves deleteLocationText string.

    // GTNSettingsFragment:
    public String getSettingsText() { return settingsText; } // getSettingsText(): Retrieves settingsText string.
    public String getLanguageOptionsText() { return languageOptionsText; } // getLanguageOptionsText(): Retrieves languageOptionsText string.
    public String getLanguageTitleText() { return languageTitleText; } // getLanguageTitleText(): Retrieves languageTitleText string.
    public String getLanguageText() { return languageText; } // getLanguageText(): Retrieves languageText string.
    public String getTransportationTitleText() { return transportationTitleText; } // getTransportationTitleText(): Retrieves transportationTitleText string.
    public String getSyncTitleText() { return syncTitleText; } // getSyncTitleText(): Retrieves syncTitleText string.
    public String getSyncOptionsText() { return syncOptionsText; } // getSyncOptionsText(): Retrieves syncOptionsText string.
    public String getSyncOnText() { return syncOnText; } // getSyncOnText(): Retrieves syncOnText string.
    public String getSyncOffText() { return syncOffText; } // getSyncOffText(): Retrieves syncOffText string.
    public String getCancelText() { return cancelText; } // getCancelText(): Retrieves the cancelText string.

    // GTNShortcutsFragment:
    public String getQuickNavigationText() { return quickNavigationText; } // getQuickNavigationText(): Retrieves the quickNavigationText string.
    public String getQuickNavigationHintText() { return quickNavigationHintText; } // getQuickNavigationHintText(): Retrieves the quickNavigationHintText string;
    public String getLocationShortcutText() { return locationShortcutText; } // getLocationShortcutText(): Retrieves locationShortcutText string.

    // GTNVoiceIntent:
    public String getVoiceCommandHeaderText() { return voiceCommandHeaderText; } // getVoiceCommandHeaderText(): Retrieves the voiceCommandHeaderText;

    // GTNCardActivity:
    public String getWearInstructionText() { return wearInstructionText; } // getWearInstructionText(): Retrieves the wearInstructionText string.
    public String getVoiceInstructionText() { return voiceInstructionText; } // getVoiceInstructionText(): Retrieves the voiceInstructionText string.

    // Miscellaneous:
    public String getAddingShortcutText() { return addingShortcutText; } // getAddingShortcutText(): Retrieves addingShortcutText string.
    public String getAddressErrorText() { return addressErrorText; } // getAddressErrorText(): Retrieves addressErrorText string.
    public String getDeletingLocationText() { return deletingLocationText; } // getDeletingLocationText(): Retrieves deletingLocationText string.
    public String getGoogleMapsErrorText() { return googleMapsErrorText; } // getGoogleMapsErrorText(): Retrieves googleMapsErrorText string.
    public String getLaunchNavigationText() { return launchNavigationText; } // getLaunchNavigationText(): Retrieves launchNavigationText string.
    public String getLocationFoundText() { return locationFoundText; } // getLocationFoundText(): Retrieves locationFoundText string.
    public String getQueryErrorText() { return queryErrorText; } // getQueryErrorText(): Retrieves queryErrorText string.
    public String getSavingLocationText() { return savingLocationText; } // getSavingLocationText(): Retrieves savingLocationText string.
    public String getShortcutNameErrorText() { return shortcutNameErrorText; } // getShortcutNameErrorText(): Retrieves shortcutNameErrorText string.

    /** LANGUAGE FUNCTIONALITY _________________________________________________________________ **/

    // getLanguageText(): Sets the proper text strings based on the language value.
    private void getLanguageText(String lang, Context context) {

        // GTNActionFragment:
        addShortcutText = context.getResources().getString(R.string.add_shortcut_text); // Sets the proper text for the ADD NEW SHORTCUT object.
        modifyShortcutText = context.getResources().getString(R.string.modify_shortcut_text); // Sets the proper text for the MODIFY SHORTCUT object.
        shortcutText = context.getResources().getString(R.string.shortcut_text); // Sets the proper text for the SHORTCUT NAME object.
        addressText = context.getResources().getString(R.string.address_text); // Sets the proper text for the SHORTCUT ADDRESS object.
        navigationText = context.getResources().getString(R.string.navigation_text); // Sets the proper text for the START NAVIGATION object.
        saveLocationText = context.getResources().getString(R.string.save_location_text); // Sets the proper text for the SAVE LOCATION object.
        createShortcutText = context.getResources().getString(R.string.create_shortcut_text); // Sets the proper text for the CREATE SHORTCUT.
        queryLocationText = context.getResources().getString(R.string.query_location_text); // Sets the proper text for the QUERY CURRENT LOCATION object.
        deleteLocationText = context.getResources().getString(R.string.delete_location_text); // Sets the proper text for the DELETE LOCATION object.

        // GTNSettingsFragment:
        settingsText = context.getResources().getString(R.string.settings_text); // Stores the string text value for SETTINGS.
        languageOptionsText = context.getResources().getString(R.string.language_options_text); // Stores the string text for the LANGUAGE OPTIONS.
        languageTitleText = context.getResources().getString(R.string.language_title_text); // Stores the string text for the LANGUAGE title.
        languageText = context.getResources().getString(R.string.english); // Stores the string text for the LANGUAGE selection.
        syncTitleText = context.getResources().getString(R.string.sync_title_text); // Stores the string text for ANDROID WEAR SYNCHRONIZATION.
        syncOptionsText = context.getResources().getString(R.string.sync_options_text); // Stores the string text for SYNCHRONIZATION SETTINGS.
        syncOnText = context.getResources().getString(R.string.sync_on_text); // Stores the string text for ANDROID WEAR SYNCHRONIZATION ON text.
        syncOffText = context.getResources().getString(R.string.sync_off_text); // Stores the string text for the ANDROID WEAR SYNCHRONIZATION OFF text.
        transportationTitleText = context.getResources().getString(R.string.transportation_title_text); // Stores the string text for the DEFAULT TRANSPORTATION text.
        cancelText = context.getResources().getString(R.string.cancel_text); // Stores the string text for the CANCEL button.

        // GTNShortcutsFragment:
        locationShortcutText = context.getResources().getString(R.string.location_shortcut_text); // Stores the string text value for LOCATION SHORTCUTS.
        quickNavigationText = context.getResources().getString(R.string.quick_navigation_text); // Stores the string text value for QUICK NAVIGATION.
        quickNavigationHintText = context.getResources().getString(R.string.quick_navigation_hint_text); // Stores the string text value for the QUICK NAVIGATION hint.

        // GTNVoiceIntent:
        voiceCommandHeaderText = context.getResources().getString(R.string.voice_command_header_text); // Stores the string text value for the voice command header text.

        // GTNCardActivity:
        wearInstructionText = context.getResources().getString(R.string.wear_instruction_text); // Stores the string text value for the default card fragment.
        voiceInstructionText = context.getResources().getString(R.string.voice_instruction_text); // Stores the string text value for the voice card fragment.

        // Miscellaneous:
        googleMapsErrorText = context.getResources().getString(R.string.google_maps_error_text); // Stores the Google Maps error string message.
        shortcutNameErrorText = context.getResources().getString(R.string.shortcut_name_error_text); // Stores the shortcut name input error string message.
        addressErrorText = context.getResources().getString(R.string.address_error_text); // Stores the address input error string message.
        addingShortcutText = context.getResources().getString(R.string.adding_shortcut_text); // Stores the adding shortcut string message.
        locationFoundText = context.getResources().getString(R.string.location_found_text); // Stores the location found string message.
        queryErrorText = context.getResources().getString(R.string.query_error_text); // Stores the query error string message.
        launchNavigationText = context.getResources().getString(R.string.launch_navigation_text); // Stores the start navigation string message.
        savingLocationText = context.getResources().getString(R.string.saving_location_text); // Stores the saving location string message.
        deletingLocationText = context.getResources().getString(R.string.deleting_location_text); // Stores the deleting location string message.
    }

    // getSystemLanguage(): Detects the device's current language settings and sets the application
    // language preference.
    public static String getSystemLanguage(Context context) {

        String language; // Stores the String value of the language currently used by the application.
        String systemLanguage = Locale.getDefault().getLanguage(); // Retrieves the device's current system language.

        // ENGLISH: Sets the application language preferences to English, as the default language for the application.
        language = context.getResources().getString(R.string.english);

        return language;
    }

    // isRTL(): Determines if the device is in RTL (Right-To-Left) mode.
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean isRTL(Context context) {
        Configuration config = context.getResources().getConfiguration();
        if (config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            return true;
        } else {
            return false;
        }
    }
}