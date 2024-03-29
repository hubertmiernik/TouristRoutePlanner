package com.example.touristrouteplanner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.touristrouteplanner.model.Route;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RoutesActivityCopy extends AppCompatActivity {

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



        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        routeList = new ArrayList<>();
        routeList = getRoutes();

        routeRecyclerViewAdapter = new RouteRecyclerViewAdapter(this, routeList);
        recyclerView.setAdapter(routeRecyclerViewAdapter);
        routeRecyclerViewAdapter.notifyDataSetChanged();


    }


    public List<Route> getRoutes(){
        // routeList.clear();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Const.URL_ROUTES, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try{
                    JSONArray routesArray = response.getJSONArray("routes");

                    for (int i=0; i<routesArray.length(); i++){

                        JSONObject routeObj = routesArray.getJSONObject(i);



                        Route route = new Route();
                        route.setName(routeObj.getString("name"));
                        route.setRegion(routeObj.getString("region"));
                        route.setPicture(routeObj.getString("picture"));
                        route.setDescription(routeObj.getString("description"));
                        route.setLongitude(routeObj.getDouble("longitude"));
                        route.setLatitude(routeObj.getDouble("latitude"));
                        route.setDifficulty(routeObj.getString("difficulty"));
                        route.setLength(routeObj.getString("length"));

                        route.setEndLongitude(routeObj.getDouble("endlongitude"));
                        route.setEndLatitude(routeObj.getDouble("endlatitude"));


//
//                        Log.d("Routes ", route.getName());


                        routeList.add(route);

                    }

                    routeRecyclerViewAdapter.notifyDataSetChanged();
                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(jsonObjectRequest);
        return routeList;



    }

}
