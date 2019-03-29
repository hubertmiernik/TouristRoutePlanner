package com.example.touristrouteplanner;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.touristrouteplanner.Model.Route;
import com.squareup.picasso.Picasso;

public class RouteDetailActivity extends AppCompatActivity {

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


    }

}
