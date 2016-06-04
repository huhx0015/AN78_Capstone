package com.huhx0015.gotherenow.intents;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.speech.RecognizerIntent;
import com.huhx0015.gotherenow.ui.language.GTNLanguage;
import java.util.List;
import java.util.Locale;

/** -----------------------------------------------------------------------------------------------
 *  [GTNVoiceIntent] CLASS
 *  PROGRAMMER: Huh X0015
 *  DESCRIPTION: GTNVoiceIntent class is used to provide functions to retrieve voice input from the
 *  user.
 *  -----------------------------------------------------------------------------------------------
 */

public class GTNVoiceIntent {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // VOICE VARIABLES
    private static final int VOICE_INPUT_CODE = 6400; // Code value used to listen for voice-related input.

    /** VOICE FUNCTIONALITY ____________________________________________________________________ **/

    // checkVoiceCapabilities(): Determines if the device has voice input capabilities or not.
    public static boolean checkVoiceCapabilities(Activity activity) {

        // Disable button if no recognition service is present
        PackageManager pm = activity.getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);

        // If no voice recognition services are present, a false value is returned.
        if (activities.size() == 0) { return false; }

        // Indicates that at least one voice recognition service is available.
        else { return true; }
    }

    // startVoiceRecognitionActivity(): Fires an intent to start the voice recognition activity.
    public static void startVoiceRecognitionActivity(Activity activity, GTNLanguage language) {

        // Sets up an Intent to launch the device's default voice recognition application to
        // retrieve the user's voice input.
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, language.getVoiceCommandHeaderText());
        activity.startActivityForResult(intent, VOICE_INPUT_CODE);
    }
}
