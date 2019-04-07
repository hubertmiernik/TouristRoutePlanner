package com.example.touristrouteplanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PlannerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner);
        getSupportActionBar().setTitle("Zaplanuj trasę");
    }
}
