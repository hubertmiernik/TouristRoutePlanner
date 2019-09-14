package com.example.touristrouteplanner;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class DataParser {

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<>();
        int index = 0, length = encoded.length();
        int latitude = 0, longitude = 0;

        while (index < length) {
            int b, transfer = 0, score = 0;
            do {
                b = encoded.charAt(index++) - 63;
                score |= (b & 0x1f) << transfer;
                transfer += 5;
            } while (b >= 0x20);
            int dlat = ((score & 1) != 0 ? ~(score >> 1) : (score >> 1));
            latitude += dlat;
            transfer = 0;
            score = 0;
            do {
                b = encoded.charAt(index++) - 63;
                score |= (b & 0x1f) << transfer;
                transfer += 5;
            } while (b >= 0x20);
            int dlng = ((score & 1) != 0 ? ~(score >> 1) : (score >> 1));
            longitude += dlng;

            LatLng p = new LatLng((((double) latitude / 1E5)),
                    (((double) longitude / 1E5)));
            poly.add(p);
        }
        return poly;
    }

    public List<List<HashMap<String, String>>> parse(JSONObject jObject) {

        List<List<HashMap<String, String>>> routes = new ArrayList<>();
        JSONArray routesArray;
        JSONArray legsArray;
        JSONArray stepsArray;

        try {
            routesArray = jObject.getJSONArray("routes");
            for (int i = 0; i < routesArray.length(); i++) {
                legsArray = ((JSONObject) routesArray.get(i)).getJSONArray("legs");
                List path = new ArrayList<>();
                for (int j = 0; j < legsArray.length(); j++) {
                    stepsArray = ((JSONObject) legsArray.get(j)).getJSONArray("steps");

                    for (int k = 0; k < stepsArray.length(); k++) {
                        String poly;
                        poly = (String) ((JSONObject) ((JSONObject) stepsArray.get(k)).get("polyline")).get("points");
                        List<LatLng> list = decodePoly(poly);

                        for (int l = 0; l < list.size(); l++) {
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("lng", Double.toString((list.get(l)).longitude));
                            hashMap.put("lat", Double.toString((list.get(l)).latitude));
                            path.add(hashMap);
                        }
                    }
                    routes.add(path);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return routes;
    }
}