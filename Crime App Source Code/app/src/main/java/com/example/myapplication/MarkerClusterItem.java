package com.example.myapplication;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MarkerClusterItem implements ClusterItem { //POJO class to Initialize with the Marker values for our Custom Cluster Class

    private LatLng latLng;
    private String title;
    private String snippet;

    public MarkerClusterItem(LatLng latLng, String title, String snippet){
        this.latLng = latLng;
        this.title = title;
        this.snippet = snippet;
    }
   // Return the Initialized Marker Values
    @Override
    public LatLng getPosition() {
        return latLng;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getSnippet() {
        return snippet;
    }
}
