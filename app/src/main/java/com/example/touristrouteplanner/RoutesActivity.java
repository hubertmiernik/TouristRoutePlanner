package com.example.touristrouteplanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RoutesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RouteRecyclerViewAdapter routeRecyclerViewAdapter;
    private List<Route> routeList;
    private RequestQueue queue;
    public static final String URL = "http://192.168.21.19/android_register_login/test.php";




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
        //getRoutes();

//        Route route = new Route();
//        route.setName("test name");
//        route.setRegion("test region");
//        route.setPicture("https://static.polskieszlaki.pl/zdjecia/wycieczki/2013-04/900_470/malopolska-trasa-unesco-191589.jpg");
//        routeList.add(route);

        routeList = getRoutes();

        routeRecyclerViewAdapter = new RouteRecyclerViewAdapter(this, routeList);
        recyclerView.setAdapter(routeRecyclerViewAdapter);
        routeRecyclerViewAdapter.notifyDataSetChanged();


    }


    public List<Route> getRoutes(){
        // routeList.clear();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
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
                        route.setLongitude(routeObj.getString("longitude"));
                        route.setLatitude(routeObj.getString("latitude"));

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
