package com.badiniibrahim.EuropeVelibStations.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.badiniibrahim.EuropeVelibStations.Adapter.VilleAdapter;
import com.badiniibrahim.EuropeVelibStations.App.AppController;
import com.badiniibrahim.EuropeVelibStations.Models.Ville;
import com.badiniibrahim.EuropeVelibStations.R;
import com.badiniibrahim.EuropeVelibStations.Utils.Constante;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class ListVilleActivity extends AppCompatActivity {


    AlertDialog dialog;
    String name, commercial_name, code;
    ArrayList<Ville> mVilles = new ArrayList<>();
    ArrayAdapter<Ville> barrayAdapter;
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_ville);

        setTitle("Choissisez votre ville");
        mListView = (ListView)findViewById(R.id.list_ville);
        dialog = new SpotsDialog(this, R.style.Custom);
        loadVille();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("nom", mVilles.get(position).getName());
                intent.putExtra("number", " ");
                intent.putExtra("state", false);
                startActivity(intent);
            }
        });
    }

    public void loadVille(){

        showpDialog();
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET, "https://api.jcdecaux.com/vls/v1/contracts?apiKey=581d78ef09660a6e43ba457957aa3c727ac7a567",
                null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                parseLocationResult(response);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "RequeteErreur : " + error.getLocalizedMessage());

            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }
    private void parseLocationResult(JSONArray result) {

        for (int i = 0 ; i < result.length(); i++){

            try {
                JSONObject jsonObject = result.getJSONObject(i);
                name = jsonObject.getString(Constante.NAME);
                commercial_name = jsonObject.getString(Constante.NAME_COMMERCIAL);
                code = jsonObject.getString(Constante.COUNTRYCODE);

                Ville ville = new Ville(name,commercial_name, code);
                mVilles.add(ville);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            barrayAdapter = new VilleAdapter(this, R.layout.custom_item, mVilles);
            mListView.setAdapter(barrayAdapter);
        }


        hidepDialog();
    }

    public void showpDialog() {
        if (!dialog.isShowing())
            dialog.show();
    }

    public void hidepDialog() {
        if (dialog.isShowing())
            dialog.dismiss();
    }
}
