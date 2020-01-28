package com.badiniibrahim.EuropeVelibStations.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.badiniibrahim.EuropeVelibStations.Favoris.FavorisListViewAdapter;
import com.badiniibrahim.EuropeVelibStations.Models.Favoris;
import com.badiniibrahim.EuropeVelibStations.Preference.ShardedPreference;
import com.badiniibrahim.EuropeVelibStations.R;
import com.daimajia.swipe.SwipeLayout;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity {

    ListView mListView;
    ShardedPreference mShardedPreference;
    ArrayList<Favoris> mFavorisArrayList;
    ArrayAdapter<Favoris> adapter;
    //to swipe
    private SwipeLayout mSwipeLayout;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        setTitle("Mes Favoris");
        mListView = (ListView)findViewById(R.id.list);
        mShardedPreference = new ShardedPreference();
        mFavorisArrayList = mShardedPreference.getFavorites(getApplicationContext());
        setListViewAdapter();
    }


    private void setListViewAdapter(){
        adapter = new FavorisListViewAdapter(this, R.layout.item_listview_favoris, mFavorisArrayList);
        if (mFavorisArrayList == null){
            mListView.setEmptyView(findViewById(R.id.empty));
        }else {
            mListView.setAdapter(adapter);

        }
    }


    /*
      to refresh current activity
     http://stackoverflow.com/questions/18080135/refresh-current-activity-without-delay
    */
    public void updateAdapter() {
        adapter.notifyDataSetChanged();
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

}
