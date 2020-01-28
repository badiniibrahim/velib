package com.badiniibrahim.EuropeVelibStations.Activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;

import com.badiniibrahim.EuropeVelibStations.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //fragement
        FragmentManager fragment = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragment.beginTransaction();

        AppPreference appPreferencefragment = new AppPreference();
        fragmentTransaction.add(android.R.id.content, appPreferencefragment,"SETTINGS_FRAGMENT");
        fragmentTransaction.commit();

    }

    /**
     * this to permit to implement fragment to use to display
     * the preference activity
     */
    public static class AppPreference extends PreferenceFragment{
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preference_settings);

        }
    }



}
