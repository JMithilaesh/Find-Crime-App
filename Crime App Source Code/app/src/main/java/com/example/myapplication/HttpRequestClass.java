package com.example.myapplication;

import android.os.AsyncTask;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

// Asynchronous task to fetch JSON data from the URL
public class HttpRequestClass extends AsyncTask<Void, Void, JSONArray> {
    @Override
    protected JSONArray doInBackground(Void... params) {
        //link to the JSON Data
        String jsonlink = "http://129.174.126.176:8080/api/crimerate/Lati=38.9236/Longi=-77.5211";
        JSONArray json = null;
        HttpURLConnection conn = null;
        final StringBuilder stringBuilder = new StringBuilder();
        try {
            URL url = new URL(jsonlink);
            conn = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            json = new JSONArray(stringBuilder.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return json;//return the fetched JSON data
    }
}
