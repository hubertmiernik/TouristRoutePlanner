package com.example.touristrouteplanner;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PointsParser extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
    TaskLoadedCallback taskCallback;
    String directionMode;

    public PointsParser(Context mContext, String directionMode) {
        this.taskCallback = (TaskLoadedCallback) mContext;
        this.directionMode = directionMode;
    }

    @Override
    protected void onPostExecute(List<List<HashMap<String, String>>> output) {
        ArrayList<LatLng> pointlist;
        PolylineOptions lineOptions = null;
        for (int i = 0; i < output.size(); i++) {
            pointlist = new ArrayList<>();
            lineOptions = new PolylineOptions();
            List<HashMap<String, String>> path = output.get(i);
            for (int j = 0; j < path.size(); j++) {
                HashMap<String, String> object = path.get(j);
                double lat = Double.parseDouble(object.get("lat"));
                double lng = Double.parseDouble(object.get("lng"));
                LatLng position = new LatLng(lat, lng);
                pointlist.add(position);
            }
            lineOptions.addAll(pointlist);
            if (directionMode.equalsIgnoreCase("walking")) {
                lineOptions.width(10);
                lineOptions.color(Color.RED);
            }
            else if(directionMode.equalsIgnoreCase("bicycling")){
                lineOptions.width(10);
                lineOptions.color(Color.GREEN);
            }
            else {
                lineOptions.width(10);
                lineOptions.color(Color.BLUE);
            }
        }
        if (lineOptions != null) {
            taskCallback.onTaskDone(lineOptions);
        } else {
            System.out.println("no polilyne");        }
    }

    @Override
    protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
        JSONObject jObject;
        List<List<HashMap<String, String>>> routes = null;
        try {
            jObject = new JSONObject(jsonData[0]);
            DataParser parser = new DataParser();
            routes = parser.parse(jObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return routes;
    }
}