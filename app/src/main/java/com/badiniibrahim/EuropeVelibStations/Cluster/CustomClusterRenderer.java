package com.badiniibrahim.EuropeVelibStations.Cluster;

import android.content.Context;

import com.badiniibrahim.EuropeVelibStations.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

/**
 * Created by Promobile on 30/03/2017.
 * To change the basic icons for markers and clusters,
 * you need to create a renderer, extending it from
 * DefaultClusterRenderer:
 */

public class CustomClusterRenderer extends DefaultClusterRenderer<MyItem> {

    private final Context mContext;
    private final IconGenerator mClusterIconGenerator;

    public CustomClusterRenderer(Context context, GoogleMap map, ClusterManager<MyItem> clusterManager) {
        super(context, map, clusterManager);
            mContext = context;
            mClusterIconGenerator = new IconGenerator(mContext.getApplicationContext());
    }

    /**
     * is invoked before rendering a conventional marker.
     * @param item
     * @param markerOptions
     */
    @Override
    protected void onBeforeClusterItemRendered(MyItem item, MarkerOptions markerOptions) {
        super.onBeforeClusterItemRendered(item, markerOptions);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_velo));
    }

    /**
     *
     * @param cluster
     * @param markerOptions
     */

    @Override
    protected void onBeforeClusterRendered(Cluster<MyItem> cluster, MarkerOptions markerOptions) {
        super.onBeforeClusterRendered(cluster, markerOptions);
    }
}
