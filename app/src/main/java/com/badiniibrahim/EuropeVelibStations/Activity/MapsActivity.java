package com.badiniibrahim.EuropeVelibStations.Activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.badiniibrahim.EuropeVelibStations.App.AppController;
import com.badiniibrahim.EuropeVelibStations.Cluster.CustomClusterRenderer;
import com.badiniibrahim.EuropeVelibStations.Cluster.MyItem;
import com.badiniibrahim.EuropeVelibStations.Models.Favoris;
import com.badiniibrahim.EuropeVelibStations.Preference.ShardedPreference;
import com.badiniibrahim.EuropeVelibStations.R;
import com.badiniibrahim.EuropeVelibStations.Utils.Constante;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import dmax.dialog.SpotsDialog;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,  GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {


    GoogleMap mGoogleMap;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private ClusterManager<MyItem> mClusterManager;
    private static String valueIntent;
    private static boolean mStatus;
    private static String number;
    private StringBuilder stringBuilder;
    private android.app.AlertDialog dialog;
    private CustomClusterRenderer renderer;
    private TextView mAdresse, mVeloNumber,mParkingNumber;
    private Button mButton;
    private ShardedPreference mShardedPreference;
    private Favoris mFavorisModel;
    private LinearLayout mLinearLayout;
    private InterstitialAd mInterstitialAd;







    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_dashboard:
                    Intent intentLicence = new Intent(getApplicationContext(), Licence.class);
                    startActivity(intentLicence);
                    return true;
                case R.id.navigation_favoris:
                    Intent intent = new Intent(getApplicationContext(), FavoriteActivity.class);
                        startActivity(intent);
                        return true;
               /* case R.id.settings_preference:
                    Intent settings_intent= new Intent(getApplicationContext(), SettingsActivity.class);
                    startActivity(settings_intent);
                    return true;*/


            }
            return false;
        }

    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mLinearLayout = (LinearLayout)findViewById(R.id.container);
        annonce();
        getDefaultPereference();
        //Custom Alert Dialogue
        dialog = new SpotsDialog(this, R.style.Custom);
        extractBundleValue();

    }
    public void extractBundleValue(){
        Bundle extras = getIntent().getExtras();
        valueIntent = extras.getString("nom");
        number = extras.getString("number");
        mStatus = extras.getBoolean("mStatus");
        //Verification to display favoris in maps
        loadVelo(valueIntent);

    }

    public void annonce(){
        //pub
        mInterstitialAd = new InterstitialAd(this);
        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.body_annonce));
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(adRequest);

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    public void loadVelo(String nom){
        stringBuilder = new StringBuilder(Constante.URL);
        stringBuilder.append(nom);
        stringBuilder.append("&apiKey=");
        stringBuilder.append(Constante.KEY);

        showpDialog();
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET, stringBuilder.toString(),
                null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                loadResult(response);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hidepDialog();
                onStop();
                Snackbar snackbar = Snackbar.make(mLinearLayout, "Erreur de connexion", Snackbar.LENGTH_SHORT);
                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.RED);
                snackbar.show();

            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap=googleMap;
        mClusterManager = new ClusterManager<MyItem>(this, mGoogleMap);
        //create cluster
        renderer = new CustomClusterRenderer(getApplicationContext(), mGoogleMap, mClusterManager);
        mClusterManager.setRenderer(renderer);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
                mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                //Request Location Permission
               //mClusterManager = new ClusterManager<MyItem>(this, mGoogleMap);
                checkLocationPermission();
            }
        }
        else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
            //initialiastion of the cluster
            //mClusterManager = new ClusterManager<MyItem>(this, mGoogleMap);
            //create cluster
            //renderer = new CustomClusterRenderer(getApplicationContext(), mGoogleMap, mClusterManager);
            //mClusterManager.setRenderer(renderer);

        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 1));
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(16), 250, null);

            mGoogleMap.setOnCameraChangeListener(mClusterManager);
            mGoogleMap.setOnMarkerClickListener(mClusterManager);

            //When to click on itemCluster
            mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MyItem>() {
                @Override
                public boolean onClusterItemClick(final MyItem myItem) {
                    //inflater view to display custom result in alertdialog
                    final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MapsActivity.this);
                    LayoutInflater layoutInflater = getLayoutInflater();
                    View builDialog = layoutInflater.inflate(R.layout.personnal_dialog, null);
                    mAdresse = (TextView)builDialog.findViewById(R.id.adress);
                    mVeloNumber = (TextView)builDialog.findViewById(R.id.velo_num);
                    mParkingNumber = (TextView)builDialog.findViewById(R.id.parking_num);
                    mButton = (Button)builDialog.findViewById(R.id.add_favoris);
                    ImageView directions = (ImageView)builDialog.findViewById(R.id.direction);

                    //set view
                    mAdresse.setText(myItem.getAdresse());
                    mVeloNumber.setText(String.valueOf(myItem.getAvailable_bikes()));
                    mParkingNumber.setText(String.valueOf(myItem.getAvailable_bike_stands()));
                    builder.setView(builDialog);
                    //show alert dialogue and dismiss
                    final android.app.AlertDialog show = builder.show();
                    show.show();
                    mButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mFavorisModel = new Favoris(myItem.getName(), myItem.getPosition(), valueIntent);
                            mShardedPreference = new ShardedPreference();
                            ///check if favoris is not content in the list
                            if (checkFavoriteItem(mFavorisModel)){
                                show.dismiss();
                                showMessageWhenFavorisExiste();
                            }else {
                                mShardedPreference.addFavorite(getApplicationContext(),mFavorisModel);
                                show.dismiss();
                                showMessage();
                            }
                        }
                    });

                    directions.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Uri Uriintent = Uri.parse("google.navigation:q="+ myItem.getPosition().latitude + "," +
                                    myItem.getPosition().longitude +"&mode=w");
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uriintent);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            startActivity(mapIntent);
                        }
                    });
                    return true;
                }
            });

            //when to click on cluster
           /* mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<MyItem>() {
                @Override
                public boolean onClusterClick(Cluster<MyItem> cluster) {
                    Toast.makeText(getApplicationContext(), "Cluster click", Toast.LENGTH_LONG).show();

                    return false;
                }
            });*/


           /* mClusterManager.getMarkerCollection().setOnInfoWindowAdapter(new CustomInfoViewAdapter(LayoutInflater.from(this)));
            mGoogleMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());


            //When to clik on wondows
            mClusterManager.setOnClusterItemInfoWindowClickListener(new ClusterManager.OnClusterItemInfoWindowClickListener<MyItem>() {
                @Override
                public void onClusterItemInfoWindowClick(MyItem myItem) {
                    Toast.makeText(getApplicationContext(), "val : " + myItem.getAdresse(), Toast.LENGTH_LONG).show();

                }
            });

           /* mGoogleMap.setOnInfoWindowClickListener(mClusterManager);
            mGoogleMap.setOnInfoWindowClickListener(mClusterManager);
            mGoogleMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());*/
            mGoogleMap.setOnCameraChangeListener(mClusterManager);
            mGoogleMap.setOnMarkerClickListener(mClusterManager);
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    public boolean checkFavoriteItem(Favoris checkProduct) {
        boolean check = false;
        List<Favoris> favorites = mShardedPreference.getFavorites(getApplicationContext());
        if (favorites != null) {
            for (Favoris product : favorites) {
                if (product.equals(checkProduct)) {
                    check = true;
                    break;
                }
            }
        }
        return check;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    public void showpDialog() {
        if (!dialog.isShowing())
            dialog.show();
    }

    public void hidepDialog() {
        if (dialog.isShowing())
            dialog.dismiss();
    }


    private void loadResult(JSONArray result) {
        //mClusterManager = new ClusterManager<MyItem>(this, mGoogleMap);
        //mClusterManager = new ClusterManager<MyItem>(this, mGoogleMap);
        //create cluster
        //renderer = new CustomClusterRenderer(getApplicationContext(), mGoogleMap, mClusterManager);
        //mClusterManager.setRenderer(renderer);

        for (int i = 0 ; i < result.length(); i++){
            try {
                JSONObject jsonObject = result.getJSONObject(i);
                LatLng latLng = new LatLng(jsonObject.getJSONObject(Constante.POSTION).getDouble(Constante.LAT), jsonObject.getJSONObject(Constante.POSTION).getDouble(Constante.LNG));
                MyItem offsetItem = new MyItem(latLng,jsonObject.getString(Constante.ADDRESS), jsonObject.getInt(Constante.AVAILABLE_BIKE_STANDS),
                        jsonObject.getInt(Constante.AVAILABLE_BIKES),jsonObject.optString(Constante.NAME));
                mClusterManager.addItem(offsetItem);



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        hidepDialog();

    }

    public void showMessageWhenFavorisExiste(){
        Snackbar snackbar = Snackbar.make(mLinearLayout, "Favoris spécifié existe déjà dans la liste", Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.RED);
        snackbar.show();
    }

    public void showMessage(){
        Snackbar snackbar = Snackbar.make(mLinearLayout, "Ajouté dans la liste avec succès", Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.GREEN);
        snackbar.show();

    }

    /**
     * this function permeit to get thedefaulte preference
     */
    private void getDefaultPereference(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

}
