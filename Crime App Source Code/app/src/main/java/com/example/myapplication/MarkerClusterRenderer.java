package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;


public class MarkerClusterRenderer<T extends ClusterItem> extends DefaultClusterRenderer<T> {

    public MarkerClusterRenderer(Context context, GoogleMap googleMap, ClusterManager<T> clusterManager){
        super(context, googleMap, clusterManager);
    }


    @Override // Function to display individual markers in varying colors based on the Incident type
    protected void onBeforeClusterItemRendered(T item, MarkerOptions markerOptions) {
        super.onBeforeClusterItemRendered(item, markerOptions);
        if(markerOptions.getTitle().contains("Assault") || markerOptions.getTitle().contains("Missing"))
         markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.five));
        if(markerOptions.getTitle().contains("Theft") || markerOptions.getTitle().contains("Property") || markerOptions.getTitle().contains("Breaking") || markerOptions.getTitle().contains("Vehicle"))
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.four));
        if(markerOptions.getTitle().contains("Disorder") || markerOptions.getTitle().contains("Traffic") || markerOptions.getTitle().contains("Liquor"))
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.three));
    }
    @Override // Set the color of the Cluster displayed
    protected int getColor(int clusterSize) {
        return Color.rgb(174,209,31);// Return any color you want here. You can base it on clusterSize.
    }

@Override // Define threshold value for forming Clusters
    protected boolean shouldRenderAsCluster(Cluster cluster) {
        return cluster.getSize() >= 20;
    }
}
