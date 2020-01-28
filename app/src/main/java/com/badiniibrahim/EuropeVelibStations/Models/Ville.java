package com.badiniibrahim.EuropeVelibStations.Models;

/**
 * Created by Promobile on 14/03/2017.
 */

public class Ville {

    private String name;
    private String commercial_name;
    private String country_code;

    public Ville(String name, String commercial_name) {
        this.name = name;
        this.commercial_name = commercial_name;
    }

    public Ville(String name, String commercial_name, String country_code) {
        this.name = name;
        this.commercial_name = commercial_name;
        this.country_code = country_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommercial_name() {
        return commercial_name;
    }

    public void setCommercial_name(String commercial_name) {
        this.commercial_name = commercial_name;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    @Override
    public String toString() {
        return name /*+ commercial_name*/ ;

    }
}
