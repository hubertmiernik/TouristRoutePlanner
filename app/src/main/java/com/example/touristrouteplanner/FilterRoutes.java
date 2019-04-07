package com.example.touristrouteplanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;

import java.util.ArrayList;
import java.util.List;

public class FilterRoutes extends AppCompatActivity {


    private RecyclerView recyclerView;
    private FilterRecyclerViewAdapter filterRecyclerViewAdapter;
    private List<Route> routeList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_routes);


        routeList = new ArrayList<>();

        filterRecyclerViewAdapter = new FilterRecyclerViewAdapter(this, routeList);
        recyclerView.setAdapter(filterRecyclerViewAdapter);
        filterRecyclerViewAdapter.notifyDataSetChanged();


    }
}
