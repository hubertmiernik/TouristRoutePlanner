package com.example.touristrouteplanner;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class DirectionActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {

    GoogleMap map;
    Button btnGetDirection, btnGetDirectionWalking;
    MarkerOptions place1, place2;
    Polyline currentPolyline;

    private Context context;

    private Route route;
    private String routeLatitude;
    private String routeLongitude;
    private String routeEndLatitude;
    private String routeEndLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);

        route = (Route) getIntent().getSerializableExtra("route");
        routeLatitude = route.getLatitude();
        routeLongitude = route.getLongitude();
        routeEndLatitude = route.getEndLatitude();
        routeEndLongitude = route.getEndLongitude();

        btnGetDirection = findViewById(R.id.btnGetDirection);
        btnGetDirectionWalking = findViewById(R.id.btnGetDirectionWalking);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFrag);
        mapFragment.getMapAsync(this);

//        place1 = new MarkerOptions().position(new LatLng(50.0641644,19.9408873)).title("Krakow");
//        place2 = new MarkerOptions().position(new LatLng(51.0518361,20.7966426)).title("Suchedniow");

        Double doubleLatitude = Double.valueOf(routeLatitude);
        Double doubleLongitude = Double.valueOf(routeLongitude);

        Double doubleEndLatitude = Double.valueOf(routeEndLatitude);
        Double doubleEndLongitude = Double.valueOf(routeEndLongitude);

        place1 = new MarkerOptions().position(new LatLng(doubleLatitude,doubleLongitude)).title("Początek trasy");
        place2 = new MarkerOptions().position(new LatLng(doubleEndLatitude,doubleEndLongitude)).title("Koniec trasy");

        btnGetDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = getUrl(place1.getPosition(), place2.getPosition(), "driving");
                new FetchUrl(DirectionActivity.this).execute(url, "driving");


            }
        });

        btnGetDirectionWalking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = getUrl(place1.getPosition(), place2.getPosition(), "walking");
                new FetchUrl(DirectionActivity.this).execute(url, "walking");

            }
        });




    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.addMarker(place1);
        map.addMarker(place2);

        Double doubleLatitude = Double.valueOf(routeLatitude);
        Double doubleLongitude = Double.valueOf(routeLongitude);

        Double doubleEndLatitude = Double.valueOf(routeEndLatitude);
        Double doubleEndLongitude = Double.valueOf(routeEndLongitude);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng((doubleLatitude+doubleEndLatitude)/2,(doubleLongitude+doubleEndLongitude)/2), 8));
    }


    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }






    @Override
    public void onTaskDone(Object... values) {
        if(currentPolyline!=null)
            currentPolyline.remove();
        currentPolyline = map.addPolyline((PolylineOptions) values[0]);
    }


}



















