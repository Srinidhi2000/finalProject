package com.example.android.project.specificvenue;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.android.project.retrieve_json;

public class venueDetails_loader extends AsyncTaskLoader<VenueDetails> {
    private String url;
    public venueDetails_loader(Context context,String givenUrl) {
        super(context);
        url=givenUrl;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
//Fetch details from json
    @Override
    public VenueDetails loadInBackground() {
        if(url==null)
        return null;
        VenueDetails details=retrieve_json.fetchDetails(url);
        return details;
    }
}
