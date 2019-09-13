package com.example.touristrouteplanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.touristrouteplanner.model.Route;

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




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);
        getSupportActionBar().setTitle("Trasy turystyczne");


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        routeRecyclerViewAdapter = new RouteRecyclerViewAdapter(this);
        recyclerView.setAdapter(routeRecyclerViewAdapter);

        getRoutes();

    }

        private void getRoutes() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Const.URL_ROUTES,
            new Response.Listener<String>() {
                @Override
            public void onResponse(String response) {

                try {
                    routeRecyclerViewAdapter.setRouteList(
                            Common.getInstance()
                                    .getRoutesFromJSONResponse(response));

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(RoutesActivity.this,
                            "Error "+e.toString(),
                            Toast.LENGTH_SHORT)
                            .show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(stringRequest);



    }

}
