package com.example.webantbeta;

import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;

import com.example.webantbeta.adapter.Adapter;
import com.example.webantbeta.adapter.AdapterPage;
import com.example.webantbeta.fragment.NewGalleryFragment;
import com.example.webantbeta.fragment.PopularGalleryFragment;

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

    private ViewPager mViewPager;
    private static Toolbar toolbar;
    private AdapterPage mAdapter;
    private TabLayout mTabLayout;


    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Started.");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_all);

        Button btn = new Button(this);
        btn.setText("click");
        int check = 1;
        btn.setId(check);
        btn.setWidth(30);
        btn.setHeight(30);
        toolbar.addView(btn);

        setSupportActionBar(toolbar);

        mAdapter  = new AdapterPage(getSupportFragmentManager());

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.container);

        mAdapter.addFragment(new NewGalleryFragment(), "New");
        mAdapter.addFragment(new PopularGalleryFragment(), "Popular");

        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

//        if (!hasConnection(this)) {
//            ImageView noConnectionImageView = (ImageView) findViewById(R.id.no_connection_image_view);
//            noConnectionImageView.setImageResource(R.drawable.no_connection);
//            noConnectionImageView.setVisibility(View.VISIBLE);
//        } else {
//            Log.d(TAG, "onCreate: Connection!");
//        }
    }
//    private class ParseTask extends AsyncTask<Void, Void, String> {
//
//        HttpURLConnection urlConnection = null;
//        BufferedReader reader = null;
//        String resultJson = "";
//
//        @Override
//        protected String doInBackground(Void... params) {
//            // получаем данные с внешнего ресурса
//            try {
//                URL url = new URL("http://gallery.dev.webant.ru/api/photos?page=1&limit=23");
////                URL url = new URL("http://androiddocs.ru/api/friends.json");
//
//                urlConnection = (HttpURLConnection) url.openConnection();
//                urlConnection.setRequestMethod("GET");
//                urlConnection.connect();
//
//                InputStream inputStream = urlConnection.getInputStream();
//                StringBuffer buffer = new StringBuffer();
//
//                reader = new BufferedReader(new InputStreamReader(inputStream));
//
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    buffer.append(line);
//                }
//
//                resultJson = buffer.toString();
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return resultJson;
//        }
//
//        @Override
//        protected void onPostExecute(String strJson) {
//            super.onPostExecute(strJson);
//            // выводим целиком полученную json-строку
//            Log.d(TAG, strJson);
//
//            JSONObject dataJsonObj = null;
//
//            try {
//                dataJsonObj = new JSONObject(strJson);
//                JSONArray data = dataJsonObj.getJSONArray("data");
//                for (int i = 0; i < data.length(); i++) {
//                    //     возращаем едемент под массива data
//                    JSONObject datafriend = data.getJSONObject(i);
//                    JSONObject imagefriend = datafriend.getJSONObject("image");
//                    String imgid = imagefriend.getString("id");
//                    String contentUrl = imagefriend.getString("contentUrl");
//                    String description = datafriend.getString("description");
//
//                    mImageUrls.add(mUrl+contentUrl);
//                    mNames.add(description);
//
//                    Log.d(TAG, "\r id: " + imgid);
//                    Log.d(TAG, "\r description: " + description);
//                    Log.d(TAG, "\r contentUrl: " + contentUrl);
//                    Log.d(TAG, "\r i: " + i);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }  finally {
//                Log.d(TAG, "\r\r array");
//                Log.d(TAG, "\t mImageUrls: " + mImageUrls);
//                Log.d(TAG, "\t mNames: " + mNames);
//            }
//        }
//    }
}
