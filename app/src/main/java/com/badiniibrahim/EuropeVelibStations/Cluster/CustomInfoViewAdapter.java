package com.badiniibrahim.EuropeVelibStations.Cluster;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.badiniibrahim.EuropeVelibStations.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Promobile on 30/03/2017.
 * to display a custom window by clicking on the marker
 */

public class CustomInfoViewAdapter implements  GoogleMap.InfoWindowAdapter {

    private final LayoutInflater mInflater;

    public CustomInfoViewAdapter(LayoutInflater inflater){
        mInflater = inflater;
    }




    @Override
    public View getInfoWindow(Marker marker) {

        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = mInflater.inflate(R.layout.custom_marker_info, null);
        TextView textView = (TextView)view.findViewById(R.id.test);
        textView.setText(marker.getTitle());
        return view;
    }
}
