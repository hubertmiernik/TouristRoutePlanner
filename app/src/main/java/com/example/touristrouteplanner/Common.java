package com.example.touristrouteplanner;

import android.util.Log;

import com.example.touristrouteplanner.model.Route;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Common {
    private static final Common ourInstance = new Common();

    public static Common getInstance() {
        return ourInstance;
    }

    private Common() {

    }

    public ArrayList<Route> getRoutesFromJSONResponse(String response) throws JSONException {
        ArrayList<Route> result = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(response);
        JSONArray jsonArray = jsonObject.getJSONArray("routes");


        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject object = jsonArray.getJSONObject(i);

            Log.d("trasa: ", object.getString("name"));
            Log.d("trudnosc: ", object.getString("difficulty"));

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
