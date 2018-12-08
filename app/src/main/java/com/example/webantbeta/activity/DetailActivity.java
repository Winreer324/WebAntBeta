package com.example.webantbeta.activity;

import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.webantbeta.R;
import com.example.webantbeta.adapter.AdapterPage;

import static com.example.webantbeta.R.drawable.arrow_left;
import static com.example.webantbeta.activity.MainActivity.countShow;

public class DetailActivity extends MainActivity {
    private static final String TAG = "DetailActivity";

    ImageView image_popup;
    TextView textDesc, textMain;
    Toolbar toolbar;
    AdapterPage mAdapter;
    TabLayout mTabLayout;
    String typeNew, typePopular;
    LinearLayout layoutPopup;

    float start = 0;
    float end = 0;
    boolean inTouch = false;
    float result = 0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_popup);
/*
   todo сделать переход из попап в меин по табам,!!!!!!!!!!!!!!!!!! забить на это !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
   todo    исправить свайп pool
   todo    переделать возращение активных табов, возращение фрагментов
   todo исправить баг с пагинацией
   todo пофиксить вывод картинки not connect
*/

//      initialization
        toolbar = findViewById(R.id.toolbar);
        image_popup = findViewById(R.id.image_popup);
        textMain = findViewById(R.id.textMain);
        textDesc = findViewById(R.id.textDesc);
        mTabLayout = findViewById(R.id.tabs_popup);
        layoutPopup = findViewById(R.id.layoutPopup);
//      initialization

        Button btn = new Button(this);
        btn.setBackgroundColor(0xFFFFFFFF);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

//      click back
        View.OnClickListener lis = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };
        btn.setOnClickListener(lis);

        mAdapter = new AdapterPage(getSupportFragmentManager());

        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.file_document_box).setText("New"));
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.fire).setText("Popular"));

        initTabs(this);
        setContentPopup();
        changeTabs();
        swipeClose();

        if (countShow < 1) {
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run () {
                    Toast.makeText(DetailActivity.this, "If you close this.\n" +
                            "Swipe down", Toast.LENGTH_SHORT).show();

                }
            },1000);
            countShow++;
        }
    }
    //      swipe down close
    private void swipeClose() {
        layoutPopup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // событие
                int actionMask = event.getActionMasked();
                // индекс касания
                float pointerIndex = event.getX();
                switch (actionMask) {
                    case MotionEvent.ACTION_DOWN: // первое касание
                        inTouch = true;
                        start = pointerIndex;
                    case MotionEvent.ACTION_POINTER_DOWN: // последующие касания
                        break;
                    case MotionEvent.ACTION_UP: // прерывание последнего касания
                        inTouch = false;
                    case MotionEvent.ACTION_POINTER_UP: // прерывания касаний
                        end = pointerIndex;
                        break;
                    case MotionEvent.ACTION_MOVE: // движение
                        break;
                }
                if (!inTouch) {
                    int a = 20;
                    result = start - end;
                    if (result > a) {
                        finish();
                    }
                }
                Log.d(TAG, "onTouch: start " + start);
                Log.d(TAG, "onTouch: end " + end);
                Log.d(TAG, "onTouch: result " + result);
                return true;
            }
        });
    }

    private void initTabs(final DetailActivity context) {
        final int tabIconColor = ContextCompat.getColor(context, R.color.colorTabsIconActive);
        mTabLayout.getTabAt(0).getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int tabIconColor = ContextCompat.getColor(context, R.color.colorTabsIconNoActive);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        item.setIcon(arrow_left);
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    //        active tab
    private void changeTabs() {
        if ( typePopular.equals("true") && typeNew.equals("true") || typePopular.equals("true")) {
            mTabLayout.getTabAt(0).setIcon(R.drawable.file_document_box);
            mTabLayout.getTabAt(1).setIcon(R.drawable.fire_active);
            mTabLayout.getTabAt(1).select();
        }
        if ( typeNew.equals("true") || typeNew.equals("true") && typePopular.equals("false") ) {
            mTabLayout.getTabAt(0).setIcon(R.drawable.file_document_box_active);
            mTabLayout.getTabAt(1).setIcon(R.drawable.fire);
            mTabLayout.getTabAt(0).select();
        }
    }

    //      create content popup
    private void setContentPopup() {
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

                Log.d(TAG, "setContentPopup: activity do typePopup New" + typeNew);
                Log.d(TAG, "setContentPopup: activity do typePopup Popular " + typePopular);

            }
            textMain.setText(name);
            textDesc.setText(description);
            image_popup.setImageURI(Uri.parse(url));

        } catch (Exception e) {
            Toast.makeText(this, "Error in DetailActivity", Toast.LENGTH_SHORT).show();
        }
    }

}
