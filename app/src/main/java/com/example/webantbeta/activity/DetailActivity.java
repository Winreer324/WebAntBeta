package com.example.webantbeta.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.webantbeta.R;
import com.example.webantbeta.adapter.AdapterPage;

public class DetailActivity extends MainActivity {
    ImageView image_popup;
    TextView textDesc, textMain;
    private Toolbar toolbar;
    private AdapterPage mAdapter;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_popup);
        boolean checkF = true;

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

// todo сделать возращение фрагмента !!!!!!!!!!!!
//                intent.putExtra("Name", mContent.get(i).getName());
//                intent.putExtra("Description", mContent.get(i).getDescription());
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
                if(mTabLayout.getSelectedTabPosition() == 0){
                    Toast.makeText(DetailActivity.this, "New", Toast.LENGTH_SHORT).show();
                }
                if(mTabLayout.getSelectedTabPosition() == 1){
                    Toast.makeText(DetailActivity.this, "Popular", Toast.LENGTH_SHORT).show();
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

        image_popup = (ImageView) findViewById(R.id.image_popup);
        textMain = (TextView) findViewById(R.id.textMain);
        textDesc = (TextView) findViewById(R.id.textDesc);
        String url = "http://gallery.dev.webant.ru/media/", name = null, description = null;
        String someText = " some text some text some text some text some text some text some text" +
                " some text some text some text some text some text some text some text some text some ";
        try {
            Bundle arguments = getIntent().getExtras();

            if (arguments != null) {
                url += arguments.get("Url").toString();
                Glide.with(this)
                        .asBitmap()
                        .load(url)
                        .into(image_popup);

                name = arguments.get("Name").toString();
                description = arguments.get("Description").toString();
                description += someText;
            }
            textMain.setText(name);
            textDesc.setText(description);
            image_popup.setImageURI(Uri.parse(url));

        } catch (Exception e) {
            Toast.makeText(this, "Error in DetailActivity", Toast.LENGTH_SHORT).show();
        }
    }
}
