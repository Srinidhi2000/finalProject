package com.example.android.project.specificvenue;

import android.content.Context;

import com.example.android.project.retrieve_json;

public class weather_loader extends android.content.AsyncTaskLoader<Weather> {
   private String Url;
    public weather_loader(Context context,String url) {
        super(context);
        Url=url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public Weather loadInBackground() {
        if(Url==null)
        return null;
        Weather report=retrieve_json.fetchWeather(Url);
        return report;
    }
}
