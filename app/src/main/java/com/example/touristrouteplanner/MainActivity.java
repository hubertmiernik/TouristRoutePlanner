package com.example.touristrouteplanner;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    public TextView nameNav, emailNav;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        nameNav = findViewById(R.id.name);
        emailNav = findViewById(R.id.email);

        HashMap<String, String> user = sessionManager.getUserDetail();
        String mName = user.get(sessionManager.NAME);
        String mEmail = user.get(sessionManager.EMAIL);

        nameNav.setText(mName);
        emailNav.setText(mEmail);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_maps:
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_routes:
                Intent intent2 = new Intent(MainActivity.this, RoutesActivity.class);
                startActivity(intent2);
                break;

            case R.id.nav_filter:
                Intent intent3 = new Intent(MainActivity.this, FilterActivity.class);
                startActivity(intent3);
                break;

            case R.id.nav_planner:
                Intent intent4 = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent4);
                break;

            case R.id.sos:
                callSOS();
                break;

            case R.id.logout:
                sessionManager.logout();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void callSOS() {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "112", null)));
    }


}
