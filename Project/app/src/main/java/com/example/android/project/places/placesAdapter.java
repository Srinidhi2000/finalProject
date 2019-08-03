package com.example.android.project.places;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.project.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class placesAdapter extends ArrayAdapter<Place> {

    public placesAdapter(@NonNull Context context, List<Place> places) {
        super(context,0,places);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listitemView=convertView;
        if(listitemView==null)
        {listitemView=LayoutInflater.from(getContext()).inflate(R.layout.place_list_item,parent,false);
        }
       Place current=getItem(position);
        TextView category= listitemView.findViewById(R.id.category);
        TextView name= listitemView.findViewById(R.id.name);
        TextView address= listitemView.findViewById(R.id.address);
        category.setText(current.getCategories());
        name.setText(current.getName());
        address.setText(current.getLocation());
        return listitemView;
    }
}
