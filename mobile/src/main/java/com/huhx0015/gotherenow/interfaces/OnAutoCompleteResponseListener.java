package com.huhx0015.gotherenow.interfaces;

import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import java.util.ArrayList;

/**
 * Created by Michael Yoon Huh on 5/31/2016.
 */
public interface OnAutoCompleteResponseListener {
    ArrayList<AutocompletePrediction> processFinish(AutocompletePredictionBuffer output);
}