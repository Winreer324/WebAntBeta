package com.example.webantbeta.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.webantbeta.DetailActivity;
import com.example.webantbeta.Item;
import com.example.webantbeta.MainActivity;
import com.example.webantbeta.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.support.v4.content.ContextCompat.startActivity;
import static com.example.webantbeta.Item.mUrl;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private List<Item> mItem;
    private Context mContext;
    private Dialog mDialog;
    private ArrayList<String> imageName = new ArrayList<>();

    public Adapter(Context context, ArrayList<Item> pictures) {
        mItem = pictures;
        mContext = context;
    }
    public Adapter(ArrayList<String> imageName ) {
       this.imageName = imageName;
    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_list_item, viewGroup, false);
        final ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        Log.d(TAG, "onBindViewHolder: called.");
        Log.d(TAG, "+mItem.get(i).getUrl(): "+mItem.get(i).getUrl());
        Glide.with(mContext)
                .asBitmap()
                .load(mUrl+mItem.get(i).getUrl())
                .into(viewHolder.image);
        viewHolder.imageName.setText(mItem.get(i).getName());
//        listener text

        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on text: "+ mItem.get(i));
                Toast.makeText(mContext, mItem.get(i).getUrl()+"\r          "+
                        mItem.get(i).getName()+"         "+ mItem.get(i).getDescription(),
                        Toast.LENGTH_LONG).show();
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("Url", mItem.get(i).getUrl());
                intent.putExtra("Name", mItem.get(i).getName());
                intent.putExtra("Description", mItem.get(i).getDescription());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView image;
        private TextView imageName;
        private RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            imageName = itemView.findViewById(R.id.image_name);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }

}