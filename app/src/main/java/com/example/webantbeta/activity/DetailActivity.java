package com.example.webantbeta.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.webantbeta.R;
import com.example.webantbeta.adapter.AdapterPage;


public class DetailActivity extends MainActivity {
    private static final String TAG = "DetailActivity";
     ImageView image_popup;
     TextView textDesc, textMain;
     Toolbar toolbar;
     AdapterPage mAdapter;
     TabLayout mTabLayout;
     String typeNew,typePopular;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_popup);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        Button btn = new Button(this);

        btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);
        btn.setBackgroundColor(0xFFFFFFFF);

        toolbar.addView(btn);

        setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);

        View.OnClickListener lis = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                finish();
                startActivity(intent);

            }
        };
        btn.setOnClickListener(lis);


        mAdapter = new AdapterPage(getSupportFragmentManager());

        mTabLayout = findViewById(R.id.tabs_popup);

        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.file_document_box).setText("New"));
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.fire).setText("Popular"));

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(mTabLayout.getSelectedTabPosition() == 0 ){

                }
                if(mTabLayout.getSelectedTabPosition() == 1 ){

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

//      create content popup

        image_popup = findViewById(R.id.image_popup);
        textMain = findViewById(R.id.textMain);
        textDesc = findViewById(R.id.textDesc);
        String url = "http://gallery.dev.webant.ru/media/", name = null, description = null;

        try {
            Bundle arguments = getIntent().getExtras();

            if (arguments != null) {
                url += arguments.get("Url").toString();
                Glide.with(this)
                        .asBitmap()
                        .load(url)
                        .into(image_popup);

                name = arguments.get("Name").toString();
                typeNew = arguments.get("New").toString();
                typePopular = arguments.get("Popular").toString();
                description = arguments.get("Description").toString();
            }
            textMain.setText(name);
            textDesc.setText(description);
            image_popup.setImageURI(Uri.parse(url));

//        active tab
            if (typeNew.equals("true") && typePopular.equals("false")) {
                mTabLayout.getTabAt( 0).setIcon(R.drawable.file_document_box_active);
                mTabLayout.getTabAt( 1).setIcon(R.drawable.fire);
                mTabLayout.getTabAt( 0).select();
            } if(typeNew.equals("false") && typePopular.equals("true")) {
                mTabLayout.getTabAt( 0).setIcon(R.drawable.file_document_box);
                mTabLayout.getTabAt( 1).setIcon(R.drawable.fire_active);
//                mTabLayout.setTabTextColors(
//                        getResources().getColor(R.color.colorTabsIconNoActive) ,
//                        getResources().getColor(R.color.colorTabsIconActive) );
                mTabLayout.getTabAt( 1).select();
            }
//         active tab

        } catch (Exception e) {
            Toast.makeText(this, "Error in DetailActivity", Toast.LENGTH_SHORT).show();
        }
    }
}
