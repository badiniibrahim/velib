package com.badiniibrahim.EuropeVelibStations.Adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.badiniibrahim.EuropeVelibStations.Activity.ListVilleActivity;
import com.badiniibrahim.EuropeVelibStations.Models.Ville;
import com.badiniibrahim.EuropeVelibStations.R;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Promobile on 30/03/2017.
 */
public class VilleAdapter extends ArrayAdapter<Ville> {

    private ListVilleActivity mListeDesVilles;
    private List<Ville> mVilles;


    public VilleAdapter(ListVilleActivity desVilles, int resource, List<Ville> villeList){
        super(desVilles, resource, villeList);
        this.mListeDesVilles = desVilles;
        this.mVilles = villeList;
    }

    @Override
    public int getCount() {
        return mVilles.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        LayoutInflater inflater = (LayoutInflater)mListeDesVilles.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null){
            convertView = inflater.inflate(R.layout.custom_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(mVilles.get(position).getName());
        switch (mVilles.get(position).getCountry_code()){
            case "FR":
                Glide.with(mListeDesVilles).load(R.drawable.france).into(holder.flag);
                break;
            case "LU":
                Glide.with(mListeDesVilles).load(R.drawable.luxembourg).into(holder.flag);
                break;
            case "IE":
                Glide.with(mListeDesVilles).load(R.drawable.irlande).into(holder.flag);
                break;
            case "ES":
                Glide.with(mListeDesVilles).load(R.drawable.espagne).into(holder.flag);
                break;
            case "SE":
                Glide.with(mListeDesVilles).load(R.drawable.suede).into(holder.flag);
                break;
            case "NO":
                Glide.with(mListeDesVilles).load(R.drawable.norvege).into(holder.flag);
                break;
            case "SI":
                Glide.with(mListeDesVilles).load(R.drawable.sloveni).into(holder.flag);
                break;
            case "BE":
                Glide.with(mListeDesVilles).load(R.drawable.belg).into(holder.flag);
                break;
            case "LT":
                Glide.with(mListeDesVilles).load(R.drawable.lituanie).into(holder.flag);
                break;
            case "JP":
                Glide.with(mListeDesVilles).load(R.drawable.japon).into(holder.flag);
                break;
            case "RU":
                Glide.with(mListeDesVilles).load(R.drawable.russie).into(holder.flag);
                break;
            case "AU":
                Glide.with(mListeDesVilles).load(R.drawable.russie).into(holder.flag);
                break;
            default:
                Glide.with(mListeDesVilles).load(R.drawable.ic_flag_default).into(holder.flag);
                break;
        }

        return convertView;
    }


    private class ViewHolder{
        private TextView name;
        private ImageView flag;

        public ViewHolder(View v){
            name = (TextView)v.findViewById(R.id.notification_item_title);
            flag = (ImageView)v.findViewById(R.id.flag);
        }


    }
}

