package com.example.android.project;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private MapView mMapView;
    private GoogleMap gmap;
    private LatLngBounds mapBoundary;
    private double latitude = viewHome.latitude;
    private double longitude = viewHome.longitude;
EditText search_address;
RelativeLayout address_bar;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        search_address=findViewById (R.id.search_address);
        address_bar=findViewById(R.id.searchaddress_bar);
        address_bar.setVisibility(View.GONE);
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mMapView =  findViewById(R.id.user_map);
        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);

    }
    //To zoom in and set the marker at the location specified
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
    // Checks if the user types in a location or wants the current location
private void setLocation()
{
    search_address.setOnEditorActionListener(new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if(actionId==EditorInfo.IME_ACTION_SEARCH||actionId==EditorInfo.IME_ACTION_DONE||event.getAction()==KeyEvent.KEYCODE_ENTER)
            {
getdetails();
            }

            return false;
        }
    });
}
//If the user types a location then the function gets the lat and lng coordinates of the place
    private void getdetails()
    { Geocoder geocoder=new Geocoder(MapsActivity.this);
      String searchString=search_address.getText().toString();
        List<Address> addresses=new ArrayList<>();

        try{ addresses=geocoder.getFromLocationName(searchString,1);
          } catch (IOException e) {
            e.printStackTrace();
        }
        if(addresses.size()>0)
        { Address address=addresses.get(0);
           latitude=address.getLatitude();
           longitude=address.getLongitude();
        viewHome.latitude=latitude;
        viewHome.longitude=longitude;
        viewHome.setToCurrentLoc=1;
            setCameraview();
        gmap.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).title("Marker"));
        }

    }

    @Override
    public void onMapReady(GoogleMap map) {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        View locationButton = ((View) mMapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams layout = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        layout.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        layout.setMargins(0, 180, 180, 0);
        map.setMyLocationEnabled(true);
        gmap=map;
    address_bar.setVisibility(View.VISIBLE);
        setLocation();
        setCameraview();
   gmap.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).title("Marker"));

    }

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
}
