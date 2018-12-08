package com.example.webantbeta.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.webantbeta.R;
import com.example.webantbeta.adapter.Adapter;
import com.example.webantbeta.connect.ParseJSON;
import com.example.webantbeta.content.Content;

import java.util.ArrayList;

import static com.example.webantbeta.connect.CheckConnection.hasConnection;
import static com.example.webantbeta.content.Content.countOfPages;

public class PopularGalleryFragment extends AbstractFragment {
    private static final String TAG = "NewPicturesFragment";
    //load
    private Animation mAnimation;
    private LinearLayout linearLoad;
    private ImageView circle1, circle2, circle3;
    //load
    private SwipeRefreshLayout swipeRefreshLayout;
    ImageView imageView;
    TabLayout mTabLayout;
    Adapter adapter;
    ProgressBar progressBar;
    private GridLayoutManager manager;
    private boolean load = false;
    private boolean isScrolling = false;
    private int currentItems, totalItems, scrollOutItem;

    private ParseJSON connect = new ParseJSON();
    private static int page = 1;
    private static String url = "http://gallery.dev.webant.ru/api/photos?popular=true&page=" + page + "&limit=10";

    private RecyclerView mRecyclerView;
    private ArrayList<Content> mContent = new ArrayList<>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        new MyTask().execute();

        Log.d(TAG, "onCreate: created.");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.layout_popular_fragment, container, false);
        mRecyclerView = view.findViewById(R.id.popular_fragment);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);

        mTabLayout = view.findViewById(R.id.tabs);
        progressBar = view.findViewById(R.id.progressBar);
// load
        linearLoad = view.findViewById(R.id.layoutLoad);
        linearLoad.setVisibility(View.INVISIBLE);
        circle1 = view.findViewById(R.id.circle1);
        circle2 = view.findViewById(R.id.circle2);
        circle3 = view.findViewById(R.id.circle3);
// load
        manager = new GridLayoutManager(getContext(), 2);

        mRecyclerView.setLayoutManager(manager);
//      swipe down
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                    Log.d(TAG, "onCreate: scroll change.");
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = manager.getChildCount();
                totalItems = manager.getItemCount();
                scrollOutItem = manager.findFirstVisibleItemPosition();

                if (page == countOfPages) {
                    totalItems++;
                }
                if (isScrolling && (currentItems + scrollOutItem == totalItems)) {
                    isScrolling = false;
                    fetchDate();
                }
                linearLoad.setVisibility(View.INVISIBLE);
            }
        });

//      pool refresh
        Log.d(TAG, "onCreateView: created.");
        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                check(view);
                linearLoad.setVisibility(View.VISIBLE);
                loadAnimation();
                swipeRefreshLayout.setEnabled(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        linearLoad.setVisibility(View.INVISIBLE);
                        swipeRefreshLayout.setEnabled(true);
                    }
                }, 3000);
            }
        });
//
        tab(view);
        initRecyclerView();

        return view;
    }

    private void loadAnimation() {
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.scale);
                circle1.startAnimation(mAnimation);
            }
        }, 150);
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.scale);
                circle2.startAnimation(mAnimation);
            }
        }, 450);
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.scale);
                circle3.startAnimation(mAnimation);
            }
        }, 750);
        circle1.clearAnimation();
        circle2.clearAnimation();
        circle3.clearAnimation();
    }

    private void initRecyclerView() {
        adapter = new Adapter(getContext(), mContent);
        mRecyclerView.setAdapter(adapter);
    }

    private void check(View view) {
        imageView = view.findViewById(R.id.image_not_connect);
        imageView.setImageResource(R.drawable.not_connect);
        FrameLayout frameLayout = view.findViewById(R.id.frame_layout);
        if (!hasConnection(getActivity())) {
            mRecyclerView.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.VISIBLE);
            frameLayout.removeView(mRecyclerView);
            load = true;
        }
        if (hasConnection(getActivity())) {
            imageView.setImageResource(0);
            mRecyclerView.setVisibility(View.VISIBLE);
            int start = 1;
            if (load && page == start) {
                new MyTask().execute();
            }
        }
    }

    private void fetchDate() {
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (page <= countOfPages) {
                    page++;
                    url = "http://gallery.dev.webant.ru/api/photos?popular=true&page=" + page + "&limit=10";
                    new MyTask().execute();
                    progressBar.setVisibility(View.GONE);
                }
            }
        }, 1000);
    }

    private void tab(View view) {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        toolbar.setTitle("Popular");
    }

    private class MyTask extends AsyncTask<Void, Void, String> {
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {
            return resultJson = connect.connect(url);
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
            connect.ParseJson(mContent, doInBackground());
            adapter.notifyDataSetChanged();
        }
    }

}