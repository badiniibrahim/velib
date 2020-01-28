package com.badiniibrahim.EuropeVelibStations.Favoris;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.badiniibrahim.EuropeVelibStations.Activity.FavoriteActivity;
import com.badiniibrahim.EuropeVelibStations.Activity.MapsActivity;
import com.badiniibrahim.EuropeVelibStations.Models.Favoris;
import com.badiniibrahim.EuropeVelibStations.Preference.ShardedPreference;
import com.badiniibrahim.EuropeVelibStations.R;
import com.daimajia.swipe.SwipeLayout;

import java.util.List;

/**
 * Created by Promobile on 31/03/2017.
 */

public class FavorisListViewAdapter extends ArrayAdapter<Favoris> {

    private FavoriteActivity mActivity;
    private List<Favoris> mFavorisList;
    ShardedPreference mShardedPreference;
    MapsActivity mMainActivity;


    public FavorisListViewAdapter(FavoriteActivity activity, int resource, List<Favoris> favorises){
        super(activity, resource,favorises );
        this.mActivity = activity;
        this.mFavorisList = favorises;
        mMainActivity = new MapsActivity();
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater)mActivity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_listview_favoris, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(mFavorisList.get(position).getName());
        //to delete
        holder.btnDelete.setOnClickListener(onDeleteListener(position, holder));
        holder.btnEdit.setOnClickListener(onGoListener(position, holder));
        return convertView;
    }

    private View.OnClickListener onDeleteListener(final int position, final ViewHolder holder){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.swipeLayout.close();
                mShardedPreference = new ShardedPreference();
                mShardedPreference.removeFavorite(mActivity,mFavorisList.get(position));
                holder.swipeLayout.close();
                mActivity.updateAdapter();
            }
        };
    }

    private View.OnClickListener onGoListener(final int position, final ViewHolder holder){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.swipeLayout.close();
                Uri Uriintent = Uri.parse("google.navigation:q="+ mFavorisList.get(position).getPosition().latitude + "," +
                        mFavorisList.get(position).getPosition().longitude +"&mode=w");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uriintent);
                mapIntent.setPackage("com.google.android.apps.maps");
                getContext().startActivity(mapIntent);

            }
        };
    }


    private class ViewHolder{
        private TextView name;
        private View btnDelete;
        private View btnEdit;
        private SwipeLayout swipeLayout;

        public ViewHolder(View v) {
            swipeLayout = (SwipeLayout)v.findViewById(R.id.swipe_layout);
            btnDelete = v.findViewById(R.id.delete);
            btnEdit = v.findViewById(R.id.edit_query);
            name = (TextView) v.findViewById(R.id.name);
            swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        }
    }
/*
    @Override
    public int getCount() {
        return mFavorisList.size();
    }*/

    @Override
    public boolean isEmpty() {
        return mFavorisList.size() == 1;
    }

    @Nullable
    @Override
    public Favoris getItem(int position) {
        return mFavorisList.get(position);
    }

    @Override
    public void add(Favoris favoris) {
        super.add(favoris);
        mFavorisList.add(favoris);
        notifyDataSetChanged();
    }

    @Override
    public void remove(Favoris favoris) {
        super.remove(favoris);
        mFavorisList.remove(favoris);
        notifyDataSetChanged();
    }


}

