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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.os.Handler;

import com.example.webantbeta.R;
import com.example.webantbeta.activity.MainActivity;
import com.example.webantbeta.adapter.Adapter;
import com.example.webantbeta.connect.ParseJSON;
import com.example.webantbeta.content.Content;
import java.util.ArrayList;

import static com.example.webantbeta.connect.CheckConnection.hasConnection;

public class NewGalleryFragment extends Fragment
{
    private static final String TAG = "NewPicturesFragment";

    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView imageView;
    private ProgressBar progressBar;
    private GridLayoutManager manager;
    private boolean load=false;
    private boolean isScrolling = false;
    private int currentItems,totalItems,scrollOutItem;

    private ParseJSON connect = new ParseJSON();
//    private static final String url = "http://gallery.dev.webant.ru/api/photos?new=true";
    private static int page = 1;
    private static String url = "http://gallery.dev.webant.ru/api/photos?new=true&page="+page+"&limit=8";

    private RecyclerView mRecyclerView;
    private ArrayList<Content> mContent = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        new NewGalleryFragment.MyTask().execute();

        Log.d(TAG, "onCreate: created.");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.layout_new_fragment, container, false);
        final View viewV = inflater.inflate(R.layout.activity_main, container, false);

        mRecyclerView = view.findViewById(R.id.new_fragment);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        progressBar = view.findViewById(R.id.progressBar1);

        manager = new GridLayoutManager(getContext(),2);

        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);

                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                {
                    isScrolling = true;
                    Log.d(TAG, "onCreate: scroll change.");
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
                Log.d(TAG, "onScrolled: on scroll");
                currentItems = manager.getChildCount();
                totalItems = manager.getItemCount();
                scrollOutItem = manager.findFirstVisibleItemPosition();

                Log.d(TAG, "onScrolled: urlMain scrollOutItem = "+scrollOutItem);
                Log.d(TAG, "onScrolled: urlMain currentItems = "+currentItems);
                Log.d(TAG, "onScrolled: urlMain totalItems = "+totalItems);

                if(page==2){totalItems++;}
                Log.d(TAG, "onScrolled: urlMain btotalItems = "+totalItems);
                if( isScrolling && (currentItems + scrollOutItem == totalItems) )
                {
                    isScrolling = false;
                    fetchDate(view);
                }
            }
        });

        Log.d(TAG, "onCreateView: created.");

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                check(view);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },3000);
            }
        });
        tab(view);

        return view;
    }

    private void fetchDate(final View view) {
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                int i = new Content().getCountOfPages();
                Log.d(TAG, "run: urlMain i "+i);
                if ( page <= 2 ) {
//                    if(load)
//                    {
                        check(view);
                        page++;
                        url = "http://gallery.dev.webant.ru/api/photos?new=true&page=" + page + "&limit=8";
                        new NewGalleryFragment.MyTask().execute();
//                    }
                    progressBar.setVisibility(View.GONE);
                }
            }
        }, 2000);
    }

    public void initRecyclerView()
    {
        Adapter adapter = new Adapter(getContext(), mContent);
        mRecyclerView.setAdapter(adapter);
//        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

    }
    public void check(View view)
    {
        imageView = view.findViewById(R.id.image_not_connect);
        imageView.setImageResource(R.drawable.not_connect);
        FrameLayout frameLayout = view.findViewById(R.id.frame_layout);
        if ( !hasConnection(getActivity()) )
        {
            mRecyclerView.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.VISIBLE);
            frameLayout.removeView(mRecyclerView);
            load=true;
        }
        if( hasConnection(getActivity()) )
        {
            imageView.setImageResource(0);
            mRecyclerView.setVisibility(View.VISIBLE);
            if(!load){ new NewGalleryFragment.MyTask().execute();}
        }
    }
    private void tab(View view)
    {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        toolbar.setTitle("New");
    }
    private class MyTask extends AsyncTask<Void, Void, String>
    {
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) { return resultJson = connect.connect(url); }

        @Override
        protected void onPostExecute(String strJson)
        {
            super.onPostExecute(strJson);
            connect.ParseJson(mContent,doInBackground());
            initRecyclerView();
        }
    }
}