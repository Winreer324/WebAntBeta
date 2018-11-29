package com.example.webantbeta.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.webantbeta.R;
import com.example.webantbeta.adapter.Adapter;
import com.example.webantbeta.connect.ParseJSON;
import com.example.webantbeta.content.Content;
import java.util.ArrayList;

public class NewGalleryFragment extends Fragment {
    private static final String TAG = "NewPicturesFragment";

    private ParseJSON connect = new ParseJSON();
    private static final String url = "http://gallery.dev.webant.ru/api/photos?new=true";

    private RecyclerView mRecyclerView;
    private ArrayList<Content> mContent = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        new NewGalleryFragment.MyTask().execute();

        Log.d(TAG, "onCreate: created.");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_new_fragment, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.new_fragment);
        Log.d(TAG, "onCreateView: created.");
        TabLayout tabLayout = view.findViewById(R.id.tabs);

        return view;
    }

    public void initRecyclerView() {
        Adapter adapter = new Adapter(getContext(), mContent);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
    }
    private class MyTask extends AsyncTask<Void, Void, String> {
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) { return resultJson = connect.connect(url); }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
            connect.ParseJson(mContent,doInBackground());
            initRecyclerView();
        }
    }
}
