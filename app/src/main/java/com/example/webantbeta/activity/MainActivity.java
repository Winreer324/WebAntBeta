package com.example.webantbeta.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.webantbeta.R;
import com.example.webantbeta.adapter.AdapterPage;
import com.example.webantbeta.fragment.NewGalleryFragment;
import com.example.webantbeta.fragment.PopularGalleryFragment;

import static com.example.webantbeta.connect.CheckConnection.hasConnection;

public class MainActivity extends AppCompatActivity {

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

        mAdapter.addFragment(new NewGalleryFragment(), "New");
        mAdapter.addFragment(new PopularGalleryFragment(), "Popular");

        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        setupTabIcons();

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        };
//        btn.setOnClickListener(listener);

        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        Log.d(TAG, "onCreateView: created.");
//        check();
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                check();
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        swipeRefreshLayout.setRefreshing(false);
//                    }
//                },4000);
//            }
//        });
    }
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
