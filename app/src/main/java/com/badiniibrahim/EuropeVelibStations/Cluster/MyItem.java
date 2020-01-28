package com.badiniibrahim.EuropeVelibStations.Cluster;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by Promobile on 30/03/2017.
 */

public class MyItem implements ClusterItem {
    private final LatLng mPosition;
    private String adresse;
    private int available_bike_stands;
    private int available_bikes;
    private String name;

    public MyItem(LatLng position, String adresse, int available_bike_stands, int available_bikes, String name) {
        mPosition = position;
        this.adresse = adresse;
        this.available_bike_stands = available_bike_stands;
        this.available_bikes = available_bikes;
        this.name = name;
    }


    public int getAvailable_bike_stands() {
        return available_bike_stands;
    }

    public void setAvailable_bike_stands(int available_bike_stands) {
        this.available_bike_stands = available_bike_stands;
    }

    public int getAvailable_bikes() {
        return available_bikes;
    }

    public void setAvailable_bikes(int available_bikes) {
        this.available_bikes = available_bikes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
}

