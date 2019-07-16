package com.example.touristrouteplanner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.touristrouteplanner.model.Route;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilterRoutesCopy extends AppCompatActivity {


    private RecyclerView recyclerView;
    private FilterRecyclerViewAdapter filterRecyclerViewAdapter;
    private List<Route> routeList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_routes);
        getSupportActionBar().setTitle("Filtrowanie tras");


        String region = getIntent().getStringExtra("region");
        String difficulty = getIntent().getStringExtra("difficulty");

        String lengthFrom = getIntent().getStringExtra("lengthFrom");
        String lengthTo = getIntent().getStringExtra("lengthTo");



        routeList = new ArrayList<>();
        routeList = getFilterRoutes(region, difficulty, lengthFrom, lengthTo);
        System.out.println(routeList.toString());


        recyclerView = findViewById(R.id.filterRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        filterRecyclerViewAdapter = new FilterRecyclerViewAdapter(this, routeList);
        recyclerView.setAdapter(filterRecyclerViewAdapter);
        filterRecyclerViewAdapter.notifyDataSetChanged();

    }

    private List<Route> getFilterRoutes(final String region, final String difficulty, final String lengthFrom, final String lengthTo) {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.URL_FILTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            routeList = Common.getInstance().getRoutesFromJSONResponse(response);


                            filterRecyclerViewAdapter.setRouteList(
                                    Common.getInstance()
                                            .getRoutesFromJSONResponse(response));

//                            filterRecyclerViewAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(FilterRoutesCopy.this,
                                    "Error "+e.toString(),
                                    Toast.LENGTH_SHORT)
                                    .show();
                        }


//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            JSONArray jsonArray = jsonObject.getJSONArray("routes");
//
//
//                            for (int i=0; i<jsonArray.length(); i++){
//
//                                JSONObject object = jsonArray.getJSONObject(i);
//
//
//                                Route route = new Route();
//                                route.setName(object.getString("name"));
//                                route.setRegion(object.getString("region"));
//                                route.setLongitude(object.getString("longitude"));
//                                route.setLatitude(object.getString("latitude"));
//                                route.setEndLongitude(object.getString("endlongitude"));
//                                route.setEndLatitude(object.getString("endlatitude"));
//                                route.setDifficulty(object.getString("difficulty"));
//                                route.setLength(object.getString("length"));
//                                route.setPicture(object.getString("picture"));
//                                route.setDescription(object.getString("description"));
//
//
//                                Log.d("trasa: ", object.getString("name"));
//                                Log.d("trudnosc: ", object.getString("difficulty"));
//
//
//                                routeList.add(route);
//
//                            }
//
//                              filterRecyclerViewAdapter.notifyDataSetChanged();
//
//
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Toast.makeText(FilterRoutes.this, "Error "+e.toString(), Toast.LENGTH_SHORT).show();
//                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(FilterRoutesCopy.this, "Error "+error.toString(), Toast.LENGTH_SHORT).show();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("region", region);
                params.put("difficulty", difficulty);
                params.put("lengthFrom", lengthFrom);
                params.put("lengthTo", lengthTo);
                return params;
            }
        };

        queue.add(stringRequest);
        return routeList;




    }
}
