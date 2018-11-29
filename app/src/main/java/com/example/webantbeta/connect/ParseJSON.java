package com.example.webantbeta.connect;

import android.os.AsyncTask;
import android.util.Log;

import com.example.webantbeta.content.Content;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class ParseJSON {

    private HttpURLConnection urlConnection = null;
    private BufferedReader reader = null;
    private String resultJson = "";

    private static final String TAG = "ParseJSON: ";


    public String connect(String connectUrl) {
        try {
            URL url = new URL(connectUrl);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            resultJson = buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultJson;
    }

    public void ParseJson(ArrayList<Content> mContent, String strJson) {
        Log.d(TAG, strJson);
        JSONObject dataJsonObj = null;

        try {
            dataJsonObj = new JSONObject(strJson);
            JSONArray data = dataJsonObj.getJSONArray("data");

            for (int i = 0; i < data.length(); i++) {
                JSONObject images = data.getJSONObject(i);

                JSONObject image = images.getJSONObject("image");

                mContent.add(new Content(
                        images.getString("name"),
                        image.getString("contentUrl"),
                        images.getString("description")
                ));
                Log.d(TAG, "\ronPostExecute: "
                        + "Name: " + mContent.get(i).getName()
                        + ", Url: " + mContent.get(i).getUrl()
                        + ", Description " + mContent.get(i).getDescription()
                );
            }
        } catch (JSONException e) { e.printStackTrace(); }
    }
}
