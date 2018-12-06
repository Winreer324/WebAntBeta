package com.example.webantbeta.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

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

        Log.d(TAG, "onCreate: Started.");

        mAdapter = new AdapterPage(getSupportFragmentManager());

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.container);

        mAdapter.addFragment(new NewGalleryFragment(), "New");
        mAdapter.addFragment(new PopularGalleryFragment(), "Popular");

        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        setupTabIcons();

    }
    private void check() {
        ImageView imageView = findViewById(R.id.image_not_connect);
        imageView.setImageResource(R.drawable.not_connect);
        FrameLayout frameLayout = findViewById(R.id.frame_layout);
        if (!hasConnection(this)) {
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setImageResource(0);
            frameLayout.removeView(imageView);
            new NewGalleryFragment();
            new PopularGalleryFragment();
        }
    }

    private void setupTabIcons() {
        mTabLayout.getTabAt( 0).setIcon(R.drawable.file_document_box_active);
        mTabLayout.getTabAt( 1).setIcon(R.drawable.fire);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(mTabLayout.getSelectedTabPosition() == 0 ){
                    mTabLayout.getTabAt( 0).setIcon(R.drawable.file_document_box_active);
                    mTabLayout.getTabAt( 1).setIcon(R.drawable.fire);
                }
                if(mTabLayout.getSelectedTabPosition() == 1 ){
                    mTabLayout.getTabAt( 0).setIcon(R.drawable.file_document_box);
                    mTabLayout.getTabAt( 1).setIcon(R.drawable.fire_active);
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }
            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
    }
}
