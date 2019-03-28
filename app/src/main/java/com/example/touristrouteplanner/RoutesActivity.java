package com.example.touristrouteplanner;

import android.app.DownloadManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.touristrouteplanner.Data.RouteRecyclerViewAdapter;
import com.example.touristrouteplanner.Model.Route;
import com.example.touristrouteplanner.Util.Constans;
import com.example.touristrouteplanner.Util.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoutesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RouteRecyclerViewAdapter routeRecyclerViewAdapter;
    private List<Route> routeList;
    private RequestQueue queue;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);
        getSupportActionBar().setTitle("Trasy turystyczne");

        queue = Volley.newRequestQueue(this);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//            }
//        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        routeList = new ArrayList<>();

//        Prefs prefs = new Prefs(RoutesActivity.this);
//        String search = prefs.getSearch();

       // getRoutes();

        routeList = getRoutes();

        routeRecyclerViewAdapter = new RouteRecyclerViewAdapter(this, routeList);
        recyclerView.setAdapter(routeRecyclerViewAdapter);
        routeRecyclerViewAdapter.notifyDataSetChanged();




    }


    //get routes
    public List<Route> getRoutes(){
        routeList.clear();


        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET,
                Constans.URL,null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i=0; i<response.length(); i++){
                    try {
                        JSONObject routeObject = response.getJSONObject(i);

                        //Log.d("nazwa: ", routeObject.getString("picture"));

                        Route route = new Route();
                        route.setName(routeObject.getString("name"));
                        route.setRegion(routeObject.getString("region"));
//                        route.setLatitude(routeObject.getString("latitude"));
//                        route.setLongitude(routeObject.getString("longitude"));
                        route.setPicture(routeObject.getString("picture"));


                       routeList.add(route);


                        Log.d("Name: ", route.getName());
                        Log.d("Region: ", route.getRegion());
                        Log.d("Picture: ", route.getPicture());




                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                // Log.d("Response", response.toString());



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error dupa", error.getMessage());

            }
        });
        queue.add(arrayRequest);
        return routeList;



    }

}
