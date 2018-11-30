package com.example.webantbeta.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Handler;

import com.example.webantbeta.R;
import com.example.webantbeta.activity.MainActivity;
import com.example.webantbeta.adapter.Adapter;
import com.example.webantbeta.connect.ParseJSON;
import com.example.webantbeta.content.Content;
import java.util.ArrayList;

import static com.example.webantbeta.connect.CheckConnection.hasConnection;

public class NewGalleryFragment extends Fragment {
    private static final String TAG = "NewPicturesFragment";

    private SwipeRefreshLayout swipeRefreshLayout;
    private  ImageView imageView;
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
        final View view = inflater.inflate(R.layout.layout_new_fragment, container, false);
        mRecyclerView = view.findViewById(R.id.new_fragment);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);

        Log.d(TAG, "onCreateView: created.");

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                check(view);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },2000);
            }
        });
        tab(view);

        return view;
    }

    public void check(View view) {
        imageView = view.findViewById(R.id.image_not_connect);
        imageView.setImageResource(R.drawable.not_connect);
        FrameLayout frameLayout = view.findViewById(R.id.frame_layout);
        if (!hasConnection(getActivity()) ) {
            mRecyclerView.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.VISIBLE);
            frameLayout.removeView(mRecyclerView);
        } else {
            imageView.setImageResource(0);
            mRecyclerView.setVisibility(View.VISIBLE);
            new NewGalleryFragment.MyTask().execute();
        }
    }
    private void tab(View view) {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        toolbar.setTitle("New");
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
