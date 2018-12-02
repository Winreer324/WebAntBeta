package com.example.webantbeta.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.webantbeta.R;
import com.example.webantbeta.adapter.AdapterPage;
import com.example.webantbeta.fragment.NewGalleryFragment;
import com.example.webantbeta.fragment.PopularGalleryFragment;

import static com.example.webantbeta.connect.CheckConnection.hasConnection;

public class MainActivity extends AppCompatActivity {

    private Button btn;
    private ProgressBar progressBar;
    private LinearLayoutManager manager;
    private RecyclerView mRecyclerView;
    private Boolean isScrolling = false;
    private int currentItems,totalItems,scrollOutItem;

    private static int page = 2;
    private static String url = "http://gallery.dev.webant.ru/api/photos?new=true&page=1&limit=8";


    private static final String TAG = "MainActivity";

    private ViewPager mViewPager;
    private AdapterPage mAdapter;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        todo сделать запрет ориентиции, начать елать пагинацию
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        Log.d(TAG, "onCreate: Started.");

        mAdapter = new AdapterPage(getSupportFragmentManager());

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.container);
        mRecyclerView = findViewById(R.id.recyclerView);

        mAdapter.addFragment(new NewGalleryFragment(), "New");
        mAdapter.addFragment(new PopularGalleryFragment(), "Popular");

        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        setupTabIcons();
//        progressBar = findViewById(R.id.progressBar);
//        progressBar.setVisibility(View.VISIBLE);
//        Log.d(TAG, "onCreateView: created.");




//
//        manager = new LinearLayoutManager(this);
////        btn = new Button(this);
//        mRecyclerView.setLayoutManager(manager);
//
//        currentItems = manager.getChildCount();
//        totalItems = manager.getItemCount();
//        scrollOutItem = manager.findFirstVisibleItemPosition();
//
//        Log.d(TAG, "onScrolled: urlMain scrollOutItem = "+scrollOutItem);
//        Log.d(TAG, "onScrolled: urlMain currentItems = "+currentItems);
//        Log.d(TAG, "onScrolled: urlMain totalItems = "+totalItems);
//
//        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
//        {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState)
//            {
//                super.onScrollStateChanged(recyclerView, newState);
//                Log.d(TAG, "onCreate: scroll onScrollStateChanged.");
//                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
//                {
//                    isScrolling = true;
//                    Log.d(TAG, "onCreate: scroll change.");
//                    progressBar.setVisibility(View.VISIBLE);
//                }
//                progressBar.setVisibility(View.VISIBLE);
//            }
////
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy)
//            {
//                super.onScrolled(recyclerView, dx, dy);
//                Log.d(TAG, "onScrolled: on scroll");
//
//                currentItems = manager.getChildCount();
//                totalItems = manager.getItemCount();
//                scrollOutItem = manager.findFirstVisibleItemPosition();
//
//                Log.d(TAG, "onScrolled: urlMain scrollOutItem = "+scrollOutItem);
//                Log.d(TAG, "onScrolled: urlMain currentItems = "+currentItems);
//                Log.d(TAG, "onScrolled: urlMain totalItems = "+totalItems);
//                progressBar.setVisibility(View.VISIBLE);
//                if( isScrolling && (currentItems + scrollOutItem == totalItems) )
//                {
//                    Log.d(TAG, "onCreate: scroll onScrolled.");
//                    isScrolling = false;
//                    fetchDate();
//                }
//                fetchDate();
//            }
//        });
//
//        btn.setText("click");
//
//        btn.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v) {
//                if(page<=6)
//                {
//                    Log.d(TAG, "onCreateView: click.");
//                    Log.d(TAG, "onCreateView: urlMain: "+url);
//                    Log.d(TAG, "onCreateView: urlMain: "+page);
//                    url = "http://gallery.dev.webant.ru/api/photos?new=true&page="+page+"&limit=5";
//                    page++;
////                    new NewGalleryFragment.MyTask().execute();
//                }
//            }
//        });
    }

//    private void fetchDate() {
//        progressBar.setVisibility(View.VISIBLE);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if(page<=6) {
//                    url = "http://gallery.dev.webant.ru/api/photos?new=true&page=" + page + "&limit=4";
////                    new NewGalleryFragment.MyTask().execute();
//                    Log.d(TAG, "onScrolled: urlMain url = " + url);
//                    progressBar.setVisibility(View.GONE);
//                    page++;
//                }
//            }
//        }, 6000);
//    }


    public void check() {
        RecyclerView mRecyclerView = findViewById(R.id.recyclerView);
        ImageView imageView = findViewById(R.id.image_not_connect);
        imageView.setImageResource(R.drawable.not_connect);
        FrameLayout frameLayout = findViewById(R.id.frame_layout);
        if (!hasConnection(this) ) {
            mRecyclerView.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.VISIBLE);
//            frameLayout.removeView(mRecyclerView);
        } else {
            imageView.setImageResource(0);
            frameLayout.removeView(imageView);
            mRecyclerView.setVisibility(View.VISIBLE);
            new NewGalleryFragment();
            new PopularGalleryFragment();
        }
    }
    private void setupTabIcons() {
        mTabLayout.getTabAt(0).setIcon(R.drawable.file_document_box);
        mTabLayout.getTabAt(1).setIcon(R.drawable.fire);
    }
}
