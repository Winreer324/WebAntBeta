package com.example.webantbeta.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.webantbeta.R;
import com.example.webantbeta.adapter.AdapterPage;
import com.example.webantbeta.fragment.NewGalleryFragment;
import com.example.webantbeta.fragment.PopularGalleryFragment;

import static com.example.webantbeta.connect.CheckConnection.hasConnection;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ViewPager mViewPager;
    private Toolbar toolbar;
    private AdapterPage mAdapter;
    private TabLayout mTabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        Log.d(TAG, "onCreate: Started.");

//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle("");

//        Button btn = new Button(this);
//        btn.setText("click");
//        int check = 1;
//        btn.setId(check);
//        btn.setWidth(30);
//        btn.setHeight(30);
//        toolbar.addView(btn);

//        BottomNavigationView bottomNavigationView = findViewById(R.id.tab_nav);
//        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

//        setSupportActionBar(toolbar);

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
                CoordinatorLayout mainLayout = (CoordinatorLayout) findViewById(R.id.tabs);
                ImageView imageView = new ImageView(MainActivity.this);
                mainLayout.removeView(imageView);
                check();
                mainLayout.removeView(imageView);
            }
        };
//        btn.setOnClickListener(listener);

        check();
    }
    public void check() {
            CoordinatorLayout mainLayout = (CoordinatorLayout) findViewById(R.id.main_content);
            ImageView imageView = new ImageView(MainActivity.this);
            imageView.setImageResource(R.drawable.not_connect);
            ViewPager viewPager = findViewById(R.id.container);
            RecyclerView recyclerView = findViewById(R.id.recyclerView);
        if (!hasConnection(MainActivity.this)) {
            viewPager.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
            mainLayout.removeView(imageView);
            mainLayout.addView(imageView);
//            Toast.makeText(MainActivity.this, "MainActivity connection is not found", Toast.LENGTH_SHORT).show();
        } else {
            viewPager.setVisibility(View.VISIBLE);
            mainLayout.removeView(imageView);
            recyclerView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);
//            Toast.makeText(MainActivity.this, "click connect reload", Toast.LENGTH_SHORT).show();
        }
    }
    private void setupTabIcons() {
        mTabLayout.getTabAt(0).setIcon(R.drawable.file_document_box);
        mTabLayout.getTabAt(1).setIcon(R.drawable.fire);
    }
}
