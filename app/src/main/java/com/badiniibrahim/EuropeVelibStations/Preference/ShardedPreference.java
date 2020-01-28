package com.badiniibrahim.EuropeVelibStations.Preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.badiniibrahim.EuropeVelibStations.Activity.FavoriteActivity;
import com.badiniibrahim.EuropeVelibStations.Models.Favoris;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Promobile on 31/03/2017.
 */

public class ShardedPreference {

    private static final String PREFS_NAME = "favoris_velo";
    public static final String FAVORITS_LIST = "favoris_list";
    private SharedPreferences.Editor editor;
    private SharedPreferences preferences;
    private FavoriteActivity mActivity;



    public ShardedPreference(){
        super();
    }

    public void saveFavoris(Context context, List<Favoris> favorisList){
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
        //A revoir a quoi sa sert on dirait que l'on utiliser du json a la place
        Gson gson = new Gson();
        String jsonFavoris = gson.toJson(favorisList);
        editor.putString(FAVORITS_LIST, jsonFavoris);
        editor.commit();
        //pour l'utiliser apres
        //// double latitude = Double.longBitsToDouble(prefs.getLong("Latitude", 0);
    }
    public void addFavorite(Context context, Favoris favoris) {
        List<Favoris> favorites = getFavorites(context);
        if (favorites == null)
            favorites = new ArrayList<Favoris>();
        favorites.add(favoris);
        saveFavoris(context, favorites);
    }

    public void removeFavorite(Context context, Favoris favoris) {

        ArrayList<Favoris> favorites = getFavorites(context);
        if (favorites != null) {
            favorites.remove(favoris);
            saveFavoris(context, favorites);
        }
    }

    public ArrayList<Favoris> getFavorites(Context context) {
        List<Favoris> favorites;

        preferences = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (preferences.contains(FAVORITS_LIST)) {
            String jsonFavorites = preferences.getString(FAVORITS_LIST, null);
            Gson gson = new Gson();
            Favoris[] favoriteItems = gson.fromJson(jsonFavorites, Favoris[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<Favoris>(favorites);
        } else
            return null;

        return (ArrayList<Favoris>) favorites;
    }
}
