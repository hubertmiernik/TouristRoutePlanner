package com.example.touristrouteplanner;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class FilterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText region, difficulty;
    private Button btn_filter;
    private RecyclerView recyclerView;
    private FilterRecyclerViewAdapter filterRecyclerViewAdapter;
    private static String URL_FILTER = "http://192.168.21.19/android_register_login/filter.php";
    private List<Route> routeList;
    private RequestQueue queue;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        getSupportActionBar().setTitle("Filtrowanie tras");

//        region = findViewById(R.id.region);
//        difficulty = findViewById(R.id.difficulty);
        btn_filter = findViewById(R.id.btn_filter);

//        queue = Volley.newRequestQueue(this);
//        routeList = new ArrayList<>();

        final Spinner spinner = findViewById(R.id.spinner1);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.regions, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        final Spinner spinner2 = findViewById(R.id.spinner2);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.difficulty, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(this);




        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tempRegion = String.valueOf(spinner.getSelectedItem());
                String tempDifficulty = String.valueOf(spinner2.getSelectedItem());



                Intent intent = new Intent(FilterActivity.this, FilterRoutes.class);

                intent.putExtra("region", tempRegion);
                intent.putExtra("difficulty", tempDifficulty);

                startActivity(intent);


            }
        });


    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



        if(parent.getId() == R.id.spinner1)
        {
            String region = parent.getItemAtPosition(position).toString();

        }
        else if(parent.getId() == R.id.spinner2)
        {
            String difficulty = parent.getItemAtPosition(position).toString();

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {


    }
}
