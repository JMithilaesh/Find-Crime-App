package com.example.myapplication;
//list of packages used in the app

import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



//Main class of the app
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    //List of variables used in the app functions
    private JSONArray jsonArray;
    Spinner year;
    Spinner month;
    private GoogleMap map;
    private List<MarkerOptions> listMarkers = new ArrayList<>();
    private List<MarkerOptions> felonylistMarkers = new ArrayList<>();
    private List<MarkerOptions> misdemeanerlistMarkers = new ArrayList<>();
    private List<MarkerOptions> infractionlistMarkers = new ArrayList<>();
    private ClusterManager<MarkerClusterItem> clusterManager;

    private void addMarkers(GoogleMap map) { // Function to fetch the co-ordinates and the incident related details from the JSONArray
                try {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String date = jsonObject.getString("incident_datetime").substring(0, 7);
                        double lat = jsonObject.getDouble("latitude");
                        double lng = jsonObject.getDouble("longitude");
                        String title = jsonObject.getString("parent_incident_type") + "(" + date + ")";
                        String description = jsonObject.getString("address_1") + "::" + jsonObject.getString("incident_description");
                        LatLng latLng = new LatLng(lat, lng); // Create a LatLng from the co-ordinates
                        // Create a Marker with the fetched details
                        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(title).snippet(description);
                        listMarkers.add(markerOptions); // Add the the types of crime Markers to an ArrayList for passing to ClusterManager later
                        //Add the markers based on the crime type into crime specific array list for using later when crime type buttons are triggered
                        if (title.contains("Assault") || title.contains("Missing")) {
                            felonylistMarkers.add(markerOptions);
                        }
                        if (title.contains("Theft") || title.contains("Property") || title.contains("Breaking") || title.contains("Vehicle")) {
                            misdemeanerlistMarkers.add(markerOptions);
                        }
                        if (title.contains("Disorder") || title.contains("Traffic") || title.contains("Liquor")) {
                            infractionlistMarkers.add(markerOptions);
                        }
                    }
                    // Focus on the First Marker Position after Initializing the Map
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(listMarkers.get(0).getPosition(), 13.0f));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


    private void addClusterItems() {
        for(MarkerOptions markeroptions : listMarkers){
            MarkerClusterItem clusterItem = new MarkerClusterItem(markeroptions.getPosition(), markeroptions.getTitle(),markeroptions.getSnippet());
            clusterManager.addItem(clusterItem); // Adding the Marker from the list to the Cluster Manager
        }
    }

    private void setRenderer(GoogleMap googleMap) { // Initializing our Custom Cluster Renderer defined in Class MarkerClusterRenderer
        MarkerClusterRenderer<MarkerClusterItem> clusterRenderer = new MarkerClusterRenderer<>(this, googleMap, clusterManager);
        clusterManager.setRenderer(clusterRenderer);
    }

    private void setupClusterManager(GoogleMap googleMap) { //Calling all the previously defined functions to set up the Cluster
        addClusterItems();
        setRenderer(googleMap);
        clusterManager.cluster();
        googleMap.setOnCameraIdleListener(clusterManager);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) { // Define the App behavior initially when it is built and launched
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        month = findViewById(R.id.month);
        year = findViewById(R.id.year);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
// As soon the Map is Ready to be displayed, we call the JSON processing function and set up Clusters from JSON Data
    public void onMapReady(GoogleMap googleMap) { 
        map = googleMap;
        HttpRequestClass obj = new HttpRequestClass();
        obj.execute();// Execute the Asynchronous task
        try {//Get the JSON data returned by the Asynchronous task and store it in a local class JSON Array
            jsonArray = new JSONArray(obj.get().toString());
        }catch (Exception e) {
            e.printStackTrace();
        }
        addMarkers(map);
        clusterManager = new ClusterManager<>(this, map);
        setupClusterManager(map);
    }

    public void filterDate(List<MarkerOptions> crimearray)// Function to display crime based on the selected timeline
    {
	// Clears all the previously set up cluster to render the Map with new Clusters
        clusterManager.clearItems();
        map.clear();
	//Get the user selected date from the drop down in the App
        String crimeyear = year.getSelectedItem().toString();
        String crimemonth = month.getSelectedItem().toString();
        for (MarkerOptions markeroptions : crimearray) {
	// Conditions to load the crime data based on the selected timeline 
            if (markeroptions.getTitle().contains(crimeyear + "-" + crimemonth)) {
                MarkerClusterItem ncluster = new MarkerClusterItem(markeroptions.getPosition(), markeroptions.getTitle(), markeroptions.getSnippet());
                clusterManager.addItem(ncluster);
            }
            if(markeroptions.getTitle().contains(crimeyear) && crimemonth.equals("All")) {
                    MarkerClusterItem ncluster = new MarkerClusterItem(markeroptions.getPosition(), markeroptions.getTitle(), markeroptions.getSnippet());
                    clusterManager.addItem(ncluster);
                }
            if (markeroptions.getTitle().contains("-" + crimemonth) && crimeyear.equals("All"))
                {
                    MarkerClusterItem ncluster = new MarkerClusterItem(markeroptions.getPosition(), markeroptions.getTitle(), markeroptions.getSnippet());
                    clusterManager.addItem(ncluster);
                }
            if (crimemonth.equals("All") && crimeyear.equals("All"))
                {
                    MarkerClusterItem ncluster = new MarkerClusterItem(markeroptions.getPosition(), markeroptions.getTitle(), markeroptions.getSnippet());
                    clusterManager.addItem(ncluster);
                }
            }
	//Set up the cluster after loading all the crime data within selected timeline
        setRenderer(map);
        clusterManager.cluster();
        map.setOnCameraIdleListener(clusterManager);
    }

    public void changeMap(View view) { // Function to render the Map based on the Crime Category Selected by the User
	//Condition to pass the user selected crime category data to the Filter date function
        if (view.getId() == R.id.felonies)
        {
            filterDate(felonylistMarkers);
        }
        if(view.getId() == R.id.Misdemeanors)
        {
            filterDate(misdemeanerlistMarkers);
        }
        if(view.getId() == R.id.Infractions)
        {
            filterDate(infractionlistMarkers);
        }
        if(view.getId() == R.id.AllCrimes)
        {
            filterDate(listMarkers);
        }
    }

}
