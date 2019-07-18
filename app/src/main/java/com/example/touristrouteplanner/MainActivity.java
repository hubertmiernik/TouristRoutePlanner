package com.example.touristrouteplanner;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    private DrawerLayout drawer;
    public TextView nameNav, emailNav, nearRoute;
    public Button nearbyButton, allRoutesButton;
    SessionManager sessionManager;
    public RequestQueue queue;

    public Context context;


    private LocationManager locationManager;
    private LocationListener locationListener;

    private String locationTestLat;
    private String locationTestLon;

    private MapView mMapView;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    public String test ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();





        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
              //  Log.d("Location ", location.toString());

                locationTestLat = Location.convert(location.getLatitude(), Location.FORMAT_DEGREES);
                locationTestLon = Location.convert(location.getLongitude(), Location.FORMAT_DEGREES);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        //uzytkownik wyraza zgode na uzywanie gps

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);


            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        locationTestLat = Location.convert(location.getLatitude(), Location.FORMAT_DEGREES);
        locationTestLon = Location.convert(location.getLongitude(), Location.FORMAT_DEGREES);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        nameNav = findViewById(R.id.name);
//        emailNav = findViewById(R.id.email);

        HashMap<String, String> user = sessionManager.getUserDetail();
        String mName = user.get(sessionManager.NAME);


//        String mEmail = user.get(sessionManager.EMAIL);
//        emailNav.setText(mEmail);
        nameNav.setText(mName);


        queue = Volley.newRequestQueue(this);
        getNearbyRoute();



        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        double longitude = Double.valueOf(locationTestLon);
        double latitude = Double.valueOf(locationTestLat);
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            System.out.println("MIASTO " + city +
                                "\n ADRES " + address +
                                "\n WOJEWODZTWO " + state +
                                "\n KRAJ " + country +
                                "\n KOD " + postalCode);
        } catch (IOException e) {
            e.printStackTrace();
        }


        allRoutesButton = findViewById(R.id.btnGetAllRoutes);

        allRoutesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent2 = new Intent(MainActivity.this, RoutesActivity.class);
                startActivity(intent2);

            }
        });

        initGoogleMap(savedInstanceState);



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            }
        }
    }

    private void getNearbyRoute(){

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Const.URL_ROUTES, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Route nearbyRoute = null;

                try {
                    JSONArray routes = response.getJSONArray("routes");

                    for (int i=0; i<routes.length(); i++){
                        Route route;

                        JSONObject object = routes.getJSONObject(i);

                        double lon = Double.valueOf(object.getString("longitude"));
                        double lat = Double.valueOf(object.getString("latitude"));


                        double lonTest = Double.valueOf(locationTestLon);
                        double latTest = Double.valueOf(locationTestLat);

                        double distance = Math.sqrt(Math.pow((lon - lonTest), 2) + Math.pow((lat - latTest), 2))*73;
//                        System.out.println("test lon " + lon + " lat " + lat);
//                        System.out.println("test2 lon " + lonTest + "test2 lat" + latTest);
//                        Log.d("Longitude", routesObject.getString("longitude"));
//                        Log.d("Latitude", routesObject.getString("latitude"));
//                        System.out.println("Route " + i + " distance from point: " + distance);

                        route = new Route(
                                object.getString("name"),
                                object.getString("description"),
                                object.getString("region"),
                                object.getString("latitude"),
                                object.getString("longitude"),
                                object.getString("picture"),
                                object.getString("difficulty"),
                                object.getString("endlatitude"),
                                object.getString("endlongitude"),
                                object.getString("length"),
                                distance
                        );
                        System.out.println(route.toString());

                        if (i == 0) {
                            nearbyRoute = route;
                        } else if (nearbyRoute.getDistanceFromPoint() > route.getDistanceFromPoint()) {
                            nearbyRoute = route;
                        }
                    }

                    System.out.println("NEARBY ROUTE: ");
                    assert nearbyRoute != null;
                    Common.getInstance().setNearbyRoute(nearbyRoute);
                    System.out.println(nearbyRoute.toString());


                    System.out.println("NAZWA " + nearbyRoute.getName());
                    System.out.println("WOJEWODZTWO " + nearbyRoute.getRegion());


                    nearRoute = findViewById(R.id.nearRoute);
                    nearRoute.setText(nearbyRoute.getName());

                    test = nearbyRoute.getLatitude();
                    System.out.println("TEST W OBREBIE METODY" + test);

                    nearbyButton = findViewById(R.id.btnGetNearRoute);

                    System.out.println("MAP FROM COMMON: " + Common.getInstance().getNearbyRoute().getName());

                    final Route finalNearbyRoute = nearbyRoute;
                    nearbyButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, RouteDetailActivity.class);
                            intent.putExtra("route", finalNearbyRoute);
                            startActivity(intent);



                        }
                    });

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
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_maps:
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_routes:
                Intent intent2 = new Intent(MainActivity.this, RoutesActivity.class);
                startActivity(intent2);
                break;

            case R.id.nav_filter:
                Intent intent3 = new Intent(MainActivity.this, FilterActivity.class);
                startActivity(intent3);
                break;

            case R.id.nav_planner:
                Intent intent4 = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent4);
                break;

            case R.id.sos:
                callSOS();
                break;

            case R.id.logout:
                sessionManager.logout();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        TextView emailNavigationMenu = findViewById(R.id.emailNav3);
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        emailNavigationMenu.setText(user.get(sessionManager.EMAIL));

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void callSOS() {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "112", null)));
    }




        private void initGoogleMap(Bundle savedInstanceState){

            // *** IMPORTANT ***
            // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
            // objects or sub-Bundles.
            Bundle mapViewBundle = null;
            if (savedInstanceState != null) {
                mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
            }
            mMapView = findViewById(R.id.mapViewMain);
            mMapView.onCreate(mapViewBundle);

            mMapView.getMapAsync((OnMapReadyCallback) this);

        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);

            Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
            if (mapViewBundle == null) {
                mapViewBundle = new Bundle();
                outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
            }

            mMapView.onSaveInstanceState(mapViewBundle);
        }


        @Override
        public void onResume() {
            super.onResume();
            mMapView.onResume();
        }

        @Override
        public void onStart() {
            super.onStart();
            mMapView.onStart();
        }

        @Override
        public void onStop() {
            super.onStop();
            mMapView.onStop();
        }

        @Override
        public void onMapReady(GoogleMap map) {



            double doubleLatitude = Double.parseDouble(locationTestLat);
            double doubleLongitude = Double.parseDouble(locationTestLon);

            System.out.println("TEST LAAT" + test);





            map.addMarker(new MarkerOptions()
                    .position(new LatLng(doubleLatitude, doubleLongitude))
                    .title("Jestes tutaj")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));


            System.out.println("HELLO FROM MAP READY!");
            if (Common.getInstance().getNearbyRoute() != null) {
                Route nearbyRoute = Common.getInstance().getNearbyRoute();
                double nerLat = Double.parseDouble(nearbyRoute.getLatitude());
                double nerLon = Double.parseDouble(nearbyRoute.getLongitude());

                map.addMarker(new MarkerOptions()
                        .position(new LatLng(nerLat, nerLon))
                        .title(nearbyRoute.getName())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

            }

           map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(doubleLatitude,doubleLongitude), 5));



        }


        @Override
        public void onPause() {
            mMapView.onPause();
            super.onPause();
        }

        @Override
        public void onDestroy() {
            mMapView.onDestroy();
            super.onDestroy();
        }

        @Override
        public void onLowMemory() {
            super.onLowMemory();
            mMapView.onLowMemory();
        }







}










