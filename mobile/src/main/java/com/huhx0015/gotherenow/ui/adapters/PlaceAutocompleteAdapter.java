/*
 * Copyright (C) 2015 Google Inc. All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.huhx0015.gotherenow.ui.adapters;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.data.DataBufferUtils;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLngBounds;
import com.huhx0015.gotherenow.interfaces.OnAutoCompleteResponseListener;
import com.huhx0015.gotherenow.model.AutoCompleteParameters;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Adapter that handles Autocomplete requests from the Places Geo Data API.
 * {@link AutocompletePrediction} results from the API are frozen and stored directly in this
 * adapter. (See {@link AutocompletePrediction#freeze()}.)
 * <p>
 * Note that this adapter requires a valid {@link com.google.android.gms.common.api.GoogleApiClient}.
 * The API client must be maintained in the encapsulating Activity, including all lifecycle and
 * connection states. The API client must be connected with the {@link Places#GEO_DATA_API} API.
 */
public class PlaceAutocompleteAdapter extends ArrayAdapter<AutocompletePrediction> implements Filterable {

    private static final String TAG = "PlaceAutocompleteAdapter";
    private static final CharacterStyle STYLE_BOLD = new StyleSpan(Typeface.BOLD);
    /**
     * Current results returned by this adapter.
     */
    private ArrayList<AutocompletePrediction> mResultList;

    /**
     * Handles autocomplete requests.
     */
    private GoogleApiClient mGoogleApiClient;

    /**
     * The bounds used for Places Geo Data autocomplete API requests.
     */
    private LatLngBounds mBounds;

    /**
     * The autocomplete filter used to restrict queries to a specific set of place types.
     */
    private AutocompleteFilter mPlaceFilter;

    /**
     * Initializes with a resource for text rows and autocomplete query bounds.
     *
     * @see android.widget.ArrayAdapter#ArrayAdapter(android.content.Context, int)
     */
    public PlaceAutocompleteAdapter(Context context, GoogleApiClient googleApiClient,
                                    LatLngBounds bounds, AutocompleteFilter filter) {
        super(context, android.R.layout.simple_expandable_list_item_2, android.R.id.text1);
        mGoogleApiClient = googleApiClient;
        mBounds = bounds;
        mPlaceFilter = filter;
    }

    /**
     * Sets the bounds for all subsequent queries.
     */
    public void setBounds(LatLngBounds bounds) {
        mBounds = bounds;
    }

    /**
     * Returns the number of results received in the last autocomplete query.
     */
    @Override
    public int getCount() {

        if (mResultList != null) {
            return mResultList.size();
        }
        else {
            return 0;
        }
    }

    /**
     * Returns an item from the last autocomplete query.
     */
    @Override
    public AutocompletePrediction getItem(int position) {
        return mResultList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = super.getView(position, convertView, parent);

        // Sets the primary and secondary text for a row.
        // Note that getPrimaryText() and getSecondaryText() return a CharSequence that may contain
        // styling based on the given CharacterStyle.

        AutocompletePrediction item = getItem(position);

        TextView textView1 = (TextView) row.findViewById(android.R.id.text1);
        TextView textView2 = (TextView) row.findViewById(android.R.id.text2);
        textView1.setText(item.getPrimaryText(STYLE_BOLD));
        textView2.setText(item.getSecondaryText(STYLE_BOLD));

        return row;
    }

    /**
     * Returns the filter for the current set of autocomplete results.
     */
    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                // Skip the autocomplete query if no constraints are given.
                if (constraint != null) {

                    // Query the autocomplete API for the (constraint) search string.
                    getAutocomplete(constraint);

                    if (mResultList != null) {

                        // The API successfully returned results
                        results.values = mResultList;
                        results.count = mResultList.size();
                    }
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {

                    // The API returned at least one result, update the data.
                    notifyDataSetChanged();
                } else {

                    // The API did not return any results, invalidate the data set.
                    notifyDataSetInvalidated();
                }
            }

            @Override
            public CharSequence convertResultToString(Object resultValue) {

                // Override this method to display a readable result in the AutocompleteTextView
                // when clicked.
                if (resultValue instanceof AutocompletePrediction) {
                    return ((AutocompletePrediction) resultValue).getFullText(null);
                } else {
                    return super.convertResultToString(resultValue);
                }
            }
        };
    }

    /**
     * Submits an autocomplete query to the Places Geo Data Autocomplete API.
     * Results are returned as frozen AutocompletePrediction objects, ready to be cached.
     * objects to store the Place ID and description that the API returns.
     * Returns an empty list if no results were found.
     * Returns null if the API client is not available or the query did not complete
     * successfully.
     * This method MUST be called off the main UI thread, as it will block until data is returned
     * from the API, which may include a network request.
     *
     * @param constraint Autocomplete query string
     * @return Results from the autocomplete API or null if the query was not successful.
     * @see Places#GEO_DATA_API#getAutocomplete(CharSequence)
     * @see AutocompletePrediction#freeze()
     */
    private void getAutocomplete(CharSequence constraint) {

        if (mGoogleApiClient.isConnected()) {

            Log.d(TAG, "Starting autocomplete query: " + constraint);

            AutoCompleteParameters parameters = new AutoCompleteParameters(mPlaceFilter, constraint, mGoogleApiClient, mBounds);
            GooglePlacesAutoCompleteAsync asyncTask = new GooglePlacesAutoCompleteAsync(new OnAutoCompleteResponseListener() {
                @Override
                public ArrayList<AutocompletePrediction> processFinish(AutocompletePredictionBuffer predictionOutput) {

                    // Confirm that the query completed successfully, otherwise return null
                    final Status status = predictionOutput.getStatus();
                    if (!status.isSuccess()) {
                        Log.i("Error contacting API", "Error contacting API");
                        Toast.makeText(getContext(), "Error contacting API: " + status.toString(),
                                Toast.LENGTH_SHORT).show();
                        predictionOutput.release();
                        return null;
                    }
                    mResultList = DataBufferUtils.freezeAndClose(predictionOutput);

                    // Freeze the results immutable representation that can be stored safely.
                    return mResultList;
                }
            });
            asyncTask.execute(parameters);

        } else {
            Log.e(TAG, "Google API client is not connected for autocomplete query.");
        }
    }

    /** SUBCLASSES _____________________________________________________________________________ **/

    public class GooglePlacesAutoCompleteAsync extends AsyncTask<AutoCompleteParameters, Void, AutocompletePredictionBuffer>{

        public OnAutoCompleteResponseListener listener = null;

        public GooglePlacesAutoCompleteAsync(OnAutoCompleteResponseListener listener) {
            this.listener = listener;
        }

        @Override
        protected AutocompletePredictionBuffer doInBackground(AutoCompleteParameters... params) {

            AutocompleteFilter placeFilter = params[0].getmPlaceFilter();
            CharSequence constraint = params[0].getmConstraint();
            GoogleApiClient googleApiClient = params[0].getmGoogleApiClient();
            LatLngBounds bounds = params[0].getmBounds();

            PendingResult<AutocompletePredictionBuffer> results =
                    Places.GeoDataApi.getAutocompletePredictions(googleApiClient, constraint.toString(), bounds, placeFilter);

            return results.await(60, TimeUnit.SECONDS);
        }

        @Override
        protected void onPostExecute(AutocompletePredictionBuffer result){
            listener.processFinish(result);
        }
    }
}