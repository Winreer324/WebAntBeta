package com.example.webantbeta.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.webantbeta.content.Content;
import com.example.webantbeta.activity.DetailActivity;
import com.example.webantbeta.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.webantbeta.content.Content.mUrl;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private List<Content> mContent;
    private Context mContext;

    public Adapter(Context context, ArrayList<Content> pictures) {
        mContent = pictures;
        mContext = context;
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
        Log.d(TAG, "+mContent.get(i).getUrl(): "+ mContent.get(i).getUrl());
        Glide.with(mContext)
                .asBitmap()
                .load(mUrl+ mContent.get(i).getUrl())
                .into(viewHolder.image);

//        listener text

        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on text: "+ mContent.get(i));
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("Url", mContent.get(i).getUrl());
                intent.putExtra("Name", mContent.get(i).getName());
                intent.putExtra("Description", mContent.get(i).getDescription());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mContent.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private RelativeLayout parentLayout;

         ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }

}