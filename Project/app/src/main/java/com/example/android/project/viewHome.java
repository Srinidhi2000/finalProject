package com.example.android.project;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.android.project.login.MainActivity;
import com.example.android.project.places.display_places;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

//Homepage
public class viewHome extends AppCompatActivity implements View.OnClickListener {
    boolean locationpermission = false;
    public static double latitude;
    public static double longitude;
    private static final String TAG = "Home";
    private static final int PERMISSIONS_REQUEST_ENABLE_GPS = 100;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 101;
    private static final int ERROR_DIALOG_REQUEST = 102;
    private FusedLocationProviderClient fusedLocation;
    TextView curlocationtext,setcurloc;
    EditText search;
 static int setToCurrentLoc=0;
 LinearLayout topPicks,food,shopping;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        setcurloc=findViewById(R.id.setCurLoc);
        curlocationtext=findViewById(R.id.curLocationText);
        topPicks=findViewById(R.id.topPicks);
        topPicks.setOnClickListener(this);
        food=findViewById(R.id.food);
        food.setOnClickListener(this);
        shopping=findViewById(R.id.shopping);
        shopping.setOnClickListener(this);
        search=findViewById(R.id.search_edittext);
        //To search based on what the user types on the search view
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Intent intent=new Intent(viewHome.this,display_places.class);
                intent.putExtra("type",search.getText().toString());
                intent.putExtra("lat",latitude);
                intent.putExtra("lon",longitude);
                startActivity(intent);
                return false;
            }
        });
        fusedLocation = LocationServices.getFusedLocationProviderClient(this);
    setcurloc.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setToCurrentLoc=0;
            preferLocation();
        }
    });
    }
//to check if current location to be displayed or there is a change in the map
private void preferLocation()
{
   if(setToCurrentLoc==0)
    {
        Log.d(TAG, "current");
        getLastLocation();
    }
else
   { if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
       return;
   }
    getdetails();
   }
}
//To get the last known location
    private void getLastLocation() {

        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocation.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
              if(task.isSuccessful()){
                  Location location=task.getResult();
              latitude=location.getLatitude();
                  longitude=location.getLongitude();
               getdetails();
              }
            }
        });
    }
    //To get the address to be displayed
    private void getdetails()
    { Geocoder geocoder=new Geocoder(viewHome.this, Locale.getDefault());
    try{
        List<Address> addresses=geocoder.getFromLocation(latitude,longitude,1);
      Address address=addresses.get(0);
        String locationName;
      if(address.getLocality()==null)
      { if(address.getAdminArea()==null)
      {
          locationName=address.getCountryName();
      }
      else
          locationName=address.getAdminArea()+","+address.getCountryName();
      }
      else
          {
              locationName=address.getLocality()+","+address.getAdminArea()+","+address.getCountryName();}
              curlocationtext.setText(locationName);
    } catch (IOException e) {
        e.printStackTrace();
    }

    }
//Checks if location is enabled and whether the app can use GPS
    private boolean checkMapServices(){
        if(isServicesOK()){
            if(isMapsEnabled()){
                return true;
            }
        }
        return false;
    }
//Show a dialog box if GPS is not enabled
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This application requires GPS to work properly, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Intent enableGpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public boolean isMapsEnabled(){
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
            return false;
        }
        return true;
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled  callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationpermission = true;
            getPlace();
            preferLocation();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(viewHome.this);

        if(available == ConnectionResult.SUCCESS){
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(viewHome.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        locationpermission = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationpermission = true;
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: called.");
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ENABLE_GPS: {
                if(locationpermission){
                    getPlace();
                   preferLocation();
                }
                else{
                    getLocationPermission();
                }
            }
        }

    }
    //To change the location
    private void getPlace()
    {
        curlocationtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(viewHome.this,MapsActivity.class);
                intent.putExtra("use","display");
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(checkMapServices()){
            if(locationpermission){
                getPlace();
                preferLocation();
            }
            else{
                getLocationPermission();
            }
        }
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public void onClick(View v) {
        int id= v.getId();
        switch(id)
        {
            case R.id.topPicks:
            {Intent intent=new Intent(viewHome.this,display_places.class);
                intent.putExtra("type","top");
                intent.putExtra("lat",latitude);
                intent.putExtra("lon",longitude);
                startActivity(intent);
                break;}
            case R.id.food: {
                Intent intent2 = new Intent(viewHome.this, display_places.class);
                intent2.putExtra("type", "food");
                intent2.putExtra("lat",latitude);
                intent2.putExtra("lon",longitude);
                startActivity(intent2);
                break;
            }
                case R.id.shopping: {
                    Intent intent3 = new Intent(viewHome.this, display_places.class);
                    intent3.putExtra("type", "shopping");
                    intent3.putExtra("lat",latitude);
                    intent3.putExtra("lon",longitude);
                    startActivity(intent3);
                    break;
                }
                }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
int id=item.getItemId();
if(id==R.id.signout)
{ Intent intent=new Intent(viewHome.this,MainActivity.class);
startActivity(intent);}
if(id==R.id.markedPlaces)
{ Intent intent=new Intent(viewHome.this,markedPlaces_list.class);
startActivity(intent);
}

        return super.onOptionsItemSelected(item);
    }
}
