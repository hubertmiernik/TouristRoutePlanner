package com.example.touristrouteplanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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

//        String region = getIntent().getStringExtra("region");
//        String difficulty = getIntent().getStringExtra("difficulty");

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        String email = user.get(sessionManager.EMAIL);

        routeList = new ArrayList<>();
        routeList = getFilterRoutes(email);
        System.out.println(routeList.toString());


        recyclerView = findViewById(R.id.historyRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        historyRecyclerViewAdapter = new HistoryRecyclerViewAdapter(this, routeList);
        recyclerView.setAdapter(historyRecyclerViewAdapter);
        historyRecyclerViewAdapter.notifyDataSetChanged();

    }

    private List<Route> getFilterRoutes(final String email) {
        String URL_HISTORY = "http://192.168.21.19/android_register_login/test2.php";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_HISTORY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("routes");



                            for (int i=0; i<jsonArray.length(); i++){

                                JSONObject object = jsonArray.getJSONObject(i);


                                Route route = new Route();
                                route.setName(object.getString("name"));
                                route.setRegion(object.getString("region"));
                                route.setLongitude(object.getString("longitude"));
                                route.setLatitude(object.getString("latitude"));
                                route.setEndLongitude(object.getString("endlongitude"));
                                route.setEndLatitude(object.getString("endlatitude"));
                                route.setDifficulty(object.getString("difficulty"));

                                Log.d("trasa: ", object.getString("name"));
                                Log.d("trudnosc: ", object.getString("difficulty"));


                                routeList.add(route);

                            }

                            historyRecyclerViewAdapter.notifyDataSetChanged();



                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(HistoryActivity.this, "Error "+e.toString(), Toast.LENGTH_SHORT);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(HistoryActivity.this, "Error "+error.toString(), Toast.LENGTH_SHORT);

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
        return routeList;




    }
}
