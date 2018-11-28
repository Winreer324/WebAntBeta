package com.example.webantbeta;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.webantbeta.adapter.Adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private String mUrl = "http://gallery.dev.webant.ru/media/";

    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Started.");

        initImageBitmaps();
    }

    private void initImageBitmaps() {
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        new ParseTask().execute();
        mImageUrls.add("http://gallery.dev.webant.ru/media/5baca83c2674e262290860.jpeg");
        mNames.add("lol");
        initRecyclerView();
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init RecyclerView");

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        Adapter adapter = new Adapter(this, mNames, mImageUrls);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    private class ParseTask extends AsyncTask<Void, Void, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {
            // получаем данные с внешнего ресурса
            try {
                URL url = new URL("http://gallery.dev.webant.ru/api/photos?page=1&limit=23");
//                URL url = new URL("http://androiddocs.ru/api/friends.json");

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

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
            // выводим целиком полученную json-строку
            Log.d(TAG, strJson);

            JSONObject dataJsonObj = null;

            try {
                dataJsonObj = new JSONObject(strJson);
                JSONArray data = dataJsonObj.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    //     возращаем едемент под массива data
                    JSONObject datafriend = data.getJSONObject(i);
                    JSONObject imagefriend = datafriend.getJSONObject("image");
                    String imgid = imagefriend.getString("id");
                    String contentUrl = imagefriend.getString("contentUrl");
                    String description = datafriend.getString("description");

                    mImageUrls.add(mUrl+contentUrl);
                    mNames.add(description);

                    Log.d(TAG, "\r id: " + imgid);
                    Log.d(TAG, "\r description: " + description);
                    Log.d(TAG, "\r contentUrl: " + contentUrl);
                    Log.d(TAG, "\r i: " + i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }  finally {
                Log.d(TAG, "\r\r array");
                Log.d(TAG, "\t mImageUrls: " + mImageUrls);
                Log.d(TAG, "\t mNames: " + mNames);
            }
        }
    }
}
