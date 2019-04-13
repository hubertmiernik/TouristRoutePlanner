package com.example.touristrouteplanner;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RouteDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Context context;

    private Route route;
    private ImageView routeImage;
    private TextView name, region, latitude, longitude, description;

    private String routeName;
    private String routeDescription;
    private String routePicture;
    private String routeRegion;
    private String routeLatitude;
    private String routeLongitude;

    private MapView mMapView;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    private Button btn_history;

    SessionManager sessionManager;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_detail);
        getSupportActionBar().setTitle("Szczegóły trasy");

        route = (Route) getIntent().getSerializableExtra("route");
        routeName = route.getName();
        routeDescription = route.getDescription();
        routePicture = route.getPicture();
        routeRegion = route.getRegion();
        routeLatitude = route.getLatitude();
        routeLongitude = route.getLongitude();

        routeImage = findViewById(R.id.routeImageID);
        name = findViewById(R.id.routeNameID);
        region = findViewById(R.id.routeRegionID);
        latitude = findViewById(R.id.routeLatitude);
        longitude = findViewById(R.id.routeLongitude);
        description = findViewById(R.id.routeDescription);

        name.setText(routeName);
        description.setText(routeDescription);
        region.setText(routeRegion);
        latitude.setText(routeLatitude);
        longitude.setText(routeLongitude);
        Picasso.with(context).load(routePicture).placeholder(android.R.drawable.ic_btn_speak_now).into(routeImage);

        initGoogleMap(savedInstanceState);

        btn_history = findViewById(R.id.btn_history);
        btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addToHistory();

            }
        });



    }

    private void addToHistory() {

        sessionManager = new SessionManager(RouteDetailActivity.this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        final String mEmail = user.get(sessionManager.EMAIL);



        System.out.println("UZYTKOWNIK" + mEmail + "TRASA " +  routeName);
       // Toast.makeText(RouteDetailActivity.this, "Dodano " + routeName +" do historii tras!", Toast.LENGTH_SHORT).show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.URL_ADD_HISTORY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if(success.equals("1")){
                                Toast.makeText(RouteDetailActivity.this, "Dodano " + routeName +" do historii tras!", Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RouteDetailActivity.this, "Error! " + e.toString(), Toast.LENGTH_SHORT).show();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RouteDetailActivity.this, "Error! " + error.toString(), Toast.LENGTH_SHORT).show();


                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", mEmail);
                params.put("name", routeName);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }








    private void initGoogleMap(Bundle savedInstanceState){

        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mMapView = findViewById(R.id.mapView);
        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(this);

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

        Double doubleLatitude = Double.valueOf(routeLatitude);
        Double doubleLongitude = Double.valueOf(routeLongitude);

        LatLng latLng = new LatLng(doubleLatitude, doubleLongitude);
        map.addMarker(new MarkerOptions().position(latLng).title(routeName));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, (float) 4.5));
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
