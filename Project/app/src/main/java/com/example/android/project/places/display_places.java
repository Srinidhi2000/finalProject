package com.example.android.project.places;


import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.app.LoaderManager.LoaderCallbacks;

import com.example.android.project.R;
import com.example.android.project.specificvenue.venueDetailsActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
//Displays a list of venues based on the user's query
public class display_places extends AppCompatActivity implements LoaderCallbacks<List<Place>> {
    private TextView Empty;
    private placesAdapter adapter;
    ProgressBar loading;
    ListView placeslistview;
    private static final int PLACE_LOADER_ID = 1;
    public static String BASE_URL=null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String CLIENT_ID = getString(R.string.CLIENT_ID);
        String CLIENT_PASS = getString(R.string.CLIENT_PASS);
        BASE_URL="https://api.foursquare.com/v2/venues/explore?client_id="+ CLIENT_ID +"&client_secret="+ CLIENT_PASS +"&limit=20";
        Intent intent=getIntent();
        String type=intent.getStringExtra("type");
        findtype(type);
        double lat=intent.getDoubleExtra("lat",0);
        double lon=intent.getDoubleExtra("lon",0);
        BASE_URL=BASE_URL+"&ll="+lat+","+lon;
        String date = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
        BASE_URL=BASE_URL+"&v="+date;
        setContentView(R.layout.places);
        Empty=findViewById(R.id.emptystate);
        loading=findViewById(R.id.loading);
        placeslistview=findViewById(R.id.list);
        placeslistview.setEmptyView(Empty);
        adapter=new placesAdapter(this,new ArrayList<Place>());
        placeslistview.setAdapter(adapter);
        placeslistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Place current=adapter.getItem(position);
                String placeId=current.getId();
                Intent intent=new Intent(display_places.this,venueDetailsActivity.class);
                intent.putExtra("ID",placeId);
                startActivity(intent);
            }
        });
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo active = connectivityManager.getActiveNetworkInfo();
        if (active != null && active.isConnected()) {
            android.app.LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(PLACE_LOADER_ID, null, this);

        } else {
            loading.setVisibility(View.GONE);
            Empty.setText("No internet connection.");
        }
    }
    //Checks if the user clicked food or top picks or shopping
private void findtype(String type)
{switch(type)
{
    case "top":break;
    case "food":{BASE_URL=BASE_URL+"&query=food";break;}
    case "shopping": {BASE_URL=BASE_URL+"&query=shop";break;}
    default :BASE_URL=BASE_URL+"&query="+type;
}
}

    @Override
    public Loader<List<Place>> onCreateLoader(int id, Bundle args) {
        return new places_loader(this,BASE_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Place>> loader, List<Place> data) {
loading.setVisibility(View.GONE);
adapter.clear();
Empty.setText("No nearby places found");
if(data!=null&&!data.isEmpty())
{ adapter.addAll(data);
  Empty.setVisibility(View.GONE);
}
    }

    @Override
    public void onLoaderReset(Loader<List<Place>> loader) {
adapter.clear();
    }
}
