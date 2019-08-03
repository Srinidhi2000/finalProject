package com.example.android.project;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import com.example.android.project.data.contract;

public class markedPlaces_adapter extends CursorAdapter {
    public markedPlaces_adapter(Context context, Cursor c) {
        super(context, c);
}
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.place_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView name =  view.findViewById(R.id.name);
        TextView category =  view.findViewById(R.id.category);
        TextView address =  view.findViewById(R.id.address);
        int nameIndex = cursor.getColumnIndex(contract.loginEntry.c8name);
        int categoryIndex = cursor.getColumnIndex(contract.loginEntry.c9category);
        int addressIndex = cursor.getColumnIndex(contract.loginEntry.c10address);
        String name_text = cursor.getString(nameIndex);
        String category_text = cursor.getString(categoryIndex);
        String address_text = cursor.getString(addressIndex);
        name.setText(name_text);
        category.setText(category_text);
        address.setText(address_text);
    }
}

