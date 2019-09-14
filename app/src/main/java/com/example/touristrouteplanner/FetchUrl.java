package com.example.touristrouteplanner;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class FetchUrl extends AsyncTask<String, Void, String> {
    Context context;
    String directionMode = "walking";

    public FetchUrl(Context context) {
        this.context = context;
    }

    String data;
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        PointsParser parserTask = new PointsParser(context, directionMode);
        parserTask.execute(s);
    }

    private String downloadUrl(String strUrl) throws IOException {
        data = "";
        InputStream inputStream = null;
        HttpURLConnection urlcon = null;
        try {
            URL url = new URL(strUrl);
            urlcon = (HttpURLConnection) url.openConnection();
            urlcon.connect();
            inputStream = urlcon.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
            data = stringBuffer.toString();
            bufferedReader.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        } finally {
            inputStream.close();
            urlcon.disconnect();
        }
        return data;
    }

    @Override
    protected String doInBackground(String... strings) {
        data = "";
        directionMode = strings[1];
        try {
            data = downloadUrl(strings[0]);
            System.out.println(data);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return data;
    }
}
