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

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class FilterRoutes extends AppCompatActivity {


    private RecyclerView recyclerView;
    private FilterRecyclerViewAdapter filterRecyclerViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_routes);
        getSupportActionBar().setTitle("Filtrowanie tras");


        String region = getIntent().getStringExtra("region");
        String difficulty = getIntent().getStringExtra("difficulty");
        String lengthFrom = getIntent().getStringExtra("lengthFrom");
        String lengthTo = getIntent().getStringExtra("lengthTo");

        recyclerView = findViewById(R.id.filterRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        filterRecyclerViewAdapter = new FilterRecyclerViewAdapter(this);
        recyclerView.setAdapter(filterRecyclerViewAdapter);

        getFilterRoutes(region, difficulty, lengthFrom, lengthTo);

    }

    private void getFilterRoutes(final String region, final String difficulty, final String lengthFrom, final String lengthTo) {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.URL_FILTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            filterRecyclerViewAdapter.setRouteList(
                                    Common.getInstance()
                                            .getRoutesFromJSONResponse(response));

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(FilterRoutes.this,
                                    "Error "+e.toString(),
                                    Toast.LENGTH_SHORT)
                                    .show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(FilterRoutes.this, "Error "+error.toString(), Toast.LENGTH_SHORT).show();

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
    }
}
