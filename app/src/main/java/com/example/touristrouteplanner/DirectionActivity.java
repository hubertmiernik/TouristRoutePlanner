package com.example.touristrouteplanner;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.touristrouteplanner.model.Route;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DirectionActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {

    GoogleMap map;
    FloatingActionButton btnGetDirection, btnGetDirectionWalking, btnGetDirectionBicycling;
    MarkerOptions place1, place2;
    Polyline currentPolyline;

    private Context context;

    private Route route;
    private String routeLatitude;
    private String routeLongitude;
    private String routeEndLatitude;
    private String routeEndLongitude;
    private RequestQueue queue;

    TextView distance, duration;

    private List<Route> routeDetailsWalking;
    private List<Route> routeDetailsDriving;
    private List<Route> routeDetailsBicycling;


    SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);
        getSupportActionBar().setTitle("Mapa");


        route = (Route) getIntent().getSerializableExtra("route");
        routeLatitude = route.getLatitude();
        routeLongitude = route.getLongitude();
        routeEndLatitude = route.getEndLatitude();
        routeEndLongitude = route.getEndLongitude();

        btnGetDirection = findViewById(R.id.btnGetDirection);
        btnGetDirectionWalking = findViewById(R.id.btnGetDirectionWalking);
        btnGetDirectionBicycling = findViewById(R.id.btnGetDirectionBicycling);


        distance = findViewById(R.id.distance);
        duration = findViewById(R.id.duration);



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

        place1 = new MarkerOptions().position(new LatLng(doubleLatitude,doubleLongitude)).title("Początek trasy")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

        place2 = new MarkerOptions().position(new LatLng(doubleEndLatitude,doubleEndLongitude)).title("Koniec trasy")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        btnGetDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = getUrl(place1.getPosition(), place2.getPosition(), "driving");
                new FetchUrl(DirectionActivity.this).execute(url, "driving");
                getDistanceDriving();

                Route route = routeDetailsDriving.get(0);

                distance.setText(route.getDistance());
                duration.setText(route.getDuration());


            }
        });

        btnGetDirectionWalking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = getUrl(place1.getPosition(), place2.getPosition(), "walking");
                new FetchUrl(DirectionActivity.this).execute(url, "walking");
                getDistanceWalking();

                Route route = routeDetailsWalking.get(0);

                distance.setText(route.getDistance());
                duration.setText(route.getDuration());


            }
        });

        btnGetDirectionBicycling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = getUrl(place1.getPosition(), place2.getPosition(), "bicycling");
                new FetchUrl(DirectionActivity.this).execute(url, "bicycling");
                getDistanceWalking();

                Route route = routeDetailsBicycling.get(0);

                distance.setText(route.getDistance());
                duration.setText(route.getDuration());


            }
        });


        routeDetailsWalking = new ArrayList<>();
        routeDetailsWalking = getDistanceWalking();

        routeDetailsDriving = new ArrayList<>();
        routeDetailsDriving = getDistanceDriving();

        routeDetailsBicycling = new ArrayList<>();
        routeDetailsBicycling = getDistanceBicycling();

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        String mName = user.get(sessionManager.NAME);
        String mEmail = user.get(sessionManager.EMAIL);



        System.out.println("UZYTKOWNIK IMIE " + mName + "MAIL " + mEmail);

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


    public List<Route> getDistanceWalking(){

        Double doubleLatitude = Double.valueOf(routeLatitude);
        Double doubleLongitude = Double.valueOf(routeLongitude);

        Double doubleEndLatitude = Double.valueOf(routeEndLatitude);
        Double doubleEndLongitude = Double.valueOf(routeEndLongitude);

        String URL = "https://maps.googleapis.com/maps/api/directions/json?origin="+doubleLatitude+","+doubleLongitude +
                "&destination="+doubleEndLatitude+","+doubleEndLongitude+"&mode=walking&key="+ getString(R.string.google_maps_key);



       // System.out.println("TEST TEST" + URL);
        queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    JSONArray routesArray = response.getJSONArray("routes");
                        JSONObject routesJSONObjct = routesArray.getJSONObject(0);

                        JSONArray legsArray = routesJSONObjct.getJSONArray("legs");

                        JSONObject distanceObj = legsArray.getJSONObject(0);
                        JSONObject durationObj = legsArray.getJSONObject(0);


                        JSONObject distance = distanceObj.getJSONObject("distance");
                        JSONObject duration = durationObj.getJSONObject("duration");


                            Route route = new Route();
                            route.setDistance(distance.getString("text"));
                            route.setDuration(duration.getString("text"));


                                Log.d("DETAIL WALKING distance", route.getDistance());
                                Log.d("DETAIL WALKING duration", route.getDuration());

                                routeDetailsWalking.add(route);



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(jsonObjectRequest);
        return routeDetailsWalking;



    }

    public List<Route> getDistanceDriving(){

        Double doubleLatitude = Double.valueOf(routeLatitude);
        Double doubleLongitude = Double.valueOf(routeLongitude);

        Double doubleEndLatitude = Double.valueOf(routeEndLatitude);
        Double doubleEndLongitude = Double.valueOf(routeEndLongitude);

        String URL = "https://maps.googleapis.com/maps/api/directions/json?origin="+doubleLatitude+","+doubleLongitude +
                "&destination="+doubleEndLatitude+","+doubleEndLongitude+"&mode=driving&key="+ getString(R.string.google_maps_key);

        // System.out.println("TEST TEST" + URL);
        queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    JSONArray routesArray = response.getJSONArray("routes");
                    JSONObject routesJSONObjct = routesArray.getJSONObject(0);

                    JSONArray legsArray = routesJSONObjct.getJSONArray("legs");

                    JSONObject distanceObj = legsArray.getJSONObject(0);
                    JSONObject durationObj = legsArray.getJSONObject(0);


                    JSONObject distance = distanceObj.getJSONObject("distance");
                    JSONObject duration = durationObj.getJSONObject("duration");


                    Route route = new Route();
                    route.setDistance(distance.getString("text"));
                    route.setDuration(duration.getString("text"));


                        Log.d("DETAIL DRIVING distance", route.getDistance());
                        Log.d("DETAIL DRIVING duration", route.getDuration());

                    routeDetailsDriving.add(route);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonObjectRequest);
        return routeDetailsDriving;


    }

    public List<Route> getDistanceBicycling(){

        Double doubleLatitude = Double.valueOf(routeLatitude);
        Double doubleLongitude = Double.valueOf(routeLongitude);

        Double doubleEndLatitude = Double.valueOf(routeEndLatitude);
        Double doubleEndLongitude = Double.valueOf(routeEndLongitude);

        String URL = "https://maps.googleapis.com/maps/api/directions/json?origin="+doubleLatitude+","+doubleLongitude +
                "&destination="+doubleEndLatitude+","+doubleEndLongitude+"&mode=bicycling&key="+ getString(R.string.google_maps_key);

        // System.out.println("TEST TEST" + URL);
        queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    JSONArray routesArray = response.getJSONArray("routes");
                    JSONObject routesJSONObjct = routesArray.getJSONObject(0);

                    JSONArray legsArray = routesJSONObjct.getJSONArray("legs");

                    JSONObject distanceObj = legsArray.getJSONObject(0);
                    JSONObject durationObj = legsArray.getJSONObject(0);


                    JSONObject distance = distanceObj.getJSONObject("distance");
                    JSONObject duration = durationObj.getJSONObject("duration");


                    Route route = new Route();
                    route.setDistance(distance.getString("text"));
                    route.setDuration(duration.getString("text"));


                    Log.d("DETAIL DRIVING distance", route.getDistance());
                    Log.d("DETAIL DRIVING duration", route.getDuration());

                    routeDetailsBicycling.add(route);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonObjectRequest);
        return routeDetailsBicycling;


    }









//https://maps.googleapis.com/maps/api/directions/json?origin=27.658143,85.3199503&destination=27.667491,85.3208583&mode=walking&key=AIzaSyCLn_Azn-WHmB5DnmlmC70FZJ9mq6B8qy8



}



















