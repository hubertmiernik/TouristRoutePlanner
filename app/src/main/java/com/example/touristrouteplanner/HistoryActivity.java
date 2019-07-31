package com.example.touristrouteplanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.touristrouteplanner.model.Route;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private HistoryRecyclerViewAdapter historyRecyclerViewAdapter;
    private List<Route> routeList;

    SessionManager sessionManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_routes);
        getSupportActionBar().setTitle("Historia tras");


//        String region = getIntent().getStringExtra("region");
//        String difficulty = getIntent().getStringExtra("difficulty");

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        String email = user.get(sessionManager.EMAIL);

        recyclerView = findViewById(R.id.historyRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        historyRecyclerViewAdapter = new HistoryRecyclerViewAdapter(this);
        recyclerView.setAdapter(historyRecyclerViewAdapter);

        getFilterRoutes(email);

    }

    private void getFilterRoutes(final String email) {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.URL_HISTORY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            historyRecyclerViewAdapter.setRouteList(
                                    Common.getInstance()
                                            .getRoutesFromJSONResponse(response));

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(HistoryActivity.this,
                                    "Error "+e.toString(),
                                    Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(HistoryActivity.this, "Error "+error.toString(), Toast.LENGTH_SHORT).show();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                return params;
            }
        };

        queue.add(stringRequest);




    }
}
