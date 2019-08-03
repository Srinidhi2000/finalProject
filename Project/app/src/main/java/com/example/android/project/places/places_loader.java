package com.example.android.project.places;

import android.content.Context;

import java.util.List;
import android.content.AsyncTaskLoader;

import com.example.android.project.retrieve_json;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class places_loader extends AsyncTaskLoader<List<Place>> {
    private String url;
    public places_loader(@NonNull Context context,String requiredUrl) {
        super(context);
        url=requiredUrl;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
//Fetches the json
    @Nullable
    @Override
    public List<Place> loadInBackground() {
      if(url==null)
        return null;
      List<Place> places=retrieve_json.fetchdata(url);
      return places;
    }
}
