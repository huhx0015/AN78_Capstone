package com.huhx0015.gotherenow.model;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.maps.model.LatLngBounds;

/**
 * Created by Michael Yoon Huh on 5/31/2016.
 */
public class AutoCompleteParameters {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    private AutocompleteFilter mPlaceFilter;
    private CharSequence mConstraint;
    private GoogleApiClient mGoogleApiClient;
    private LatLngBounds mBounds;

    /** CONSTRUCTOR METHODS ____________________________________________________________________ **/

    public AutoCompleteParameters(AutocompleteFilter filter, CharSequence constraint,
                                  GoogleApiClient client, LatLngBounds bounds) {
        this.mPlaceFilter = filter;
        this.mConstraint = constraint;
        this.mGoogleApiClient = client;
        this.mBounds = bounds;
    }

    /** GET METHODS ____________________________________________________________________________ **/

    public AutocompleteFilter getmPlaceFilter() {
        return mPlaceFilter;
    }

    public CharSequence getmConstraint() {
        return mConstraint;
    }

    public GoogleApiClient getmGoogleApiClient() {
        return mGoogleApiClient;
    }

    public LatLngBounds getmBounds() {
        return mBounds;
    }

    /** SET METHODS ____________________________________________________________________________ **/

    public void setmPlaceFilter(AutocompleteFilter mPlaceFilter) {
        this.mPlaceFilter = mPlaceFilter;
    }

    public void setmConstraint(CharSequence mConstraint) {
        this.mConstraint = mConstraint;
    }

    public void setmGoogleApiClient(GoogleApiClient mGoogleApiClient) {
        this.mGoogleApiClient = mGoogleApiClient;
    }

    public void setmBounds(LatLngBounds mBounds) {
        this.mBounds = mBounds;
    }
}