package com.badiniibrahim.EuropeVelibStations.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.badiniibrahim.EuropeVelibStations.R;

public class Licence extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_licence);
        //if extend AppCompa use this methode to display back button in actionbar
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
