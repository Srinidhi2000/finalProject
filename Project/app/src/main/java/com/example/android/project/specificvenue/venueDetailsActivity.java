package com.example.android.project.specificvenue;

import android.Manifest;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.project.R;
import com.example.android.project.diary;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
//To display the details of a specific venue
public class venueDetailsActivity extends AppCompatActivity  {
    private static  String SPECIFIC_VENUE="null";
    ProgressBar loading;
    String id;
    TextView condition,temp;
    TextView empty,description,url,phone,address,category,rating,status;
    LinearLayout details,weather;
     public static String name,address_text,category_text;
    ImageView bookmark;
    private static final int PLACE_LOADER_ID=1;
    private static final int WEATHER_LOADER_ID=2;
    private MapView mMapView;
    private GoogleMap gmap;
    private LatLngBounds mapBoundary;
    private double latitude ;
    private double longitude;
    Bundle mapViewBundle=null;
    private static String WEATHER_URL="http://api.openweathermap.org/data/2.5/weather?APPID=d8104cf3baf5fb92aaeccdd160277e88";
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.venue_details);
        empty=findViewById(R.id.empty);
        loading=findViewById(R.id.loading);
        bookmark=findViewById(R.id.bookmark);
        description=findViewById(R.id.description);
        url=findViewById(R.id.url);
        phone=findViewById(R.id.phone);
        address=findViewById(R.id.address);
        category=findViewById(R.id.category);
        rating=findViewById(R.id.rating);
        status=findViewById(R.id.open);
        details=findViewById(R.id.details);
        condition=findViewById(R.id.condition);
        temp=findViewById(R.id.temp);
        weather=findViewById(R.id.weatherReport);
        details.setVisibility(View.GONE);
        weather.setVisibility(View.GONE);
        Intent intent=getIntent();
        id=intent.getStringExtra("ID");
        SPECIFIC_VENUE="https://api.foursquare.com/v2/venues/"+id+"?client_id="+
                getString(R.string.CLIENT_ID)+"&client_secret="+getString(R.string.CLIENT_PASS);
        String date = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
        SPECIFIC_VENUE=SPECIFIC_VENUE+"&v="+date;
        //To check for internet connection
        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo active=connectivityManager.getActiveNetworkInfo();
        if(active!=null&&active.isConnected())
        {
            LoaderManager loaderManager=getLoaderManager();
            loaderManager.initLoader(PLACE_LOADER_ID,null,venue);

        }else{
            loading.setVisibility(View.GONE);
            empty.setText("No internet connection.");
        }

        getLoaderManager().initLoader(WEATHER_LOADER_ID, null, report);
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mMapView = findViewById(R.id.venue_map);
        mMapView.onCreate(mapViewBundle);

    }
//Loader for getting the weather report
private LoaderCallbacks<Weather> report=new LoaderCallbacks<Weather>() {
    @Override
    public Loader<Weather> onCreateLoader(int id, Bundle args) {
        WEATHER_URL=WEATHER_URL+"&lat="+latitude+"&lon="+longitude;
        return new weather_loader(getApplicationContext(),WEATHER_URL);
    }

    @Override
    public void onLoadFinished(Loader<Weather> loader, Weather data) {
if(data!=null)
{

temp.setText(data.getTemp());
condition.setText(data.getCondition());
}
    }

    @Override
    public void onLoaderReset(Loader<Weather> loader) {

    }
};
    //Loader for getting the venue details
    private LoaderCallbacks<VenueDetails> venue=new LoaderCallbacks<VenueDetails>() {
        @Override
        public Loader<VenueDetails> onCreateLoader(int id, Bundle args) {
            return new venueDetails_loader(getApplicationContext(),SPECIFIC_VENUE);
        }

        @Override
        public void onLoadFinished(Loader<VenueDetails> loader, VenueDetails data) {
            loading.setVisibility(View.GONE);
            empty.setText("No info found");
            if(data!=null)
            {
                description.setVisibility(View.VISIBLE);
                details.setVisibility(View.VISIBLE);
                name=data.getName();
                setTitle(name);
                phone.setText(data.getPhone());
                latitude=Double.parseDouble(data.getLatitude());
                longitude=Double.parseDouble(data.getLongitude());
                address.setText(data.getAddress());
                address_text=data.getAddress();
                category.setText(data.getCategory());
                category_text=data.getCategory();
                rating.setText(data.getRating());
                description.setText(data.getDescription());
                status.setText(data.getStatus());
                url.setText(data.getUrl());
                final Uri selectUrl=Uri.parse(data.getUrl());
                url.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(Intent.ACTION_VIEW,selectUrl);
                        startActivity(intent);
                    }
                });
                weather.setVisibility(View.VISIBLE);
                mMapView.getMapAsync(setvenue);
                empty.setVisibility(View.GONE);
            }

        }

        @Override
        public void onLoaderReset(Loader<VenueDetails> loader) {

        }
    };
//To zoom in and set the marker at the venue
    private void setCameraview() {
        double bottom = latitude - .1;
        double left = longitude - .1;
        double top = latitude + .1;
        double right = longitude + .1;
        mapBoundary = new LatLngBounds(
                new LatLng(bottom, left), new LatLng(top, right)
        );
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.12);
        gmap.moveCamera(CameraUpdateFactory.newLatLngBounds(mapBoundary, width,height,padding));
        gmap.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).title("Marker"));

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }
    //TO display the map when it is ready
    private OnMapReadyCallback setvenue=new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                   return;
            }
            googleMap.setMyLocationEnabled(true);
            gmap=googleMap;
            setCameraview();

        }
    };

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
//To write into the user's personal diary
    public void diary(View view)
    {Intent intent=new Intent(venueDetailsActivity.this,diary.class);
    intent.putExtra("bookmark",false);
    intent.putExtra("UserID",id);
    intent.putExtra("name",name);
    startActivity(intent);

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
