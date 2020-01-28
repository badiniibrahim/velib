package com.badiniibrahim.EuropeVelibStations.Models;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Promobile on 14/03/2017.
 */
public class Favoris {

    private String name;
    private String number;
    private String country_name;
    private  LatLng mPosition = null;

    public LatLng getPosition() {
        return mPosition;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Favoris favoris = (Favoris) o;

        if (!name.equals(favoris.name)) return false;
        if (!country_name.equals(favoris.country_name)) return false;
        return mPosition.equals(favoris.mPosition);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + country_name.hashCode();
        result = 31 * result + mPosition.hashCode();
        return result;
    }

    public Favoris(String name, LatLng position, String country_name) {
        this.name = name;
        this.country_name = country_name;
        this.mPosition = position;

    }




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }





    @Override
    public String toString() {
        return name ;
    }
}
