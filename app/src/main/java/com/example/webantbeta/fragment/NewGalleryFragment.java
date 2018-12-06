package com.example.webantbeta.fragment;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
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
import android.widget.Toast;

import com.example.webantbeta.R;
import com.example.webantbeta.adapter.Adapter;
import com.example.webantbeta.connect.ParseJSON;
import com.example.webantbeta.content.Content;

import java.util.ArrayList;

import static com.example.webantbeta.connect.CheckConnection.hasConnection;
import static com.example.webantbeta.content.Content.countOfPages;
import static com.example.webantbeta.activity.DetailActivity.typePopup;

public class NewGalleryFragment extends AbstractFragment
{
    private static final String TAG = "NewPicturesFragment";
    public static Boolean typeNew = true;
    //load
    private Animation mEnlargeAnimation, mShrinkAnimation;
    private  LinearLayout linearLoad;
    private ImageView circle1, circle2, circle3;
    //load
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView imageView;
    private TabLayout mTabLayout;
    private GridLayoutManager manager;
    private boolean load = false;
    private boolean isScrolling = false;
    private int currentItems, totalItems, scrollOutItem;

    private ParseJSON connect = new ParseJSON();
    private static int page = 1;
    private static String url = "http://gallery.dev.webant.ru/api/photos?new=true&page=" + page + "&limit=10";

    private RecyclerView mRecyclerView;
    private ArrayList<Content> mContent = new ArrayList<>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        new NewGalleryFragment.MyTask().execute();

        Log.d(TAG, "onCreate: created.");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.layout_new_fragment, container, false);
        NewGalleryFragment n = new NewGalleryFragment();
        Toast.makeText(getActivity(), ""+n.getId(), Toast.LENGTH_SHORT).show();
        mRecyclerView = view.findViewById(R.id.new_fragment);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);

        mTabLayout = view.findViewById(R.id.tabs);
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
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                {
                    isScrolling = true;
                    Log.d(TAG, "onCreate: scroll change.");
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = manager.getChildCount();
                totalItems = manager.getItemCount();
                scrollOutItem = manager.findFirstVisibleItemPosition();

                if (page == countOfPages) { totalItems++; }
                if ( isScrolling && (currentItems + scrollOutItem == totalItems) )
                {
                    isScrolling = false;
                    fetchDate();
                }
                linearLoad.setVisibility(View.INVISIBLE);
            }
        });

//      pool refresh
        Log.d(TAG, "onCreateView: created.");
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                check(view);
                linearLoad.setVisibility(View.VISIBLE);
                load();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        linearLoad.setVisibility(View.INVISIBLE);
                    }
                }, 3000);
            }
        });
//
        tab(view);

        return view;
    }
    private void load()
    {
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mEnlargeAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.enscale);
                mShrinkAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.scale);
                circle1.startAnimation(mEnlargeAnimation);
            }
        }, 150);
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mEnlargeAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.enscale);
                mShrinkAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.scale);
                circle2.startAnimation(mEnlargeAnimation);
            }
        }, 450);
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mEnlargeAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.enscale);
                mShrinkAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.scale);
                circle3.startAnimation(mEnlargeAnimation);
            }
        }, 750);
        circle1.clearAnimation();
        circle2.clearAnimation();
        circle3.clearAnimation();
    }
    private void initRecyclerView()
    {
        Adapter adapter = new Adapter(getContext(), mContent);
        mRecyclerView.setAdapter(adapter);
//        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
    }

    private void check(View view)
    {
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
                Toast.makeText(getContext(), "first", Toast.LENGTH_SHORT).show();
                new NewGalleryFragment.MyTask().execute();
            }
        }
    }

    private void fetchDate()
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if ( page <= countOfPages )
                {
                    page++;
                    url = "http://gallery.dev.webant.ru/api/photos?new=true&page=" + page + "&limit=10";

                    new MyTask().execute();
                   }
                }
        }, 1000);
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
            connect.ParseJson(mContent, doInBackground());
            initRecyclerView();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // return result type popup
//        try {
//            if(typePopup == 0){ mTabLayout.getTabAt(0).select();}
//            if(typePopup == 1){ mTabLayout.getTabAt(1).select();}
//            Toast.makeText(getActivity(), "typePopup = "+typePopup, Toast.LENGTH_SHORT).show();
//            Log.d(TAG, "onResume: activity posle typePopup"+typePopup);
//        } catch (Exception e) {
//            Toast.makeText(getActivity(), "Error in DetailActivity return New", Toast.LENGTH_SHORT).show();
//        }
//
    }
}