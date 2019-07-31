package com.example.touristrouteplanner;

import android.util.Log;

import com.example.touristrouteplanner.model.Route;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Common {
    private static final Common ourInstance = new Common();
    private Route nearbyRoute;
    private Route route;

    public static Common getInstance() {
        return ourInstance;
    }

    private Common() {
        nearbyRoute = new Route();
        route = new Route();
    }

    public static Common getOurInstance() {
        return ourInstance;
    }

    public Route getNearbyRoute() {
        return nearbyRoute;
    }

    public Route getRoute(){
        return route;
    }

    public void setNearbyRoute(Route nearbyRoute) {
        this.nearbyRoute = nearbyRoute;
    }

    public ArrayList<Route> getRoutesFromJSONResponse(String response) throws JSONException {
        ArrayList<Route> result = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(response);
        JSONArray jsonArray = jsonObject.getJSONArray("routes");


        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject object = jsonArray.getJSONObject(i);

            result.add(
                    new Route(
                            object.getString("name"),
                            object.getString("description"),
                            object.getString("region"),
                            object.getString("latitude"),
                            object.getString("longitude"),
                            object.getString("picture"),
                            object.getString("difficulty"),
                            object.getString("endlatitude"),
                            object.getString("endlongitude"),
                            object.getString("length"))
            );
        }
        return result;
    }

    public ArrayList<Route> getRoutesFromJSONResponse(JSONObject response) throws JSONException {
        ArrayList<Route> result = new ArrayList<>();
        JSONArray jsonArray = response.getJSONArray("routes");


        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject object = jsonArray.getJSONObject(i);

            result.add(
                    new Route(
                            object.getString("name"),
                            object.getString("description"),
                            object.getString("region"),
                            object.getString("latitude"),
                            object.getString("longitude"),
                            object.getString("picture"),
                            object.getString("difficulty"),
                            object.getString("endlatitude"),
                            object.getString("endlongitude"),
                            object.getString("length"))
            );
        }
        return result;
    }
}
